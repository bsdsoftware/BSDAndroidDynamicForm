package it.bsdsoftware.dynamicquestion.library.models.response;

import java.util.List;

import it.bsdsoftware.dynamicquestion.library.models.QuestionType;

/**
 * Created by Simone on 03/11/15.
 */
public abstract class BSDResponse {

    protected int questionID;
    protected QuestionType type;
    protected String responseText;
    protected int choice;
    protected List<Integer> multiChoice;
    protected String questionText;


}
