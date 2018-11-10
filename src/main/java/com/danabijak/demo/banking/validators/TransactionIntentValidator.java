package com.danabijak.demo.banking.validators;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.model.ValidationReport;

public interface TransactionIntentValidator {
	
	public ValidationReport validate(TransactionIntent intent);
}