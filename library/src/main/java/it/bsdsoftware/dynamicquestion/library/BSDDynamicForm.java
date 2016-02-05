package it.bsdsoftware.dynamicquestion.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.List;
import it.bsdsoftware.dynamicquestion.library.models.BSDQuestionModel;
import it.bsdsoftware.dynamicquestion.library.models.CallbackComplete;
import it.bsdsoftware.dynamicquestion.library.models.CancelCallback;
import it.bsdsoftware.dynamicquestion.library.models.response.BSDResponse;
import it.bsdsoftware.dynamicquestion.library.models.response.MultiChoice;
import it.bsdsoftware.dynamicquestion.library.models.response.MultiLineText;
import it.bsdsoftware.dynamicquestion.library.models.response.SingleChoice;
import it.bsdsoftware.dynamicquestion.library.models.response.SingleLineText;

/**
 * Created by Simone on 03/11/15.
 */
public class BSDDynamicForm extends LinearLayout {

    private int styleSaveButton = -1;
    private int styleTextSaveButton = -1;
    private int styleTextQuestion = -1;
    private int styleTextResponse = -1;
    private String saveButtonText, cancelButtonText;
    private int colorBackgroundSaveButton = Color.TRANSPARENT;
    private Drawable backgroundSaveButton = null;
    //private BSDFormAdapter adapter;
    private boolean textIsPlaceholder = false;
    private List<BSDQuestionModel> questions = new ArrayList<>();
    private List<BSDFormItem> items = new ArrayList<>();
    private CallbackComplete callbackComplete;
    private CancelCallback cancelCallback;

    public BSDDynamicForm(Context context) {
        super(context);
        afterCreate(null);
    }

    public BSDDynamicForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        afterCreate(attrs);
    }

    public BSDDynamicForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        afterCreate(attrs);
    }

    private void afterCreate(AttributeSet attrs){
        this.setOrientation(VERTICAL);
        Activity activity;
        if(getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }else{
            ContextThemeWrapper ctw = (ContextThemeWrapper) getContext();
            activity = (Activity) ctw.getBaseContext();
        }

        if(attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BSDDynamicForm);
            styleSaveButton = a.getResourceId(R.styleable.BSDDynamicForm_save_button_style, -1);
            styleTextSaveButton = a.getResourceId(R.styleable.BSDDynamicForm_save_button_text_style, -1);
            saveButtonText = a.getString(R.styleable.BSDDynamicForm_save_button_text);
            cancelButtonText = a.getString(R.styleable.BSDDynamicForm_cancel_button_text);
            textIsPlaceholder = a.getBoolean(R.styleable.BSDDynamicForm_text_is_placeholder, false);
            styleTextQuestion = a.getResourceId(R.styleable.BSDDynamicForm_question_text_style, -1);
            styleTextResponse = a.getResourceId(R.styleable.BSDDynamicForm_response_text_style, -1);
            colorBackgroundSaveButton = a.getColor(R.styleable.BSDDynamicForm_save_button_backgrond_color, Color.TRANSPARENT);
            int drawableRes = a.getResourceId(R.styleable.BSDDynamicForm_save_button_background_resource, -1);
            if(drawableRes != -1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    backgroundSaveButton = getResources().getDrawable(drawableRes, activity.getTheme());
                } else {
                    backgroundSaveButton = getResources().getDrawable(drawableRes);
                }
            }
            a.recycle();
        }

        if(saveButtonText == null)
            saveButtonText = getContext().getString(R.string.save_button);
        /*adapter = new BSDFormAdapter(activity);
        adapter.setStyleSaveButton(styleSaveButton);
        adapter.setStyleTextSaveButton(styleTextSaveButton);
        adapter.setStyleTextQuestion(styleTextQuestion);
        adapter.setStyleTextResponse(styleTextResponse);
        adapter.setBackgroundSaveButton(backgroundSaveButton);
        adapter.setColorBackgroundSaveButton(colorBackgroundSaveButton);
        adapter.setSaveButtonText(saveButtonText);
        adapter.setCancelButtonText(cancelButtonText);
        adapter.setTextIsPlaceholder(textIsPlaceholder);
        super.setAdapter(adapter);*/
    }

    public void setQuestions(List<BSDQuestionModel> questions){
        this.removeAllViews();
        Activity activity;
        if(getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }else{
            ContextThemeWrapper ctw = (ContextThemeWrapper) getContext();
            activity = (Activity) ctw.getBaseContext();
        }
        questions.add(null);
        for(BSDQuestionModel model : questions){
            BSDFormItem item = new BSDFormItem(activity);
            item.setStyleSaveButton(styleSaveButton);
            item.setStyleTextSaveButton(styleTextSaveButton);
            item.setStyleTextQuestion(styleTextQuestion);
            item.setStyleTextResponse(styleTextResponse);
            item.setBackgroundSaveButton(backgroundSaveButton);
            item.setColorBackgroundSaveButton(colorBackgroundSaveButton);
            item.setSaveButtonText(saveButtonText);
            item.setCancelButtonText(cancelButtonText);
            item.setTextIsPlaceholder(textIsPlaceholder);
            item.setCancelCallback(cancelCallback);
            item.setCallbackComplete(new BSDFormItem.Callback() {
                @Override
                public void call() {
                    List<BSDResponse> res = checkResponse();
                    callbackComplete.doOnComplete(res);
                }
            });
            item.setQuestionModel(model);
            item.init();
            this.addView(item);
            this.items.add(item);
        }

        /*adapter.clear();
        adapter.addAll(questions);
        adapter.add(null);//barra bottoni
        adapter.notifyDataSetChanged();*/
    }

    private List<BSDResponse> checkResponse() {
        List<BSDResponse> response = new ArrayList<>();
        for(int i = 0; i < this.items.size(); i++){
            BSDQuestionModel model = this.items.get(i).getQuestionModel();
            if(model!=null){
                switch (model.getType()) {
                    case SINGLE_LINE_TEXT:
                        SingleLineText singleLineText = new SingleLineText(model.getQuestionID(), model.getQuestion(), model.getResultText());
                        response.add(singleLineText);
                        break;
                    case MULTI_LINE_TEXT:
                        MultiLineText multiLineText = new MultiLineText(model.getQuestionID(), model.getQuestion(), model.getResultText());
                        response.add(multiLineText);
                        break;
                    case SINGLE_CHOICE:
                        SingleChoice singleChoice = new SingleChoice(model.getQuestionID(), model.getQuestion(), model.getResultChoice());
                        response.add(singleChoice);
                        break;
                    case MULTI_CHOICE:
                        MultiChoice multiChoice = new MultiChoice(model.getQuestionID(), model.getQuestion(), model.getResultMultiChoices());
                        response.add(multiChoice);
                        break;
                }
            }
        }
        return response;
    }

    public void setCallbackComplete(CallbackComplete callbackComplete){
        //adapter.setCallbackComplete(callbackComplete);
        this.callbackComplete = callbackComplete;
    }

    public void addQuestion(BSDQuestionModel question){
        questions.add(question);
        setQuestions(questions);
    }

    public void clearQuestions(){
        questions = new ArrayList<>();
        setQuestions(questions);
    }


    public void setCancelCallback(CancelCallback cancelCallback){
        //adapter.setCancelCallback(cancelCallback);
        this.cancelCallback = cancelCallback;
    }

 /*   @Override
    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported by BSDDynamicForm.");
    }*/

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

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public boolean isTextIsPlaceholder() {
        return textIsPlaceholder;
    }

    public void setTextIsPlaceholder(boolean textIsPlaceholder) {
        this.textIsPlaceholder = textIsPlaceholder;
    }
}
