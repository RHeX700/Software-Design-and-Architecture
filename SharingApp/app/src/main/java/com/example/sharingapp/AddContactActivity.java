package com.example.sharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddContactActivity extends AppCompatActivity {

    private ContactList contact_list = new ContactList();
    private Context context;
    private EditText username;
    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        username = (EditText) findViewById(R.id.username);
        email =(EditText) findViewById(R.id.email);

        context = getApplicationContext();
        contact_list.loadContacts(context);
    }

    public void saveContact(View view){
        String username_str = username.getText().toString();
        String email_str = email.getText().toString();

        if(username_str.equals("")){
            username.setError("Empty Field");
            return;
        }

        if (email_str.equals("")){
            email.setError("Empty Field");
            return;
        }

        if (!email_str.contains("@")){
            email.setError("Must be an email address");
        }

        if(contact_list.isUsernameAvailable(username_str)){
            username.setError("Username already taken");
            return;
        }

        Contact contact = new Contact(username_str, email_str, null);
        ContactController contact_controller = new ContactController(contact);

        AddContactCommand add_contact_command = new AddContactCommand(contact_list, contact_controller, context);
        add_contact_command.execute();

        boolean success = add_contact_command.isExecuted();

        if(!success){
            return;
        }

        finish();
    }
}