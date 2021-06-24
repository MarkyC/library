<#-- @ftlvariable name="books" type="java.util.List<name.marcocirillo.library.notification.factory.OverdueCheckoutNotificationFactory.TemplateOverdueBook>" -->
You have overdue books:
<#list books as book>
 - ${book.quantity}x ${book.title}
</#list>