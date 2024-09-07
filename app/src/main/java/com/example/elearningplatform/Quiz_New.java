package com.example.elearningplatform;

import com.example.elearningplatform.Quiz_Question;

import java.util.List;

public class Quiz_New {

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    int qid;

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    int cid;
    List<Quiz_Question> QuestionsList ;
    String Quiz_Title;
    int QuestionsNumber;

    public List<Quiz_Question> getQuestionsList() {
        return QuestionsList;
    }

    public void setQuestionsList(List<Quiz_Question> questionsList) {
        QuestionsList = questionsList;
    }

    public String getQuiz_Title() {
        return Quiz_Title;
    }

    public void setQuiz_Title(String quiz_Title) {
        Quiz_Title = quiz_Title;
    }

    public int getQuestionsNumber() {
        return QuestionsNumber;
    }

    public void setQuestionsNumber(int questionsNumber) {
        QuestionsNumber = questionsNumber;
    }

    public String getWeekno() {
        return Weekno;
    }

    public void setWeekno(String weekno) {
        Weekno = weekno;
    }

    String Weekno;
}
