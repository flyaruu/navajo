package com.dexels.navajo.adapter.mt940;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AvailableBalance implements Serializable {
	private static final long serialVersionUID = 1986520816142772323L;
	private String debitCreditIndicator;
	private Date date;
	private String currency;
	private BigDecimal amount;
	
	public AvailableBalance() {}
	
	public String toString() {
		StringBuffer output = new StringBuffer();
		output.append("\t\t** Start of AvailableBalance **" + '\n');
		output.append("\t\t\tDebitCreditIndicator : " + this.getDebitCreditIndicator() + '\n');
		output.append("\t\t\tDate                 : " + this.getDate() + '\n');
		output.append("\t\t\tCurrency             : " + this.getCurrency() + '\n');
		output.append("\t\t\tAmount               : " + this.getAmount() + '\n');
		output.append("\t\t** End of AvailableBalance **");
		return output.toString();
	}
	
	public String getDebitCreditIndicator() {
		return debitCreditIndicator;
	}
	public void setDebitCreditIndicator(String debitCreditIndicator) {
		this.debitCreditIndicator = debitCreditIndicator;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
