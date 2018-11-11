package com.danabijak.demo.banking.transactions.http;

import java.util.Date;
import java.util.List;

import com.danabijak.demo.banking.entity.Transaction;

public class BankAccountStatementClientResponse {
	public final long bankAccountId;
	public final String user;
	public final List<TransactionClientResponse> transactions;
	public final Date createdAt;
	
	public BankAccountStatementClientResponse(
			long bankAccountId,
			String user,
			List<TransactionClientResponse> transactions) {
		super();
		this.bankAccountId = bankAccountId;
		this.user = user;
		this.transactions = transactions;
		this.createdAt = new Date();
	}
}