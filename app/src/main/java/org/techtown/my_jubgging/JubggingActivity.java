package org.techtown.my_jubgging;

import android.app.Activity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class JubggingActivity extends AppCompatActivity {

    // FrameLayout 에 각 메뉴의 Fragment 를 바꿔 줌
    private FragmentManager fragmentManager;
    private JubggingFragment jubggingFragment;

    private FrameLayout frameLayout;

    private Chronometer chronometer;
    private boolean isRunning;
    private Button start_end_button;
    private long pauseOffset;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jubbgging);

        context = getApplicationContext();
        fragmentManager = getSupportFragmentManager();
        jubggingFragment = new JubggingFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.jubgging_frame_layout, jubggingFragment).commitAllowingStateLoss();
    }


    private void customToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
