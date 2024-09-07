package com.example.elearningplatform;

import java.io.Serializable;

public class Courses implements Serializable {

    public String getCourse_title() {
        return Course_title;
    }

    public void setCourse_title(String course_title) {
        Course_title = course_title;
    }

    public String getCourse_Description() {
        return Course_Description;
    }

    public void setCourse_Description(String course_Description) {
        Course_Description = course_Description;
    }

    public String getCourse_image() {
        return Course_image;
    }

    public void setCourse_image(String course_image) {
        Course_image = course_image;
    }

    String Course_title;
String Course_Description;
String Course_image;

int cid;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
