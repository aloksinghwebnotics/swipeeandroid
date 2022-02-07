package com.webnotics.swipee.CustomUi;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

/**
 *  Created by Developer on 10/23/2021.
 */

public class PopinsRegularCheckBox extends AppCompatCheckBox {

    public PopinsRegularCheckBox(Context context) {
        super(context);
        setFont();
    }

    public PopinsRegularCheckBox(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public PopinsRegularCheckBox(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Poppins-Regular.ttf");
        setTypeface(typeface);
        setIncludeFontPadding(false);//function used to set font

    }
}