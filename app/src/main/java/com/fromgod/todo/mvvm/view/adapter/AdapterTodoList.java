package com.fromgod.todo.mvvm.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fromgod.todo.R;
import com.fromgod.todo.mvvm.model.entity.Todo;
import com.fromgod.todo.mvvm.view.interfaces.ItemClickListener;


public class AdapterTodoList extends ListAdapter<Todo, AdapterTodoList.TodoListHolder> {
    private ItemClickListener mListener;

    public AdapterTodoList() {
        super(DIFF_CALLBACK);
    }

    // AsyncListDiffer to calculate the differences between the old data
    // set and the new one we get passed in the LiveData’s onChanged method
    private static final DiffUtil.ItemCallback<Todo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Todo>() {
        //to check weather to items have same id or not
        @Override
        public boolean areItemsTheSame(Todo oldItem, Todo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        //to check weather to items have same contects or not
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
                .inflate(R.layout.layout_todo_item, parent, false);

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
     * @param itemClickListener the item click listener
     * allows clicks events to be caught
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }


    public class TodoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;
        ImageButton EditImage, DeleteImage;
        ImageButton statusCheck;


        public TodoListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_item_title);
            description = itemView.findViewById(R.id.tv_item_content);
            EditImage = itemView.findViewById(R.id.edit_todo);
            EditImage.setOnClickListener(this);
            DeleteImage = itemView.findViewById(R.id.delete_todoImg);
            DeleteImage.setOnClickListener(this);
            statusCheck = itemView.findViewById(R.id.status_checked);
            statusCheck.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            switch (view.getId()) {
                case R.id.edit_todo: {
                    if (mListener != null)
                        mListener.onEditItem(getItem(position));
                    break;
                }
                case R.id.delete_todoImg: {
                    if (mListener != null)
                        mListener.onDeleteItem(getItem(position));
                    break;
                }
                case R.id.status_checked: {
                    if (mListener != null)
                        mListener.onCheckItem(getItem(position));
                    break;
                }
                default: {
                    break;
                }

            }       //end switch{}

        }       //end onClick()


    }       //end inner class


}       //end class

