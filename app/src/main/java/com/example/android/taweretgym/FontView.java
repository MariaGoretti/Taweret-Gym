package com.example.android.taweretgym;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class FontView extends android.support.v7.widget.AppCompatTextView{

    public FontView(Context context) {
        super(context);
        initTypeface(context);
    }

    public FontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface(context);
    }

    public FontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface(context);
    }

    private void initTypeface (Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "GreatVibes-Regular.ttf");
        this.setTypeface(typeface);
    }
}
