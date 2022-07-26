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

public class ContactActivity extends AppCompatActivity implements Observer{

    private ContactList contact_list = new ContactList();
    private ContactListController contact_list_controller = new ContactListController(contact_list);

    private ListView my_contacts;
    private ArrayAdapter<Contact> adapter;
    private Context context;

    private ItemList item_list = new ItemList();
    private ItemListController item_list_controller = new ItemListController(item_list);

    private ContactList active_borrower_list = new ContactList();
    private ContactListController active_borrower_list_controller = new ContactListController(active_borrower_list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        context = getApplicationContext();

        contact_list_controller.addObserver(this);
        contact_list_controller.loadContacts(context);
        item_list_controller.loadItems(context);

        my_contacts = findViewById(R.id.my_contacts);
        adapter = new ContactAdapter(ContactActivity.this, contact_list.getContacts());

        my_contacts.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        my_contacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Contact contact = adapter.getItem(pos);

                ArrayList<Contact>  active_borrowers = item_list_controller.getActiveBorrowers();
                active_borrower_list_controller.setContacts(active_borrowers);

                if(active_borrower_list != null){
                    if(active_borrower_list_controller.hasContact(contact)){
                        CharSequence text = "Cannot delete or edit active borrower";

                        int duration = Toast.LENGTH_SHORT;

                        Toast.makeText(context, text, duration).show();
                        return true;

                    }
                }

                contact_list_controller.loadContacts(context);
                int meta_pos = contact_list_controller.getIndex(contact);
                Intent intent = new Intent(context, EditContactActivity.class);

                intent.putExtra("position", meta_pos);
                startActivity(intent);
                return true;
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        context = getApplicationContext();
        contact_list_controller.loadContacts(context);

        my_contacts = (ListView) findViewById(R.id.my_contacts);

        adapter = new ContactAdapter(ContactActivity.this, contact_list.getContacts());

        my_contacts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void addContactActivity(View view){
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contact_list_controller.removeObserver(this);
    }


    @Override
    public void update() {
        my_contacts = (ListView) findViewById(R.id.my_contacts);
        adapter = new ContactAdapter(ContactActivity.this, contact_list_controller.getContacts());
        my_contacts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}