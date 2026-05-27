package christian.skillfactory.registro.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "professore")
public class ProfessoreEntity extends Persona {

    private String materia;
    private LocalDate dataNascita;
    private String matricola;

    /**
     * One-to-One relationship with Account.
     * Maps the professor to their specific user credentials.
     */
    @OneToOne
    @JoinColumn(name="id_account")
    private Account account;
    
    /**
     * Many-to-Many relationship with TitoloStudio.
     * This side is the owner of the relationship and defines the 'professore_titolo' join table.
     */
    @ManyToMany
    @JoinTable(
        name = "professore_titolo",                    
        joinColumns = @JoinColumn(name = "professore_id"),
        inverseJoinColumns = @JoinColumn(name = "titolo_id")
    )
    private Set<TitoloStudio> titoli;
    
    /**
     * Many-to-Many relationship with RegistroEntity.
     * This is the inverse side of the relationship, mapped by 'listaProf' inside RegistroEntity.
     */
    @ManyToMany(mappedBy="listaProf")
    private List<RegistroEntity> registro = new ArrayList<>();
                
    //  CONSTRUCTORS 
    public ProfessoreEntity() {}

    public ProfessoreEntity(String nome, String cognome, String codiceFiscale, String materia, LocalDate dataNascita, String matricola) {
        super(nome, cognome, codiceFiscale);
        this.materia = materia;
        this.dataNascita = dataNascita;
        this.matricola = matricola;
    }

    //  GETTERS AND SETTERS 
    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Set<TitoloStudio> getTitoli() {
        return titoli;
    }

    public void setTitoli(Set<TitoloStudio> titoli) {
        this.titoli = titoli;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<RegistroEntity> getRegistro() {
        return registro;
    }

    public void setRegistro(List<RegistroEntity> registro) {
        this.registro = registro;
    }

    //  JPA COMPLIANT EQUALS & HASHCODE 
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfessoreEntity)) return false;
        ProfessoreEntity that = (ProfessoreEntity) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}