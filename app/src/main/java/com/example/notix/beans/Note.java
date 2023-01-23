package com.example.notix.beans;

import java.io.Serializable;

public class Note implements Serializable  {

    String student_dni;
    int subject_id;
    float eva1;
    float eva2;
    float eva3;
    int final1;
    int final2;

    public Note() { }

    public Note(String student_dni, int subject_id, float eva1, float eva2, float eva3, int final1, int final2) {
        this.student_dni = student_dni;
        this.subject_id = subject_id;
        this.eva1 = eva1;
        this.eva2 = eva2;
        this.eva3 = eva3;
        this.final1 = final1;
        this.final2 = final2;
    }

    public String getStudent_dni() { return student_dni; }

    public void setStudent_dni(String student_dni) { this.student_dni = student_dni; }

    public int getSubject_id() { return subject_id; }

    public void setSubject_id(int subject_id) { this.subject_id = subject_id; }

    public float getEva1() { return eva1; }

    public void setEva1(float eva1) { this.eva1 = eva1; }

    public float getEva2() { return eva2; }

    public void setEva2(float eva2) { this.eva2 = eva2; }

    public float getEva3() { return eva3; }

    public void setEva3(float eva3) { this.eva3 = eva3; }

    public int getFinal1() { return final1; }

    public void setFinal1(int final1) { this.final1 = final1; }

    public int getFinal2() { return final2; }

    public void setFinal2(int final2) { this.final2 = final2; }

    @Override
    public String toString() {
        return "{" +
                '"' + "studentDni" + '"' + ":" + '"' + student_dni + '"' +
                "," + '"' + "subjectId" + '"' + ":" + '"'+ subject_id + '"' +
                "," + '"' + "eva1" + '"' + ":" + '"'+ eva1 + '"' +
                "," + '"' + "eva2" + '"' + ":" + '"'+ eva2 + '"' +
                "," + '"' + "eva3" + '"' + ":" + '"'+ eva3 + '"' +
                "," + '"' + "final1" + '"' + ":" + '"'+ final1 + '"' +
                "," + '"' + "final2" + '"' + ":" + '"'+ final2 + '"' +
                "}";
    }

}

