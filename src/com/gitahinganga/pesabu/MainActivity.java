package com.gitahinganga.pesabu;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import com.gitahinganga.pesabu.R;

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
//		Intent tBillIntent = new Intent(this, TBillActivity.class);
//		startActivity(tBillIntent);
	}
}
