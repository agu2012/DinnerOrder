package com.bocs.dodemo.util;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

public class AmountTextWatcher implements TextWatcher {
	private String sAmtOrg = null;

	private int pointCount(String str) {
		int num = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '.')
				num++;
		}
		return num;
	}

	@Override
	public void afterTextChanged(Editable edt) {
		String temp = edt.toString();

		if (temp.indexOf(".") < 0 && sAmtOrg.indexOf(".") >= 0) {
			edt.replace(0, edt.length(), sAmtOrg);
			Selection.setSelection(edt, edt.length() - 3);
			return;
		}

		if ("0.00".equals(temp)) {
			return;
		}

		if ("000".equals(temp)) {
			edt.delete(1, 3);
			return;
		}

		if ("0.".equals(temp)) {
			edt.append("00");
			Selection.setSelection(edt, 2);
			return;
		}

		if (".00".equals(temp)) {
			edt.delete(1, 3);
		}

		if (".0.00".equals(temp)) {
			edt.delete(0, 1);
			Selection.setSelection(edt, 2);
			return;
		}

		if (temp.startsWith(".")) {
			edt.insert(0, "0");
		}

		if (temp.startsWith("0")) {
			if (!temp.startsWith("0.")) {
				edt.delete(0, 1);
				return;
			}
		}

		int posDot = temp.indexOf(".");
		if (pointCount(temp) > 1) {
			if (temp.length() == Selection.getSelectionEnd(edt))
				edt.delete(Selection.getSelectionEnd(edt) - 1,
						Selection.getSelectionEnd(edt));
			else
				edt.delete(Selection.getSelectionEnd(edt),
						Selection.getSelectionEnd(edt) + 1);
			Selection.setSelection(edt, Selection.getSelectionEnd(edt));
			return;
		}

		if (posDot == 8 && !(".".equals(edt.charAt(7)))) {
			edt.delete(7, 8);
		}
		// if (posDot == 0) {
		// edt.append("0.00");
		// return;
		// // edt.delete(posDot, posDot + 1);
		// // Selection.setSelection(edt, 2);
		// }

		if (posDot < 0) {
			// if (Long.valueOf(temp) == 0L) {
			// edt.delete(1, temp.length()-1);
			// }
			edt.append(".00");
			Selection.setSelection(edt, 1);
			return;
		}

		if (temp.length() - posDot - 1 > 2) {
			// Selection.setSelection(edt, posDot+1);
			edt.delete(posDot + 3, posDot + 4);
		} else if (temp.length() - posDot - 1 < 2) {
			edt.append("0");
			/**
			 * 光标不在小数点后面
			 */
			if (Selection.getSelectionEnd(edt) - posDot != 1)
				Selection.setSelection(edt, Selection.getSelectionEnd(edt) - 1);
		}

		// if (posDot <= 0)
		// return;
		// if (temp.length() - posDot - 1 > 2)
		// {
		// edt.delete(posDot + 3, posDot + 4);
		// }
	}

	// @Override
	// public void afterTextChanged(Editable edt) {
	// // TODO Auto-generated method stub
	//
	// String n = edt.toString();
	// double num = Double.parseDouble(n);
	// NumberFormat formater = null;
	// formater = new DecimalFormat("###,###.00");
	// String returnStr = formater.format(num);
	//
	// edt.replace(0, edt.length(), returnStr);
	// }

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		sAmtOrg = arg0.toString();
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

}
