package org.techtown.my_jubgging.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.techtown.my_jubgging.NewpageActivity;
import org.techtown.my_jubgging.R;


public class TogetherFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_together, container, false);

        ImageButton Button = rootView.findViewById(R.id.together_add_button);
        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewpageActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}