package com.gitahinganga.pesabu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoanActivity extends PesabuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.loan, menu);
		return true;
	}

	public void clearField(View view) {
		EditText editText = null;
		switch (view.getId()) {
		case R.id.loanPrincipleButton:
			editText = (EditText) findViewById(R.id.loanPrincipleEditText);
			break;
		case R.id.loanInterestButton:
			editText = (EditText) findViewById(R.id.loanInterestEditText);
			break;
		case R.id.loanPeriodButton:
			editText = (EditText) findViewById(R.id.loanPeriodEditText);
			break;
		case R.id.loanInstallmentButton:
			editText = (EditText) findViewById(R.id.loanInstallmentEditText);
			break;
		}
		editText.setText("");
		editText.requestFocus();
	}

	public void calculateLoan(View view) {
		List<EditText> fieldList = new ArrayList<EditText>();
		fieldList.add((EditText) findViewById(R.id.loanPrincipleEditText));
		fieldList.add((EditText) findViewById(R.id.loanInterestEditText));
		fieldList.add((EditText) findViewById(R.id.loanPeriodEditText));
		fieldList.add((EditText) findViewById(R.id.loanInstallmentEditText));

		List<EditText> emptyFieldList = new ArrayList<EditText>();

		for (EditText field : fieldList) {
			if (field.getText().toString().equals("")) {
				emptyFieldList.add(field);
			}
		}

		if (emptyFieldList.size() == 1) {
			EditText emptyField = emptyFieldList.get(0);
			DecimalFormat decimalFormat = Util.createDecimalFormat();

			double principle = 0.0;
			double interest = 0.0;
			int period = 0;
			double installment = 0.0;

			switch (emptyField.getId()) {
			case R.id.loanPrincipleEditText:
				interest = Double.parseDouble(fieldList.get(1).getText()
						.toString().replace(",", ""));
				period = Integer
						.parseInt(fieldList.get(2).getText().toString());
				installment = Double.parseDouble(fieldList.get(3).getText()
						.toString().replace(",", ""));
				principle = calculatePrinciple(interest, period, installment);
				emptyField.setText(Util.comify(String.valueOf(decimalFormat
						.format(principle))));
				break;
			case R.id.loanInterestEditText:
				principle = Double.parseDouble(fieldList.get(0).getText()
						.toString().replace(",", ""));
				period = Integer
						.parseInt(fieldList.get(2).getText().toString());
				installment = Double.parseDouble(fieldList.get(3).getText()
						.toString().replace(",", ""));
				interest = calculateInterest(principle, period, installment);
				emptyField.setText(Util.comify(String.valueOf(interest)));
				break;
			case R.id.loanPeriodEditText:
				principle = Double.parseDouble(fieldList.get(0).getText()
						.toString().replace(",", ""));
				interest = Double.parseDouble(fieldList.get(1).getText()
						.toString().replace(",", ""));
				installment = Double.parseDouble(fieldList.get(3).getText()
						.toString().replace(",", ""));
				period = calculatePeriod(principle, interest, installment);
				emptyField.setText(String.valueOf(period));
				break;
			case R.id.loanInstallmentEditText:
				principle = Double.parseDouble(fieldList.get(0).getText()
						.toString().replace(",", ""));
				interest = Double.parseDouble(fieldList.get(1).getText()
						.toString().replace(",", ""));
				period = Integer
						.parseInt(fieldList.get(2).getText().toString());
				installment = calculateInstallment(principle, interest, period);
				emptyField.setText(Util.comify(String.valueOf(decimalFormat
						.format(installment))));
				break;
			}

			double totalRepaid = installment * period;
			double totalInterest = totalRepaid - principle;

			EditText totalRepaidEditText = (EditText) findViewById(R.id.totalRepaidEditText);
			EditText totalInterestEditText = (EditText) findViewById(R.id.totalInterestEditText);

			totalRepaidEditText.setText(Util.comify(String
					.valueOf(decimalFormat.format(totalRepaid))));
			totalInterestEditText.setText(Util.comify(String
					.valueOf(decimalFormat.format(totalInterest))));

			Util.hideInput(this, view);
		} else {
			String message = this.getString(R.string.loan_calculator_message1);
			EditText fieldToFocus = null;
			if (!emptyFieldList.isEmpty()) {
				fieldToFocus = emptyFieldList.get(0);
			} else {
				fieldToFocus = fieldList.get(0);
				message += " " + this.getString(R.string.loan_calculator_message2);
			}
			Util.showInformationMessage(this, message);
			fieldToFocus.requestFocus();
		}
	}

	private double calculatePrinciple(double interest, int period,
			double installment) {
		double principle = 0.0;
		double monthlyInterest = ((interest / 12) / 100);
		principle = installment
				/ ((monthlyInterest / (Math.pow((1 + monthlyInterest), period) - 1)) + monthlyInterest);
		return principle;
	}

	private double calculateInterest(double principle, int period,
			double installment) {
		double interest = 0.0;
		// double monthlyInterest = ((interest / 12) / 100);
		double installmentOverPrinciple = installment / principle;
		interest = (Math.pow(
				(installmentOverPrinciple / (installmentOverPrinciple - 1)),
				(1 / period)) - 1) * 12;
		return interest;
	}

	private int calculatePeriod(double principle, double interest,
			double installment) {
		int period = 0;
		double monthlyInterest = ((interest / 12) / 100);
		double doublePeriod = Math
				.round(Math
						.log(((monthlyInterest / ((installment / principle) - monthlyInterest))) + 1)
						/ Math.log((1 + monthlyInterest)));
		period = (int) doublePeriod;
		return period;
	}

	private double calculateInstallment(double principle, double interest,
			int period) {
		double installment = 0.0;
		double monthlyInterest = ((interest / 12) / 100);
		installment = principle
				* ((monthlyInterest / (Math.pow((1 + monthlyInterest), period) - 1)) + monthlyInterest);
		return installment;
	}
}
