package com.example.apirequest_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private ListView lv;
    private ToDoAdapter toDoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillUpSpinner();

        //Default API Link
        final String apiLink = "https://jsonplaceholder.typicode.com/todos/";
        generateList(apiLink);

    }

    public void fillUpSpinner() {
        //Original JSON
        JsonArray originalJson = getAPIAsJsonArray(getAPIMessage("https://jsonplaceholder.typicode.com/todos/"));

        List<String> ids = new ArrayList<String>(Collections.singletonList("Select"));

        //if the id is not in the list yet, then add it to the list.
        for(int i = 0; i< originalJson.size(); i++) {
            JsonObject currOriginalObj = originalJson.get(i).getAsJsonObject();
            if(!ids.contains(currOriginalObj.get("userId").toString())) {
                ids.add(currOriginalObj.get("userId").toString());
            }
        }

        spinner = findViewById(R.id.idsspinner);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, ids);
        spinner.setAdapter(spinAdapter);
    }

    public void generateList(String apiLink) {
        //Convert API Message to JSON
        JsonArray json = getAPIAsJsonArray(getAPIMessage(apiLink));
        Log.d("JSON", json.toString());

        //Fill in the titles and ids arraylists with the json objects properties that are found
        //within the json array.
        ArrayList<ToDo> toDos = new ArrayList<>();
        lv = (ListView) findViewById(R.id.mainListView);

        for(int i =0; i<json.size(); i++) {
            JsonObject currObj = json.get(i).getAsJsonObject();

            int id = currObj.get("id").getAsInt();
            int userId = currObj.get("userId").getAsInt();
            String title = currObj.get("title").getAsString();
            boolean completed = currObj.get("completed").getAsBoolean();

            toDos.add(new ToDo(id, userId, title, completed));
        }

        toDoAdapter = new ToDoAdapter(MainActivity.this, toDos);

        lv.setAdapter(toDoAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        id+"", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String newAPILink;
                if(position != 0){
                    newAPILink = "https://jsonplaceholder.typicode.com/todos?userId=" + position;
                } else {
                    newAPILink = "https://jsonplaceholder.typicode.com/todos";
                }
                generateList(newAPILink);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Nothing Selected", "Nothing Selected");
            }
        });
    }

    public JsonArray getAPIAsJsonArray(String apiMessage) {
        return new JsonParser().parse(apiMessage).getAsJsonArray();
    }

    public String getAPIMessage(String apiLink) {
        try {
            StringBuilder message = new StringBuilder();
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        URL apiURL = new URL(apiLink);

                        HttpURLConnection conn = (HttpURLConnection) apiURL.openConnection();
                        conn.connect();

                        if (conn.getResponseCode() == 200) {
                            Scanner sc = new Scanner(apiURL.openStream());

                            while (sc.hasNext()) {
                                message.append(sc.nextLine());
                            }
                            sc.close();
                        }
                    } catch (Exception e) {
                        Log.d("Exception", e.toString());
                    }
                }
            });
            thread.start();
            thread.join();

            return message.toString();
        }catch(Exception e) {
            Log.d("Exception", e.toString());
            return "";
        }
    }
}