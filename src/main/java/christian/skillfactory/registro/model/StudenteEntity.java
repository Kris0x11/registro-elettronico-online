package christian.skillfactory.registro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "studente")
public class StudenteEntity extends Persona {

    private String matricola;
    private LocalDate dataNascita;
    
    /**
     * One-to-One relationship with Account.
     * Links the student to their personal login credentials.
     */
    @OneToOne
    @JoinColumn(name = "id_account")
    private Account account;
    
    /**
     * Many-to-One relationship with RegistroEntity.
     * Multiple students belong to a single class registry.
     */
    @ManyToOne
    @JoinColumn(name = "registro_id")
    private RegistroEntity regstudenti;
    
    /**
     * One-to-Many relationship with Voto.
     * Mapped by the 'votoStudente' field inside the Voto entity.
     */
    @OneToMany(mappedBy = "votoStudente")
    private List<Voto> voti = new ArrayList<>();
    
    /**
     * Many-to-Many relationship with Sanzione.
     * Inverse side of the relationship, mapped by the 'studenti' field in the Sanzione entity.
     */
    @ManyToMany(mappedBy = "studenti")
    private List<Sanzione> listaSanzione;
    
    /**
     * One-to-Many relationship with Presenza.
     * Mapped by the 'studente' field inside the Presenza entity.
     */
    @OneToMany(mappedBy = "studente")
    private List<Presenza> listaPresenze;

    // === CONSTRUCTORS ===
    public StudenteEntity() {}

    public StudenteEntity(String nome, String cognome, String codiceFiscale, String matricola, LocalDate dataNascita) {
        super(nome, cognome, codiceFiscale);
        this.matricola = matricola;
        this.dataNascita = dataNascita;
    }

    // === GETTERS AND SETTERS ===
    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public RegistroEntity getRegstudenti() {
        return regstudenti;
    }

    public void setRegstudenti(RegistroEntity regstudenti) {
        this.regstudenti = regstudenti;
    }

    public List<Voto> getVoti() {
        return voti;
    }

    public void setVoti(List<Voto> voti) {
        this.voti = voti;
    }

    public List<Sanzione> getListaSanzione() {
        return listaSanzione;
    }

    public void setListaSanzione(List<Sanzione> listaSanzione) {
        this.listaSanzione = listaSanzione;
    }

    public List<Presenza> getListaPresenze() {
        return listaPresenze;
    }

    public void setListaPresenze(List<Presenza> listaPresenze) {
        this.listaPresenze = listaPresenze;
    }

    // === JPA COMPLIANT EQUALS & HASHCODE ===
    // Relying strictly on the inherited primary key (id) from Persona.
    // This safely isolates object comparisons from complex, nested collection graphs and prevents recursion loops.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudenteEntity)) return false;
        StudenteEntity that = (StudenteEntity) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
