package com.example.sharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity implements Observer{

    private ContactList contact_list = new ContactList();
    private ContactListController contact_list_controller = new ContactListController(contact_list);

    private Contact contact;
    private ContactController contact_controller;

    private EditText email;
    private EditText username;
    private Context context;
    private boolean onCreateUpdate = false;

    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        context = getApplicationContext();
        contact_list_controller.loadContacts(context);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        contact = contact_list.getContact(pos);
        contact_controller = new ContactController(contact);
        contact_controller.addObserver(this);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        onCreateUpdate = true;
        update();
        onCreateUpdate = false;
    }

    public void saveContact(View view){
        String email_str = email.getText().toString();
        if (email_str.equals("")) {
            email.setError("Empty field!");
            return;
        }
        if (!email_str.contains("@")){
            email.setError("Must be an email address!");
            return;
        }
        String username_str = username.getText().toString();
        String id = contact_controller.getId(); // Reuse the contact id
// Check that username is unique AND username is changed (Note: is username was not changed
// then this should be fine, because it was already unique.)

        if (!contact_list_controller.isUsernameAvailable(username_str) &&
                !(contact_controller.getUsername().equals(username_str))) {
            username.setError("Username already taken!");
            return;
        }
        Contact updated_contact = new Contact(username_str, email_str, id);
        ContactController updated_contact_controller = new ContactController(updated_contact);


        EditContactCommand edit_contact_command = new EditContactCommand(contact_list, contact_controller, updated_contact_controller, context);
        edit_contact_command.execute();

        boolean success = edit_contact_command.isExecuted();

        if(!success){
            return;
        }
        contact_controller.removeObserver(this);

// End EditContactActivity
        finish();

    }

    public void deleteContact(View view){

        DeleteContactCommand delete_contact_command = new DeleteContactCommand(contact_list, contact, context);
        delete_contact_command.execute();

        boolean success = delete_contact_command.isExecuted();

        if(!success){
            return;
        }

        // End EditContactActivity
        contact_list_controller.removeObserver(this);

        contact_controller.removeObserver(this);
        finish();
    }


    @Override
    public void update() {
        if (onCreateUpdate){
            username.setText(contact.getUsername());
            email.setText(contact.getEmail());
        }
    }
}