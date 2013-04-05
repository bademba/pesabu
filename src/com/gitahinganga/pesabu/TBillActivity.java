package com.gitahinganga.pesabu;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.support.v4.app.NavUtils;
import com.gitahinganga.pesabu.R;

@SuppressWarnings("deprecation")
public class TBillActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_tbill);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, BuyTBillActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("buy").setIndicator("Buy",
	                      res.getDrawable(R.drawable.buy_tbill))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, SellTBillActivity.class);
	    spec = tabHost.newTabSpec("sel").setIndicator("Sell",
	                      res.getDrawable(R.drawable.sell_tbill))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
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

}
