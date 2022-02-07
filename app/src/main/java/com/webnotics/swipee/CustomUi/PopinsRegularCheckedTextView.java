package com.webnotics.swipee.CustomUi;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 *  Created by Developer on 10/23/2021.
 */

public class PopinsRegularCheckedTextView extends CheckedTextView {

    public PopinsRegularCheckedTextView(Context context) {
        super(context);
        setFont();
    }

    public PopinsRegularCheckedTextView(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public PopinsRegularCheckedTextView(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Poppins-Regular.ttf");
        setTypeface(typeface);
        setIncludeFontPadding(false);//function used to set font

    }
}