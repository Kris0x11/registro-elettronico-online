package christian.skillfactory.registro.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "presenza")
public class Presenza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    
    @Column(name = "nome_classe")
    private String nomeClasse;

    /**
     * Many-to-One relationship with StudenteEntity.
     * Multiple attendance records can be linked to a single student.
     */
    @ManyToOne
    @JoinColumn(name = "studente_id")
    private StudenteEntity studente;

    private boolean presente;

    // CONSTRUCTORS 
    public Presenza() {}

    public Presenza(LocalDate data, String nomeClasse, StudenteEntity studente, boolean presente) {
        this.data = data;
        this.nomeClasse = nomeClasse;
        this.studente = studente;
        this.presente = presente;
    }

    //  GETTERS AND SETTERS 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public StudenteEntity getStudente() {
        return studente;
    }

    public void setStudente(StudenteEntity studente) {
        this.studente = studente;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    // TO STRING 
    @Override
    public String toString() {
        return "Appello [id=" + id + ", data=" + data + ", nomeClasse=" + nomeClasse + ", studente=" + studente
                + ", presente=" + presente + "]";
    }

    //  JPA COMPLIANT EQUALS & HASHCODE =
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false; 
        if (!(o instanceof Presenza)) return false;
        Presenza presenza = (Presenza) o;
        return id != null && id.equals(presenza.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}