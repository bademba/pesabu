package com.gitahinganga.pesabu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

public class Util {

	public static void hideInput(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static String comify(String numberString) {
		String[] parts = numberString.split("\\.");
		String wholePart = parts[0];
		String decimalPart = "";
		if (parts.length > 1) {
			decimalPart = "." + parts[1];
		}
		String comifiedWholePart = "";
		char[] charArray = wholePart.toCharArray();
		int j = 1;
		for (int i = charArray.length - 1; i >= 0; i--) {
			comifiedWholePart = charArray[i] + comifiedWholePart;
			if ((j % 3 == 0) && (i != 0) && (i != charArray.length - 1)) {
				comifiedWholePart = "," + comifiedWholePart;
			}
			j++;
		}
		return comifiedWholePart + decimalPart;
	}

	public static void showInformationMessage(Activity activity, String message) {
		AlertDialog ad = new AlertDialog.Builder(activity).create();
		ad.setCancelable(true);
		ad.setMessage(message);
		ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						dialog.dismiss();
					}
				});
		ad.show();
	}

	public static String loadHtml(Activity activity, int resourceId) {
		InputStream raw = null;
		ByteArrayOutputStream stream = null;
		try {
			raw = activity.getResources().openRawResource(resourceId);
			stream = new ByteArrayOutputStream();
			int i = raw.read();
			while (i != -1) {
				stream.write(i);
				i = raw.read();
			}
			raw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream.toString();
	}

	public static DecimalFormat createDecimalFormat(int decimals) {
		String decimalFormatString = "#.";
		for (int i = 1; i <= decimals; i++) {
			decimalFormatString += "#";
		}
		return new DecimalFormat(decimalFormatString);
	}

	public static DecimalFormat createDecimalFormat() {
		return createDecimalFormat(2);
	}

	public static View detectEmptyView(List<View> viewList) {
		for (View v : viewList) {
			if (v.getClass() == EditText.class) {
				EditText et = (EditText) v;
				if (et.getText().toString().equalsIgnoreCase("")) {
					return v;
				}
			} else if (v.getClass() == Spinner.class) {
				Spinner s = (Spinner) v;
				if (s.getSelectedItem() == null) {
					return v;
				}
			}
		}
		return null;
	}
}
