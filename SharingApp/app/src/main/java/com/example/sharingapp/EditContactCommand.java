package com.example.sharingapp;

import android.content.Context;

public class EditContactCommand extends Command{
    private ContactListController contact_list_controller;
    private ContactController old_contact_controller;
    private ContactController new_contact_controller;
    private Context context;


    public EditContactCommand(ContactListController contact_list_controller, ContactController old_contact_controller, ContactController new_contact_controller, Context context){
        this.contact_list_controller = contact_list_controller;
        this.old_contact_controller = old_contact_controller;
        this.new_contact_controller = new_contact_controller;
        this.context = context;
    }

    @Override
    public void execute() {
        contact_list_controller.deleteContact(old_contact_controller);
        contact_list_controller.addContact(new_contact_controller);
        setIs_executed(contact_list_controller.saveContacts(context));
    }
}
