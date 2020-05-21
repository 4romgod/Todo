package com.fromgod.todo.mvvm.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fromgod.todo.R;
import com.fromgod.todo.mvvm.ViewModelTodo;
import com.fromgod.todo.mvvm.model.entity.Todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FragTodoUpdate extends Fragment implements View.OnClickListener {
    private static final String TAG = "FragTodoUpdate";

    //VIEWS
    View layoutMain=null;
    Toolbar toolbar=null;
    ActionBar actionBar=null;

    private EditText editTextTitle;
    private EditText editTextTime;
    private Calendar myCalendar;

    // DATE
    private static Date eventDate;
    private String startDateStr;

    Todo todo;

    ViewModelTodo viewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutMain = inflater.inflate(R.layout.frag_todo_update, container, false);
        initViews();
        setHasOptionsMenu(true);
        return layoutMain;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //set it as current date.
        String date = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date());
        editTextTime.setText(date);

        viewModel = ViewModelProviders.of(this).get(ViewModelTodo.class);
    }


    public void initViews(){
        toolbar = layoutMain.findViewById(R.id.layout_toolbar_update);
        toolbar.setTitle("Update Todo");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        editTextTitle = layoutMain.findViewById(R.id.et_todo_title);
        editTextTime = layoutMain.findViewById(R.id.et_todo_content);

        //to disable editing in date section
        editTextTime.setKeyListener(null);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

    }       //end initViews()


    //method to update date and title
    private void updateTodo(Todo todo) {
        String title = editTextTitle.getText().toString();
        String time = editTextTime.getText().toString();

        //check if title or date id empty
        if (title.trim().isEmpty() || time.trim().isEmpty()) {
            Toast.makeText(getActivity(), "Please insert a title and date", Toast.LENGTH_SHORT).show();
            return;
        }

        todo.setTitle(title);
        todo.setCreated_at(time);
        viewModel.update(todo);

        Toast.makeText(getContext(), "Updated...", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    //method to get date
    private void getDate() {
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //create a date string
                String myFormat = "dd MMM, yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                try {
                    eventDate = sdf.parse(sdf.format(myCalendar.getTime()));
                    startDateStr = sdf.format(myCalendar.getTime());
                    editTextTime.setText(startDateStr);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        DatePickerDialog datePickerDialog = new
                DatePickerDialog(getActivity(), date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        // Limiting access to past dates in the step below:
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }       //end getDate()


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save: {
                Todo todo = (Todo) getArguments().getSerializable("TODO_ITEM");

                updateTodo(todo);
                break;
            }
            default: {
                //Toast.makeText(getActivity(), "Invalid Item Selected", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

    }


}       //end class
