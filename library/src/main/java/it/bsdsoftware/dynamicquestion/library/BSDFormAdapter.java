package it.bsdsoftware.dynamicquestion.library;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;
import it.bsdsoftware.dynamicquestion.library.models.BSDChoiceModel;
import it.bsdsoftware.dynamicquestion.library.models.BSDQuestionModel;
import it.bsdsoftware.dynamicquestion.library.models.CallbackComplete;
import it.bsdsoftware.dynamicquestion.library.models.response.BSDResponse;
import it.bsdsoftware.dynamicquestion.library.models.response.MultiChoice;
import it.bsdsoftware.dynamicquestion.library.models.response.MultiLineText;
import it.bsdsoftware.dynamicquestion.library.models.response.SingleChoice;
import it.bsdsoftware.dynamicquestion.library.models.response.SingleLineText;

/**
 * Created by Simone on 02/11/15.
 */
class BSDFormAdapter extends ArrayAdapter<BSDQuestionModel> {

    private static final int VIEW_TYPE_SINGLE_LINE_TEXT = 0;
    private static final int VIEW_TYPE_MULTI_LINE_TEXT = 1;
    private static final int VIEW_TYPE_SINGLE_CHOICE = 2;
    private static final int VIEW_TYPE_MULTI_CHOICE = 3;
    private static final int VIEW_TYPE_END = 4;

    private static final int VIEW_TYPE_COUNT = 5;

    private Activity context;
    private CallbackComplete callbackComplete;

    public BSDFormAdapter(Activity context) {
        super(context, R.layout.single_line_text);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        int type = VIEW_TYPE_END;
        BSDQuestionModel model = getItem(position);
        if(model != null && model.getType() != null) {
            switch (model.getType()) {
                case SINGLE_LINE_TEXT:
                    type = VIEW_TYPE_SINGLE_LINE_TEXT;
                    break;
                case MULTI_LINE_TEXT:
                    type = VIEW_TYPE_MULTI_LINE_TEXT;
                    break;
                case SINGLE_CHOICE:
                    type = VIEW_TYPE_SINGLE_CHOICE;
                    break;
                case MULTI_CHOICE:
                    type = VIEW_TYPE_MULTI_CHOICE;
                    break;
            }
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);

        ViewHolder viewHolder = null;
        if(convertView == null) {
            int layoutID;
            switch (itemViewType){
                case VIEW_TYPE_SINGLE_LINE_TEXT:
                    layoutID = R.layout.single_line_text;
                    break;
                case VIEW_TYPE_MULTI_LINE_TEXT:
                    layoutID = R.layout.multi_line_text;
                    break;
                case VIEW_TYPE_SINGLE_CHOICE:
                    layoutID = R.layout.single_choice;
                    break;
                case VIEW_TYPE_MULTI_CHOICE:
                    layoutID = R.layout.multi_choice;
                    break;
                case VIEW_TYPE_END:
                default:
                    layoutID = R.layout.bottom_button;
            }
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutID, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        if (viewHolder == null) {
            Object tag = convertView.getTag();
            if (tag instanceof ViewHolder) {
                viewHolder = (ViewHolder) tag;
            }
        }

        final BSDQuestionModel model = getItem(position);

        if(viewHolder!=null) {
            if(viewHolder.question!=null)
                viewHolder.question.setText(model.getQuestion());
            switch (itemViewType){
                case VIEW_TYPE_SINGLE_LINE_TEXT:
                case VIEW_TYPE_MULTI_LINE_TEXT:
                    viewHolder.text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            model.setResultText(s.toString());
                        }
                    });
                    break;
                case VIEW_TYPE_SINGLE_CHOICE:
                    if(viewHolder.spinner.getAdapter()==null) {
                        BSDSpinnerAdapter adapter = new BSDSpinnerAdapter(context);
                        adapter.addAll(model.getChoices());
                        viewHolder.spinner.setAdapter(adapter);
                        viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                BSDChoiceModel bsdChoiceModel = (BSDChoiceModel) parent.getAdapter().getItem(position);
                                model.setResultChoice(bsdChoiceModel.getValue());
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                    break;
                case VIEW_TYPE_MULTI_CHOICE:
                    if(viewHolder.multiSpinner.isItemsVoid()) {
                        viewHolder.multiSpinner.setOnChangeSelectedItem(new MultiChoiceSpinner.OnChangeSelectedItem() {
                            @Override
                            public void onChange(MultiChoiceModel multiChoiceModel) {
                                model.setResultMultiChoices(multiChoiceModel.getValues());
                            }
                        });
                        viewHolder.multiSpinner.setItems(model.getChoices());
                    }
                    break;
                case VIEW_TYPE_END:
                    viewHolder.save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<BSDResponse> response = checkResponse();
                            if(callbackComplete!=null)
                                callbackComplete.doOnComplete(response);
                        }
                    });
                    break;
            }
        }
        return convertView;
    }

    private List<BSDResponse> checkResponse(){
        List<BSDResponse> response = new ArrayList<>();
        for(int i = 0; i < this.getCount(); i++){
            BSDQuestionModel model = this.getItem(i);
            int viewType = this.getItemViewType(i);
            switch (viewType) {
                case VIEW_TYPE_SINGLE_LINE_TEXT:
                    SingleLineText singleLineText = new SingleLineText(model.getQuestionID(), model.getQuestion(), model.getResultText());
                    response.add(singleLineText);
                    break;
                case VIEW_TYPE_MULTI_LINE_TEXT:
                    MultiLineText multiLineText = new MultiLineText(model.getQuestionID(), model.getQuestion(), model.getResultText());
                    response.add(multiLineText);
                    break;
                case VIEW_TYPE_SINGLE_CHOICE:
                    SingleChoice singleChoice = new SingleChoice(model.getQuestionID(), model.getQuestion(), model.getResultChoice());
                    response.add(singleChoice);
                    break;
                case VIEW_TYPE_MULTI_CHOICE:
                    MultiChoice multiChoice = new MultiChoice(model.getQuestionID(), model.getQuestion(), model.getResultMultiChoices());
                    response.add(multiChoice);
                    break;
            }
        }
        return response;
    }

    class ViewHolder {

        public final TextView question;
        public final EditText text;
        public final Spinner spinner;
        public final MultiChoiceSpinner multiSpinner;
        public final Button save;

        public ViewHolder(View view){
            question = (TextView) view.findViewById(R.id.textview_question);
            text = (EditText) view.findViewById(R.id.edittext_text);
            spinner = (Spinner) view.findViewById(R.id.spinner_single_choice);
            multiSpinner = (MultiChoiceSpinner) view.findViewById(R.id.spinner_multi_choice);
            save = (Button) view.findViewById(R.id.button_save);
        }
    }

    public void setCallbackComplete(CallbackComplete callbackComplete) {
        this.callbackComplete = callbackComplete;
    }
}
