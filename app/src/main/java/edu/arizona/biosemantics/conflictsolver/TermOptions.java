package edu.arizona.biosemantics.conflictsolver;

public class Option {

    private static String term;
    private static String sentence;
    private String option;
    private String definition;
    private String imageLink;

    public Option(String term, String option, String definition, String imageLink) {
        this.term        = term;
        this.option      = option;
        this.definition  = definition;
        this.imageLink   = imageLink;
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

    public String getOption() {

        return option;
    }

    public void setOption(String option) {

        this.option = option;
    }

    public String getDefinition() {

        return definition;
    }

    public void setDefinition(String definition) {

        this.term = definition;
    }

    public String getImageLink() {

        return imageLink;
    }

    public void setImageLink(String imageLink) {

        this.imageLink = imageLink;
    }
}