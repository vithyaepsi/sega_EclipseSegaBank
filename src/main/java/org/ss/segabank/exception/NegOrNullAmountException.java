package org.ss.segabank.exception;

import org.ss.segabank.domain.Account;

public class NegOrNullAmountException extends NumberFormatException {
	
	private Account account;
	private double amount;
	
	public NegOrNullAmountException( Account account, double amount ) {
		this.account = account;
		this.amount = amount;
	}
	
	public NegOrNullAmountException( String s, Account account, double amount ) {
		super( s );
		this.account = account;
		this.amount = amount;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount( Account account ) {
		this.account = account;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount( double amount ) {
		this.amount = amount;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
