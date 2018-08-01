package com.pet.lesnick.letterappwithfragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends FragmentActivity {
    private String currentEmail;
    private String currentContent;
    private String currentHeader;
    private String CurrentFragmentTag = LetterContentFragment.TAG;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment toolbarFragment;
    private Fragment letterFragment;
    private Fragment contactsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        manager = getSupportFragmentManager();
        toolbarFragment = new ToolbarFragment();
        letterFragment = new LetterContentFragment();
        contactsFragment = new RecyclerConctactFragment();

        onStartScreenShow();
    }

    public void onStartScreenShow() {
        transaction = manager.beginTransaction();
        if (CurrentFragmentTag.equals(LetterContentFragment.TAG)) {

            if (manager.findFragmentByTag(ToolbarFragment.TAG) == null) {
                transaction.add(R.id.toolbarLayout, toolbarFragment, ToolbarFragment.TAG);
            }
            if (manager.findFragmentByTag(LetterContentFragment.TAG) == null) {
                transaction.add(R.id.fragmentLayout, letterFragment, LetterContentFragment.TAG);
            }
            if (manager.findFragmentByTag(RecyclerConctactFragment.TAG) != null) {
                transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(RecyclerConctactFragment.TAG)));
            }

        } else {
            if (manager.findFragmentByTag(RecyclerConctactFragment.TAG) == null) {
                transaction.add(contactsFragment, RecyclerConctactFragment.TAG);
            }
            if (manager.findFragmentByTag(LetterContentFragment.TAG) != null) {
                transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(LetterContentFragment.TAG)));
            }
            if (manager.findFragmentByTag(ToolbarFragment.TAG) != null) {
                transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(ToolbarFragment.TAG)));

            }
        }
        transaction.commit();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try{
            TextView emailTextView = findViewById(R.id.emailAddressView);
            currentEmail = emailTextView.getText().toString();
            TextView headerTextView = findViewById(R.id.letterHeaderView);
            currentHeader = headerTextView.getText().toString();
            TextView contentTextView = findViewById(R.id.letterContentView);
            currentContent = contentTextView.getText().toString();
            outState.putString("LetterFragmentEmail", currentEmail);
            outState.putString("LetterFragmentHeader", currentHeader);
            outState.putString("LetterFragmentContent", currentContent);
            outState.putString("CurrentFragmentTag", CurrentFragmentTag);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView emailTextView = findViewById(R.id.emailAddressView);
        emailTextView.setText(currentEmail);
        TextView headerTextView = findViewById(R.id.letterHeaderView);
        headerTextView.setText(currentHeader);
        TextView contentTextView = findViewById(R.id.letterContentView);
        contentTextView.setText(currentContent);
    }

    public void onContactsShow(View view) {
        transaction = manager.beginTransaction();
        transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(ToolbarFragment.TAG)));
        transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(LetterContentFragment.TAG)));
        transaction.add(R.id.fragmentLayout, contactsFragment, RecyclerConctactFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onContactChoice(String contact) {
        currentEmail = contact;
        transaction = manager.beginTransaction();
        transaction.remove(Objects.requireNonNull(manager.findFragmentByTag(RecyclerConctactFragment.TAG)));
        Bundle bundle = new Bundle();
        bundle.putString("LetterFragmentEmail",currentEmail);
        transaction.add(toolbarFragment, ToolbarFragment.TAG);
        transaction.add(letterFragment, LetterContentFragment.TAG);
        letterFragment.setArguments(bundle);
        transaction.commit();
    }

    public void onLetterSend(View view)
    {
        EditText emailAddressView = findViewById(R.id.emailAddressView);
        EditText letterHeaderView = findViewById(R.id.letterHeaderView);
        EditText letterContentView = findViewById(R.id.letterContentView);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailAddressView.getText().toString()));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, letterHeaderView.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, letterContentView.getText().toString());

        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }
}
