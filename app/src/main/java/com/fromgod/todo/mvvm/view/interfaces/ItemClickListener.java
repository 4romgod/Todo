package com.fromgod.todo.mvvm.view.interfaces;

import com.fromgod.todo.mvvm.model.entity.Todo;

/**
 * The interface Item click listener.
 * parent activity will implement this method to respond to click events
 */
public interface ItemClickListener {
    void onDeleteItem(Todo todo);
    void onEditItem(Todo todo);
    void onCheckItem(Todo todo);
}