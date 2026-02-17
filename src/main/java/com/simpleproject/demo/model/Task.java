package com.simpleproject.demo.model;

public class Task {
    private long id;
    private String name;
    private boolean isCompleted;
    private String description;


    public Task(long id, String name, boolean isCompleted, String description) {
        this.id = id;
        this.name = name;
        this.isCompleted = isCompleted;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getDescription() {
        return description;
    }
}
