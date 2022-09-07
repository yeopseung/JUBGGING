package org.techtown.my_jubgging;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.google.gson.TypeAdapterFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegionPickerActivity extends Activity {
    Context context;

    Resources res;
    String[] gu;
    String[] dong;
    
    TextView[] region;
    int regionCnt;
    
    HashMap<String, Integer> guID;

    ImageButton backBtn;
    TextView okBtn;
    boolean isDong;

    int targetNum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choice_regions);

        context = getApplicationContext();

        Intent data = getIntent();
        targetNum = data.getIntExtra("targetNum", 3);
        regionCnt = data.getIntExtra("regionCnt", 0);

        setViewById();
        setOnClick();
        setInitRegion(data);

        isDong = false;

        printGu();
        setGuID();
    }
    private void setInitRegion(Intent data) {
        String get;
        String key[] = new String[3];
        key[0] = "region1";
        key[1] = "region2";
        key[2] = "region3";

        for (int i = 0; i < 3; ++i) {
            get = data.getStringExtra(key[i]);
            region[i].setText(get);
        }

        for (int i = 0; i < regionCnt; ++i)
            region[i].setBackgroundResource(R.drawable.rounded_rectangle);

        for (int i = regionCnt; i < targetNum; ++i)
            region[i].setBackgroundResource(R.drawable.rounded_rectangle_gray);
    }

    private void setViewById() {
        res = getResources();
        gu = res.getStringArray(R.array.spinner_seoul);

        region = new TextView[3];

        region[0] = findViewById(R.id.region_picker_region1);
        region[1] = findViewById(R.id.region_picker_region2);
        region[2] = findViewById(R.id.region_picker_region3);

        for (int i = targetNum; i < 3; ++i)
            region[i].setVisibility(View.INVISIBLE);

        backBtn = findViewById(R.id.region_picker_back_button);
        okBtn = findViewById(R.id.region_picker_ok_button);
    }

    private void setOnClick() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { okBtnPressed(); }
        });

        region[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRegion(0);
            }
        });
        region[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRegion(1);
            }
        });
        region[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRegion(2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isDong) {
            isDong = false;
            printGu();
        }
        else {
            okBtnPressed();
        }
    }

    private void okBtnPressed() {
        Intent intent = new Intent();

        String tmpStr[] = new String[regionCnt];
        for (int i = 0; i < regionCnt; ++i)
            tmpStr[i] = region[i].getText().toString();
        Arrays.sort(tmpStr);

        String returnStr[] = new String[3];
        for (int i = 0; i < regionCnt; ++i)
            returnStr[i] = tmpStr[i];
        for (int i = regionCnt; i < 3; ++i)
            returnStr[i] = "+";

        intent.putExtra("region1", returnStr[0]);
        intent.putExtra("region2", returnStr[1]);
        intent.putExtra("region3", returnStr[2]);
        intent.putExtra("regionCnt", regionCnt);

        setResult(RESULT_OK, intent);

        super.onBackPressed();
        finish();
    }

    private void back() {
        onBackPressed();
    }

    private void printGu() {
        LinearLayout bodyLayout = findViewById(R.id.dialog_choice_regions_linearLayout);

        bodyLayout.removeAllViews();

        LinearLayout newLine;
        for (int cnt = 0; cnt < gu.length; cnt += 3) {
            String reg[] = new String[3];
            for (int i = 0; i < 3; ++i)
                if (cnt + i < gu.length)
                    reg[i] = gu[cnt + i];
                else
                    reg[i] = null;

            newLine = makeNewLine(reg[0], reg[1], reg[2], false);
            ((ViewGroup)bodyLayout).addView((View)newLine);
        }

    }

    private void printDong(String dongName) {
        LinearLayout bodyLayout = findViewById(R.id.dialog_choice_regions_linearLayout);

        bodyLayout.removeAllViews();
        dong = res.getStringArray(guID.get(dongName));

        LinearLayout newLine;
        for (int cnt = 0; cnt < dong.length; cnt += 3) {
            String reg[] = new String[3];
            for (int i = 0; i < 3; ++i)
                if (cnt + i < dong.length)
                    reg[i] = dong[cnt + i];
                else
                    reg[i] = null;

            newLine = makeNewLine(reg[0], reg[1], reg[2], true);
            ((ViewGroup)bodyLayout).addView((View)newLine);
        }

    }
    private void setGuID() {
        guID = new HashMap<String, Integer>();
        guID.put("강남구" , (int)R.array.spinner_seoul_gangnam);
        guID.put("강동구" , (int)R.array.spinner_seoul_gangdong);
        guID.put("강북구" , (int)R.array.spinner_seoul_gangbuk);
        guID.put("강서구" , (int)R.array.spinner_seoul_gangseo);
        guID.put("관악구" , (int)R.array.spinner_seoul_gwanak);
        guID.put("광진구" , (int)R.array.spinner_seoul_gwangjin);
        guID.put("구로구" , (int)R.array.spinner_seoul_guro);
        guID.put("금천구" , (int)R.array.spinner_seoul_geumcheon);
        guID.put("노원구", (int)R.array.spinner_seoul_nowon);
        guID.put("도봉구" , (int)R.array.spinner_seoul_dobong);
        guID.put("동대문구" , (int)R.array.spinner_seoul_dongdaemun);
        guID.put("동작구" , (int)R.array.spinner_seoul_dongjag);
        guID.put("마포구" , (int)R.array.spinner_seoul_mapo);
        guID.put("서대문구" , (int)R.array.spinner_seoul_seodaemun);
        guID.put("서초구" , (int)R.array.spinner_seoul_seocho);
        guID.put("성동구" , (int)R.array.spinner_seoul_seongdong);
        guID.put("성북구" , (int)R.array.spinner_seoul_seongbuk);
        guID.put("송파구" , (int)R.array.spinner_seoul_songpa);
        guID.put("양천구" , (int)R.array.spinner_seoul_yangcheon);
        guID.put("영등포구" , (int)R.array.spinner_seoul_yeongdeungpo);
        guID.put("용산구" , (int)R.array.spinner_seoul_yongsan);
        guID.put("은평구" , (int)R.array.spinner_seoul_eunpyeong);
        guID.put("종로구" , (int)R.array.spinner_seoul_jongno);
        guID.put("종구" , (int)R.array.spinner_seoul_jung);
        guID.put("중랑구" , (int)R.array.spinner_seoul_jungnanggu);
        // 바보 같아...
    }

    LinearLayout makeNewLine(String reg1, String reg2, String reg3, boolean mode) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(40, 20, 40, 20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 140);
        linearLayout.setLayoutParams(params);

        Button btn1 = makeNewButton(reg1, mode);
        Button btn2 = makeNewButton(reg2, mode);
        Button btn3 = makeNewButton(reg3, mode);

        LinearLayout.LayoutParams paramsSpace = new LinearLayout.LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
        Space space1 = new Space(this);
        space1.setLayoutParams(paramsSpace);
        Space space2 = new Space(this);
        space2.setLayoutParams(paramsSpace);

        ((ViewGroup)linearLayout).addView((View)btn1);
        ((ViewGroup)linearLayout).addView((View)space1);
        ((ViewGroup)linearLayout).addView((View)btn2);
        ((ViewGroup)linearLayout).addView((View)space2);
        ((ViewGroup)linearLayout).addView((View)btn3);

        return linearLayout;
    }

    Button makeNewButton(String text, boolean mode) {
        Button btn = new Button(this);
        btn.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle));

        btn.setGravity(Gravity.CENTER);
        btn.setPadding(0, 0, 0, 0);
        btn.setTextColor(context.getResources().getColor(R.color.text_color));
        btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
        btn.setTextSize(14);

        if (text != null)
            btn.setText(text);
        else
            btn.setVisibility(View.INVISIBLE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        btn.setLayoutParams(params);

        if (mode) {
            // #Case : dong
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addNewRegion(btn.getText().toString());
                }
            });
        }
        else {
            // #Case : gu
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    isDong = true;
                    printDong(btn.getText().toString());
                }
            });
        }

        return btn;
    }

    private void addNewRegion(String regionName) {
        int i;

        // 기존에 추가되어 있는 지역을 거르는 필터
        for (i = 0; i < regionCnt; ++i)
            if (regionName.equals(region[i].getText().toString()))
                return;

        if (regionCnt < targetNum) {
            region[regionCnt].setBackgroundResource(R.drawable.rounded_rectangle);
            region[regionCnt++].setText(regionName);
        }
    }

    private void removeRegion(int index) {
        if (regionCnt == 0)
            return;

        for (int i = index + 1; i < regionCnt; ++i)
            region[i - 1].setText(region[i].getText());

        region[--regionCnt].setBackgroundResource(R.drawable.rounded_rectangle_gray);
        region[regionCnt].setText("+");
    }

    private boolean customErrorToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        return false;
    }
}
