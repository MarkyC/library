CREATE TABLE IF NOT EXISTS book
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    isbn CHAR(13) NOT NULL,
    author_id UUID NOT NULL REFERENCES author(id),
    available BOOLEAN NOT NULL DEFAULT false,
    stock INT NOT NULL DEFAULT 0,
    created TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_book_title ON book(title);
CREATE INDEX IF NOT EXISTS idx_book_available ON book(available);
CREATE UNIQUE INDEX IF NOT EXISTS idx_book_isbn ON book(isbn); -- ISBNs are globally unique, even across editions!