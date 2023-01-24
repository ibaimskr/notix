package com.example.notix.Network.Professor.beans;

import java.io.Serializable;

public class Subject implements Serializable {

    int subject_id;
    int grade_edition_id;
    String professor_dni;
    String name;
    int duration;

    public Subject() { }

    public Subject(int subject_id, int grade_edition_id, String professor_dni, String name, int duration) {
        this.subject_id = subject_id;
        this.grade_edition_id = grade_edition_id;
        this.professor_dni = professor_dni;
        this.name = name;
        this.duration = duration;
    }

    public int getSubject_id() { return subject_id; }

    public void setSubject_id(int subject_id) { this.subject_id = subject_id; }

    public int getGrade_edition_id() { return grade_edition_id; }

    public void setGrade_edition_id(int grade_edition_id) { this.grade_edition_id = grade_edition_id; }

    public String getProfessor_dni() { return professor_dni; }

    public void setProfessor_dni(String professor_dni) { this.professor_dni = professor_dni; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    @Override
    public String toString() {
        return "{" +
                "," + '"' + "subjectId" + '"' + ":" + '"'+ subject_id + '"' +
                "," + '"' + "gradeEditionId" + '"' + ":" + '"'+ grade_edition_id + '"' +
                "," + '"' + "professorDni" + '"' + ":" + '"'+ professor_dni + '"' +
                "," + '"' + "name" + '"' + ":" + '"'+ name + '"' +
                "," + '"' + "duration" + '"' + ":" + '"'+ duration + '"' +
                "}";
    }
}
