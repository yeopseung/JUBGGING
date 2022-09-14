package org.techtown.my_jubgging;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.kakao.usermgmt.response.model.User;

import org.techtown.my_jubgging.ranking.RankInfo;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JubggingResultActivity extends AppCompatActivity {
    Context context;
    UserInfo userInfo;
    RetrofitAPI retrofitAPI;

    LinearLayout intagramLayout;

    long runTime;
    int walkingNum;
    Double calorie;
    Double kilometer;
    boolean isPhotoInserted = false;

    static final int REQUEST_TAKE_PHOTO = 1;

    /* */
    TextView kmTxt;
    TextView timeTxt;
    TextView calorieTxt;

    ImageButton addPhotoBtn;
    ImageButton shareStoryBtn;

    Button exitBtn;

    TextView addedPointTxt;
    TextView nowPointTxt;

    String currentPhotoPath;
    File tmpPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jubgging_result);

        context = getApplicationContext();
        userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        getViewById();
        setValue();
        setOnClick();
    }

    private void getViewById() {
        intagramLayout = findViewById(R.id.jubgging_result_instagram_layout);

        kmTxt = findViewById(R.id.jubgging_result_kilometer_text);
        timeTxt = findViewById(R.id.jubgging_result_time_text);
        calorieTxt = findViewById(R.id.jubgging_result_calorie_text);

        addPhotoBtn = findViewById(R.id.jubgging_result_add_photo_button);
        shareStoryBtn = findViewById(R.id.jubgging_result_share_story_button);

        exitBtn = findViewById(R.id.jubgging_result_exit_button);

        addedPointTxt = findViewById(R.id.jubgging_result_added_point);
        nowPointTxt = findViewById(R.id.jubgging_result_now_point);
    }

    private void setValue() {
        Intent intent = getIntent();

        walkingNum = intent.getIntExtra("step", 0);
        runTime = (long)intent.getLongExtra("runtime", 0L);

        calorie = (double)walkingNum * 0.04;
        kilometer = (double)walkingNum * 0.00071429;

        kmTxt.setText(String.format("%.1f", kilometer));
        calorieTxt.setText(String.format("%.1f", calorie));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(runTime);
        if (runTime < 60 * 60 * 1000)
            date.setHours(0);

        timeTxt.setText(timeFormat.format(date));
    }

    private void setOnClick() {
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;

                    try { photoFile = createImageFile(); }
                    catch (IOException ex) { }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(context, "org.techtown.my_jubgging.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });

        shareStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap;
                Uri backgroundAssetUri = FileProvider.getUriForFile(context, "org.techtown.my_jubgging.fileprovider", tmpPhoto);
                String sourceApplication = "org.techtown.my_jubgging";

                Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");

                intent.setDataAndType(backgroundAssetUri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setPackage("com.instagram.android");
                grantUriPermission(
                        "com.instagram.android", backgroundAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (getPackageManager().resolveActivity(intent, 0) != null) {
                    try {
                        startActivity(intent);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainMenu.class);
                intent.putExtra("userInfo",userInfo); //< FIXME
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);

            Bitmap imageBitmap = null;
            // #Case : SDK >= 29
            if (Build.VERSION.SDK_INT >= 29) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try { imageBitmap = ImageDecoder.decodeBitmap(source); }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // #Case : SDK < 29
            else {
                try { imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file)); }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (imageBitmap != null) {
                Bitmap targetBitmap = imageBitmap.copy(Bitmap.Config.ARGB_8888, false);
                Bitmap dest = Bitmap.createBitmap(targetBitmap.getWidth(), targetBitmap.getHeight(),
                        Bitmap.Config.ARGB_8888);

                Canvas cs = new Canvas(dest);

                Paint paint = new Paint();

                float textSize = targetBitmap.getHeight() / 16;
                paint.setTextSize(textSize);
                paint.setTypeface(Typeface.create(ResourcesCompat.getFont(context, R.font.bahnschrift), Typeface.BOLD));
                paint.setColor(Color.WHITE);

                Paint krPaint = new Paint();
                krPaint.setTextSize(textSize * 0.6f);
                krPaint.setColor(Color.WHITE);

                Paint title = new Paint();
                title.setTextSize(textSize);
                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                title.setColor(Color.WHITE);

                Bitmap logoBig = BitmapFactory.decodeResource(getResources(), R.drawable.image_log);
                Bitmap logo = Bitmap.createScaledBitmap(logoBig, 256, 256, true);

                float hight = paint.measureText("yY");

                cs.drawBitmap(targetBitmap, 0f, 0f, null);
                cs.drawBitmap(logo, textSize, textSize, null);

                cs.drawText("함께 줍깅", textSize * 2, hight + textSize, title);

                cs.drawText("거리", textSize, hight + textSize * 8, krPaint);
                cs.drawText(kmTxt.getText().toString() + " km", textSize, hight + textSize * 9, paint);
                cs.drawText("시간", textSize, hight + textSize * 10, krPaint);
                cs.drawText(timeTxt.getText().toString(), textSize, hight + textSize * 11, paint);
                cs.drawText("칼로리", textSize, hight + textSize * 12, krPaint);
                cs.drawText( calorieTxt.getText().toString() + " kcal", textSize, hight + textSize * 13, paint);

                addPhotoBtn.setImageBitmap(dest);
                addPhotoBtn.setScaleType(ImageView.ScaleType.FIT_XY);

                isPhotoInserted = true;
                intagramLayout.setVisibility(View.VISIBLE);

                try {
                    tmpPhoto = saveImg(dest);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            savePoint();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imgFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File saveImg(Bitmap bitmap) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_" + timeStamp + "_";

        File tempFile = File.createTempFile(
                imgFileName,
                ".jpg",
                storageDir
        );

        try {
            tempFile.createNewFile();

            FileOutputStream out = new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);

            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile;
    }

    private void savePoint() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", Long.parseLong(userInfo.userId));
        data.put("walkingNum", walkingNum);
        Call<Map<String, Integer>> call = retrofitAPI.addPoint(data);

        call.enqueue(new Callback<Map<String, Integer>>() {
             @Override
             public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                 if (!response.isSuccessful()) {
                     customToast("Code : " + response.code() + response.message() + response.errorBody());
                     return;
                 }

                 Map<String, Integer> read = response.body();

                 showPoint(read.get("add"), read.get("nowPoint"));
             }

             @Override
             public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

             }
         });
    }

    private void showPoint(int addedPoint, int nowPoint) {
        Animation cntDownAni = AnimationUtils.loadAnimation(context, R.anim.count_down);
        Animation fadeOutAni = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        addedPointTxt.setText("+" + addedPoint);
        nowPointTxt.setText(nowPoint + " Point");

        addedPointTxt.startAnimation(cntDownAni);
        nowPointTxt.startAnimation(cntDownAni);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addedPointTxt.setText("");
                nowPointTxt.setText("");
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addedPointTxt.startAnimation(fadeOutAni);
                nowPointTxt.startAnimation(fadeOutAni);
            }
        }, 2000);
    }

    private void customToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
