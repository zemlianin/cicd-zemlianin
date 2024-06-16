package org.example.models.entities;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Customer {
    public Customer() {
    }

    public Customer(Long id, String firstName, String lastName, LocalDate birthDay) {
        this.customerId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long customerId;

    String firstName;

    String lastName;

    LocalDate birthDay;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long id) {
        this.customerId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }
}