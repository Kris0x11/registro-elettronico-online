package christian.skillfactory.registro.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "titolo_studio")
public class TitoloStudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descrizione;
    
    private String tipo;

    /**
     * Many-to-Many relationship with ProfessoreEntity.
     * This is the inverse side of the relationship, mapped by the 'titoli' field inside ProfessoreEntity.
     */
    @ManyToMany(mappedBy = "titoli")
    private List<ProfessoreEntity> professori;

    // === CONSTRUCTORS ===
    public TitoloStudio() {}

    public TitoloStudio(String descrizione, String tipo) {
        this.descrizione = descrizione;
        this.tipo = tipo;
    }

    // === GETTERS AND SETTERS ===
    public Long getId() { 
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() { 
        return descrizione; 
    }

    public void setDescrizione(String descrizione) { 
        this.descrizione = descrizione; 
    }

    public List<ProfessoreEntity> getProfessori() { 
        return professori; 
    }

    public void setProfessori(List<ProfessoreEntity> professori) { 
        this.professori = professori; 
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // === JPA COMPLIANT EQUALS & HASHCODE ===
    // Relying strictly on the database primary key (id) to avoid recursive loops 
    // with the bidirectional relationship linked to the professor entities.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof TitoloStudio)) return false;
        TitoloStudio that = (TitoloStudio) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}