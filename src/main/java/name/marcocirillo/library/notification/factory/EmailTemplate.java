package name.marcocirillo.library.notification.factory;

public enum EmailTemplate {
    CHECKOUT_CREATED("checkout-created.html.ftl", "checkout-created.txt.ftl"),
    OVERDUE_CHECKOUT("overdue-checkout.html.ftl", "overdue-checkout.txt.ftl"),
    FAVOURITE_BOOKS("favourite-books.html.ftl", "favourite-books.txt.ftl"),
    ;
    private final String htmlTemplate;
    private final String textTemplate;

    EmailTemplate(String htmlTemplate, String textTemplate) {
        this.htmlTemplate = htmlTemplate;
        this.textTemplate = textTemplate;
    }

    public String getHtmlTemplate() {
        return htmlTemplate;
    }

    public String getTextTemplate() {
        return textTemplate;
    }
}
