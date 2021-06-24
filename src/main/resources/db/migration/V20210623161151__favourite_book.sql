-- This view will be used to determine which books are available for checkout
CREATE OR REPLACE VIEW favourite_book AS
SELECT
    gen_random_uuid() AS id, -- JPA requires an ID for all entities
    checkout.account_id AS account_id,
    checkout.book_id AS book_id,
    count(checkout.book_id)::INTEGER AS times_checked_out
FROM checkout
GROUP BY checkout.book_id, checkout.account_id