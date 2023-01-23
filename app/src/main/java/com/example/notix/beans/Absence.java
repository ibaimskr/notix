package com.example.notix.beans;

import java.io.Serializable;

public class Absence implements Serializable  {

    String student_dni;
    int subject_id;
    String foul;
    Boolean justified;

    public Absence() {
    }

    public Absence(String student_dni, int subject_id, String foul, Boolean justified) {
        this.student_dni = student_dni;
        this.subject_id = subject_id;
        this.foul = foul;
        this.justified = justified;
    }

    public String getStudent_dni() {
        return student_dni;
    }

    public void setStudent_dni(String student_dni) {
        this.student_dni = student_dni;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getFoul() {
        return foul;
    }

    public void setFoul(String foul) {
        this.foul = foul;
    }

    public Boolean getJustified() {
        return justified;
    }

    public void setJustified(Boolean justified) {
        this.justified = justified;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "studentDni" + '"' + ":" + '"' + student_dni + '"' +
                "," + '"' + "subjectId" + '"' + ":" + '"'+ subject_id + '"' +
                "," + '"' + "foul" + '"' + ":" + '"'+ foul + '"' +
                "," + '"' + "justified" + '"' + ":" + '"'+ justified + '"' +
                "}";
    }

}
