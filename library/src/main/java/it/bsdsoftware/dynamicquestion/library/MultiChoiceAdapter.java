package it.bsdsoftware.dynamicquestion.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Simone on 02/11/15.
 */
class MultiChoiceAdapter extends ArrayAdapter<MultiChoiceModel> {

    private int styleText = -1;

    public MultiChoiceAdapter(Context context) {
        super(context, R.layout.spinner_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        MultiChoiceModel mcm = getItem(position);
        TextView text = (TextView) view.findViewById(android.R.id.text1);
        text.setText(mcm.getLabelSelected());
        if(styleText != -1){
            Utils.setTextAppearance(getContext(), text, styleText);
        }

        return view;
    }

    public void setStyleText(int styleText) {
        this.styleText = styleText;
    }
}
