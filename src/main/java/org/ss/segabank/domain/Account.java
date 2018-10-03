package org.ss.segabank.domain;

import org.ss.segabank.exception.BalanceNotEnoughException;
import org.ss.segabank.exception.NegOrNullAmountException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "bank_account" )
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn( name = "type" )
public abstract class Account implements Serializable {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	protected Long id;
	protected double balance;
	@ManyToOne
	@JoinColumn( name = "id_agency" )
	protected Agency agency;
	@OneToMany( mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
	protected Set<Transaction> transactions;
	
	public Account() {
		transactions = new HashSet<>();
	}
	
	public Account( double balance ) {
		
		this();
		this.balance = balance;
	}
	
	public Account( Long id, double balance ) {
		this();
		this.id = id;
		this.balance = balance;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId( Long id ) {
		this.id = id;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public final void setBalance( double balance ) {}
	
	public void payMoney( double amount ) throws NegOrNullAmountException {
		
		if ( amount <= 0 ) throw new NegOrNullAmountException( this, amount );
		else {
			Transaction transaction = new Transaction( Transaction.Type.PM, new Date(), amount, balance );
			balance += amount;
			transaction.setBalanceAfter( balance );
			this.addTransaction( transaction );
		}
	}
	
	public void drawMoney( double amount ) throws BalanceNotEnoughException, NegOrNullAmountException {
		if ( amount <= 0 ) throw new NegOrNullAmountException( this, amount );
		else if ( amount > balance ) throw new BalanceNotEnoughException( this, amount );
		else {
			Transaction transaction = new Transaction( Transaction.Type.DM, new Date(), amount, balance );
			balance -= amount;
			transaction.setBalanceAfter( balance );
			this.addTransaction( transaction );
		}
	}
	
	public Agency getAgency() {
		return agency;
	}
	
	public void setAgency( Agency agency ) {
		if ( this.agency != null ) {
			this.agency.getAccounts().remove( this );
		}
		this.agency = agency;
		if ( this.agency != null ) {
			this.agency.getAccounts().add( this );
		}
	}
	
	public Set<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions( Set<Transaction> transactions ) {
		this.transactions = transactions;
	}
	
	public void addTransaction( Transaction transaction ) { transaction.setAccount( this );	}
	
	public void removeTransaction( Transaction transaction ) {	transaction.setAccount( null );	}
	
	public void initBalance( double amount ) {
		if ( amount <= 0 ) throw new NegOrNullAmountException( this, amount );
		else {
			Transaction transaction = new Transaction( Transaction.Type.IPM, new Date(), amount, balance );
			balance += amount;
			transaction.setBalanceAfter( balance );
			this.addTransaction( transaction );
		}
	}
	
	
}
