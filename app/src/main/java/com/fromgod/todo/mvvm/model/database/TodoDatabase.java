package com.fromgod.todo.mvvm.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fromgod.todo.mvvm.model.dao.TodoDao;
import com.fromgod.todo.mvvm.model.entity.Todo;


@Database(entities = Todo.class, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase INSTANCE;

    public abstract TodoDao todoDao();

    public static synchronized TodoDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                    , TodoDatabase.class, "layout_todo_item")
                    .fallbackToDestructiveMigration().build();
        }
        return INSTANCE;

    }       //end getINSTANCE()


}       //end class
