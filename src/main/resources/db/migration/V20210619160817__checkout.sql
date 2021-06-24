CREATE TABLE IF NOT EXISTS checkout (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL REFERENCES book(id),
    account_id UUID NOT NULL REFERENCES account(id),
    returned BOOLEAN NOT NULL DEFAULT false,
    due TIMESTAMPTZ NOT NULL,
    created TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_checkout_returned ON checkout(returned);