package edu.arizona.biosemantics.conflictsolver;

import java.util.Vector;

public class TermOptions {

    private  String term;
    private  String sentence;

    private Vector<String> optionArr     = new Vector<String>();
    private Vector<String> definitionArr = new Vector<String>();
    private Vector<String> imageLinkArr  = new Vector<String>();

    public TermOptions(){

        this.term       = "";
        this.sentence   = "";
        this.optionArr.addElement("");
        this.definitionArr.addElement("");
        this.imageLinkArr.addElement("");

    };
    public TermOptions(String term, String sentence, String option, String definition,
                       String imageLink) {
        this.term       = term;
        this.sentence   = sentence;
        this.optionArr.addElement(option);
        this.definitionArr.addElement(definition);
        this.imageLinkArr.addElement(imageLink);
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

    public Vector<String> getOptions() {

        return optionArr;
    }

    public void addOption(String option) {

        this.optionArr.addElement(option);
    }

    public Vector<String> getDefinitions() {

        return this.definitionArr;
    }

    public void addDefinition(String definition) {

        this.definitionArr.addElement(definition);
    }

    public Vector<String> getImageLinks() {

        return this.imageLinkArr;
    }

    public void setImageLink(String imageLink) {

        this.imageLinkArr.addElement(imageLink);
    }
}