package it.bsdsoftware.dynamicquestion.library.models.response;

import java.util.List;
import it.bsdsoftware.dynamicquestion.library.models.QuestionType;

/**
 * Created by Simone on 03/11/15.
 */
public class MultiChoice extends BSDResponse {

    private MultiChoice(){
        type = QuestionType.MULTI_CHOICE;
    }

    public MultiChoice(int questionID, String questionText, List<Integer> choices){
        this();
        this.questionID = questionID;
        this.questionText = questionText;
        this.multiChoice = choices;
    }

    public int getQuestionID(){
        return questionID;
    }

    public String getQuestionText(){
        return questionText;
    }

    public List<Integer> getMultiChoices(){
        return multiChoice;
    }
}
