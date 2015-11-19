package it.bsdsoftware.dynamicquestion.library;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;
import it.bsdsoftware.dynamicquestion.library.models.BSDChoiceModel;
import it.bsdsoftware.dynamicquestion.library.models.BSDQuestionModel;
import it.bsdsoftware.dynamicquestion.library.models.CallbackComplete;
import it.bsdsoftware.dynamicquestion.library.models.CancelCallback;
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
    private CancelCallback cancelCallback;

    private boolean textIsPlaceholder = false;
    private int styleSaveButton = -1;
    private String saveButtonText, cancelButtonText;
    private int styleTextSaveButton = -1;
    private int styleTextQuestion = -1;
    private int styleTextResponse = -1;
    private int colorBackgroundSaveButton = Color.TRANSPARENT;
    private Drawable backgroundSaveButton = null;

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

    public View instantiateConvertView(View convertView, int itemViewType, BSDQuestionModel model, ViewGroup parent){
        ViewHolder viewHolder;
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
        if(itemViewType == VIEW_TYPE_SINGLE_LINE_TEXT || itemViewType == VIEW_TYPE_MULTI_LINE_TEXT){
            viewHolder = new ViewHolder(convertView, model.getInputType());
        }else {
            viewHolder = new ViewHolder(convertView);
        }
        if(itemViewType != VIEW_TYPE_END)
            viewHolder.idQuestion = model.getQuestionID();
        else
            viewHolder.idQuestion = -1;

        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        final BSDQuestionModel model = getItem(position);

        ViewHolder viewHolder = null;

        if(convertView == null) {
            convertView = instantiateConvertView(convertView, itemViewType, model, parent);
        }

        Object tag = convertView.getTag();
        if (tag instanceof ViewHolder) {
            viewHolder = (ViewHolder) tag;
        }

        int idQuestion = -1;
        if(model!=null)
            idQuestion = model.getQuestionID();

        if(viewHolder.idQuestion != idQuestion) {
            convertView = instantiateConvertView(convertView, itemViewType, model, parent);
            tag = convertView.getTag();
            if (tag instanceof ViewHolder) {
                viewHolder = (ViewHolder) tag;
            }
        }

        if(viewHolder.question!=null) {
            viewHolder.question.setText(model.getQuestion());
        }

        switch (itemViewType) {
            case VIEW_TYPE_SINGLE_LINE_TEXT:
            case VIEW_TYPE_MULTI_LINE_TEXT:
                if(viewHolder.textWatcher!=null)
                    viewHolder.text.removeTextChangedListener(viewHolder.textWatcher);
                viewHolder.text.setText(model.getResultText());
                viewHolder.textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        model.setResultText(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                viewHolder.text.addTextChangedListener(viewHolder.textWatcher);
                if(textIsPlaceholder){
                    viewHolder.text.setHint(model.getQuestion());
                    viewHolder.question.setText("");
                }
                break;
            case VIEW_TYPE_SINGLE_CHOICE:
                BSDSpinnerAdapter adapter;
                if(viewHolder.spinner.getAdapter()==null) {
                    adapter = new BSDSpinnerAdapter(context);
                    if(styleTextResponse!=-1){
                        adapter.setStyleText(styleTextResponse);
                    }
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
                }else{
                    adapter = (BSDSpinnerAdapter) viewHolder.spinner.getAdapter();
                }
                viewHolder.spinner.setSelection(adapter.getIndexAtElement(model.getResultChoice()), false);
                break;
            case VIEW_TYPE_MULTI_CHOICE:
                if(viewHolder.multiSpinner.isItemsVoid()) {
                    viewHolder.multiSpinner.setItems(model.getChoices());
                    viewHolder.multiSpinner.setOnChangeSelectedItem(new MultiChoiceSpinner.OnChangeSelectedItem() {
                        @Override
                        public void onChange(MultiChoiceModel multiChoiceModel) {
                            model.setResultMultiChoices(multiChoiceModel.getValues());
                        }
                    });
                }
                viewHolder.multiSpinner.selectItems(model.getResultMultiChoices());
                break;
            case VIEW_TYPE_END:
                if(viewHolder.btn_container.findViewById(R.id.save_button)==null) {
                    Button saveButton;
                    Button cancelButton;
                    if(styleSaveButton !=-1){
                        saveButton = new Button(new ContextThemeWrapper(context, styleSaveButton));
                        cancelButton = new Button(new ContextThemeWrapper(context, styleSaveButton));
                    }else{
                        saveButton = new Button(context);
                        cancelButton = new Button(context);
                    }
                    if(styleTextSaveButton != -1){
                        Utils.setTextAppearance(context, saveButton, styleTextSaveButton);
                        Utils.setTextAppearance(context, cancelButton, styleTextSaveButton);
                    }
                    if(backgroundSaveButton!=null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            saveButton.setBackground(backgroundSaveButton);
                            cancelButton.setBackground(backgroundSaveButton);
                        }else {
                            saveButton.setBackgroundDrawable(backgroundSaveButton);
                            cancelButton.setBackgroundDrawable(backgroundSaveButton);
                        }
                    }else{
                        saveButton.setBackgroundColor(colorBackgroundSaveButton);
                        cancelButton.setBackgroundColor(colorBackgroundSaveButton);
                    }
                    saveButton.setId(R.id.save_button);
                    saveButton.setText(saveButtonText);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<BSDResponse> response = checkResponse();
                            if (callbackComplete != null)
                                callbackComplete.doOnComplete(response);
                        }
                    });
                    cancelButton.setId(R.id.cancel_button);
                    cancelButton.setText(cancelButtonText);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cancelCallback!=null)
                                cancelCallback.onCancel();
                        }
                    });
                    viewHolder.btn_container.addView(cancelButton);
                    viewHolder.btn_container.addView(saveButton);
                }
                break;
            }
        return convertView;
    }

    private List<BSDResponse> checkResponse() {
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
        public final LinearLayout btn_container;
        public TextWatcher textWatcher;
        public int idQuestion;

        public ViewHolder(View view){
            question = (TextView) view.findViewById(R.id.textview_question);
            text = (EditText) view.findViewById(R.id.edittext_text);
            spinner = (Spinner) view.findViewById(R.id.spinner_single_choice);
            multiSpinner = (MultiChoiceSpinner) view.findViewById(R.id.spinner_multi_choice);
            btn_container = (LinearLayout) view.findViewById(R.id.ll);
            if(question!= null && styleTextQuestion!=-1)
                Utils.setTextAppearance(context, question, styleTextQuestion);
            if(styleTextResponse!= -1){
                if(text!=null)
                    Utils.setTextAppearance(context, text, styleTextResponse);
                if(multiSpinner!=null)
                    multiSpinner.setStyleTextResponse(styleTextResponse);
            }
        }

        public ViewHolder(View view, int inputType){
            this(view);
            if(text!=null)
                text.setInputType(inputType);
        }
    }

    public void setCallbackComplete(CallbackComplete callbackComplete) {
        this.callbackComplete = callbackComplete;
    }

    public void setStyleSaveButton(int styleSaveButton) {
        this.styleSaveButton = styleSaveButton;
    }

    public void setStyleTextSaveButton(int styleTextSaveButton) {
        this.styleTextSaveButton = styleTextSaveButton;
    }

    public void setColorBackgroundSaveButton(int colorBackgroundSaveButton) {
        this.colorBackgroundSaveButton = colorBackgroundSaveButton;
    }

    public void setBackgroundSaveButton(Drawable backgroundSaveButton) {
        this.backgroundSaveButton = backgroundSaveButton;
    }

    public void setStyleTextQuestion(int styleTextQuestion) {
        this.styleTextQuestion = styleTextQuestion;
    }

    public void setStyleTextResponse(int styleTextResponse) {
        this.styleTextResponse = styleTextResponse;
    }

    public void setSaveButtonText(String saveButtonText) {
        this.saveButtonText = saveButtonText;
    }

    public void setCancelCallback(CancelCallback cancelCallback) {
        this.cancelCallback = cancelCallback;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public void setTextIsPlaceholder(boolean textIsPlaceholder) {
        this.textIsPlaceholder = textIsPlaceholder;
    }
}
