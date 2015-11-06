package it.bsdsoftware.dynamicquestion.library.models;

import android.text.InputType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simone on 02/11/15.
 */
public class BSDQuestionModel {

    private String question;
    private int questionID;
    private QuestionType type;
    private List<BSDChoiceModel> choices = new ArrayList<>();
    private String resultText = "";
    private int resultChoice;
    private List<Integer> resultMultiChoices = new ArrayList<>();
    private int inputType;

    private BSDQuestionModel(String question, int questionID) {
        this.question = question;
        this.questionID = questionID;
    }

    public BSDQuestionModel(String question, int questionID, QuestionType questionType){
        this(question, questionID, questionType, "");
    }

    public BSDQuestionModel(String question, int questionID, QuestionType questionType, String defaultValue){
        this(question, questionID);
        this.type = questionType;
        if(questionType == QuestionType.MULTI_LINE_TEXT)
            inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
        else
            inputType = InputType.TYPE_CLASS_TEXT;
        this.resultText = defaultValue;
    }

    public BSDQuestionModel(String question, int questionID, QuestionType questionType, int inputType){
        this(question, questionID, questionType, inputType, "");
    }

    public BSDQuestionModel(String question, int questionID, QuestionType questionType, int inputType, String defaultValue){
        this(question, questionID);
        this.type = questionType;
        this.inputType = inputType;
        this.resultText = defaultValue;
    }

    public BSDQuestionModel(String question, int questionID, QuestionType questionType, List<BSDChoiceModel> choices){
        this(question, questionID, questionType, choices, -1);
    }

    public BSDQuestionModel(String question, int questionID, QuestionType questionType, List<BSDChoiceModel> choices, int choiceDefault){
        this(question, questionID, questionType);
        this.choices = choices;
        if(choiceDefault==-1 && choices.size()>0){
            this.resultChoice = 0;
        }
        else {
            this.resultChoice = choiceDefault;
        }
    }

    public BSDQuestionModel(String question, int questionID, QuestionType questionType, List<BSDChoiceModel> choices, List<Integer> choicesDefault){
        this(question, questionID, questionType, choices);
        this.resultMultiChoices = choicesDefault;
    }

    public String getQuestion() {
        return question;
    }

    public int getQuestionID() {
        return questionID;
    }

    public QuestionType getType() {
        return type;
    }

    public List<BSDChoiceModel> getChoices() {
        return choices;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public int getResultChoice() {
        return resultChoice;
    }

    public void setResultChoice(int resultChoice) {
        this.resultChoice = resultChoice;
    }

    public List<Integer> getResultMultiChoices() {
        return resultMultiChoices;
    }

    public void setResultMultiChoices(List<Integer> resultMultiChoices) {
        this.resultMultiChoices = resultMultiChoices;
    }

    public int getInputType() {
        return inputType;
    }

    public void setDeafultValue(String defaultValue){
        this.resultText = defaultValue;
    }

    public void setDeafultValue(int defaultValue){
        this.resultChoice = defaultValue;
    }

    public void setDeafultValue(List<Integer> defaultValue){
        this.resultMultiChoices = defaultValue;
    }
}
