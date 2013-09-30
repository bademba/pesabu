package com.gitahinganga.pesabu.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import com.gitahinganga.pesabu.R;
import com.gitahinganga.pesabu.business.CalculationManager;
import com.gitahinganga.pesabu.business.CalculationPiece;
import com.gitahinganga.pesabu.business.Calculator;
import com.gitahinganga.pesabu.business.calculationpiece.LoanOperand;
import com.gitahinganga.pesabu.business.calculationpiece.LoanResult;

public class LoanActivity extends PesabuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan);
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

	public void calculate(View view) {

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
			Map<CalculationPiece, Object> operands = new HashMap<CalculationPiece, Object>();
			Map<CalculationPiece, Object> results = null;

			DecimalFormat decimalFormat = Util.createDecimalFormat();
			EditText emptyField = emptyFieldList.get(0);

			switch (emptyField.getId()) {
				case R.id.loanPrincipleEditText:
					operands.put(LoanOperand.MISSING, LoanOperand.PRINCIPAL);
					operands.put(LoanOperand.RATE, Double.parseDouble(fieldList.get(1).getText().toString().replace(",", "")));
					operands.put(LoanOperand.PERIOD, Integer.parseInt(fieldList.get(2).getText().toString()));
					operands.put(LoanOperand.INSTALLMENT, Double.parseDouble(fieldList.get(3).getText().toString().replace(",", "")));
					results = CalculationManager.calculate(operands, Calculator.Type.LOAN);
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanOperand.PRINCIPAL)))));
					break;
				case R.id.loanInterestEditText:
					operands.put(LoanOperand.MISSING, LoanOperand.RATE);
					operands.put(LoanOperand.PRINCIPAL, Double.parseDouble(fieldList.get(0).getText().toString().replace(",", "")));
					operands.put(LoanOperand.PERIOD, Integer.parseInt(fieldList.get(2).getText().toString()));
					operands.put(LoanOperand.INSTALLMENT, Double.parseDouble(fieldList.get(3).getText().toString().replace(",", "")));
					results = CalculationManager.calculate(operands, Calculator.Type.LOAN);
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanOperand.RATE)))));
					break;
				case R.id.loanPeriodEditText:
					operands.put(LoanOperand.MISSING, LoanOperand.PERIOD);
					operands.put(LoanOperand.PRINCIPAL, Double.parseDouble(fieldList.get(0).getText().toString().replace(",", "")));
					operands.put(LoanOperand.RATE, Double.parseDouble(fieldList.get(1).getText().toString()));
					operands.put(LoanOperand.INSTALLMENT, Double.parseDouble(fieldList.get(3).getText().toString().replace(",", "")));
					results = CalculationManager.calculate(operands, Calculator.Type.LOAN);
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanOperand.PERIOD)))));
					break;
				case R.id.loanInstallmentEditText:
					operands.put(LoanOperand.MISSING, LoanOperand.INSTALLMENT);
					operands.put(LoanOperand.PRINCIPAL, Double.parseDouble(fieldList.get(0).getText().toString().replace(",", "")));
					operands.put(LoanOperand.RATE, Double.parseDouble(fieldList.get(1).getText().toString()));
					operands.put(LoanOperand.PERIOD, Integer.parseInt(fieldList.get(2).getText().toString().replace(",", "")));
					results = CalculationManager.calculate(operands, Calculator.Type.LOAN);
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanOperand.INSTALLMENT)))));
					break;
			}
			EditText totalRepaid = (EditText) findViewById(R.id.totalRepaidEditText);
			EditText totalInterest = (EditText) findViewById(R.id.totalInterestEditText);

			totalRepaid.setText(Util.comify(String.valueOf(decimalFormat.format(results.get(LoanResult.TOTAL_REPAID)))));
			totalInterest.setText(Util.comify(String.valueOf(decimalFormat.format(results.get(LoanResult.TOTAL_INTEREST)))));

			Util.hideInput(this, view);
		} else {
			String message = this.getString(R.string.loan_calculator_message1);
			EditText fieldToFocus;
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

	public void clear(View view) {
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
}
