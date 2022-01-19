package com.devabhijeet.amazingmart.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AMTextView(context: Context, attrs: AttributeSet):  AppCompatTextView(context, attrs) {

    init {
        applyFont();
    }


    private fun applyFont() {

        //get font file from assets folder and apply font to textview

        val typeface: Typeface = Typeface.createFromAsset(context.assets, "mandali.ttf");
        setTypeface(typeface);

    }


}