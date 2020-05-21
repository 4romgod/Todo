package com.fromgod.todo.mvvm.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.fromgod.todo.R;
import com.fromgod.todo.mvvm.ViewModelTodo;
import com.fromgod.todo.mvvm.model.entity.Todo;
import com.fromgod.todo.mvvm.view.adapter.AdapterCompletedList;
import com.fromgod.todo.mvvm.view.adapter.AdapterTodoList;
import com.fromgod.todo.mvvm.view.interfaces.ItemCompletedListener;

import java.util.List;


public class FragTodoListDone extends Fragment implements ItemCompletedListener {
    private static final String TAG = "FragTodoListDone";

    ViewModelTodo viewModel;
    AdapterCompletedList adapterTodoDone;

    //VIEWS
    View layoutMain=null;
    Toolbar toolbar = null;
    RecyclerView recyclerView;
    ImageView imgEmpty;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutMain = inflater.inflate(R.layout.frag_todo_list_done, container, false);
        initViews();

        return layoutMain;

    }       // end onCreateView()


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapterTodoDone = new AdapterCompletedList();
        recyclerView.setAdapter(adapterTodoDone);

        viewModel = ViewModelProviders.of(this).get(ViewModelTodo.class);
        viewModel.getCompletedTasks().observe(getActivity(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todoList) {
                Log.d(TAG, "onChanged: todoList size: " + todoList.size() );

                if (todoList.size() > 0) {
                    imgEmpty.setVisibility(View.GONE);
                }
                else{
                    imgEmpty.setVisibility(View.VISIBLE);
                }
                adapterTodoDone.submitList(todoList);
            }
        });

        adapterTodoDone.setCompletedListener(this);

    }       // end onViewCreated()


    public void setToolBar(){
        toolbar = layoutMain.findViewById(R.id.layout_toolbar_done);
        toolbar.setTitle("Completed Todo");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    public void initViews(){
        setToolBar();

        imgEmpty = layoutMain.findViewById(R.id.empty);

        recyclerView = layoutMain.findViewById(R.id.rv_todo_list_done);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

    }       //end initViews()


    @Override
    public void onDeleteItem(final Todo todo) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        viewModel.delete(todo);

                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();

    }       // end onDeleteItem()


    @Override
    public void onUndoItem(final Todo todo) {
        new AlertDialog.Builder(getContext())
                .setTitle("Undo")
                .setMessage("Are you sure you want to Undo?")
                .setPositiveButton("Undo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        todo.setStatus(0);
                        viewModel.update(todo);

                        Toast.makeText(getContext(), "Todo Undone", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();

    }       // end onUndoItem()


}       // end class
