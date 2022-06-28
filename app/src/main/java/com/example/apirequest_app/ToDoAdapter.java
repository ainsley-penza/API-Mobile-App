package com.example.apirequest_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter<ToDo> {
    private Context toDoContext;
    private List<ToDo> toDoList = new ArrayList<>();

    public ToDoAdapter(@NonNull Context context, ArrayList<ToDo> list) {
        super(context, 0, list);
        toDoContext = context;
        toDoList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v,
                        @NonNull ViewGroup parent) {
        View listItem = v;

        if(listItem == null) {
            listItem = LayoutInflater.from(toDoContext).inflate(R.layout.rowitem,
                    parent, false);
        }
        ToDo currentToDo = toDoList.get(position);

        TextView toDoItem = (TextView) listItem.findViewById(R.id.todoitem);
        toDoItem.setText(currentToDo.getTitle());

        CheckBox checkBox = (CheckBox) listItem.findViewById(R.id.checkbox);
        checkBox.setChecked(currentToDo.isCompleted());

        checkBox.setClickable(false);

        toDoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), toDoList.get(position).getUserId()+"", Toast.LENGTH_SHORT).show();
            }
        });

        return listItem;
    }
}
