package com.example.personaltodolist_starter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ListItemAdapter extends ArrayAdapter<ListItem> {
    Context context;
    ArrayList<ListItem> itemsList = null;

    public ListItemAdapter(Context context, int resource, ArrayList<ListItem> arraylistcheckedbooks) {
        super(context, resource, arraylistcheckedbooks);
        this.context = context;
        this.itemsList = arraylistcheckedbooks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return convertView;
    }
}
