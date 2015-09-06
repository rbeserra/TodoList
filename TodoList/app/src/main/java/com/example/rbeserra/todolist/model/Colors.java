package com.example.rbeserra.todolist.model;

import com.example.rbeserra.todolist.R;

/**
 * Enum that provides easy access to the color used by dynamic views.
 * Added text values in rgb as well to workaround problem in TextField#setColor
 * Created by renato on 9/5/15.
 *
 */
public enum Colors {
    RED(R.color.red, R.color.darkred,"#FFCC0000"),
    BLUE(R.color.blue, R.color.darkblue,"#FF0099CC"),
    ORANGE(R.color.orange, R.color.darkorange,"#FFFF8800"),
    PURPLE(R.color.purple, R.color.darkpurple,"#FF9933CC"),
    GREEN(R.color.green, R.color.darkgreen,"#FF669900"),
    DEFAULT(R.color.white, R.color.black,"#FF000000"), ;

    private int resId;
    private int textResId;
    private String textRgb;



    Colors(int resId, int textResId, String textRgb) {
        this.resId=resId;
        this.textResId = textResId;
        this.textRgb = textRgb;
    }


    public int getResId() {
        return resId;
    }

    public int getTextResId() {
        return textResId;
    }

    public String getTextRgb() {
        return textRgb;
    }
}
