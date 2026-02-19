package com.simpleproject.demo.dao;

import com.simpleproject.demo.model.Task;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//dao for task using jdbc
public class JdbcTaskDAO implements DAO<Task, Long> {

    private final DataSource dataSource;

    public JdbcTaskDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void save(Task task) {
        String sql = """
                INSERT INTO tasks (name, status, description)
                VALUES (?,?,?)
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, task.getName());
            ps.setBoolean(2, task.isCompleted());
            ps.setString(3, task.getDescription());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save a task", e);
        }
    }

    @Override
    public List<Task> listAll() {
        String sql = """
                SELECT id,name, status, description
                FROM tasks
                ORDER BY id
                """;

        List<Task> tasks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {

                tasks.add(new Task(rs.getLong("id"), rs.getString("name"), rs.getBoolean("status"), rs.getString("description")));

            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all tasks!", e);
        }
        return tasks;
    }

    @Override
    public void delete(Long id) {
        String sql = """
                DELETE FROM tasks
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);


            int rows = ps.executeUpdate();
            if(rows==0){
                throw new RuntimeException("Task with given id was not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete the task", e);
        }
    }

    @Override
    public Optional<Task> findByID(Long id) {

        String sql = """
                SELECT id,name,status,description
                FROM tasks
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Task(rs.getLong("id"), rs.getString("name"), rs.getBoolean("status"), rs.getString("description")));

                } else return Optional.empty();
            }


        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the task by id", e);
        }


    }

    @Override
    public void update(Long id, Task task) {
        String sql = """
                UPDATE tasks
                SET name = ?,status = ?,description = ?
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, task.getName());
            ps.setBoolean(2, task.isCompleted());
            ps.setString(3, task.getDescription());
            ps.setLong(4, id);

            int rows = ps.executeUpdate();
            if(rows==0){
                throw new RuntimeException("Task with given id was not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update the task", e);
        }

    }
}
