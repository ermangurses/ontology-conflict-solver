package edu.arizona.biosemantics.conflictsolver;

public class Option {

    private int optionId;
    private String term;
    private String definition;
    private String imageLink;

    public Option(int id, String term, String definition, String imageLink) {
        this.optionId = id;
        this.term = term;
        this.definition = definition;
        this.imageLink = imageLink;
    }

    public int getId() {

        return optionId;
    }

    public void setId(int optionId) {

        this.optionId = optionId;
    }

    public String getTerm() {

        return term;
    }

    public void setTerm(String term) {

        this.term = term;
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