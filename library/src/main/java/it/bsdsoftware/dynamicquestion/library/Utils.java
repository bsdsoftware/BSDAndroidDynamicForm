package it.bsdsoftware.dynamicquestion.library;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

/**
 * Created by Simone on 04/11/15.
 */
class Utils {

    public static void setTextAppearance(Context context, TextView view, int style){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setTextAppearance(style);
        }else{
            view.setTextAppearance(context, style);
        }
    }
}
