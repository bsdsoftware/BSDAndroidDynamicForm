package it.bsdsoftware.dynamicquestion.library;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
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

    private BSDFormAdapter adapter;
    private List<BSDQuestionModel> questions = new ArrayList<>();

    public BSDDynamicForm(Context context) {
        super(context);
        afterCreate();
    }

    public BSDDynamicForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        afterCreate();
    }

    public BSDDynamicForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        afterCreate();
    }

    private void afterCreate(){
        Activity activity = (Activity) getContext();
        adapter = new BSDFormAdapter(activity);
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
        throw new RuntimeException(
                "setAdapter is not supported by BSDDynamicForm.");
    }
}
