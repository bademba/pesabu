package com.gitahinganga.pesabu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

public class TBillActivity extends Activity {

	private final int DAYS_IN_A_YEAR = 365;
	private final int PAR_VALUE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tbill);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tbill, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void calculateTBill(View view) {
		List<View> requiredInputViewList = new ArrayList<View>();
		requiredInputViewList.add(findViewById(R.id.tbillDaysSpinner));
		requiredInputViewList.add(findViewById(R.id.tbillInterestEditText));
		requiredInputViewList.add(findViewById(R.id.tbillFaceValueEditText));
		
		View emptyView = Util.detectEmptyView(requiredInputViewList);
		if (emptyView != null) {
			Util.showInformationMessage(this, this.getString(R.string.missing_input_field_message));
			emptyView.requestFocus();
			return;
		}
		
		DecimalFormat decimalFormat = Util.createDecimalFormat();

		int days = Integer
				.parseInt(((Spinner) findViewById(R.id.tbillDaysSpinner))
						.getSelectedItem().toString());
		double interestRate = Double
				.parseDouble(((EditText) findViewById(R.id.tbillInterestEditText))
						.getText().toString());
		double faceValue = Double
				.parseDouble(((EditText) findViewById(R.id.tbillFaceValueEditText))
						.getText().toString());
		boolean taxExempt = ((CheckBox) findViewById(R.id.tbillTaxExemptCheckBox))
				.isChecked();

		double discountedValue = calculateDiscountedValue(days, interestRate);
		int units = calculateUnits(faceValue);
		double discountedTotalValue = calculateDiscountedTotalValue(
				discountedValue, units);
		double preTaxProfit = calculatePreTaxProfit(faceValue,
				discountedTotalValue);
		double tax = 0.0;
		if (!taxExempt) {
			tax = calculateTax(15.0, preTaxProfit);
		}
		double investment = calculateInvestment(discountedTotalValue, tax);
		double netReturns = calculateNetReturns(faceValue, investment);

		EditText dvet = (EditText) findViewById(R.id.tbillDiscountedValueEditText);
		dvet.setText(Util.comify(String.valueOf(decimalFormat
				.format(discountedValue))));
		EditText uet = (EditText) findViewById(R.id.tbillUnitsEditText);
		uet.setText(Util.comify(String.valueOf(decimalFormat.format(units))));
		EditText iet = (EditText) findViewById(R.id.tbillInvestmentEditText);
		iet.setText(Util.comify(String.valueOf(decimalFormat.format(investment))));
		EditText ptet = (EditText) findViewById(R.id.tbillDiscountedTotalValueEditText);
		ptet.setText(Util.comify(String.valueOf(decimalFormat
				.format(discountedTotalValue))));
		EditText tet = (EditText) findViewById(R.id.tbillTaxEditText);
		tet.setText(Util.comify(String.valueOf(decimalFormat.format(tax))));
		EditText atpet = (EditText) findViewById(R.id.tbillTotalReturnsEditText);
		atpet.setText(Util.comify(String.valueOf(decimalFormat
				.format(netReturns))));
		
		Util.hideInput(this, view);
	}

	private double calculateDiscountedValue(int days, double interestRate) {
		return PAR_VALUE
				* (1 / (1 + ((interestRate / 100) * ((double) days / (double) DAYS_IN_A_YEAR))));
	}

	private int calculateUnits(double faceValue) {
		return (int) faceValue / PAR_VALUE;
	}

	private double calculateDiscountedTotalValue(double discountedValue,
			int units) {
		return discountedValue * units;
	}

	private double calculatePreTaxProfit(double faceValue,
			double discountedTotalValue) {
		return faceValue - discountedTotalValue;
	}

	private double calculateTax(double taxRate, double preTaxProfit) {
		return taxRate / 100 * preTaxProfit;
	}

	private double calculateInvestment(double discountedTotalValue, double tax) {
		return discountedTotalValue + tax;
	}

	private double calculateNetReturns(double faceValue, double investment) {
		return faceValue - investment;
	}
}
