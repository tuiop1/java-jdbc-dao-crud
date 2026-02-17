package com.simpleproject.demo.dao;

import com.simpleproject.demo.config.DataSourceFactory;
import com.simpleproject.demo.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTaskDAO implements DAO<Task, Long> {
    @Override
    public void save(Task task) {
        String sql = """
                INSERT INTO tasks (name, status, description)
                VALUES (?,?,?)
                """;
        try (Connection connection = DataSourceFactory.getDataSource().getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, task.getName());
            ps.setBoolean(2,task.isCompleted());
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
                """;

        List<Task> tasks = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getDataSource().getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {

                tasks.add(new Task(rs.getInt("id"), rs.getString("name"), rs.getBoolean("status"), rs.getString("description")));

            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to list all tasks", e);
        }
        return tasks;
    }

    @Override
    public void delete(Long id) {
        String sql = """
                DELETE FROM tasks
                WHERE id = ?
                """;
        try (Connection connection = DataSourceFactory.getDataSource().getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);


            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete the task", e);
        }
    }

    @Override
    public Task findByID(Long id) {

        String sql = """
                SELECT id,name,status,description
                FROM tasks
                WHERE id = ?
                """;
        try (Connection connection = DataSourceFactory.getDataSource().getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Task(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getBoolean("status"),
                            rs.getString("description")
                    );

                } else return null;
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
        try (Connection connection = DataSourceFactory.getDataSource().getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, task.getName());
            ps.setBoolean(2, task.isCompleted());
            ps.setString(3, task.getDescription());
            ps.setLong(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save a task", e);
        }

    }
}
