package com.example.notix.Network.Professor.beans;

public class StudentForAbsences {
    String name ;
    String surname ;
    String dni ;

    public StudentForAbsences() {

    }
    public StudentForAbsences(String name, String surname, String dni) {
        this.name = name;
        this.surname = surname;
        this.dni = dni;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
    public String toString() {
        return surname + "," + name ;
    }
}
