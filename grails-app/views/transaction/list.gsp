<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>See transactions</title>
    <meta name="layout" content="main"/>
</head>

<body>
<div style="padding-left: 30px;padding-top: 30px;">
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
    <g:form action="list">
        <b>Person:</b>
        <g:select name="id" from="${accountList}" optionKey="id" optionValue="name"/>
        <br/>
        <b><g:submitButton name="List transactions"/></b>
    </g:form>

    <br/><br/>
    <g:if test="${account}">
        <div>Balance: ${account.balance}</div>
    </g:if>
    <div style="padding: 20px;20px;20px;20px">
        <g:if test="${transactions}">
            <table>
                <thead>
                <th>In</th>
                <th>Out</th>
                <th>Date</th>
                </thead>
                <g:each in="${transactions}" var="transaction">
                    <tr>
                        <g:if test="${transaction.payee.id == account.id}">
                            <td>${transaction.amount}</td>
                            <td>&nbsp;</td>
                        </g:if>
                        <g:else>
                            <td>&nbsp;</td>
                            <td>${transaction.amount}</td>
                        </g:else>
                        <td><g:formatDate date="${transaction.dateOfPayment}" format="dd/MM/yyyy HH:mm"/></td>
                    </tr>
                </g:each>
            </table>
        </g:if>
    </div>
</div>
</body>
</html>
