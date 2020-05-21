package com.fromgod.todo.mvvm.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fromgod.todo.R;
import com.fromgod.todo.mvvm.model.entity.Todo;
import com.fromgod.todo.mvvm.view.interfaces.ItemCompletedListener;


public class AdapterCompletedList extends ListAdapter<Todo, AdapterCompletedList.TodoListHolder> {
    private ItemCompletedListener mListener;

    public AdapterCompletedList() {
        super(DIFF_CALLBACK);
    }

    //to check weather to items have same id or not
    private static final DiffUtil.ItemCallback<Todo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Todo>() {
        @Override
        public boolean areItemsTheSame(Todo oldItem, Todo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        //to check weather to items have same contacts or not
        @Override
        public boolean areContentsTheSame(Todo oldItem, Todo newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getCreated_at().equals(newItem.getCreated_at());
        }
    };

    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_todo_item_done, parent, false);

        return new TodoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListHolder holder, int position) {
        Todo currentTodo = getItem(position);
        holder.title.setText(currentTodo.getTitle());
        holder.description.setText(currentTodo.getCreated_at());
    }

    /**
     * Sets click listener.
     * @param itemCompletedListener the item click listener
     * allows clicks events to be caught
     */
    public void setCompletedListener(ItemCompletedListener itemCompletedListener) {
        this.mListener = itemCompletedListener;
    }


    public class TodoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView description;
        ImageButton DeleteTodo;
        ImageView undoTodo;

        public TodoListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_item_title);
            description = itemView.findViewById(R.id.tv_item_content);
            DeleteTodo = itemView.findViewById(R.id.delete_todo);
            DeleteTodo.setOnClickListener(this);
            undoTodo = itemView.findViewById(R.id.undo_todo);
            undoTodo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            switch (view.getId()) {
                case R.id.delete_todo: {
                    if (mListener != null)
                        mListener.onDeleteItem(getItem(position));
                    break;
                }
                case R.id.undo_todo: {
                    if (mListener != null)
                        mListener.onUndoItem(getItem(position));
                    break;
                }
                default: {
                    break;
                }
            }

        }       //end onClick()


    }       //end inner class


}       //end class
