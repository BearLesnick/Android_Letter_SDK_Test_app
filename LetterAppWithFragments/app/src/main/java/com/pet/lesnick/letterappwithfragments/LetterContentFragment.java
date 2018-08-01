package com.pet.lesnick.letterappwithfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LetterContentFragment extends Fragment {
    public static final String TAG = "LetterContentFragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle savedInstanceState =  getArguments();
        EditText email = getActivity().findViewById(R.id.emailAddressView);
        LetterContentFragment frag = this;
        if (savedInstanceState != null) {
            email.setText(savedInstanceState.getString("LetterFragmentEmail"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.letter_layout, container, false);
    }

}
