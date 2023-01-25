package com.example.notix.beans;

public class Promotion {

    String student_dni;
    int grade_ed_id;

    public Promotion() { }

    public Promotion(String student_dni, int grade_ed_id) {
        this.student_dni = student_dni;
        this.grade_ed_id = grade_ed_id;
    }

    public String getStudent_dni() {
        return student_dni;
    }

    public void setStudent_dni(String student_dni) {
        this.student_dni = student_dni;
    }

    public int getGrade_ed_id() {
        return grade_ed_id;
    }

    public void setGrade_ed_id(int grade_ed_id) {
        this.grade_ed_id = grade_ed_id;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "studentDni" + '"' + ":" + '"' + student_dni + '"' +
                "," + '"' + "gradeEdId" + '"' + ":" + '"'+ grade_ed_id + '"' +
                "}";
    }
}
