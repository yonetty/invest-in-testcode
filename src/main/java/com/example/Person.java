package com.example;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class Person {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    public Person(String firstName, String lastName, LocalDate birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDay;
    }

    public Person(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    public Optional<Integer> getAgeAt(LocalDate theDate) {
        if (checkDateForAgeCalculation(theDate)) return Optional.empty();

        int years = Period.between(birthDate, theDate).getYears();
        return Optional.of(years);
    }

    private boolean checkDateForAgeCalculation(LocalDate theDate) {
        if (theDate == null) {
            throw new IllegalArgumentException();
        }
        if (birthDate == null) {
            return true;
        }
        if (theDate.isBefore(birthDate)) {
            return true;
        }
        return false;
    }

    public Optional<Integer> getKazoedoshiAt(LocalDate theDate) {
        if (checkDateForAgeCalculation(theDate)) return Optional.empty();

        int birthYear = birthDate.getYear();
        int theYear = theDate.getYear();
        int year = theYear - birthYear + 1;

        return Optional.of(year);
    }

}
