package com.pet.lesnick.letterappwithfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pet.lesnick.letterappwithfragments.adapter.RecyclerAdapter;
import com.pet.lesnick.letterappwithfragments.adapter.UserCardsAdapter;
import com.pet.lesnick.letterappwithfragments.layoutmanager.UserContactLayoutManager;
import com.pet.lesnick.letterappwithfragments.model.UserContactCard;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class RecyclerConctactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView recyclerView;
    public static final String TAG = "ContactFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycler_view_list, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = getActivity().findViewById(R.id.recycler_view);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        int permissionStatus = ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()), Manifest.permission.READ_CONTACTS);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            RecyclerAdapter adapter = new RecyclerAdapter(Objects.requireNonNull(getContext()), initContacts(),getActivity());

            recyclerView.setAdapter(adapter);
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()), new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }

    private List<UserContactCard> initContacts() {
        List<UserContactCard> cards = new LinkedList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = getContext().getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
                // data of
                // contacts
                Cursor dataCursor = getContext().getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);// Retrun data cusror represntative to
                // contact ID
                String name = "";
                String email = "";
                String otherEmail = "";
                String photo = "";
                if (dataCursor.moveToFirst()) {
                    do {
                        name = dataCursor.getString(
                                dataCursor.getColumnIndex(
                                        ContactsContract.Contacts.DISPLAY_NAME));
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            // In this get all Emails like home, work etc and
                            // add them to email string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    email = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK: {
                                    otherEmail = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                }

                                break;

                            }

                        }
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                            byte[] photoByte = dataCursor.getBlob(dataCursor
                                    .getColumnIndex("data15")); // get photo in
                            // byte

                            if (photoByte != null) {

                                // Now make a cache folder in file manager to
                                // make cache of contacts images and save them
                                // n .png
                                Bitmap bitmap = BitmapFactory.decodeByteArray(
                                        photoByte, 0, photoByte.length);
                                File cacheDirectory = getContext()
                                        .getCacheDir();
                                File tmp = new File(cacheDirectory.getPath()
                                        + "/_androhub" + contctId + ".png");
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(
                                            tmp);
                                    bitmap.compress(Bitmap.CompressFormat.PNG,
                                            100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                    photo = tmp.getPath();

                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }

                            }

                        }

                    }
                    while (dataCursor.moveToNext());
                    if (!email.equals("")) {

                        cards.add(new UserContactCard(email, name, photo));
                    } else {
                        if (!otherEmail.equals("")) {
                            cards.add(new UserContactCard(otherEmail, name, photo));
                        }
                    }
                }
            } while (contactsCursor.moveToNext());
        }
        contactsCursor.close();

        return cards;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        MainActivity activity = (MainActivity) getActivity();
        UserContactCard choisen = (UserContactCard) l.getItemAtPosition(position);
        activity.onContactChoice(choisen.getEmail());
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}