package christian.skillfactory.registro.model;

import java.util.Objects;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;




/**
 * MappedSuperclass to share common personal data fields 
 * with inherited entities like ProfessoreEntity and StudenteEntity.
 */

@MappedSuperclass
public abstract class Persona {

    @Override
	public int hashCode() {
		return Objects.hash(codiceFiscale, cognome, id, nome);
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        
        // Using instanceof ensures compatibility with Hibernate lazy-loading proxies
        if (!(obj instanceof Persona)) return false;
        
        Persona other = (Persona) obj;
        return id != null && id.equals(other.id);
    }

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nome;
    protected String cognome;
    protected String codiceFiscale;
    
 // CONSTRUCTORS
    public Persona() {}
    
    public Persona(String nome, String cognome,String codiceFiscale)
    {
    	this.nome=nome;
    	this.cognome=cognome;
    	this.codiceFiscale=codiceFiscale;
    }

 // GETTERS AND SETTERS 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
    
    
  
    
    
}
