package paymentapp

class TransactionController {

    def transactionService

    def index = {
        redirect(action: "pay")
    }

    def list = {

        if (params.id) {
            Account account = Account.get(params.int("id"))
            Set transactions = account.incomingTransactions + account.outgoingTransactions
            [accountList: Account.list(), account: account, transactions: transactions.sort {it.dateOfPayment}]
        } else {
            [accountList: Account.list()]
        }
    }

    def pay = {
        render(view: "pay", model: [accountList: Account.list()])
    }

    def complete = {

        Account payer = Account.get(params.int("payer"))
        Account payee = Account.get(params.int("payee"))
        BigDecimal amount = params.amount as BigDecimal
        Transaction transaction = new Transaction(
                payer: payer,
                payee: payee,
                amount: amount
        )
        if (transaction.validate()) {
            transactionService.saveTransaction(transaction)
            flash.message = "Payment of £${amount} from ${payer.name} to ${payee.name} was successful"
            redirect(action: "pay")
        } else {
            render(view: "pay", model: [transaction: transaction, accountList: Account.list()])
        }
    }
}
