package it.bsdsoftware.dynamicquestion.library;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import it.bsdsoftware.dynamicquestion.library.models.BSDChoiceModel;

/**
 * Created by Simone on 02/11/15.
 */
class BSDSpinnerAdapter extends ArrayAdapter<BSDChoiceModel> {

    private Activity context;

    public BSDSpinnerAdapter(Activity context) {
        super(context, R.layout.spinner_item);
        this.context = context;
        this.setDropDownViewResource(R.layout.spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        BSDChoiceModel model = getItem(position);
        TextView spinnerText = (TextView) convertView.findViewById(android.R.id.text1);
        spinnerText.setText(model.getLabel());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        BSDChoiceModel model = getItem(position);
        CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
        checkedTextView.setText(model.getLabel());

        return convertView;
    }

    public int getIndexAtElement(int value){
        for(int i = 0; i < getCount(); i++){
            if(getItem(i).getValue() == value){
                return i;
            }
        }
        return -1;
    }
}
