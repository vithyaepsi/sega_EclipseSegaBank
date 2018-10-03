package org.ss.segabank.domain;

import org.ss.segabank.exception.BalanceNotEnoughException;
import org.ss.segabank.exception.NegOrNullAmountException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue( "SIMPLE_ACCOUNT" )
public class SimpleAccount extends Account {
	
	private double overdraft;
	
	public SimpleAccount() { super(); }
	
	public SimpleAccount( double overdraft ) {
		super();
		this.overdraft = overdraft;
	}
	
	public SimpleAccount( double balance, double overdraft ) {
		super( balance );
		this.overdraft = overdraft;
	}
	
	public SimpleAccount( Long id, double balance, double overdraft ) {
		super( id, balance );
		this.overdraft = overdraft;
	}
	
	public double getOverdraft() {
		return overdraft;
	}
	
	public void setOverdraft( double overdraft ) {
		this.overdraft = overdraft;
	}
	
	@Override
	public void drawMoney( double amount ) throws BalanceNotEnoughException {
		if ( amount <= 0 ) throw new NegOrNullAmountException( this, amount );
		else if ( amount > balance + overdraft ) throw new BalanceNotEnoughException( this, amount );
		else {
			Transaction transaction = new Transaction( Transaction.Type.DM, new Date(), amount, balance );
			balance -= amount;
			transaction.setBalanceAfter( balance );
			this.addTransaction( transaction );
		}
	}
	
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer( "SimpleAccount{" );
		sb.append( "id=" ).append( id );
		sb.append( ", balance=" ).append( balance );
		sb.append( ", overdraft=" ).append( overdraft );
		sb.append( '}' );
		return sb.toString();
	}
}
