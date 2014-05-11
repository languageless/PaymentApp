<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>See transactions</title>
    <meta name="layout" content="main" />
</head>
<body>
<div>
    <h2>Pay</h2>
    <br/>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${account}">
        <div class="errors">
            <g:renderErrors bean="${account}" as="list"/>
        </div>
    </g:hasErrors>
    <b>Person:</b> <g:select name="person" from="${paymentapp.Account.list()}" optionKey="id" optionValue="name"/>
    <br/>
    <b><g:submitButton name="List transactions"/></b>
    ------------------------------------------------------
    <div>Balance: ${account.balance}</div>
    <div>
        ${transactions}
    </div>
</div>
</body>
</html>