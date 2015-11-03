package it.bsdsoftware.dynamicquestion.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simone on 02/11/15.
 */
class MultiChoiceModel {

    private List<String> labels = new ArrayList<>();
    private List<Integer> values = new ArrayList<>();

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public String getLabelSelected(){
        String selected = "";
        for(String str : labels){
            selected += str + ", ";
        }
        if(!selected.equals("")){
            selected = selected.substring(0, selected.length()-2);
        }
        return selected;
    }
}
