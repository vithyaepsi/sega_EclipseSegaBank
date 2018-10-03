package org.ss.segabank.domain;

import org.ss.segabank.exception.BalanceNotEnoughException;
import org.ss.segabank.exception.NegOrNullAmountException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue( "CHARGED_ACCOUNT" )
public class ChargedAccount extends Account {
	
	private double chargeRate = 0.05;
	
	public ChargedAccount() {
		super();
	}
	
	public ChargedAccount( double chargeRate ) {
		super();
		this.chargeRate = chargeRate;
	}
	
	public ChargedAccount( double balance, double chargeRate ) {
		super( balance );
		this.chargeRate = chargeRate;
	}
	
	public ChargedAccount( Long id, double balance, double chargeRate ) {
		super( id, balance );
		this.chargeRate = chargeRate;
	}
	
	public double getChargeRate() {
		return chargeRate;
	}
	
	public void setChargeRate( double chargeRate ) {
		this.chargeRate = chargeRate;
	}
	
	@Override
	public void payMoney( double amount ) throws NegOrNullAmountException {
		if ( amount <= 0 ) throw new NegOrNullAmountException( this, amount );
		else {
			Transaction transaction = new Transaction( Transaction.Type.PM, new Date(), amount, balance );
			balance += amount * (1 - chargeRate);
			transaction.setBalanceAfter( balance );
			this.addTransaction( transaction );
		}
	}
	
	@Override
	public void drawMoney( double amount ) throws BalanceNotEnoughException, NegOrNullAmountException {
		if ( amount <= 0 ) throw new NegOrNullAmountException( this, amount );
		else if ( amount > balance ) throw new BalanceNotEnoughException( this, amount );
		else {
			Transaction transaction = new Transaction( Transaction.Type.DM, new Date(), amount, balance );
			balance -= amount * (1 + chargeRate);
			transaction.setBalanceAfter( balance );
			this.addTransaction( transaction );
		}
	}
	
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer( "ChargedAccount{" );
		sb.append( "id=" ).append( id );
		sb.append( ", balance=" ).append( balance );
		sb.append( ", chargeRate=" ).append( chargeRate );
		sb.append( '}' );
		return sb.toString();
	}
}
