package com.example.projekt3okienka;

import javafx.beans.property.*;

public class Employee {
    private StringProperty imie;
    private StringProperty nazwisko;
    private DoubleProperty wynagrodzenie;
    private ObjectProperty<EmployeeCondition> stanPracownika;
    private IntegerProperty rokUrodzenia;

    public Employee() {
        this.imie = new SimpleStringProperty("");
        this.nazwisko = new SimpleStringProperty("");
        this.wynagrodzenie = new SimpleDoubleProperty(0);
        this.stanPracownika = new SimpleObjectProperty<>(EmployeeCondition.OBECNY);
        this.rokUrodzenia = new SimpleIntegerProperty(0);
    }
    public Employee(String imie, String nazwisko, double wynagrodzenie, String stan, int rokUrodzenia) {
        this.imie = new SimpleStringProperty(imie);
        this.nazwisko = new SimpleStringProperty(nazwisko);
        this.wynagrodzenie = new SimpleDoubleProperty(wynagrodzenie);

        if (stan != null) {
            this.stanPracownika = new SimpleObjectProperty<>(EmployeeCondition.valueOf(stan.toUpperCase()));
        } else {
            this.stanPracownika = new SimpleObjectProperty<>(EmployeeCondition.OBECNY); // Domyślny stan
        }

        this.rokUrodzenia = new SimpleIntegerProperty(rokUrodzenia);
    }
    public String getImie() {
        return imie.get();
    }

    public void setImie(String imie) {
        this.imie.set(imie);
    }

    public StringProperty imieProperty() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko.get();
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko.set(nazwisko);
    }

    public StringProperty nazwiskoProperty() {
        return nazwisko;
    }

    public double getWynagrodzenie() {
        return wynagrodzenie.get();
    }

    public void setWynagrodzenie(double wynagrodzenie) {
        this.wynagrodzenie.set(wynagrodzenie);
    }

    public DoubleProperty wynagrodzenieProperty() {
        return wynagrodzenie;
    }

    public EmployeeCondition getStanPracownika() {
        return stanPracownika.get();
    }

    public void setStanPracownika(EmployeeCondition stanPracownika) {
        this.stanPracownika.set(stanPracownika);
    }

    public ObjectProperty<EmployeeCondition> stanPracownikaProperty() {
        return stanPracownika;
    }

    public int getRokUrodzenia() {
        return rokUrodzenia.get();
    }

    public void setRokUrodzenia(int rokUrodzenia) {
        this.rokUrodzenia.set(rokUrodzenia);
    }

    public IntegerProperty rokUrodzeniaProperty() {
        return rokUrodzenia;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "imie=" + imie.get() +
                ", nazwisko=" + nazwisko.get() +
                ", wynagrodzenie=" + wynagrodzenie.get() +
                ", stanPracownika=" + stanPracownika.get() +
                ", rokUrodzenia=" + rokUrodzenia.get() +
                '}';
    }
}
