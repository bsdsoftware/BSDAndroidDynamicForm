package it.bsdsoftware.dynamicquestions.sample;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
                String result = "";
                for(int i = 0; i < response.size(); i++){
                    if(response.get(i) instanceof SingleLineText){
                        SingleLineText res = (SingleLineText) response.get(i);
                        result += String.format("question %s: %s\n", res.getQuestionID(), res.getResponseText());
                    }
                    if(response.get(i) instanceof MultiLineText){
                        MultiLineText res = (MultiLineText) response.get(i);
                        result += String.format("question %s: %s\n", res.getQuestionID(), res.getResponseText());
                    }
                    if(response.get(i) instanceof SingleChoice){
                        SingleChoice res = (SingleChoice) response.get(i);
                        result += String.format("question %s: %s\n", res.getQuestionID(), res.getChoice());
                    }
                    if(response.get(i) instanceof MultiChoice){
                        MultiChoice res = (MultiChoice) response.get(i);
                        String choices = "";
                        for(int k : res.getMultiChoices()){
                            choices += k + ",";
                        }
                        choices = choices.substring(0, choices.length()-1);
                        result += String.format("question %s: %s\n", res.getQuestionID(), choices);
                    }
                }
                new AlertDialog.Builder(MainActivity.this, it.bsdsoftware.dynamicquestion.library.R.style.MyAlertDialog)
                        .setTitle("Results")
                        .setMessage(result)
                        .setNegativeButton("Close", null)
                        .show();
            }
        });

        List<BSDQuestionModel> questions = new ArrayList<>();

        BSDQuestionModel model = new BSDQuestionModel("Question 1 (Single line email)", 1, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, "placeholder");
        questions.add(model);
        model = new BSDQuestionModel("Question 2 (Single line url)", 2, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI, "http://");
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

        List<Integer> defaultM = new ArrayList<>();
        defaultM.add(1);
        defaultM.add(2);

        model = new BSDQuestionModel("Question 5 (Single choice)", 5, QuestionType.SINGLE_CHOICE, choiceModels, 2);
        questions.add(model);
        model = new BSDQuestionModel("Question 6 (Single choice)", 6, QuestionType.SINGLE_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 7 (Multi choice)", 7, QuestionType.MULTI_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 8 (Multi choice)", 8, QuestionType.MULTI_CHOICE, choiceModels, defaultM);
        questions.add(model);
        model = new BSDQuestionModel("Question 9 (Multi choice)", 9, QuestionType.MULTI_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 10 (Single line number)", 10, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_NUMBER);
        questions.add(model);
        model = new BSDQuestionModel("Question 11 (Single line email)", 11, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        questions.add(model);
        model = new BSDQuestionModel("Question 12 (Single line email)", 12, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        questions.add(model);
        model = new BSDQuestionModel("Question 13 (Single line email)", 13, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        questions.add(model);
        model = new BSDQuestionModel("Question 14 (Single choice)", 14, QuestionType.SINGLE_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 15 (Single choice)", 15, QuestionType.SINGLE_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 16 (Single choice)", 16, QuestionType.SINGLE_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 17 (Multi choice)", 17, QuestionType.MULTI_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 18 (Single line number)", 18, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_NUMBER);
        questions.add(model);
        model = new BSDQuestionModel("Question 19 (Multi choice)", 19, QuestionType.MULTI_CHOICE, choiceModels);
        questions.add(model);
        model = new BSDQuestionModel("Question 20 (Single line number)", 20, QuestionType.SINGLE_LINE_TEXT, InputType.TYPE_CLASS_NUMBER);
        questions.add(model);

        bsdDynamicForm.setQuestions(questions);

        Button btn = (Button) findViewById(R.id.btn_dialog);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainDialogFragment mdf = new MainDialogFragment();
                mdf.show(getSupportFragmentManager(), "tag");
            }
        });
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
