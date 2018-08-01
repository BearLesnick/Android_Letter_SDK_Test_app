package com.pet.lesnick.letterappwithfragments.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pet.lesnick.letterappwithfragments.R;
import com.pet.lesnick.letterappwithfragments.model.UserContactCard;


import java.util.List;

public class UserCardsAdapter extends BaseAdapter {
    private List<UserContactCard> contacts;
    Context appContext;
    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return contacts.size();
    }

    public UserCardsAdapter(Context context, List<UserContactCard> contacts) {
        this.contacts = contacts;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        appContext = context;
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View currentView = view;
        if (currentView == null) {
            currentView = inflater.inflate(R.layout.user_contact_card_layout, viewGroup, false);
        }
        TextView emailTextView = currentView.findViewById(R.id.cardEmailTextView);
        ImageView imageView = currentView.findViewById(R.id.cardImageView);
        TextView nameTextView = currentView.findViewById(R.id.cardNameTextView);
        UserContactCard card = (UserContactCard) getItem(i);
        emailTextView.setText(card.getEmail());
        nameTextView.setText(card.getName());
        if (card.getImage().equals("")) {
            imageView.setImageResource(R.mipmap.ic_launcher_round);
        }
        else
            {
                Bitmap photo = BitmapFactory.decodeFile(card.getImage());
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(appContext.getResources(),photo);
                roundedBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(roundedBitmapDrawable);// TODO create bitmap uploading from contacts
            }

        return currentView;
    }
}
