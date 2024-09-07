package com.example.elearningplatform;

import android.os.Parcel;

public class Quiz_Question {

    public String Question;
    public String op1;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }

    public String op2;
    public String op3 ;
    public String op4;
    public String correct_ans;

    public String getUserAnswer() {
        return UserAnswer;
    }

    public void setUserAnswer(String setUserAnswer) {
        this.UserAnswer = setUserAnswer;
    }

    public String UserAnswer;


}
