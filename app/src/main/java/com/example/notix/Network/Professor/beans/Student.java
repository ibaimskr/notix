package com.example.notix.Network.Professor.beans;

import java.io.Serializable;

public class Student implements Serializable  {

    String student_dni;
    String name;
    String surname;
    String born_date;
    String nationality;
    String email;
    String phone;
    String photo;

    public Student() { }

    public Student(String student_dni, String name, String surname, String born_date, String nationality, String email, String phone, String photo) {
        this.student_dni = student_dni;
        this.name = name;
        this.surname = surname;
        this.born_date = born_date;
        this.nationality = nationality;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
    }

    public String getStudent_dni() {
        return student_dni;
    }

    public void setStudent_dni(String student_dni) {
        this.student_dni = student_dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBorn_date() {
        return born_date;
    }

    public void setBorn_date(String born_date) {
        this.born_date = born_date;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "studentDni" + '"' + ":" + '"' + student_dni + '"' +
                "," + '"' + "name" + '"' + ":" + '"'+ name + '"' +
                "," + '"' + "surname" + '"' + ":" + '"'+ surname + '"' +
                "," + '"' + "bornDate" + '"' + ":" + '"'+ born_date + '"' +
                "," + '"' + "nationality" + '"' + ":" + '"'+ nationality + '"' +
                "," + '"' + "email" + '"' + ":" + '"'+ email + '"' +
                "," + '"' + "phone" + '"' + ":" + '"'+ phone + '"' +
                "," + '"' + "photo" + '"' + ":" + '"'+ photo + '"' +
                "}";
    }
}

