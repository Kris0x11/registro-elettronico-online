package christian.skillfactory.registro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="account")
public class Account implements UserDetails {
	@Override
	public int hashCode() {
		return Objects.hash(id, password, ruolo, username);
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    
	    if (!(o instanceof Account)) return false;
	    
	    Account account = (Account) o;
	    
	    return id != null && id.equals(account.id);
	}
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	private String username;
	
	private String password;
	

	/**
     * Many-to-One relationship with Ruolo.
     * Multiple accounts can share the same role (e.g., ADMIN, PROFESSOR).
     */
	@ManyToOne
	@JoinColumn(name = "ruolo_nome", referencedColumnName = "nome")
	private Ruolo ruolo;
	
	// CONSTRUCTORS
	public Account() {};
	
	public Account(String username, String password)
	{
		this.username=username;
		this.password=password;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	 @Override
	    public String getPassword() {
	        return password;
	    }

	    @Override
	    public String getUsername() {
	        return username;
	    }

	
	    
	    /**
	     * SPRING SECURITY USERDETAILS IMPLEMENTATION
	     * Returns the authorities granted to the user.
	     * In this setup, the Ruolo entity acts directly as a GrantedAuthority.
	     */

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(ruolo);
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
