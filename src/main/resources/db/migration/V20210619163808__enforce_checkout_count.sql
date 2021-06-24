-- enforces 10 checkouts per user in a thread-safe way
-- adapted from: https://stackoverflow.com/a/1743742
CREATE OR REPLACE FUNCTION enforce_checkout_count() RETURNS trigger AS $$
DECLARE
    max_checkout_count INTEGER := 10;
    checkout_count INTEGER := 0;
    must_check BOOLEAN := false;
BEGIN
    IF TG_OP = 'INSERT' THEN
        must_check := true;
    END IF;

    IF TG_OP = 'UPDATE' THEN
        IF (NEW.account_id != OLD.account_id) THEN
            must_check := true;
        END IF;
    END IF;

    IF must_check THEN
        -- prevent concurrent inserts from multiple transactions
        LOCK TABLE checkout IN EXCLUSIVE MODE;

        SELECT INTO checkout_count COUNT(*)
        FROM checkout
        WHERE account_id = NEW.account_id
        AND returned = FALSE; -- returned checkouts don't count!

        IF checkout_count >= max_checkout_count THEN
            RAISE EXCEPTION 'MAX_CHECKOUTS: Cannot have more than % checkouts for each user.', max_checkout_count;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER enforce_photo_count
    BEFORE INSERT OR UPDATE ON checkout
    FOR EACH ROW EXECUTE PROCEDURE enforce_checkout_count();