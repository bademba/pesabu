package com.gitahinganga.pesabu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayeActivity extends Activity {

	private List<IncomeBracket> incomeBracketList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paye);
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
		//getMenuInflater().inflate(R.menu.loan, menu);
		return true;
	}

	private static final double PERSONAL_RELIEF = 1162.0;

	public void calculatePaye(View view) {
		EditText grossSalaryEditText = (EditText) findViewById(R.id.grossSalaryEditText);
		EditText nhifEditText = (EditText) findViewById(R.id.nhifEditText);
		EditText nssfEditText = (EditText) findViewById(R.id.nssfEditText);
		EditText otherPayeExemptEditText = (EditText) findViewById(R.id.otherPayeExemptEditText);

		double grossSalary = Double.parseDouble(grossSalaryEditText.getText().toString());
		double nhif = Double.parseDouble(nhifEditText.getText().toString());
		double nssf = Double.parseDouble(nssfEditText.getText().toString());
		double otherPayeExempt = Double.parseDouble(otherPayeExemptEditText.getText().toString());

		PayeResult payeResult = calculatePaye(grossSalary, nhif, nssf, otherPayeExempt);

		double taxBracket = payeResult.getTaxBracket();
		double paye = payeResult.getPaye();
		double netSalary = payeResult.getNetSalary();

		DecimalFormat decimalFormat = Util.createDecimalFormat();

		EditText tbet = (EditText) findViewById(R.id.taxBracketEditText);
		tbet.setText(Util.comify(String.valueOf(decimalFormat.format(taxBracket))));
		EditText pet = (EditText) findViewById(R.id.payeEditText);
		pet.setText(Util.comify(String.valueOf(decimalFormat.format(paye))));
		EditText nset = (EditText) findViewById(R.id.netSalaryEditText);
		nset.setText(Util.comify(String.valueOf(decimalFormat.format(netSalary))));
	}

	private PayeResult calculatePaye(double grossSalary, double nhif, double nssf, double otherPayeExempt) {

		double taxableIncome = grossSalary -nssf - otherPayeExempt;

		PayeResult payeResult = new PayeResult();

		double paye = 0.0 - PERSONAL_RELIEF;
		double taxBracket = 0.0;
		double taxedGrossSalary = 0.0;

		for (IncomeBracket ib : incomeBrackets()) {

			if (ib.getTo() != 0.0) {
				if (taxableIncome >= ib.getTo()) {
			   		double bracketPaye = (ib.getTo() - taxedGrossSalary) * ib.getRate() / 100;
					paye += bracketPaye;
					taxedGrossSalary = ib.getTo();
				} else {
					double bracketPaye = (taxableIncome - taxedGrossSalary) * ib.getRate() / 100;
					paye += bracketPaye;
					taxBracket = ib.getRate();
					break;
				}
			} else {
				double bracketPaye = (taxableIncome - taxedGrossSalary) * ib.getRate() / 100;
				paye += bracketPaye;
				taxBracket = ib.getRate();
				break;
			}
		}

		if (paye < 0) {
			paye = 0;
		}

		payeResult.setTaxBracket(taxBracket);
		payeResult.setPaye(paye);
		payeResult.setNetSalary(grossSalary - paye - nhif - nssf - otherPayeExempt);
		return payeResult;
	}

	private double x(IncomeBracket ib, double grossSalary, double taxedGrossSalary) {
		return (grossSalary - taxedGrossSalary);
	}

	private List<IncomeBracket> incomeBrackets() {
		if (incomeBracketList == null) {
			incomeBracketList = new ArrayList<IncomeBracket>();
			{
				IncomeBracket incomeBracket = new IncomeBracket(1, 10.0, 0.0, 10164.0);
				incomeBracketList.add(incomeBracket);
			}
			{
				IncomeBracket incomeBracket = new IncomeBracket(2, 15.0, 10165.0, 19740.0);
				incomeBracketList.add(incomeBracket);
			}
			{
				IncomeBracket incomeBracket = new IncomeBracket(3, 20.0, 19741.0, 29316.0);
				incomeBracketList.add(incomeBracket);
			}
			{
				IncomeBracket incomeBracket = new IncomeBracket(4, 25.0, 29317, 38892.0);
				incomeBracketList.add(incomeBracket);
			}
			{
				IncomeBracket incomeBracket = new IncomeBracket(5, 30.0, 38893.0, 0.0);
				incomeBracketList.add(incomeBracket);
			}
		}
		return incomeBracketList;
	}

	private class IncomeBracket {

		private int number;
		private double rate;
		private double from;
		private double to;

		private IncomeBracket(int number, double rate, double from, double to) {
			this.number = number;
			this.rate = rate;
			this.from = from;
			this.to = to;
		}

		private double getTo() {
			return to;
		}

		private double getFrom() {
			return from;
		}

		private double getRate() {
			return rate;
		}

		private int getNumber() {
			return number;
		}
	}

	private class PayeResult {

		private double taxBracket;
		private double paye;
		private double netSalary;

		private double getTaxBracket() {
			return taxBracket;
		}

		private void setTaxBracket(double taxBracket) {
			this.taxBracket = taxBracket;
		}

		private double getPaye() {
			return paye;
		}

		private void setPaye(double paye) {
			this.paye = paye;
		}

		private double getNetSalary() {
			return netSalary;
		}

		private void setNetSalary(double netSalary) {
			this.netSalary = netSalary;
		}
	}
}