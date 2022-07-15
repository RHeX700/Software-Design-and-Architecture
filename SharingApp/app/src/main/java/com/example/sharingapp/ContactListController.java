package com.example.sharingapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ContactListController {
    private ContactList contact_list;

    public ContactListController(ContactList contact_list){
        this.contact_list = contact_list;
    }

    public void setItems(ArrayList<Contact> contact_list) {
        this.contact_list.setContacts(contact_list);
    }

    public ArrayList<Contact> getContacts() {
        return contact_list.getContacts();
    }

    public boolean addContact(ContactController contact_controller, Context context){
        AddContactCommand add_contact_command = new AddContactCommand(contact_list, contact_controller, context);
        add_contact_command.execute();
        return add_contact_command.isExecuted();
    }

    public boolean deleteContact(Contact contact, Context context) {
        DeleteContactCommand delete_contact_command = new DeleteContactCommand(contact_list, contact, context);
        delete_contact_command.execute();
        return delete_contact_command.isExecuted();
    }

    public boolean editContact(ContactController contact_controller, ContactController updated_contact_controller, Context context){

        EditContactCommand edit_contact_command = new EditContactCommand(contact_list, contact_controller, updated_contact_controller, context);
        edit_contact_command.execute();
        return edit_contact_command.isExecuted();
    }

    public Contact getContact(int index) {
        return contact_list.getContact(index);
    }

    public int getIndex(Contact contact) {
        return contact_list.getIndex(contact);
    }

    public int getSize() {
        return contact_list.getSize();
    }

    public void loadContacts(Context context) {
        contact_list.loadContacts(context);
    }

    public void addObserver(Observer observer) {
        contact_list.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        contact_list.removeObserver(observer);
    }

    public List<String> getAllUsernames() {
        return null;
    }

    public boolean isUsernameAvailable(String username) {
        return contact_list.isUsernameAvailable(username);
    }

    public void deleteContact(ContactController contact_controller) {
        contact_list.deleteContact(contact_controller.getContact());
    }

    public void addContact(ContactController contact_controller) {
        contact_list.addContact(contact_controller.getContact());
    }

    public boolean saveContacts(Context context) {
        return contact_list.saveContacts(context);
    }

    public void setContacts(ArrayList<Contact> active_borrowers) {
        contact_list.setContacts(active_borrowers);
    }

    public boolean hasContact(Contact contact) {
        return contact_list.hasContact(contact);
    }

    public Contact getContactByUsername(String borrower_str) {
        return contact_list.getContactByUsername(borrower_str);
    }
}
