package paymentapp

import org.springframework.transaction.TransactionStatus

class TransactionService {

    private static final String EMAIL_SUBJECT = "Transaction confirmation"
    private static final String EMAIL_TEMPLATE = "/confirmationEmail"

    def mailService //Provided by the mail-plugin

    static transactional = false

    /**
     * Persists the transaction and its associations in the database and sends email to both payer and payee. Implemented
     * in a single transaction in order to roll back changed in case of failure
     * @param transaction
     * @return Transaction - the saved transaction
     */
    Transaction saveTransaction(Transaction transaction) {

        Transaction.withTransaction { TransactionStatus status ->

            if (transaction.save(flush: true)) {
                try {
                    transaction.payer.addToOutgoingTransactions(transaction)
                    transaction.payer.balance = transaction.payer.balance - transaction.amount
                    transaction.payee.addToIncomingTransactions(transaction)
                    transaction.payee.balance = transaction.payee.balance + transaction.amount

                    //Send out the emails
                    sendEmailToAccountHolders(transaction)
                    status.flush()
                }
                catch (Exception e) {
                    log.error "Transaction could not be completed: from ${transaction.payer} to ${transaction.payee}", e
                    status.setRollbackOnly()
                }
            }
            else {
                log.error "Transaction could not be completed: from ${transaction.payer} to ${transaction.payee}"
                status.setRollbackOnly()
            }
        }
        return transaction
    }

    /**
     * Helper method that facilitates the posting of emails
     * @param transaction
     */
    private void sendEmailToAccountHolders(Transaction transaction){
        mailService.sendMail {
            to transaction.payer.email
            subject EMAIL_SUBJECT
            body(view: EMAIL_TEMPLATE,
                    model: [name: transaction.payer.name, action: Action.DEBITED.value, amount: transaction.amount]
            )
        }
        mailService.sendMail {
            to transaction.payee.email
            subject EMAIL_SUBJECT
            body(view: EMAIL_TEMPLATE,
                    model: [name: transaction.payee.name, action: Action.CREDITED.value, amount: transaction.amount]
            )
        }
    }

    private enum Action{
        DEBITED("debited"),
        CREDITED("credited")

        private final String value

        Action(String value) {
            this.value = value
        }
        String getValue() {
            return value
        }
    }
}
