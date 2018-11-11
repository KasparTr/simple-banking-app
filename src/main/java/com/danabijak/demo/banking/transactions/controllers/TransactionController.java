package com.danabijak.demo.banking.transactions.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.danabijak.demo.banking.entity.Transaction;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.transactions.factories.TransactionIntentFactory;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientRequest;
import com.danabijak.demo.banking.transactions.http.TransactionIntentClientResponse;
import com.danabijak.demo.banking.transactions.model.TransactionClientRequest;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.services.TransactionIntentService;
import com.danabijak.demo.banking.transactions.services.TransactionService;
import com.danabijak.demo.banking.users.services.UserService;

@RestController
public class TransactionController {

	
	@Autowired
	@Qualifier("depositIntentService")
	private TransactionIntentService depositIntentService;
	
	@Autowired
	@Qualifier("withdrawIntentService")
	private TransactionIntentService withdrawIntentService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserService userService;
	

	@GetMapping("/services/transactions/{id}")
	public Transaction findById(@PathVariable long id){
		
		return transactionService.findTransactionBy(id);
	}
	
	
	//TODO: Enable multiple account support
	@PostMapping("/services/transactions/deposit")
	public ResponseEntity<TransactionIntentClientResponse> deposit(@Valid @RequestBody TransactionClientRequest request) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		
		User bank = userService.findByUsername("bankItself@bank.com");
		User user = userService.find(request.entity.id);
	
		TransactionIntent intent = factory.create(bank, user, request);		
		TransactionIntent publishedIntent = depositIntentService.attemptPublish(intent);
		
		// should be end of it, but right now instead of PUB/SUB we send directly to:
		// transactionService.processDeposit(intent)
		transactionService.porcess(publishedIntent);
		
		TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
		return ResponseEntity.ok(response);
	}
	
	//TODO: Enable multiple account support
	@PostMapping("/services/transactions/withdraw")
	public ResponseEntity<TransactionIntentClientResponse> withdraw(@Valid @RequestBody TransactionClientRequest request) {
		TransactionIntentFactory factory = new TransactionIntentFactory();
		
		User bank = userService.findByUsername("bankItself@bank.com");
		User user = userService.find(request.entity.id);
	
		
		TransactionIntent intent = factory.create(user, bank, request);		
		TransactionIntent publishedIntent = withdrawIntentService.attemptPublish(intent);
		
		// should be end of it, but right now instead of PUB/SUB we send directly to:
		// transactionService.processDeposit(intent)
		transactionService.porcess(publishedIntent);
		
		TransactionIntentClientResponse response = new TransactionIntentClientResponse(publishedIntent.isValid(), "Intent Published", publishedIntent);
		return ResponseEntity.ok(response);
		
	}	

}
