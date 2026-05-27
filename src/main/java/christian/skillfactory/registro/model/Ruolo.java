package christian.skillfactory.registro.model;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ruolo")
public class Ruolo implements GrantedAuthority {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String nome; // Primary Key: Role name (e.g., "ROLE_ADMIN", "ROLE_PROFESSOR")
    
    // === CONSTRUCTORS ===
    public Ruolo() {}

    public Ruolo(String nome) {
        this.nome = nome;
    }

    // === SPRING SECURITY GRANTEDAUTHORITY IMPLEMENTATION ===
    /**
     * Returns the string representation of the authority (role name).
     * Required by Spring Security to handle role-based access control.
     */
    @Override
    public String getAuthority() {
        return nome;
    }

    // === GETTERS AND SETTERS ===
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // === JPA COMPLIANT EQUALS & HASHCODE ===
    // Based on the primary key string 'nome' using instanceof for Hibernate proxy compatibility
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Ruolo)) return false;
        Ruolo other = (Ruolo) obj;
        return Objects.equals(nome, other.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}