package com.example.sharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private ContactList contact_list = new ContactList();
    private ListView my_contacts;
    private ArrayAdapter<Contact> adapter;
    private Context context;
    private ItemList item_list;
    private ContactList active_borrower_list = new ContactList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        context = getApplicationContext();
        contact_list.loadContacts(context);
        item_list.loadItems(context);

        my_contacts = (ListView) findViewById(R.id.my_contacts);
        adapter = new ContactAdapter(ContactActivity.this, contact_list.get_contacts());

        my_contacts.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        my_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Contact contact = adapter.getItem(pos);

                ArrayList<Contact>  active_borrowers = item_list.getActiveBorrowers();
                active_borrower_list.setContacts(active_borrowers);

                if(active_borrower_list != null){
                    if(active_borrower_list.hasContact(contact)){
                        CharSequence text = "Cannot delete or edit active borrower";

                        int duration = Toast.LENGTH_SHORT;

                        Toast.makeText(context, text, duration).show();
                        return;

                    }
                }
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        context = getApplicationContext();
        contact_list.load_contacts(context);

        my_contacts = (ListView) findViewById(R.id.my_contacts);

        adapter = new ContactAdapter(ContactActivity.this, contact_list.getContacts());

        my_contacts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void addContactsActivity(View view){
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }
}