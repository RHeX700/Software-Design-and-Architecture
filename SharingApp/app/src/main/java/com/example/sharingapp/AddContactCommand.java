package com.example.sharingapp;

import android.content.Context;

public class AddContactCommand extends Command{
    private ContactList contact_list;
    private ContactController contact_controller;
    private Context context;


    public AddContactCommand(ContactList contact_list, ContactController contact_controller, Context context){
        this.contact_list = contact_list;
        this.contact_controller = contact_controller;
        this.context = context;
    }

    @Override
    public void execute() {

        contact_list.addContact(contact_controller.getContact());
        setIs_executed(contact_list.saveContacts(context));
    }
}
