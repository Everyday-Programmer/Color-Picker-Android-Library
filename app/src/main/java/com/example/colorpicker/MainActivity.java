package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.everydayprogrammer.colorpicker.ColorObserver;
import com.everydayprogrammer.colorpicker.ColorPickerView;
import com.everydayprogrammer.colorpicker.ObservableColor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorPickerView colorPickerView = findViewById(R.id.colorPicker);
        colorPickerView.setColor(0xffff0000);

        TextView textView = findViewById(R.id.tv);

        colorPickerView.addColorObserver(new ColorObserver() {
            @Override
            public void updateColor(ObservableColor observableColor) {
                textView.setTextColor(observableColor.getColor());
                textView.setText(String.valueOf(observableColor.getColor()));
            }
        });
    }
}