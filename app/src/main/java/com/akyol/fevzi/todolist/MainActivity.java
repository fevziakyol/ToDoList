package com.akyol.fevzi.todolist;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

  private ListView todoList;
  private Button addButton;
  private EditText inputField;

  private ArrayList<String> todoArrayList = new ArrayList<>();
  private ArrayAdapter<String> adapter;
  private SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    initializeViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    todoArrayList = getListString("list");
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoArrayList);
    todoList.setAdapter(adapter);
  }

  @Override
  protected void onPause() {
    super.onPause();
    putListString("list", todoArrayList);
  }

  private void initializeViews() {
    todoList = (ListView) findViewById(R.id.todo_list);
    addButton = (Button) findViewById(R.id.add_button);
    inputField = (EditText) findViewById(R.id.edit_field);

    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (inputField.getText() != null && inputField.getText().length() > 0) {
          todoArrayList.add(inputField.getText().toString());
          adapter.notifyDataSetChanged();
          inputField.setText("");
        } else {
          Toast.makeText(MainActivity.this, "Lütfen geçerli bir metin giriniz", Toast.LENGTH_SHORT).show();
        }
      }
    });

    todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Ne Yapmak İstiyorsunuz ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Sil",
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    todoArrayList.remove(i);
                    adapter.notifyDataSetChanged();
                  }
                });

        builder1.setNegativeButton(
                "Düzenle",
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    if (inputField.getText() != null && inputField.getText().length() > 0) {
                      todoArrayList.set(i, inputField.getText().toString());
                      adapter.notifyDataSetChanged();
                      inputField.setText("");
                    } else {
                      Toast.makeText(MainActivity.this, "Lütfen geçerli bir metin giriniz", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                  }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        return true;
      }
    });
  }

  public void putListString(String key, ArrayList<String> stringList) {
    String[] myStringList = stringList.toArray(new String[stringList.size()]);
    preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
  }

  public ArrayList<String> getListString(String key) {
    return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
  }

}
