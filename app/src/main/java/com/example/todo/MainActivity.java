package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> tasks = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.clear){
            tasks.clear();
            arrayAdapter.notifyDataSetChanged();

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todo", Context.MODE_PRIVATE);
            HashSet<String> set = new HashSet(tasks);
            sharedPreferences.edit().putStringSet("tasks", set).apply();
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todo", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("tasks", null);

        if(set == null) {
            tasks.add("Sample Task");
        } else{
            tasks = new ArrayList<>(set);
        }

        ListView listView = (ListView) findViewById(R.id.itemList);
        TextView noTasks = (TextView) findViewById(R.id.noTasks);
        listView.setEmptyView(noTasks);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks);
        listView.setAdapter(arrayAdapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int deletedTask = i;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this task?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tasks.remove(deletedTask);
                                arrayAdapter.notifyDataSetChanged();

                                //Update Storage
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todo", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(MainActivity.tasks);
                                sharedPreferences.edit().putStringSet("tasks", set).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        FloatingActionButton addItem = (FloatingActionButton) findViewById(R.id.addItem);

        addItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final EditText editItem = new EditText(MainActivity.this);
                //editItem.addTextChangedListener();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add a Task")
                        .setView(editItem)
                        .setPositiveButton("Save", new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task = editItem.getText().toString();
                                tasks.add(task);

                                arrayAdapter.notifyDataSetChanged();

                                //Update Storage
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todo", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(MainActivity.tasks);
                                sharedPreferences.edit().putStringSet("tasks", set).apply();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });




    }
}
