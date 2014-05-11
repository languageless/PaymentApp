<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Pay Some Person</title>
    <meta name="layout" content="main"/>
</head>

<body>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<g:hasErrors bean="${transaction}">
    <div class="errors">
        <g:renderErrors bean="${transaction}" as="list"/>
    </div>
</g:hasErrors>
<div style="padding-left: 30px;padding-top: 30px;">
    <h2>Pay</h2>
    <br/>
    <g:form action="complete">
        <b>From:</b>
        <g:select name="payer" from="${accountList}" optionKey="id" optionValue="name"/>
        <br/>
        <b>To:</b>
        <g:select name="payee" from="${accountList}" optionKey="id" optionValue="name"/>
        <br/>
        <b>Amount (£):</b>
        <span class="value ${hasErrors(bean: transaction, field: 'amount', 'errors')}">
            <g:textField name="amount" size="8"/>
        </span>
        <br/>
        <b><g:submitButton name="Complete"/></b>
    </g:form>
</div>
</body>
</html>