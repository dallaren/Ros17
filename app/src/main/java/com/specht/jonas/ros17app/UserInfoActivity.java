package com.specht.jonas.ros17app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class UserInfoActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private EditText firstNameText;
    private EditText lastNameText;
    private EditText fromText;
    private EditText phoneText;

    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        sharedPref = getSharedPreferences(getString(R.string.user_info_file_key), MODE_PRIVATE);

        firstNameText = (EditText) findViewById(R.id.edit_text_first_name);
        lastNameText = (EditText) findViewById(R.id.edit_text_last_name);
        fromText = (EditText) findViewById(R.id.edit_text_from);
        phoneText = (EditText) findViewById(R.id.edit_text_phone);
        updateTextFields();

        saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save()) {
                    startActivity(new Intent(v.getContext(), MainActivity.class));
                } else {
                    Toast.makeText(UserInfoActivity.this, "Oplysningerne kunne ikke gemmes", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });
    }

    private boolean save() {
        SharedPreferences.Editor editor = sharedPref.edit();

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String from = fromText.getText().toString();
        String phone = phoneText.getText().toString();

        editor.putString(getString(R.string.saved_first_name), firstName);
        editor.putString(getString(R.string.saved_last_name), lastName);
        editor.putString(getString(R.string.saved_from), from);
        editor.putString(getString(R.string.saved_phone), phone);
        return editor.commit();
    }

    private void updateTextFields() {
        String firstName = getSharedPrefString(R.string.saved_first_name);
        firstNameText.setText(firstName);

        String lastName = getSharedPrefString(R.string.saved_last_name);
        lastNameText.setText(lastName);

        String from = getSharedPrefString(R.string.saved_from);
        fromText.setText(from);

        String phone = getSharedPrefString(R.string.saved_phone);
        phoneText.setText(phone);
    }

    private String getSharedPrefString(int resid) {
        return sharedPref.getString(getString(resid),"");
    }
}
