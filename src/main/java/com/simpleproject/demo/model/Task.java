package com.simpleproject.demo.model;

//model of task
public class Task {
    private final Long id;
    private final String name;
    private final boolean isCompleted;
    private final String description;


    public Task(Long id, String name, boolean isCompleted, String description) throws IllegalArgumentException {
        this.id = id;
        this.name = name;
        this.isCompleted = isCompleted;
        this.description = description;

        if(name == null || name.length() < 3 ) throw new IllegalArgumentException("Failed to create Task(name is not correct)");

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

    @Override
    public String toString(){
       return String.format("%d | %s%s\n(description.): %s",id,name,isCompleted ? " (completed) " :" (not completed) ",description);
    }
}
