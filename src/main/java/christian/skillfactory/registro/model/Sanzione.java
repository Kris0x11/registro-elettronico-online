package christian.skillfactory.registro.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "sanzione")
public class Sanzione {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tipo;
    private LocalDate data;
    private String descrizione;
    
    /**
     * Many-to-One relationship with RegistroEntity.
     * Multiple disciplinary sanctions can belong to a single class registry.
     */
    @ManyToOne
    @JoinColumn(name = "nome_classe")
    private RegistroEntity registro;
    
    /**
     * Many-to-Many relationship with StudenteEntity.
     * A sanction can be applied to multiple students, and a student can have multiple sanctions.
     * This side acts as the owner, defining the 'sanzione_studenti' join table.
     */
    @ManyToMany
    @JoinTable(
        name = "sanzione_studenti", 
        joinColumns = @JoinColumn(name = "sanzione_id"), 
        inverseJoinColumns = @JoinColumn(name = "studente_id")
    )
    private List<StudenteEntity> studenti;

    // CONSTRUCTORS 
    public Sanzione() {}
    
    public Sanzione(String tipo, LocalDate data, String descrizione) {
        this.tipo = tipo;
        this.data = data;
        this.descrizione = descrizione;
    }

    //  GETTERS AND SETTERS 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<StudenteEntity> getStudenti() {
        return studenti;
    }

    public void setStudenti(List<StudenteEntity> studenti) {
        this.studenti = studenti;
    }

    public RegistroEntity getRegistro() {
        return registro;
    }

    public void setRegistro(RegistroEntity registro) {
        this.registro = registro;
    }

    // === JPA COMPLIANT EQUALS & HASHCODE ===
   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Sanzione)) return false;
        Sanzione sanzione = (Sanzione) o;
        return id != null && id.equals(sanzione.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}