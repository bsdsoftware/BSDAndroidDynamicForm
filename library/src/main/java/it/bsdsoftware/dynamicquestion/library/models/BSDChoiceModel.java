package it.bsdsoftware.dynamicquestion.library.models;

/**
 * Created by Simone on 02/11/15.
 */
public class BSDChoiceModel {

    private String label;
    private int value;

    public BSDChoiceModel(String label, int value){
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
