package com.example.admin123.citytour.Fragments.Favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin123.citytour.R;

/**
 * Created by theom on 13/03/2017.
 */
public class FragmentFavouriteEdits extends Fragment {

    EditText userNotesEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_edits,container,false);

        userNotesEditText = (EditText) v.findViewById(R.id.user_notes);
        Button saveNotesButton = (Button) v.findViewById(R.id.save_notes_button);

        saveNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                userNotesEditText.getText();
                Toast.makeText(getContext(), "Saved notes: "+userNotesEditText.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
