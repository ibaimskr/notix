package com.example.notix.Network.Professor.beans;

import java.io.Serializable;

public class Professor implements Serializable {

    String professor_dni;
    String name;
    String surname;
    String nationality;
    String email;
    String adress;
    String photo;

    public Professor() { }

    public Professor(String professor_dni, String name, String surname, String nationality, String email, String adress, String photo) {
        this.professor_dni = professor_dni;
        this.name = name;
        this.surname = surname;
        this.nationality = nationality;
        this.email = email;
        this.adress = adress;
        this.photo = photo;
    }

    public String getProfessor_dni() {
        return professor_dni;
    }

    public void setProfessor_dni(String professor_dni) {
        this.professor_dni = professor_dni;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "professorDni" + '"' + ":" + '"' + professor_dni + '"' +
                "," + '"' + "name" + '"' + ":" + '"'+ name + '"' +
                "," + '"' + "surname" + '"' + ":" + '"'+ surname + '"' +
                "," + '"' + "nationality" + '"' + ":" + '"'+ nationality + '"' +
                "," + '"' + "email" + '"' + ":" + '"'+ email + '"' +
                "," + '"' + "addres" + '"' + ":" + '"'+ adress + '"' +
                "," + '"' + "photo" + '"' + ":" + '"'+ photo + '"' +
                "}";
    }
}

