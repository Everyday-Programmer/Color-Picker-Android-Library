package com.everydayprogrammer.colorpicker;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

class HexEdit {

	private static final InputFilter[] withoutAlphaDigits = {new ColorPasteLengthFilter()};
	private static final InputFilter[] withAlphaDigits = {new InputFilter.LengthFilter(8)};

	public static void setUpListeners(final EditText hexEdit, final ObservableColor observableColor) {

		class MultiObserver implements ColorObserver, TextWatcher {

			@Override
			public void updateColor(ObservableColor observableColor) {
				final String colorString = formatColor(observableColor.getColor());
				hexEdit.removeTextChangedListener(this);
				hexEdit.setText(colorString);
				hexEdit.addTextChangedListener(this);
			}

			private String formatColor(int color) {
				return shouldTrimAlphaDigits()
						? String.format("%06x", color & 0x00ffffff)
						: String.format("%08x", color);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					int color = (int) (Long.parseLong(s.toString(), 16));
					if (shouldTrimAlphaDigits()) color = color | 0xff000000;
					observableColor.updateColor(color, this);
				} catch (NumberFormatException e) {
					observableColor.updateColor(0, this);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			private boolean shouldTrimAlphaDigits() {
				return hexEdit.getFilters() == withoutAlphaDigits;
			}
		}

		final MultiObserver multiObserver = new MultiObserver();
		hexEdit.addTextChangedListener(multiObserver);
		observableColor.addObserver(multiObserver);
		setShowAlphaDigits(hexEdit, true);
	}

	public static void setShowAlphaDigits(final EditText hexEdit, boolean showAlphaDigits) {
		hexEdit.setFilters(showAlphaDigits ? withAlphaDigits : withoutAlphaDigits);
		hexEdit.setText(hexEdit.getText());
	}

	private static class ColorPasteLengthFilter implements InputFilter {

		private static final int MAX_LENGTH = 6;
		private static final int PASTED_LEN = 8;
		private final InputFilter sixDigitFilter = new InputFilter.LengthFilter(MAX_LENGTH);

		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
			final int srcLength = end - start;
			final int dstSelLength = dEnd - dStart;
			if (srcLength == PASTED_LEN && dstSelLength == dest.length()) {
				return source.subSequence(PASTED_LEN - MAX_LENGTH, PASTED_LEN);
			} else {
				return sixDigitFilter.filter(source, start, end, dest, dStart, dEnd);
			}
		}
	}
}