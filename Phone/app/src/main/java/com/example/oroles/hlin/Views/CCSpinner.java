package com.example.oroles.hlin.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;


public class CCSpinner extends Spinner {
    public CCSpinner(Context context) {
        super(context);
    }

    public CCSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CCSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CCSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CCSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
