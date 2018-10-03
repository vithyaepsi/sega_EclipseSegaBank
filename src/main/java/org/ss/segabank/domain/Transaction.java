package org.ss.segabank.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "bank_transaction" )
public class Transaction implements Serializable {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	@Enumerated( EnumType.STRING )
	private Type type;
	private Date date;
	private double amount;
	private double balanceBefore;
	private double balanceAfter;
	@ManyToOne
	@JoinColumn( name = "id_account" )
	private Account account;
	
	public Transaction() {}
	
	public Transaction( Type type, Date date, double amount, double balanceBefore ) {
		this.type = type;
		this.date = date;
		this.amount = amount;
		this.balanceBefore = balanceBefore;
	}
	
	public Transaction( Type type, double balanceBefore, double balanceAfter, Date date ) {
		this.type = type;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
		this.date = date;
	}
	
	public Transaction( Long id, Type type, double balanceBefore, double balanceAfter, Date date ) {
		this.id = id;
		this.type = type;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId( Long id ) {
		this.id = id;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType( Type type ) {
		this.type = type;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate( Date date ) {
		this.date = date;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount( double amount ) {
		this.amount = amount;
	}
	
	public double getBalanceBefore() {
		return balanceBefore;
	}
	
	public void setBalanceBefore( double balanceBefore ) {
		this.balanceBefore = balanceBefore;
	}
	
	public double getBalanceAfter() {
		return balanceAfter;
	}
	
	public void setBalanceAfter( double balanceAfter ) {
		this.balanceAfter = balanceAfter;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount( Account account ) {
		if ( this.account != null ) {
			this.account.getTransactions().remove( this );
		}
		this.account = account;
		if ( this.account != null ) {
			this.account.getTransactions().add( this );
		}
	}
	
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer( "Transaction{" );
		sb.append( "id=" ).append( id );
		sb.append( ", date=" ).append( date );
		sb.append( ", type=" ).append( type );
		sb.append( ", amount=" ).append( amount );
		sb.append( ", balanceBefore=" ).append( balanceBefore );
		sb.append( ", balanceAfter=" ).append( balanceAfter );
		sb.append( '}' );
		return sb.toString();
	}
	
	public enum Type {
		IPM( "Versement initial" ), PM( "Versement" ), DM( "Retrait" ), SIM( "Interêt" );
		
		private String value;
		
		private Type( String value ) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
}
