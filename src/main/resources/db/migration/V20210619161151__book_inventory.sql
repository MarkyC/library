-- This view will be used to determine which books are available for checkout
CREATE OR REPLACE VIEW book_inventory AS
WITH book_checkout AS (
    SELECT book_id AS book_id,
           count(book_id) AS num_checked_out
    FROM checkout
    WHERE returned = false
    GROUP BY book_id
)
SELECT
    book.id AS id,
    GREATEST(0, book.stock - COALESCE(book_checkout.num_checked_out, 0))::INTEGER AS inventory_level
FROM book
JOIN author ON author.id = book.author_id
LEFT JOIN book_checkout ON book_checkout.book_id = book.id
WHERE book.available = true