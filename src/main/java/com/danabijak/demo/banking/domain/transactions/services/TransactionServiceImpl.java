package com.danabijak.demo.banking.domain.transactions.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.domain.transactions.entity.Transaction;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionNotFoundException;
import com.danabijak.demo.banking.domain.transactions.exceptions.TransactionServiceException;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionIntentRepository;
import com.danabijak.demo.banking.domain.transactions.repositories.TransactionRepository;

@Service
public abstract class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Autowired
	private TransactionIntentRepository transactionIntentRepo;
	
	@Override
	@Async("asyncExecutor")
	public void porcessAllIntents() throws TransactionServiceException {
		List<TransactionIntent> allIntents = transactionIntentRepo.findAll();
		for(TransactionIntent i:allIntents) {
			if(!i.isPaid()) {
				processIntent(i);
			}
		}
	}
		
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> process(TransactionIntent intent) throws TransactionServiceException {
		System.out.println("TransactionServiceImp | processing intent: " + intent.toString());
		try {
			if(intent.isValid()) {				
				Transaction transaction = processIntent(intent);
				CompletableFuture<Transaction> future = new CompletableFuture<>();
				future.complete(transaction);
				return future;
			}else {
				throw new TransactionServiceException("Transaction intent is not valid. Transaction not made!");
			}
		}catch(Exception e) {
			System.out.println("TransactionServiceImp | processing | error: " + e.getMessage());

			throw new TransactionServiceException("Cannot process intent, error: " + e.getMessage());
		}
		
	}
	
	@Override
	@Async("asyncExecutor")
	public CompletableFuture<Transaction> findTransactionBy(long id) throws TransactionNotFoundException {
		Optional<Transaction> transaction = transactionRepo.findById(id);
		
		if(transaction.isPresent()) {
			CompletableFuture<Transaction> future = new CompletableFuture<>();
			future.complete(transaction.get());
			return future;
		}
		else 
			throw new TransactionNotFoundException(String.format("Transaction with ID %s not found", id));

	}
	
	private Transaction processIntent(TransactionIntent intent) throws TransactionServiceException{
		
		updateBalances(intent);
		
		Transaction transaction = new Transaction(
				intent.amount, 
				intent.beneficiary.getBankAccount(), 
				intent.source.getBankAccount(), 
				"Successfully made transaction");
		
		System.out.println("transactionRepo: " + transactionRepo);
		
		transactionRepo.save(transaction);
		intent.setPaidTo(true);
		return transaction;
	}
	
	protected abstract void updateBalances(TransactionIntent intent);



}