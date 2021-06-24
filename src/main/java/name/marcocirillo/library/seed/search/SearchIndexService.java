package name.marcocirillo.library.seed.search;

public interface SearchIndexService {
    void createIndexIfNotPresent(Index index, String schema);
    void deleteIndex(Index index);

    void refreshIndex(Index index);

    enum Index {
        BOOK("books"),
        BOOK_TEST("books_test"),
        ;

        private final String name;

        Index(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
