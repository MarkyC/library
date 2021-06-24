-- This view will be used to fill the search index
CREATE OR REPLACE VIEW search_book AS
WITH book_category_ids AS (
    SELECT
        book_category.book_id AS book_id,
        array_agg(book_category.category_id) AS category_ids
    FROM book_category
    GROUP BY book_category.book_id
)
SELECT
    author.id AS author_id,
    author.name AS author_name,
    book.id AS id,
    book.title AS title,
    book.isbn AS isbn,
    book.available AS available,
    book_inventory.inventory_level AS inventory_level,
    book_category_ids.category_ids AS category_ids
FROM book
JOIN author ON author.id = book.author_id
JOIN book_inventory ON book_inventory.id = book.id
LEFT JOIN book_category_ids ON book_category_ids.book_id = book.id