package com.example.notix.beans;

public class Grade {

    int grade_id;
    String language;
    String name;

    public Grade(int grade_id, String language, String name) {
        this.grade_id = grade_id;
        this.language = language;
        this.name = name;
    }

    public Grade() {

    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "gradeId" + '"' + ":" + '"' + grade_id + '"' +
                "," + '"' + "language" + '"' + ":" + '"'+ language + '"' +
                "," + '"' + "name" + '"' + ":" + '"'+ name + '"' +
                "}";
    }
}
