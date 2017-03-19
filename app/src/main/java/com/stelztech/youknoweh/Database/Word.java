package com.stelztech.youknoweh.Database;

/**
 * Created by Alexandre on 4/25/2016.
 */
public class Word {


    private String idWord;
    private String question;
    private String answer;
    private String moreInfo;

    Word() {
    }

    Word(String idWord, String question, String answer, String moreInfo) {
        this.idWord = idWord;
        this.question = question;
        this.answer = answer;
        this.moreInfo = moreInfo;
    }

    public String getIdWord() {
        return idWord;
    }

    public void setIdWord(String idWord) {
        this.idWord = idWord;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}
