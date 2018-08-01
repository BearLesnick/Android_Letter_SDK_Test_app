package com.pet.lesnick.letterappwithfragments.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pet.lesnick.letterappwithfragments.MainActivity;
import com.pet.lesnick.letterappwithfragments.R;
import com.pet.lesnick.letterappwithfragments.model.UserContactCard;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter {
    private List<UserContactCard> contacts;
    private Context context;
    private Activity anchoredActivity;

    public RecyclerAdapter(Context context, List<UserContactCard> contacts, Activity activity) {
        this.contacts = contacts; this.context = context; this.anchoredActivity = activity;
    }

    public static class ContactsHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView name;
        TextView email;
        ImageView photo;

        public void setActivity(Activity activity) {
            this.activity =(MainActivity) activity;
        }

        MainActivity activity;

        public ContactsHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.user_contact_cardview);
            email = itemView.findViewById(R.id.cardEmailTextView);
            name = itemView.findViewById(R.id.cardNameTextView);
            photo = itemView.findViewById(R.id.cardImageView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView emailview;
                    emailview = (TextView) view.findViewById(R.id.cardEmailTextView);
                    activity.onContactChoice(emailview.getText().toString());
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_contact_card_layout, viewGroup, false);
        ContactsHolder pvh = new ContactsHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ContactsHolder viewHolder = (ContactsHolder) holder;
        viewHolder.name.setText(contacts.get(i).getName());
        viewHolder.email.setText(contacts.get(i).getEmail());
        viewHolder.setActivity(anchoredActivity);
        if (contacts.get(i).getImage().equals("")) {
            viewHolder.photo.setImageResource(R.mipmap.ic_launcher_round);
        }
        else
        {
            Bitmap photo = BitmapFactory.decodeFile(contacts.get(i).getImage());
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(),photo);
            roundedBitmapDrawable.setCircular(true);
            viewHolder.photo.setImageDrawable(roundedBitmapDrawable);
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
