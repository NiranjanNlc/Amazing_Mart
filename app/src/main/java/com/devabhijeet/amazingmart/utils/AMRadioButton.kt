package com.devabhijeet.amazingmart.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class AMRadioButton(context: Context, attributeSet: AttributeSet): AppCompatRadioButton(context,attributeSet) {

    init {
        applyFont();
    }

    private fun applyFont()
    {

        val typeface: Typeface = Typeface.createFromAsset(context.assets, "mandali.ttf");

        setTypeface(typeface, Typeface.BOLD);

    }


}