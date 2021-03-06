package it.bsdsoftware.dynamicquestion.library.models.response;

import it.bsdsoftware.dynamicquestion.library.models.QuestionType;

/**
 * Created by Simone on 03/11/15.
 */
public class SingleLineText extends BSDResponse {

    private SingleLineText(){
        type = QuestionType.SINGLE_LINE_TEXT;
    }

    public SingleLineText(int questionID, String questionText, String responseText){
        this();
        this.questionID = questionID;
        this.questionText = questionText;
        this.responseText = responseText;
    }

    public int getQuestionID(){
        return questionID;
    }

    public String getQuestionText(){
        return questionText;
    }

    public String getResponseText(){
        return responseText;
    }

}
