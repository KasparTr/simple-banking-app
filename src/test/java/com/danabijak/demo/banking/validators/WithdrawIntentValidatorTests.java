package com.danabijak.demo.banking.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.danabijak.demo.banking.GlobalMethodsForTesting;
import com.danabijak.demo.banking.entity.Balance;
import com.danabijak.demo.banking.entity.BankAccount;
import com.danabijak.demo.banking.entity.TransactionIntent;
import com.danabijak.demo.banking.entity.TransactionIntentStatus;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.entity.TransactionIntentStatus.TRANSFER_STATUS;
import com.danabijak.demo.banking.transactions.model.TransactionIntentBuilder;
import com.danabijak.demo.banking.transactions.model.ValidationReport;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WithdrawIntentValidatorTests {
	private TransactionIntentValidator validator = new WithdrawIntentValidator();
	
	
	@Test
	public void testValidate_pass_valid_intent() {

		User beneficiary = GlobalMethodsForTesting.getDummyDefaultUser();
		User source = GlobalMethodsForTesting.getDummyDefaultUser();

		TransactionIntent validItent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Withdraw info"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(Money.of(CurrencyUnit.USD, 20))
				.build();
		ValidationReport report = validator.validate(validItent);
		assertTrue(report.valid);
		
	}
	
	@Test
	public void testValidate_inValid_intent(){
		User beneficiary = GlobalMethodsForTesting.getDummyDefaultUser();
		User source = GlobalMethodsForTesting.getDummyDefaultUser();

		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Withdraw nfo"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(Money.of(CurrencyUnit.USD, 12309133.45))
				.build();
		ValidationReport report = validator.validate(intent);
		assertFalse(report.valid);
	}
	
	@Test
	public void testValidate_inValid_intent_negative_balance_after_withdraw(){
		User beneficiary = GlobalMethodsForTesting.getDummyDefaultUser();
		User source = GlobalMethodsForTesting.getDummyDefaultUser();

		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(source.getBankAccount().getBalance().getTotal().plus(new BigDecimal(100)))
				.build();
		ValidationReport report = validator.validate(intent);
		assertFalse(report.valid);
		boolean limitDescFound = false;
		for(String desc:report.faultDescriptions.get()) {
			if(desc.contains("Balance limit")) limitDescFound=true;
		}
		assertTrue(limitDescFound);
		
	}
	
	@Test
	public void testValidate_inValid_intent_over_withdraw_limit(){
		User beneficiary = GlobalMethodsForTesting.getDummyDefaultUser();
		User source = GlobalMethodsForTesting.getDummyDefaultUser();

		TransactionIntent intent = new TransactionIntentBuilder()
				.status(new TransactionIntentStatus(TRANSFER_STATUS.CREATED, "Deposit"))
				.beneficiary(beneficiary)
				.source(source)
				.amount(Money.of(CurrencyUnit.USD, 20000.45))
				.build();
		ValidationReport report = validator.validate(intent);
		
		assertFalse(report.valid);
		boolean limitDescFound = false;
		for(String desc:report.faultDescriptions.get()) {
			if(desc.contains("Transaction limit")) limitDescFound=true;
		}
		assertTrue(limitDescFound);
		
	}


}
