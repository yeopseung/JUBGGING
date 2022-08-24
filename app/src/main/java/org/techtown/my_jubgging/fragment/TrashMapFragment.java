package org.techtown.my_jubgging.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapView;

import org.techtown.my_jubgging.MainMenu;
import org.techtown.my_jubgging.R;


public class TrashMapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_map, container, false);

        MapView mapView = new MapView(rootView.getContext());
        rootView.addView(mapView);

        return rootView;
    }
}