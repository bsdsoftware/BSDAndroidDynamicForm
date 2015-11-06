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
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.List;
import it.bsdsoftware.dynamicquestion.library.models.BSDQuestionModel;
import it.bsdsoftware.dynamicquestion.library.models.CallbackComplete;

/**
 * Created by Simone on 03/11/15.
 */
public class BSDDynamicForm extends GridView {

    private int styleSaveButton = -1;
    private int styleTextSaveButton = -1;
    private int styleTextQuestion = -1;
    private int styleTextResponse = -1;
    private String saveButtonText;
    private int colorBackgroundSaveButton = Color.TRANSPARENT;
    private Drawable backgroundSaveButton = null;
    private BSDFormAdapter adapter;
    private List<BSDQuestionModel> questions = new ArrayList<>();

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
        adapter = new BSDFormAdapter(activity);
        adapter.setStyleSaveButton(styleSaveButton);
        adapter.setStyleTextSaveButton(styleTextSaveButton);
        adapter.setStyleTextQuestion(styleTextQuestion);
        adapter.setStyleTextResponse(styleTextResponse);
        adapter.setBackgroundSaveButton(backgroundSaveButton);
        adapter.setColorBackgroundSaveButton(colorBackgroundSaveButton);
        adapter.setSaveButtonText(saveButtonText);
        super.setAdapter(adapter);
    }

    public void setQuestions(List<BSDQuestionModel> questions){
        adapter.clear();
        adapter.addAll(questions);
        adapter.add(null);
        adapter.notifyDataSetChanged();
    }

    public void setCallbackComplete(CallbackComplete callbackComplete){
        adapter.setCallbackComplete(callbackComplete);
    }

    public void addQuestion(BSDQuestionModel question){
        questions.add(question);
        setQuestions(questions);
    }

    public void clearQuestions(){
        questions = new ArrayList<>();
        setQuestions(questions);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported by BSDDynamicForm.");
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
}
