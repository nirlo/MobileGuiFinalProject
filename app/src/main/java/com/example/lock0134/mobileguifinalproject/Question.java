package com.example.lock0134.mobileguifinalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by William on 2018-04-03.
 */

public class Question implements Serializable {

    private String question;
    private int id = -1;
    private List<String> answers = new ArrayList();
    private String rightAnswer;
    private String type;
    private int digits;

    public Question(){

    }
    public Question(int id, String question, List answers, String rightAnswer, String type , int digits) {
        this.id = id;
        this.answers = answers;
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.type = type;
        this.digits = digits;
    }

    public int getID() {
        return id;
    }
    public String getRightAnswer(){
        return rightAnswer;
    }

    public List<String> getAnswers(){
        return answers;
    }

    public int getDigits(){
        return digits;
    }

    public String getType(){
        return type;
    }
    public String getQuestion() {
        return question;
    }

    public void setID(int id) {
        this.id = id;
    }
    public void setRightAnswer(String r){
        rightAnswer = r;
    }

    public void setAnswers(List ans){
        answers = ans;
    }

    public void setType(String type){
        this.type = type;
    }
    public void setQuestion(String q) {
        question = q;
    }
    public void setDigits(int digits){
        this.digits = digits;
    }


}
