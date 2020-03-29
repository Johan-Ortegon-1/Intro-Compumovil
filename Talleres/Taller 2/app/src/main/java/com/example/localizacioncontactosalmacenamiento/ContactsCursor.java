package com.example.localizacioncontactosalmacenamiento;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ContactsCursor extends CursorAdapter
{
    private static final int CONTACT_ID_INDEX = 0;
    private static final int DISPLAY_NAME_INDEX = 1;

    public ContactsCursor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contacto_personalizado,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tVIdContacto = view.findViewById(R.id.idContacto);
        TextView tVNombre = view.findViewById(R.id.nombre);
        int idnum = cursor.getInt(CONTACT_ID_INDEX);
        String nombre = cursor.getString(DISPLAY_NAME_INDEX);
        tVIdContacto.setText(String.valueOf(idnum));
        tVNombre.setText(nombre);
    }

}
