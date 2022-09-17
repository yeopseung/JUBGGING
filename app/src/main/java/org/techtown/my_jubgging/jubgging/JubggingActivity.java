package org.techtown.my_jubgging.jubgging;

import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.techtown.my_jubgging.MainMenu;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;

public class JubggingActivity extends AppCompatActivity {
    // 엑티비티 내에 하나의 프래그먼트가 존재하는 형태
    // 내부의 작동은 JubggingFragment.java에 기술

    private FragmentManager fragmentManager;
    private JubggingFragment jubggingFragment;

    private UserInfo userInfo;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jubbgging);

        context = getApplicationContext();
        userInfo = (UserInfo)getIntent().getSerializableExtra("userInfo");

        fragmentManager = getSupportFragmentManager();
        jubggingFragment = new JubggingFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.jubgging_frame_layout, jubggingFragment).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\"줍깅 끝내기\" 버튼을 누르지 않으면 지금까지의 결과가 저장되지 않아요!\n\n정말 나가실 건가요?")
                .setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    private void exit() {
        Intent intent = new Intent(context, MainMenu.class);
        intent.putExtra("userInfo",userInfo); //< FIXME
        startActivity(intent);
        finish();
    }
}
