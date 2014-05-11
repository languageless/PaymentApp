package paymentapp

class Transaction {

    BigDecimal amount
    Date dateOfPayment = new Date()
    Account payee
    Account payer

    static constraints = {
        payee(nullable: false, validator: {val, obj ->
            if(val.id == obj.payer.id){
                return ["samePayerAndPayee", "payee"]
            }
        })
        amount(nullable: false, min: 0.0, validator: {val, obj ->
            if(val > obj.payer.balance){
                return ["overBalance", "amount"]
            }
        })
    }
}
