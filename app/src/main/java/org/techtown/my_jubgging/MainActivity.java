package org.techtown.my_jubgging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.techtown.my_jubgging.fragment.HomeFragment;
import org.techtown.my_jubgging.fragment.PointShopFragment;
import org.techtown.my_jubgging.fragment.RankingFragment;
import org.techtown.my_jubgging.fragment.TogetherFragment;
import org.techtown.my_jubgging.fragment.TrashMapFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainMenu.class);
                startActivity(intent);
            }
        });
    }



}