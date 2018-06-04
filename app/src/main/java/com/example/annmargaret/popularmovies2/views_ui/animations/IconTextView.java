package com.example.annmargaret.popularmovies2.views_ui.animations;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.widget.TextView;

import com.example.annmargaret.popularmovies2.R;

public class IconTextView extends AppCompatTextView {


    /***************
     * An IconTextView implementation to control Fontawesome Typeface type
     *****************/
    private Context context;
    Typeface fontAwesome;


    public IconTextView(Context context, String fontPath) {
        super(context);
        this.context = context;
        fontAwesome = Typeface.createFromAsset(context.getAssets(), fontPath);
        createView();
    }

    public void createView(){
        setGravity(Gravity.CENTER);
        setTypeface(fontAwesome);
        setTextColor(Color.LTGRAY);
        setTextSize(18);
    }
}
