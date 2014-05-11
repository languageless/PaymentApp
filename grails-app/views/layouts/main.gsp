<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div id="grailsLogo"><a href="http://grails.org"><img src="${resource(dir:'images',file:'grails_logo.png')}" alt="Grails" border="0" /></a></div>

        <div id="menu" style="background-color: #dcdcdc; padding: 10px;">
            <g:link controller="transaction" action="pay">Make a payment</g:link>&nbsp;&nbsp;
            <g:link controller="transaction" action="list">View transactions</g:link>&nbsp;&nbsp;
            <g:link controller="greenmail" action="list">View emails sent</g:link>
        </div>
    <g:layoutBody />
    </body>
</html>