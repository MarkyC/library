CREATE TABLE IF NOT EXISTS book_category
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL REFERENCES book(id),
    category_id UUID NOT NULL REFERENCES category(id),
    created TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- a book can only be added to a category once
ALTER TABLE book_category ADD CONSTRAINT book_category_book_id_category_id UNIQUE (book_id, category_id);