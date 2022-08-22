package org.techtown.my_jubgging;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Date;

public class NewpageActivity extends AppCompatActivity {
    /* Layout Reference */
    ImageButton backBtn;

    Button regionBtn;
    EditText titleText;
    EditText contentText;

    ImageButton removeBtn;
    TextView peopleNumText;
    ImageButton addBtn;

    Spinner genderSpinner;
    TextView dateBtn;
    TextView timeBtn;

    EditText placeText;
    EditText linkText;

    Button makeBtn;

    /* */
    int regionNum = 0;

    int year;
    int month;
    int date;

    int hour;
    int min;

    Post post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together_newpage);

        setButtons();
        buttonsOnClickSet();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.target_array, R.layout.spinner_item_list);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        genderSpinner.setAdapter(adapter);
    }

    private void setButtons() {
        backBtn = (ImageButton) findViewById(R.id.together_newpage_back_button);

        regionBtn = (Button) findViewById(R.id.together_newpage_region);
        titleText = (EditText) findViewById(R.id.together_newpage_title_text);
        contentText = (EditText) findViewById(R.id.together_newpage_content_text);

        removeBtn = (ImageButton) findViewById(R.id.together_newpage_remove_people);
        peopleNumText = (TextView) findViewById(R.id.together_newpage_people_num);
        addBtn = (ImageButton) findViewById(R.id.together_newpage_add_people);

        genderSpinner = (Spinner) findViewById(R.id.together_newpage_gender_spinner);
        dateBtn = (TextView) findViewById(R.id.together_newpage_date_button);
        timeBtn = (TextView) findViewById(R.id.together_newpage_time_button);

        placeText = (EditText) findViewById(R.id.together_newpage_place);
        linkText = (EditText) findViewById(R.id.together_newpage_link);

        makeBtn = (Button) findViewById(R.id.together_newpage_make_button);
    }

    private void buttonsOnClickSet() {
        /* 뒤로가기 버튼 */
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                startActivity(intent);
            }
        });

        /* 인원 */
        // 인원 감소
        removeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                --post.peopleNum;

                if (post.peopleNum < 3)
                    post.peopleNum = 3;

                peopleNumText.setText(post.peopleNum + "명");
            }
        });
        // 인원 증가
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ++post.peopleNum;

                if (post.peopleNum > 10)
                    post.peopleNum = 10;

                peopleNumText.setText(post.peopleNum + "명");
            }
        });

        /* 날짜 */
        dateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDate();
            }
        });

        makeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkIsSatisfy();

            }
        });

    }

    private void showDate () {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                year = y;
                month = m + 1;
                date = d;

                if (y == calendar.get(Calendar.YEAR))
                    dateBtn.setText(year + "년 " + month + "월 " + date + "일");
                else
                    dateBtn.setText(month + "월 " + date + "일");
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        int textColor = ContextCompat.getColor(getApplicationContext(), R.color.text_color);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(textColor);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(textColor);
    }

    private boolean checkIsSatisfy() {
        //< FIXME region three check

        if (titleText.getText().length() <= 0)
            return customErrorToast("제목을 입력해 주세요");

        if (contentText.getText().length() <= 0)
            return customErrorToast("활동 내역을 입력해 주세요");

        if (placeText.getText().length() <= 0)
            return customErrorToast("장소를 입력해 주세요");

        if (linkText.getText().length() <= 0)
            return customErrorToast("오픈 채팅방 주소를 입력해 주세요");

        // Case : All Clear
        return true;
    }

    private boolean savePost() {
        post.userId = "S20182426";

        post.title = titleText.getText().toString();
        post.content = contentText.getText().toString();

        String gender[] = { "All", "Male", "Female" };
        post.possibleGender = gender[genderSpinner.getSelectedItemPosition()];


        return true;
    }

    private boolean customErrorToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        return false;
    }
}
