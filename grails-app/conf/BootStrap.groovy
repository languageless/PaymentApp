import paymentapp.Account

class BootStrap {

    def init = { servletContext ->

        new Account(name: 'Paty', email: 'patty@someemail.com').save()
        new Account(name: 'Joe', email: 'joe@someemail.com').save()
    }
    def destroy = {
    }
}
