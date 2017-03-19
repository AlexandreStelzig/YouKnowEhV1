package com.stelztech.youknoweh.Database;

/**
 * Created by Alexandre on 4/25/2016.
 */
public class WordPackage {


    private String idWord;


    private String idPackage;

    WordPackage() {
    }

    WordPackage(String idWord, String idPackage) {
        this.idWord = idWord;
        this.idPackage = idPackage;
    }

    public String getIdWord() {
        return idWord;
    }

    public void setIdWord(String idWord) {
        this.idWord = idWord;
    }

    public String getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(String idPackage) {
        this.idPackage = idPackage;
    }


}
