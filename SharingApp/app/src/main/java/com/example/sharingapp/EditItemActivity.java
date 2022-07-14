package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Editing a pre-existing item consists of deleting the old item and adding a new item with the old
 * item's id.
 */
public class EditItemActivity extends AppCompatActivity implements Observer{

    private ItemList item_list = new ItemList();
    private ItemListController item_list_controller = new ItemListController(item_list);

    private Item item;
    private ItemController item_controller;
    private Context context;

    private ContactList contact_list = new ContactList();
    private ContactListController contact_list_controller = new ContactListController(contact_list);



    private Bitmap image;
    private final int REQUEST_CODE = 1;
    private ImageView photo;

    private EditText title;
    private EditText maker;
    private EditText description;
    private EditText length;
    private EditText width;
    private EditText height;
    private EditText borrower;
    private Spinner borrower_spinner;
    private TextView borrower_tv;
    private Switch status;
    private EditText invisible;


    private ArrayAdapter<String> adapter;
    private boolean on_create_update = false;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        title = findViewById(R.id.title);
        maker = findViewById(R.id.maker);
        description = findViewById(R.id.description);
        length = findViewById(R.id.length);
        width = findViewById(R.id.width);
        height = findViewById(R.id.height);
        borrower_tv = findViewById(R.id.borrower_tv);
        photo = findViewById(R.id.image_view);
        status = findViewById(R.id.available_switch);

        borrower_spinner = findViewById(R.id.borrower_spinner);
        invisible = findViewById(R.id.invisible);

        invisible.setVisibility(View.GONE);


        Intent intent = getIntent(); // Get intent from ItemsFragment
        pos = intent.getIntExtra("position", 0);


        context = getApplicationContext();

        item_list_controller.addObserver(this);
        item_list_controller.loadItems(context);

        on_create_update = true;

        contact_list_controller.addObserver(this);
        contact_list_controller.loadContacts(context);

        on_create_update = false;


    }

    public void addPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent intent) {
        super.onActivityResult(request_code, result_code, intent);
        if (request_code == REQUEST_CODE && result_code == RESULT_OK) {
            Bundle extras = intent.getExtras();
            image = (Bitmap) extras.get("data");
            photo.setImageBitmap(image);
        }
    }

    public void deleteItem(View view) {
        DeleteItemCommand delete_item_command = new DeleteItemCommand(item_list, item, context);
        delete_item_command.execute();
        boolean success = delete_item_command.isExecuted();
        if (!success) {
            return;
        }

        // End EditItemActivity
        item_list_controller.removeObserver(this);


        // End EditItemActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveItem(View view) {

        String title_str = title.getText().toString();
        String maker_str = maker.getText().toString();
        String description_str = description.getText().toString();
        String length_str = length.getText().toString();
        String width_str = width.getText().toString();
        String height_str = height.getText().toString();

        Contact contact = null;

        String borrower_str = null;
        if (!status.isChecked()) {
            borrower_str = borrower_spinner.getSelectedItem().toString();
            contact = contact_list_controller.getContactByUsername(borrower_str);
        }

        Dimensions dimensions = new Dimensions(length_str, width_str, height_str);

        if (title_str.equals("")) {
            title.setError("Empty field!");
            return;
        }

        if (maker_str.equals("")) {
            maker.setError("Empty field!");
            return;
        }

        if (description_str.equals("")) {
            description.setError("Empty field!");
            return;
        }

        if (length_str.equals("")) {
            length.setError("Empty field!");
            return;
        }

        if (width_str.equals("")) {
            width.setError("Empty field!");
            return;
        }

        if (height_str.equals("")) {
            height.setError("Empty field!");
            return;
        }


        // Reuse the item id
        String id = item_controller.getId();
        Item updated_item = new Item(title_str, maker_str, description_str, dimensions, image, id);
        ItemController updated_item_controller = new ItemController(updated_item);
        updated_item_controller.setDimensions(length_str, width_str, height_str);
        boolean checked = status.isChecked();

        if (!checked) {
            updated_item_controller.setStatus("Borrowed");
            updated_item_controller.setBorrower(contact_list_controller.getContactByUsername(borrower_str));
        }



        EditItemCommand edit_item_command = new EditItemCommand(item_list, item, updated_item, context);

        edit_item_command.execute();

        boolean success = edit_item_command.isExecuted();

        if(!success){
            return;
        }

        item_list_controller.removeObserver(this);

        // End EditItemActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Checked = Available
     * Unchecked = Borrowed
     */
    public void toggleSwitch(View view){
        if (status.isChecked()) {
            // Means was previously borrowed

            borrower_tv.setVisibility(View.GONE);
            item_controller.setBorrower(null);
            item_controller.setStatus("Available");

        } else {
            // Means was previously available

            if (contact_list.getSize() == 0){

                invisible.setEnabled(false);
                invisible.setVisibility(View.VISIBLE);
                invisible.requestFocus();
                invisible.setError("No contacts available! Must add borrower to contacts.");
                status.setChecked(true);
            }else {
                borrower_spinner.setVisibility(View.VISIBLE);
                borrower_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void update() {
        if (on_create_update){
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                    contact_list_controller.getAllUsernames());
            borrower_spinner.setAdapter(adapter);

            item = item_list_controller.getItem(pos);
            item_controller = new ItemController(item);

            Contact contact = item_controller.getBorrower();
            if (contact != null){
                int contact_pos = contact_list_controller.getIndex(contact);
                borrower_spinner.setSelection(contact_pos);
            }

            title.setText(item_controller.getTitle());
            maker.setText(item_controller.getMaker());
            description.setText(item_controller.getDescription());
            length.setText(item_controller.getLength());
            width.setText(item_controller.getWidth());
            height.setText(item_controller.getHeight());

            String status_str = item_controller.getStatus();
            if (status_str.equals("Borrowed")) {
                status.setChecked(false);
            } else {
                borrower_tv.setVisibility(View.GONE);
                borrower_spinner.setVisibility(View.GONE);
            }

            image = item_controller.getImage();
            if (image != null) {
                photo.setImageBitmap(image);
            } else {
                photo.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }
}
