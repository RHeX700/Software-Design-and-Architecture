package com.example.sharingapp;

import android.content.Context;

public class EditContactCommand extends Command{
    private ContactList contact_list;
    private ContactController old_contact_controller;
    private ContactController new_contact_controller;
    private Context context;


    public EditContactCommand(ContactList contact_list, ContactController old_contact_controller, ContactController new_contact_controller, Context context){
        this.contact_list = contact_list;
        this.old_contact_controller = old_contact_controller;
        this.new_contact_controller = new_contact_controller;
        this.context = context;
    }

    @Override
    public void execute() {
        contact_list.deleteContact(old_contact_controller.getContact());
        contact_list.addContact(new_contact_controller.getContact());
        setIs_executed(contact_list.saveContacts(context));
    }
}
