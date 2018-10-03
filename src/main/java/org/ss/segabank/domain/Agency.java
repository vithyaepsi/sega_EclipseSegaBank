package org.ss.segabank.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "agency" )
public class Agency implements Serializable {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	@Column( length = 10 )
	private String code;
	@Embedded
	private Address address;
	@OneToMany( mappedBy = "agency", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
	private Set<Account> accounts;
	
	public Agency() {
		address = new Address();
		accounts = new HashSet<>();
	}
	
	public Agency( String code, Address address ) {
		this();
		this.code = code;
		this.address = address;
	}
	
	public Agency( Long id, String code, Address address ) {
		this.id = id;
		this.code = code;
		this.address = address;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId( Long id ) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode( String code ) {
		this.code = code;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress( Address address ) {
		this.address = address;
	}
	
	public Set<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts( Set<Account> accounts ) {
		this.accounts = accounts;
	}
	
	public void addAccount( Account account ) {
		account.setAgency( this );
	}
	
	public void removeAccount( Account account ) {
		account.setAgency( null );
	}
	
	
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer( "Agency{" );
		sb.append( "id=" ).append( id );
		sb.append( ", code='" ).append( code ).append( '\'' );
		sb.append( ", address=" ).append( address );
		sb.append( '}' );
		return sb.toString();
	}
}