package com.example.annmargaret.popularmovies2.views_ui.animations;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class IconTextView2 extends AppCompatTextView {

    /*********************************************
     * A More Open Implementation of IconText View
     *********************************************/

    private Context context;
    Typeface fontAwesome;

    public IconTextView2(Context context) {
        super(context);
        this.context = getContext();
        createView();
    }

    public IconTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView();
    }

    private void createView(){
        setGravity(Gravity.RIGHT);
        fontAwesome = Typeface.createFromAsset(context.getAssets(), "FontAwesome.otf");
        setTypeface(fontAwesome);
    }
}
