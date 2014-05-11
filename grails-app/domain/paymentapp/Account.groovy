package paymentapp

class Account {

    String name
    String email
    BigDecimal balance = 200.0

    static hasMany = [incomingTransactions: Transaction, outgoingTransactions: Transaction]

    static mappedBy = [incomingTransactions: 'payee', outgoingTransactions: 'payer']

    static constraints = {
        balance(min:0.0)
    }
}
