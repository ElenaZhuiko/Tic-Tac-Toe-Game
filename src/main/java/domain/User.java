package main.java.domain;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public String getUsername() {
        return username;
    }

    private final String username;

    public User(String username) {
        this.username = Objects.requireNonNull(username);
    }
}
