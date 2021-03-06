package com.danabijak.demo.banking.domain.transactions.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.danabijak.demo.banking.core.ValidationReport;
import com.danabijak.demo.banking.domain.transactions.entity.TransactionIntent;

public abstract class TransactionIntentValidatorImpl implements TransactionIntentValidator {

	@Override
	public ValidationReport validate(TransactionIntent intent) {
		ValidationReport report;
		boolean isValid = true;
		List<String> faults = new ArrayList<>();
		
		
		if(!entityTransactionLimitsAllowFor(intent)) {
			isValid=false;
			faults.add("Transaction limit reached.");
		}
		
		// TODO: FOr better error messaging implement messaged Exceptions here.
		if(!entityBalanceAllowsFor(intent)) {
			isValid=false;
			faults.add("Balance limit reached.");
		}
			
		
		if(!isValid) {
			intent.setIntentAsNotValid();
			Optional<List<String>> faultyParts = Optional.of(faults);
			report = new ValidationReport(false, faultyParts);
		}else {
			intent.setIntentAsValid();
			report = new ValidationReport(true);
		}
			
		return report;
				
	}
	
	protected abstract boolean entityTransactionLimitsAllowFor(TransactionIntent intent);
	protected abstract boolean entityBalanceAllowsFor(TransactionIntent intent);
}