package com.fromgod.todo.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fromgod.todo.mvvm.model.entity.Todo;
import com.fromgod.todo.mvvm.model.repository.TodoRepository;

import java.util.List;


public class ViewModelTodo extends AndroidViewModel {

    private TodoRepository repository;
    private LiveData<List<Todo>> todoList, completedTasks;
    private LiveData<Integer> completed, pending;


    public ViewModelTodo(@NonNull Application application) {
        super(application);

        repository = new TodoRepository(application);
        todoList = repository.getAllNotes();
        completedTasks = repository.getCompletedTodo();
        completed = repository.getCompleted();
        pending = repository.getPending();
    }

    public void insert(Todo todo) {
        repository.insert(todo);
    }

    public void delete(Todo todo) {
        repository.delete(todo);
    }

    public void update(Todo todo) {
        repository.update(todo);
    }

    public void deleteAll() {
        repository.deleteAllNotes();
    }

    public LiveData<Integer> getCompleted() {
        return completed;
    }

    public LiveData<Integer> getPending() {
        return pending;
    }

    public LiveData<List<Todo>> getTodoList() {
        return todoList;
    }

    public LiveData<List<Todo>> getCompletedTasks() {
        return completedTasks;
    }


}
