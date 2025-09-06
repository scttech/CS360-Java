package com.example.hello_database;

import androidx.annotation.NonNull;

import java.util.Locale;

public class User {
    private final int id;
    private final String name;
    private final int age;

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "ID: %d%nName: %s%nAge: %d", id, name, age);
    }
}