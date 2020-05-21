package com.fromgod.todo.mvvm.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fromgod.todo.mvvm.model.entity.Todo;

import java.util.List;


@Dao
public interface TodoDao {

    @Insert
    void insertTodo(Todo todoItem);

    @Delete
    void deleteTodo(Todo todoItem);

    @Update
    void updateTodo(Todo todoItem);

    @Query("DELETE FROM tasks WHERE status_check=0")
    void deleteAllTodo();

    // get count of pending tasks
    @Query("SELECT COUNT(id) FROM tasks WHERE status_check=0")
    LiveData<Integer> totalPendingTask();

    // get count of completed tasks
    @Query("SELECT COUNT(id) FROM tasks WHERE status_check=1 ")
    LiveData<Integer> totalCompletedTasks();

    @Query("SELECT * FROM tasks WHERE status_check=1 ORDER BY todo_createdAt DESC")
    LiveData<List<Todo>> completedTasks();

    @Query("SELECT * FROM tasks WHERE status_check=0 ORDER BY todo_createdAt DESC")
    LiveData<List<Todo>> getAllNotes();

}