package com.gitahinganga.pesabu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends PesabuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);		
		return true;
	}

	public void launchLoanActivity(View view) {
		Intent loanIntent = new Intent(this, LoanActivity.class);
		startActivity(loanIntent);
	}
	
	public void launchTBillActivity(View view) {
		Intent tBillIntent = new Intent(this, TBillActivity.class);
		startActivity(tBillIntent);
	}

	public void launchTBondActivity(View view) {
		Intent tBondIntent = new Intent(this, TBondActivity.class);
		startActivity(tBondIntent);
	}

	public void launchPayeActivity(View view) {
		Intent payeIntent = new Intent(this, PayeActivity.class);
		startActivity(payeIntent);
	}
}
