package com.mfarion.carregistry.repositories.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "users")
public class UserEntity implements UserDetails {

    //el valor del campo será generado automáticamente por la base de datos o por la persistencia
    //del proveedor IDENTITY: Utiliza una columna autoincremental en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;

    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Lob
    private byte[] image;



    //devuelve una colección de GrantedAuthority, aqui tiene el rol del usuario
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // Devuelve el email del usuario como su nombre de usuario
    @Override
    public String getUsername() {
        return email;
    }

    // Indica que la cuenta no ha expirado
    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }

    // Indica que la cuenta no está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }

    // Indica que las credenciales no han expirado
    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }

    // Indica que la cuenta está habilitada
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }
}
