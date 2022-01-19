package com.devabhijeet.amazingmart.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AMTextViewBold(context: Context, attrs: AttributeSet):  AppCompatTextView(context, attrs) {

    init {
        applyFont();
    }


    private fun applyFont() {

        //get font file from assets folder and apply font to textview

        val typeface: Typeface = Typeface.createFromAsset(context.assets, "mandali.ttf");

        setTypeface(typeface,BOLD);

    }


}