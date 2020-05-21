package com.fromgod.todo.mvvm.model.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.fromgod.todo.mvvm.model.dao.TodoDao;
import com.fromgod.todo.mvvm.model.database.TodoDatabase;
import com.fromgod.todo.mvvm.model.entity.Todo;
import java.util.List;


public class TodoRepository {

    private TodoDao todoDao;
    private LiveData<List<Todo>> todoList, completedTodo;
    private LiveData<Integer> pending, completed;

    public TodoRepository(Application application) {
        TodoDatabase database = TodoDatabase.getINSTANCE(application);
        todoDao = database.todoDao();
        todoList = todoDao.getAllNotes();
        completedTodo = todoDao.completedTasks();
        pending = todoDao.totalPendingTask();
        completed = todoDao.totalCompletedTasks();
    }


    /************************ our viewModel will access these methods ******************************/

    public void insert(Todo todo) {
        new InsertTodoAsyncTask(todoDao).execute(todo);
    }

    public void update(Todo todo) {
        new UpdateTodoAsyncTask(todoDao).execute(todo);
    }

    public void delete(Todo todo) {
        new DeleteTodoAsyncTask(todoDao).execute(todo);
    }

    public void deleteAllNotes() {
        new DeleteAllTodoAsyncTask(todoDao).execute();
    }

    public LiveData<Integer> getCompleted() {
        return completed;
    }

    public LiveData<Integer> getPending() {
        return pending;
    }

    public LiveData<List<Todo>> getAllNotes() {
        return todoList;
    }

    public LiveData<List<Todo>> getCompletedTodo() {
        return completedTodo;
    }


    /********************** all asynchronous tasks to run in background thread ************************/

    private static class InsertTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        private InsertTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todo) {
            todoDao.insertTodo(todo[0]);
            return null;
        }
    }


    private static class DeleteTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        private DeleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todo) {
            todoDao.deleteTodo(todo[0]);
            return null;
        }
    }


    private static class UpdateTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        private UpdateTodoAsyncTask(TodoDao noteDao) {
            this.todoDao = noteDao;
        }

        @Override
        protected Void doInBackground(Todo... todo) {
            todoDao.updateTodo(todo[0]);
            return null;
        }
    }


    private static class DeleteAllTodoAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoDao todoDao;

        private DeleteAllTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            todoDao.deleteAllTodo();
            return null;
        }
    }


}       //end class
