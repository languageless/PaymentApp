package paymentapp

import grails.test.*

class TransactionControllerTests extends ControllerUnitTestCase {
    Account account1
    Account account2

    protected void setUp() {
        super.setUp()
        account1 = new Account(
                name: 'Account 1',
                email: 'account1@someemail.com',
                incomingTransactions: [],
                outgoingTransactions: [])
        account2 = new Account(
                name: 'Account 2',
                email: 'account2@someemail.com',
                incomingTransactions: [],
                outgoingTransactions: [])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testViewPayPage() {

        mockDomain(Account)
        this.controller.pay()
        assertEquals 'pay', this.controller.modelAndView.viewName
    }

    void testCompletePaymentWithCorrectValuesShouldSucceed(){

        mockDomain(Account,[account1, account2])
        mockDomain(Transaction)

        def mockControl = mockFor(TransactionService)
        mockControl.demand.saveTransaction(1..1){-> new Transaction(payer: account1, payee: account2, amount: 100.0)}
        this.controller.transactionService = mockControl.createMock()

        this.controller.params.payer = account1.id
        this.controller.params.payee = account2.id
        this.controller.params.amount = 100.0

        this.controller.complete()

        assertEquals "pay", this.controller.redirectArgs["action"]
    }

    void testCompletePaymentWithMissingValuesShouldFailWithErrors(){

        mockDomain(Account,[account1, account2])
        mockDomain(Transaction)

        this.controller.params.payer = account1.id
        this.controller.params.payee = account1.id //Same recipient should fail
        this.controller.params.amount = 300.0      //Amount over budget should fail

        this.controller.complete()

        assertEquals "pay", this.controller.modelAndView.viewName

        def errors = controller.modelAndView.model["transaction"].errors
        assertEquals 2, errors.errorCount
        assertEquals "samePayerAndPayee", errors.getFieldError("payee").code
        assertEquals "overBalance", errors.getFieldError("amount").code
    }

    void testListTransactionsViewPageWithoutSelection() {

        mockDomain(Account,[account1,account2])
        def model = this.controller.list()
        assertNull model.transactions
        assertEquals 2, model.accountList.size()
    }

    void testListTransactionsShouldDisplayBalanceAndCorrectList() {

        account1.balance = account1.balance - 50
        account2.balance = account2.balance + 50
        Transaction transaction = new Transaction(payer: account1, payee: account2, amount: 50.0)
        account1.outgoingTransactions.add(transaction)
        account2.incomingTransactions.add(transaction)
        mockDomain(Transaction, [transaction])
        mockDomain(Account,[account1, account2])

        this.controller.params.id = account1.id
        def model = this.controller.list()

        assertEquals 150, model.account.balance
        assertEquals 1, model.transactions.size()
    }
}
