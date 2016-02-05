package it.bsdsoftware.dynamicquestion.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Created by Simone on 05/02/16.
 */
public class BSDFormItem extends RelativeLayout {

    private BSDQuestionModel questionModel;
    private boolean textIsPlaceholder = false;
    private int styleSaveButton = -1;
    private int styleTextSaveButton = -1;
    private int styleTextQuestion = -1;
    private int styleTextResponse = -1;
    private String saveButtonText, cancelButtonText;
    private int colorBackgroundSaveButton = Color.TRANSPARENT;
    private Drawable backgroundSaveButton = null;
    private Callback callbackComplete;
    private CancelCallback cancelCallback;

    public BSDFormItem(Context context) {
        super(context);
        afterCreate(context);
    }

    public BSDFormItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        afterCreate(context);
    }

    public BSDFormItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        afterCreate(context);
    }

    private void afterCreate(Context context){
        //
        //this.setBackgroundColor(Color.BLUE);
        //LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //this.setLayoutParams(lp);
    }

    public void init(){
        int layoutID;
        if(questionModel == null){
            layoutID = R.layout.bottom_button;
        }else{
            switch (questionModel.getType()){
                case SINGLE_LINE_TEXT:
                    layoutID = R.layout.single_line_text;
                    break;
                case MULTI_LINE_TEXT:
                    layoutID = R.layout.multi_line_text;
                    break;
                case SINGLE_CHOICE:
                    layoutID = R.layout.single_choice;
                    break;
                case MULTI_CHOICE:
                    layoutID = R.layout.multi_choice;
                    break;
                default:
                    layoutID = R.layout.bottom_button;
            }
        }
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutID, this, false);
        this.addView(view);

        Activity context;
        if(getContext() instanceof Activity) {
            context = (Activity) getContext();
        }else{
            ContextThemeWrapper ctw = (ContextThemeWrapper) getContext();
            context = (Activity) ctw.getBaseContext();
        }

        if(questionModel == null){
            LinearLayout btn_container = (LinearLayout) view.findViewById(R.id.ll);
            Button saveButton;
            Button cancelButton;
            if (styleSaveButton != -1) {
                saveButton = new Button(new ContextThemeWrapper(context, styleSaveButton));
                cancelButton = new Button(new ContextThemeWrapper(context, styleSaveButton));
            } else {
                saveButton = new Button(context);
                cancelButton = new Button(context);
            }
            if (styleTextSaveButton != -1) {
                Utils.setTextAppearance(context, saveButton, styleTextSaveButton);
                Utils.setTextAppearance(context, cancelButton, styleTextSaveButton);
            }
            if (backgroundSaveButton != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    saveButton.setBackground(backgroundSaveButton);
                    cancelButton.setBackground(backgroundSaveButton);
                } else {
                    saveButton.setBackgroundDrawable(backgroundSaveButton);
                    cancelButton.setBackgroundDrawable(backgroundSaveButton);
                }
            } else {
                saveButton.setBackgroundColor(colorBackgroundSaveButton);
                cancelButton.setBackgroundColor(colorBackgroundSaveButton);
            }
            saveButton.setId(R.id.save_button);
            saveButton.setText(saveButtonText);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callbackComplete != null)
                        callbackComplete.call();
                }
            });
            cancelButton.setId(R.id.cancel_button);
            cancelButton.setText(cancelButtonText);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelCallback != null)
                        cancelCallback.onCancel();
                }
            });
            btn_container.addView(cancelButton);
            btn_container.addView(saveButton);
        }else {

            final TextView question = (TextView) findViewById(R.id.textview_question);
            if (question != null && styleTextQuestion != -1)
                Utils.setTextAppearance(context, question, styleTextQuestion);
            question.setText(questionModel.getQuestion());
            switch (questionModel.getType()) {
                case SINGLE_LINE_TEXT:
                case MULTI_LINE_TEXT:
                    TextView text = (TextView) findViewById(R.id.edittext_text);
                    if (styleTextResponse != -1) {
                        if (text != null)
                            Utils.setTextAppearance(context, text, styleTextResponse);
                    }
                    text.setText(questionModel.getResultText());
                    text.setInputType(questionModel.getInputType());
                    TextWatcher textWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            questionModel.setResultText(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    };
                    text.addTextChangedListener(textWatcher);
                    if (textIsPlaceholder) {
                        text.setHint(questionModel.getQuestion());
                        question.setText("");
                    }
                    break;
                case SINGLE_CHOICE:
                    Spinner spinner = (Spinner) findViewById(R.id.spinner_single_choice);
                    BSDSpinnerAdapter adapter;
                    if (spinner.getAdapter() == null) {
                        adapter = new BSDSpinnerAdapter(context);
                        if (styleTextResponse != -1) {
                            adapter.setStyleText(styleTextResponse);
                        }
                        adapter.addAll(questionModel.getChoices());
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                BSDChoiceModel bsdChoiceModel = (BSDChoiceModel) parent.getAdapter().getItem(position);
                                questionModel.setResultChoice(bsdChoiceModel.getValue());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        adapter = (BSDSpinnerAdapter) spinner.getAdapter();
                    }
                    spinner.setSelection(adapter.getIndexAtElement(questionModel.getResultChoice()), false);
                    break;
                case MULTI_CHOICE:
                    MultiChoiceSpinner multiSpinner = (MultiChoiceSpinner) view.findViewById(R.id.spinner_multi_choice);
                    if (styleTextResponse != -1) {
                        if (multiSpinner != null)
                            multiSpinner.setStyleTextResponse(styleTextResponse);
                    }
                    if (multiSpinner.isItemsVoid()) {
                        multiSpinner.setItems(questionModel.getChoices());
                        multiSpinner.setOnChangeSelectedItem(new MultiChoiceSpinner.OnChangeSelectedItem() {
                            @Override
                            public void onChange(MultiChoiceModel multiChoiceModel) {
                                questionModel.setResultMultiChoices(multiChoiceModel.getValues());
                            }
                        });
                    }
                    multiSpinner.selectItems(questionModel.getResultMultiChoices());
                    break;
            }
        }
    }


    public Callback getCallbackComplete() {
        return callbackComplete;
    }

    public void setCallbackComplete(Callback callbackComplete) {
        this.callbackComplete = callbackComplete;
    }

    public CancelCallback getCancelCallback() {
        return cancelCallback;
    }

    public void setCancelCallback(CancelCallback cancelCallback) {
        this.cancelCallback = cancelCallback;
    }

    public int getStyleSaveButton() {
        return styleSaveButton;
    }

    public void setStyleSaveButton(int styleSaveButton) {
        this.styleSaveButton = styleSaveButton;
    }

    public int getStyleTextSaveButton() {
        return styleTextSaveButton;
    }

    public void setStyleTextSaveButton(int styleTextSaveButton) {
        this.styleTextSaveButton = styleTextSaveButton;
    }

    public int getStyleTextQuestion() {
        return styleTextQuestion;
    }

    public void setStyleTextQuestion(int styleTextQuestion) {
        this.styleTextQuestion = styleTextQuestion;
    }

    public int getStyleTextResponse() {
        return styleTextResponse;
    }

    public void setStyleTextResponse(int styleTextResponse) {
        this.styleTextResponse = styleTextResponse;
    }

    public String getSaveButtonText() {
        return saveButtonText;
    }

    public void setSaveButtonText(String saveButtonText) {
        this.saveButtonText = saveButtonText;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public int getColorBackgroundSaveButton() {
        return colorBackgroundSaveButton;
    }

    public void setColorBackgroundSaveButton(int colorBackgroundSaveButton) {
        this.colorBackgroundSaveButton = colorBackgroundSaveButton;
    }

    public Drawable getBackgroundSaveButton() {
        return backgroundSaveButton;
    }

    public void setBackgroundSaveButton(Drawable backgroundSaveButton) {
        this.backgroundSaveButton = backgroundSaveButton;
    }

    public boolean isTextIsPlaceholder() {
        return textIsPlaceholder;
    }

    public void setTextIsPlaceholder(boolean textIsPlaceholder) {
        this.textIsPlaceholder = textIsPlaceholder;
    }

    public BSDQuestionModel getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(BSDQuestionModel questionModel) {
        this.questionModel = questionModel;
    }

    protected interface Callback{
        void call();
    }
}
