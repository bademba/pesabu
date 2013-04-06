package com.gitahinganga.pesabu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
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

	private double calculateDiscountedValue(int days, double interestRate) {
		return PAR_VALUE
				* (1 / (1 + ((interestRate / 100) * (days / DAYS_IN_A_YEAR))));
	}

	private int calculateUnits(double faceValue) {
		return (int) faceValue / PAR_VALUE;
	}

	private double calculateInvestment(double discountedValue, int units) {
		return discountedValue * units;
	}

	private double calculatePreTaxProfit(double faceValue, double investment) {
		return 0.0;
	}

	private double calculateTax(double taxRate, double investment) {
		return taxRate / 100 * investment;
	}

	private double calculateAfterTaxProfit(double preTaxProfit, double tax) {
		return preTaxProfit - tax;
	}
}
