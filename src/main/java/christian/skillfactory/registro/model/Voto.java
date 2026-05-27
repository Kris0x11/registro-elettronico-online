package christian.skillfactory.registro.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "votazione")
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double voto;
    private LocalDate data;
    
    /**
     * Many-to-One relationship with StudenteEntity.
     * Multiple grades can be assigned to a single student.
     * The foreign key column 'studente_id' is allowed to be null to handle temporary or orphan states during database restructuring.
     */
    @ManyToOne
    @JoinColumn(name = "studente_id", nullable = true)
    private StudenteEntity votoStudente;

    // === CONSTRUCTORS ===
    public Voto() {}
    
    public Voto(Double voto, LocalDate data) {
        this.voto = voto;
        this.data = data;
    }

    // === GETTERS AND SETTERS ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getVoto() {
        return voto;
    }

    public void setVoto(Double voto) {
        this.voto = voto;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public StudenteEntity getVotoStudente() {
        return votoStudente;
    }

    public void setVotoStudente(StudenteEntity votoStudente) {
        this.votoStudente = votoStudente;
    }

    // === JPA COMPLIANT EQUALS & HASHCODE ===
    // Relying strictly on the database primary key (id) to avoid circular reference loops
    // and recursive memory issues when interacting with the associated StudenteEntity.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Voto)) return false;
        Voto other = (Voto) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}