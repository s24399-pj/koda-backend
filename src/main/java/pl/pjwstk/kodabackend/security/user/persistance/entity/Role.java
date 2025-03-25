package pl.pjwstk.kodabackend.security.user.persistance.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.ADMIN_CREATE;
import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.ADMIN_DELETE;
import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.ADMIN_READ;
import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.ADMIN_UPDATE;
import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.USER_CREATE;
import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.USER_DELETE;
import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.USER_READ;
import static pl.pjwstk.kodabackend.security.user.persistance.entity.Permission.USER_UPDATE;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Set.of(
            USER_READ,
            USER_UPDATE,
            USER_DELETE,
            USER_CREATE
    )),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionValue()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
