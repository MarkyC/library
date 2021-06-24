<#-- @ftlvariable name="books" type="java.util.List<name.marcocirillo.library.notification.factory.FavouriteBooksNotificationFactory.TemplateFavouriteBook>" -->
You have overdue books:
<#list books as book>
 - ${book.title}
</#list>