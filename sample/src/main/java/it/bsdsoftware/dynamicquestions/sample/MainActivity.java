package it.bsdsoftware.dynamicquestions.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import it.bsdsoftware.dynamicquestion.library.BSDDynamicForm;
import it.bsdsoftware.dynamicquestion.library.models.BSDChoiceModel;
import it.bsdsoftware.dynamicquestion.library.models.BSDQuestionModel;
import it.bsdsoftware.dynamicquestion.library.models.CallbackComplete;
import it.bsdsoftware.dynamicquestion.library.models.QuestionType;
import it.bsdsoftware.dynamicquestion.library.models.response.BSDResponse;
import it.bsdsoftware.dynamicquestion.library.models.response.MultiChoice;
import it.bsdsoftware.dynamicquestion.library.models.response.MultiLineText;
import it.bsdsoftware.dynamicquestion.library.models.response.SingleChoice;
import it.bsdsoftware.dynamicquestion.library.models.response.SingleLineText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BSDDynamicForm bsdDynamicForm = (BSDDynamicForm) findViewById(R.id.gridView);

        bsdDynamicForm.setCallbackComplete(new CallbackComplete() {
            @Override
            public void doOnComplete(List<? extends BSDResponse> response) {
                for(int i = 0; i < response.size(); i++){
                    if(response.get(i) instanceof SingleLineText){
                        SingleLineText res = (SingleLineText) response.get(i);
                    }
                    if(response.get(i) instanceof MultiLineText){
                        MultiLineText res = (MultiLineText) response.get(i);
                    }
                    if(response.get(i) instanceof SingleChoice){
                        SingleChoice res = (SingleChoice) response.get(i);
                    }
                    if(response.get(i) instanceof MultiChoice){
                        MultiChoice res = (MultiChoice) response.get(i);
                    }
                }
            }
        });

        List<BSDQuestionModel> questions = new ArrayList<>();

        BSDQuestionModel model = new BSDQuestionModel("Question 1 (Single line text)", 1, QuestionType.SINGLE_LINE_TEXT);
        questions.add(model);
        model = new BSDQuestionModel("Question 2 (Single line text)", 2, QuestionType.SINGLE_LINE_TEXT);
        questions.add(model);
        model = new BSDQuestionModel("Question 3 (Multi line text)", 3 , QuestionType.MULTI_LINE_TEXT);
        questions.add(model);
        model = new BSDQuestionModel("Question 4 (Multi line text)", 4 , QuestionType.MULTI_LINE_TEXT);
        questions.add(model);

        List<BSDChoiceModel> choiceModels = new ArrayList<>();
        choiceModels.add(new BSDChoiceModel("choice 1", 1));
        choiceModels.add(new BSDChoiceModel("choice 2", 2));
        choiceModels.add(new BSDChoiceModel("choice 3", 3));
        choiceModels.add(new BSDChoiceModel("choice 4", 4));
        choiceModels.add(new BSDChoiceModel("choice 5", 5));
        choiceModels.add(new BSDChoiceModel("choice 6", 6));
        choiceModels.add(new BSDChoiceModel("choice 7", 7));

        model = new BSDQuestionModel("Question 5 (Single choice)", 5, QuestionType.SINGLE_CHOICE, choiceModels);
        questions.add(model);

        model = new BSDQuestionModel("Question 6 (Single choice)", 6, QuestionType.SINGLE_CHOICE, choiceModels);
        questions.add(model);

        model = new BSDQuestionModel("Question 7 (Multi choice)", 7, QuestionType.MULTI_CHOICE, choiceModels);
        questions.add(model);

        model = new BSDQuestionModel("Question 8 (Multi choice)", 8, QuestionType.MULTI_CHOICE, choiceModels);
        questions.add(model);

        model = new BSDQuestionModel("Question 9 (Multi choice)", 9, QuestionType.MULTI_CHOICE, choiceModels);
        questions.add(model);


        bsdDynamicForm.setQuestions(questions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
