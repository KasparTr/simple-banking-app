package com.danabijak.demo.banking.transactions.services;


import javax.annotation.Resource;
import org.joda.money.Money;
import org.springframework.stereotype.Service;

import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.TransactionalEntity;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.model.ValidationReport;
import com.danabijak.demo.banking.validators.DepositIntentValidator;
import com.danabijak.demo.banking.validators.TransactionIntentValidator;

@Service
@Resource(name="depositIntentService")
public class DepositIntentService extends TransactionIntentServiceImpl{
//public class DepositIntentService implements TransactionIntentService{

	@Override
	protected ValidationReport validateIntent(TransactionIntent intent) {
		TransactionIntentValidator validator = new DepositIntentValidator();
		return validator.validate(intent);
	}
	
	@Override
	protected void reserverParticipantsBalance(TransactionIntent intent) {
		intent.beneficiary.getLimits().decreaseAllowedDeposit(intent.amount.getAmount());
	}
	
	@Override
	protected TransactionIntent makeTransactionIntent(TransactionalEntity user, TransactionalEntity bank, Money money) {
		return new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Withdraw"))
				.source(bank)
				.beneficiary(user)
				.amount(money)
				.build();
	}
}
