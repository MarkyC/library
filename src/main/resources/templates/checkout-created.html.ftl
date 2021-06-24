<#-- @ftlvariable name="books" type="java.util.List<name.marcocirillo.library.notification.factory.CheckoutCreatedNotificationFactory.TemplateBook>" -->
<!doctype html>
<html>
<head>
    <style>
        body {
            font-family: sans-serif;
            color: #454545;
            font-size: 16px;
            margin: 2em auto;
            max-width: 800px;
            padding: 1em;
            line-height: 1.4;
            text-align: justify;
        }
    </style>
</head>
<body>
    <p>You've checked out the following books:</p>
    <ul>
        <#list books as book>
            <li>${book.title}</li>
        </#list>
    </ul>
</body>
</html>