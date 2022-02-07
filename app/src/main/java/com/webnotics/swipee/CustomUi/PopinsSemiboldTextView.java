package com.webnotics.swipee.CustomUi;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 *  Created by Developer on 10/23/2021.
 */

public class PopinsSemiboldTextView extends AppCompatTextView {

    public PopinsSemiboldTextView(Context context) {
        super(context);
        setFont();
    }

    public PopinsSemiboldTextView(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public PopinsSemiboldTextView(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Poppins-SemiBold.ttf");
        setTypeface(typeface);
        setIncludeFontPadding(false);//function used to set font

    }
}