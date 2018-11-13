package com.danabijak.demo.banking.accounts.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.accounts.exceptions.BankAccountException;
import com.danabijak.demo.banking.accounts.http.AccountStatementClientResponse;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.transactions.model.AccountTransactions;


@Service
public interface AccountService {
	
	/**
	 * Get all debit transactions of the specified bank account.
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<List<Transaction>> getDebitTransactionsOf(BankAccount account) throws BankAccountException;
	
	/**
	 * Get all credit transaction of the specified bank account
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<List<Transaction>> getCreditTransactionsOf(BankAccount account) throws BankAccountException;
	
	/**
	 * Get all transaction of the specified bank account
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<AccountTransactions> getTransactionsOf(BankAccount account) throws BankAccountException;
	
	/**
	 * Get all transaction of the specified bank account
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<BankAccount> getBankAccount(long id) throws BankAccountException;
	
	/**
	 * Get all transaction of the specified bank account as a statement
	 * @param account
	 * @return
	 * @throws TransactionServiceException
	 */
	@Async("asyncExecutor")
	public CompletableFuture<AccountStatementClientResponse> getAccountStatement(long id) throws BankAccountException;

}
