<#-- @ftlvariable name="books" type="java.util.List<name.marcocirillo.library.notification.factory.CheckoutCreatedNotificationFactory.TemplateBook>" -->
You've checked out the following books:
<#list books as book>
 - ${book.title}
</#list>