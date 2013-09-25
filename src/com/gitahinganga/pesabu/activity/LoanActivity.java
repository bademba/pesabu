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
import com.gitahinganga.pesabu.business.LoanCalculator;

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
			Map<LoanCalculator.Operand, Object> operands = new HashMap<LoanCalculator.Operand, Object>();
			Map<LoanCalculator.Result, Object> results = new HashMap<LoanCalculator.Result, Object>();

			DecimalFormat decimalFormat = Util.createDecimalFormat();
			EditText emptyField = emptyFieldList.get(0);

			switch (emptyField.getId()) {
				case R.id.loanPrincipleEditText:
					operands.put(LoanCalculator.Operand.MISSING, LoanCalculator.Operand.PRINCIPAL);
					operands.put(LoanCalculator.Operand.RATE, Double.parseDouble(fieldList.get(1).getText().toString().replace(",", "")));
					operands.put(LoanCalculator.Operand.PERIOD, Integer.parseInt(fieldList.get(2).getText().toString()));
					operands.put(LoanCalculator.Operand.INSTALLMENT, Double.parseDouble(fieldList.get(3).getText().toString().replace(",", "")));
					results.putAll(new LoanCalculator().calculate(operands));
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanCalculator.Operand.PRINCIPAL)))));
					break;
				case R.id.loanInterestEditText:
					operands.put(LoanCalculator.Operand.MISSING, LoanCalculator.Operand.RATE);
					operands.put(LoanCalculator.Operand.PRINCIPAL, Double.parseDouble(fieldList.get(0).getText().toString().replace(",", "")));
					operands.put(LoanCalculator.Operand.PERIOD, Integer.parseInt(fieldList.get(2).getText().toString()));
					operands.put(LoanCalculator.Operand.INSTALLMENT, Double.parseDouble(fieldList.get(3).getText().toString().replace(",", "")));
					results.putAll(new LoanCalculator().calculate(operands));
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanCalculator.Operand.RATE)))));
					break;
				case R.id.loanPeriodEditText:
					operands.put(LoanCalculator.Operand.MISSING, LoanCalculator.Operand.PERIOD);
					operands.put(LoanCalculator.Operand.PRINCIPAL, Double.parseDouble(fieldList.get(0).getText().toString().replace(",", "")));
					operands.put(LoanCalculator.Operand.RATE, Double.parseDouble(fieldList.get(1).getText().toString()));
					operands.put(LoanCalculator.Operand.INSTALLMENT, Double.parseDouble(fieldList.get(3).getText().toString().replace(",", "")));
					results.putAll(new LoanCalculator().calculate(operands));
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanCalculator.Operand.PERIOD)))));
					break;
				case R.id.loanInstallmentEditText:
					operands.put(LoanCalculator.Operand.MISSING, LoanCalculator.Operand.INSTALLMENT);
					operands.put(LoanCalculator.Operand.PRINCIPAL, Double.parseDouble(fieldList.get(0).getText().toString().replace(",", "")));
					operands.put(LoanCalculator.Operand.RATE, Double.parseDouble(fieldList.get(1).getText().toString()));
					operands.put(LoanCalculator.Operand.PERIOD, Integer.parseInt(fieldList.get(2).getText().toString().replace(",", "")));
					results.putAll(new LoanCalculator().calculate(operands));
					emptyField.setText(Util.comify(String.valueOf(decimalFormat.format(operands.get(LoanCalculator.Operand.INSTALLMENT)))));
					break;
			}
			EditText tret = (EditText) findViewById(R.id.totalRepaidEditText);
			EditText tiet = (EditText) findViewById(R.id.totalInterestEditText);

			tret.setText(Util.comify(String.valueOf(decimalFormat.format(results.get(LoanCalculator.Result.TOTAL_REPAID)))));
			tiet.setText(Util.comify(String.valueOf(decimalFormat.format(results.get(LoanCalculator.Result.TOTAL_INTEREST)))));

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
