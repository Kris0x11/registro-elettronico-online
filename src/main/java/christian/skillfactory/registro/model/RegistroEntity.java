package christian.skillfactory.registro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro")
public class RegistroEntity {

    @Id
    @Column(name = "nome_classe")
    private String nomeClasse; // Primary Key: Unique class identifier
    
    private String annoScolastico;

    /**
     * One-to-Many relationship with Sanzione.
     * Mapped by the 'registro' field inside the Sanzione entity.
     */
    @OneToMany(mappedBy = "registro")
    private List<Sanzione> sanzione;
    
    /**
     * One-to-Many relationship with StudenteEntity.
     * Mapped by the 'regstudenti' field inside the StudenteEntity.
     */
    @OneToMany(mappedBy = "regstudenti")
    private List<StudenteEntity> studenti = new ArrayList<>();
    
    /**
     * Many-to-Many relationship with ProfessoreEntity.
     * This side acts as the owner of the relationship, managing the 'prof_registro' join table.
     */
    @ManyToMany
    @JoinTable(
        name = "prof_registro",
        joinColumns = @JoinColumn(name = "registro_id"),
        inverseJoinColumns = @JoinColumn(name = "prof_id")
    )
    private List<ProfessoreEntity> listaProf = new ArrayList<>();
    
    // === CONSTRUCTORS ===
    public RegistroEntity() {}
    
    public RegistroEntity(String nomeClasse, String annoScolastico) {
        this.nomeClasse = nomeClasse;
        this.annoScolastico = annoScolastico;
    }

    // === GETTERS AND SETTERS ===
    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public String getAnnoScolastico() {
        return annoScolastico;
    }

    public void setAnnoScolastico(String annoScolastico) {
        this.annoScolastico = annoScolastico;
    }

    public List<ProfessoreEntity> getLista_prof() {
        return listaProf;
    }

    public void setLista_prof(List<ProfessoreEntity> lista_prof) {
        this.listaProf = lista_prof;
    }

    public List<StudenteEntity> getStudenti() {
        return studenti;
    }

    public void setStudenti(List<StudenteEntity> studenti) {
        this.studenti = studenti;
    }
    
    public List<Sanzione> getSanzione() {
        return sanzione;
    }

    public void setSanzione(List<Sanzione> sanzione) {
        this.sanzione = sanzione;
    }

    // === JPA COMPLIANT EQUALS & HASHCODE ===
    // Relying strictly on the unique business key (nomeClasse) to ensure reliable collection mapping and entity safety
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistroEntity)) return false;
        RegistroEntity that = (RegistroEntity) o;
        return Objects.equals(nomeClasse, that.nomeClasse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeClasse);
    }
}


