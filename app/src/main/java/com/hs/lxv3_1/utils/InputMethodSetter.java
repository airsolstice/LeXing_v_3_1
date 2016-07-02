package com.hs.lxv3_1.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * ?????????????????????Edittext????????????
 * 
 * @author Holy-Spirit
 *
 */
public class InputMethodSetter {

	public InputMethodSetter() {

	}

	public static void hideInputMethod(Context context, View v, MotionEvent ev) {

		if (isShouldHideInput(v, ev)) {
			InputMethodManager mManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (mManager != null) {
				mManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		}
	}

	private static boolean isShouldHideInput(View view, MotionEvent event) {

		if (view != null && (view instanceof EditText)) {
			int[] leftTop = { 0, 0 };

			view.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int right = left + view.getWidth();
			int bottom = top + view.getHeight();

			if (event.getY() > top && event.getY() < bottom
					&& event.getX() > left && event.getX() < right) {

				return false;

			} else {
				return true;
			}

		}

		return false;

	}

}
