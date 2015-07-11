package rga.customer.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Extends the User class from spring security to contain an additional salt attribute.
 *
 * @author ccw
 */
public class AuthUser extends User {

    private Long salt;

    public AuthUser(String username, String password, Long salt, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.salt = salt;
    }

    public Long getSalt() {
        return this.salt;
    }
}
