package name.marcocirillo.library.auth.userdetails;

import name.marcocirillo.library.account.db.Account;

import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

public class AccountPrincipal implements Principal {
    private final UUID id;
    private final String name;
    private final String email;

    public AccountPrincipal(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountPrincipal that = (AccountPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AccountPrincipal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
