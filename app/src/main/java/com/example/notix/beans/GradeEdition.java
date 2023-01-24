package com.example.notix.beans;

import java.io.Serializable;

public class GradeEdition implements Serializable {

    int grade_edition_id;
    String fecha;
    int grade_id;
    String tutor_dni;

    public GradeEdition() { }

    public GradeEdition(int grade_edition_id, String fecha, int grade_id, String tutor_dni) {
        this.grade_edition_id = grade_edition_id;
        this.fecha = fecha;
        this.grade_id = grade_id;
        this.tutor_dni = tutor_dni;
    }

    public int getGrade_edition_id() { return grade_edition_id; }

    public void setGrade_edition_id(int grade_edition_id) { this.grade_edition_id = grade_edition_id; }

    public String getFecha() { return fecha; }

    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getGrade_id() { return grade_id; }

    public void setGrade_id(int grade_id) { this.grade_id = grade_id; }

    public String getTutor_dni() { return tutor_dni; }

    public void setTutor_dni(String tutor_dni) { this.tutor_dni = tutor_dni; }

    @Override
    public String toString() {
        return "{" +
                '"' + "gradeEdId" + '"' + ":" + '"' + grade_edition_id + '"' +
                "," + '"' + "gradeId" + '"' + ":" + '"'+ grade_id + '"' +
                "," + '"' + "tutorDni" + '"' + ":" + '"'+ tutor_dni + '"' +
                "," + '"' + "fecha" + '"' + ":" + '"'+ fecha + '"' +
                "}";
    }
}

