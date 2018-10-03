package org.ss.segabank.exception;

import org.ss.segabank.domain.Account;

public class BalanceNotEnoughException extends Exception {
	
	private Account account;
	private double requestedAmount;
	
	public BalanceNotEnoughException( Account account, double requestedAmount ) {
		this.account = account;
		this.requestedAmount = requestedAmount;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount( Account account ) {
		this.account = account;
	}
	
	public double getRequestedAmount() {
		return requestedAmount;
	}
	
	public void setRequestedAmount( double requestedAmount ) {
		this.requestedAmount = requestedAmount;
	}
	
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder( "Le compte numéro : " );
		sb.append( account.getId() );
		sb.append( " ne dispose pas d'un solde suffisant pour permettre le retrait de " + requestedAmount + " €." );
		return sb.toString();
	}
}
