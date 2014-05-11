package paymentapp

import com.icegreen.greenmail.util.GreenMailUtil
import org.springframework.validation.Errors

import javax.mail.Message

class TransactionControllerIntegrationTests extends GroovyTestCase {
    Account account1
    Account account2
    def greenMail

    protected void setUp() {
        super.setUp()

        //Test data
        account1 = new Account(name: 'Account 1', email: 'account1@someemail.com').save()
        account2 = new Account(name: 'Account 2', email: 'account2@someemail.com').save()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCompletePayment() {

        TransactionController controller = new TransactionController()
        controller.params.payer = account1.id
        controller.params.payee = account2.id
        controller.params.amount = 50

        controller.complete()

        assertEquals 1, account1.outgoingTransactions?.size()
        assertEquals 50, account1.outgoingTransactions.iterator().next().amount
        assertNull account1.incomingTransactions
        assertEquals 150, account1.balance

        assertNull account2.outgoingTransactions
        assertEquals 1, account2.incomingTransactions?.size()
        assertEquals 50, account2.incomingTransactions.iterator().next().amount
        assertEquals 250, account2.balance

        //Retrieve using GreenMail API
        Message[] messages = greenMail.getReceivedMessages()
        assertEquals 2, messages.length

        messages.each
                {
                    def body = GreenMailUtil.getBody(it).trim()
                    if (body.contains("Dear Account 1"))
                        assertTrue body.contains("Your account has been debited with &pound;50.")
                    else if (body.contains("Dear Account 2"))
                        assertTrue body.contains("Your account has been credited with &pound;50.")
                }

        assertEquals '/transaction/pay', controller.response.redirectedUrl

    }

    void testCompletePaymentEmptySelectionsShouldReturnValidationErrors() {
        TransactionController controller = new TransactionController()
        controller.complete()

        Errors errors = controller.modelAndView.model["transaction"].errors
        assertEquals 3, errors.errorCount

        assertEquals "nullable", errors.getFieldError("payer").code
        assertEquals "nullable", errors.getFieldError("payee").code
        assertEquals "nullable", errors.getFieldError("amount").code

        assertTrue controller.modelAndView.viewName.endsWith("/pay")
    }

    void testCompletePaymentWithAmountOverBalanceShouldReturnError(){
        TransactionController controller = new TransactionController()
        controller.params.payer = account1.id
        controller.params.payee = account2.id
        controller.params.amount = 300
        controller.complete()

        Errors errors = controller.modelAndView.model["transaction"].errors
        assertEquals 1, errors.errorCount

        assertEquals "overBalance", errors.getFieldError("amount").code
        assertTrue controller.modelAndView.viewName.endsWith("/pay")
    }

    void testCompletePaymentWithSamePayerAndPayeeShouldReturnError(){
        TransactionController controller = new TransactionController()
        controller.params.payer = account1.id
        controller.params.payee = account1.id
        controller.params.amount = 20
        controller.complete()

        Errors errors = controller.modelAndView.model["transaction"].errors
        assertEquals 1, errors.errorCount

        assertEquals "samePayerAndPayee", errors.getFieldError("payee").code
        assertTrue controller.modelAndView.viewName.endsWith("/pay")
    }
}
