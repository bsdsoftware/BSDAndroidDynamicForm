package it.bsdsoftware.dynamicquestion.library.models.response;

import it.bsdsoftware.dynamicquestion.library.models.QuestionType;

/**
 * Created by Simone on 03/11/15.
 */
public class SingleChoice extends BSDResponse {

    private SingleChoice(){
        type = QuestionType.SINGLE_CHOICE;
    }

    public SingleChoice(int questionID, String questionText, int choice){
        this();
        this.questionID = questionID;
        this.questionText = questionText;
        this.choice = choice;
    }

    public int getQuestionID(){
        return questionID;
    }

    public String getQuestionText(){
        return questionText;
    }

    public int getChoice(){
        return choice;
    }
}
