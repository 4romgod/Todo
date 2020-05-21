package com.fromgod.todo.mvvm.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.fromgod.todo.R;
import com.fromgod.todo.mvvm.ViewModelTodo;
import com.fromgod.todo.mvvm.model.entity.Todo;
import com.fromgod.todo.mvvm.view.adapter.AdapterTodoList;
import com.fromgod.todo.mvvm.view.interfaces.ItemClickListener;

import java.util.List;


public class FragTodoList extends Fragment implements View.OnClickListener, ItemClickListener {
    private static final String TAG = "FragTodoList";

    ViewModelTodo viewModelTodo;
    AdapterTodoList adapterTodoList;

    NavController navController = null;

    //VIEWS
    View layoutMain = null;
    Toolbar toolbar = null;
    ActionBar actionBar = null;

    RecyclerView recyclerView;
    FloatingActionButton fab;
    ImageView imgEmpty;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutMain = inflater.inflate(R.layout.frag_todo_list, container, false);
        initViews();
        setHasOptionsMenu(true);

        return layoutMain;
    }       // end onCreateView()


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        adapterTodoList = new AdapterTodoList();
        recyclerView.setAdapter(adapterTodoList);

        viewModelTodo = ViewModelProviders.of(this).get(ViewModelTodo.class);
        viewModelTodo.getTodoList().observe(getActivity(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                Log.d(TAG, "onChanged: todoList size: " + todos.size());

                adapterTodoList.submitList(todos);
            }
        });

        adapterTodoList.setClickListener(this);

    }       // end onViewCreated()


    public void setActionBar() {
        layoutMain.findViewById(R.id.fab_new_todo).setOnClickListener(this);
        toolbar = layoutMain.findViewById(R.id.layout_toolbar_list);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Todo List");
        //actionBar.setLogo(R.drawable.ic_edit_24dp);
    }


    public void initViews() {
        setActionBar();

        recyclerView = layoutMain.findViewById(R.id.rv_todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        imgEmpty = layoutMain.findViewById(R.id.empty);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.delete_all);

        //delete all option will be disabled and default image will be appeared if list will bhi empty
        viewModelTodo.getPending().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    item.setEnabled(false);
                    recyclerView.setVisibility(View.GONE);
                    imgEmpty.setVisibility(View.VISIBLE);
                } else {
                    item.setEnabled(true);
                    recyclerView.setVisibility(View.VISIBLE);
                    imgEmpty.setVisibility(View.GONE);
                }
            }
        });

    }       // end onPrepareOptionsMenu()


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.completed: {
                navController.navigate(R.id.action_fragTodoList_to_fragTodoListDone);
                break;
            }
            case R.id.delete_all: {
                new AlertDialog.Builder(getContext())
                        .setTitle("Task Completed")
                        .setMessage("Are you sure you want to delete all?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                viewModelTodo.deleteAll();

                                Toast.makeText(getContext(), "All Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setCancelable(true).show();
                break;
            }
            default: {
                Toast.makeText(getActivity(), "Invalid Item Selected", Toast.LENGTH_SHORT).show();
                break;
            }

        }       // end switch{}
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_new_todo:
                navController.navigate(R.id.action_fragTodoList_to_fragTodoCreate);
                break;
        }

    }       // end onClick()


    @Override
    public void onDeleteItem(final Todo todo) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        viewModelTodo.delete(todo);

                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();

    }       // end onDeleteItem()


    @Override
    public void onEditItem(Todo todo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("TODO_ITEM", todo);
        navController.navigate(R.id.action_fragTodoList_to_fragTodoUpdate, bundle);
    }       // end onEditItem()


    @Override
    public void onCheckItem(final Todo todo) {
        new AlertDialog.Builder(getContext())
                .setTitle("Task Completed")
                .setMessage("Are you sure you have completed this todo?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        todo.setStatus(1);      //setting status 1 and updating it

                        viewModelTodo.update(todo);

                        Toast.makeText(getContext(), "Completed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).show();

    }       // end onCheckItem()


}       //end class
