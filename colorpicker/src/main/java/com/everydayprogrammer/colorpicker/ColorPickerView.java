package com.everydayprogrammer.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

public class ColorPickerView extends FrameLayout {

	private final AlphaView alphaView;
	private final EditText hexEdit;
	private final ObservableColor observableColor = new ObservableColor(0);
	private final SwatchView swatchView;

	public ColorPickerView(Context context) {
		this(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.picker, this);

		swatchView = findViewById(R.id.swatchView);
		swatchView.observeColor(observableColor);

		HueSatView hueSatView = findViewById(R.id.hueSatView);
		hueSatView.observeColor(observableColor);

		ValueView valueView = findViewById(R.id.valueView);
		valueView.observeColor(observableColor);

		alphaView = findViewById(R.id.alphaView);
		alphaView.observeColor(observableColor);

		hexEdit = findViewById(R.id.hexEdit);
		HexEdit.setUpListeners(hexEdit, observableColor);

		applyAttributes(attrs);
	}

	private void applyAttributes(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPicker, 0, 0);
			showAlpha(a.getBoolean(R.styleable.ColorPicker_colorpicker_showAlpha, true));
			showHex(a.getBoolean(R.styleable.ColorPicker_colorpicker_showHex, true));
		}
	}

	public int getColor() {
		return observableColor.getColor();
	}

	public void setColor(int color) {
		setOriginalColor(color);
		setCurrentColor(color);
	}

	public void setOriginalColor(int color) {
		swatchView.setOriginalColor(color);
	}

	public void setCurrentColor(int color) {
		observableColor.updateColor(color, null);
	}

	public void showAlpha(boolean showAlpha) {
		alphaView.setVisibility(showAlpha ? View.VISIBLE : View.GONE);
		HexEdit.setShowAlphaDigits(hexEdit, showAlpha);
	}

	public void addColorObserver(ColorObserver observer) {
		observableColor.addObserver(observer);
	}

	public void showHex(boolean showHex) {
		hexEdit.setVisibility(showHex ? View.VISIBLE : View.GONE);
	}
}