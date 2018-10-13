package edu.arizona.biosemantics.conflictsolver;

import java.util.ArrayList;

public class TermOptions {

    private  String term;
    private  String sentence;

    private ArrayList<String> optionArr     = new ArrayList<String>();
    private ArrayList<String> definitionArr = new ArrayList<String>();
    private ArrayList<String> imageLinkArr  = new ArrayList<String>();

    private int size = 0;
    public TermOptions(){

        this.term       = "";
        this.sentence   = "";
    };
    public TermOptions(String term, String sentence, String option, String definition,
                       String imageLink) {
        this.term       = term;
        this.sentence   = sentence;
        this.optionArr. add(option);
        this.definitionArr.add(definition);
        this.imageLinkArr.add(imageLink);
    }

    public String getTerm() {

        return term;
    }

    public void setTerm(String term) {

        this.term = term;
    }
    public String getSentence() {

        return sentence;
    }

    public void setSentence(String sentence) {

        this.sentence = sentence;
    }

    public ArrayList<String> getOptions() {

        return optionArr;
    }

    public void addOption(String option) {

        this.optionArr.add(option);
    }

    public ArrayList<String> getDefinitions() {

        return this.definitionArr;
    }

    public void addDefinition(String definition) {

        this.definitionArr.add(definition);
    }

    public ArrayList<String> getImageLinks() {

        return this.imageLinkArr;
    }

    public void addImageLink(String imageLink) {

        size++;
        this.imageLinkArr.add(imageLink);
    }

    public int getSize(){

        return size;
    }
}