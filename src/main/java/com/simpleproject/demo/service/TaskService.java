package com.simpleproject.demo.service;

import com.simpleproject.demo.dao.JdbcTaskDAO;
import com.simpleproject.demo.model.Task;
import com.simpleproject.demo.dao.DAO;
import javax.sql.DataSource;
import java.util.List;

//task manager
public class TaskService {


    private final DAO<Task, Long> taskDAO;


    public TaskService(DataSource dataSource) {
        this.taskDAO = new JdbcTaskDAO(dataSource);

    }


    public void createTask(String name, String description) throws IllegalArgumentException {
        if (name.length() < 3) {
            throw new IllegalArgumentException("Failed to create new Task");
        }
        taskDAO.save(new Task(null, name, false, description));

    }


    public List<Task> getAllTasks() {
        return taskDAO.listAll();
    }


    public void deleteTask(long id) {
        if (id < 1) throw new IllegalArgumentException("Failed to delete the task(id does not match)");
        taskDAO.delete(id);
    }


    public Task findTaskByID(long id) {
        if (id < 1) throw new IllegalArgumentException("Failed to find the task(id does not match)");
        return taskDAO.findByID(id).orElseThrow(() -> new RuntimeException("No task found!"));
    }


    public void alterTask(Long id, Task task) {
        if (id < 1) throw new IllegalArgumentException("Failed to alter the task(id is not correct)");
        taskDAO.update(id, task);
    }


}
