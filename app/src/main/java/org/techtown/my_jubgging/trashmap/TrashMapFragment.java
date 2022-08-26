package org.techtown.my_jubgging.trashmap;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.response.model.User;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.auth.Login;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TrashMapFragment extends Fragment implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener,MapView.POIItemEventListener {
    private static final String LOG_TAG = "TrashMapFragment";
    private MapView mapView;

    private Retrofit retrofit = RetrofitClient.getInstance();
    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_map, container, false);

        //MapView 등록
        mapView = new MapView(rootView.getContext());
        rootView.addView(mapView);

        //MapView 이벤트리스너 등록
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        //MapView 현재 위치 트래킹기능 사용
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

        //GPS 위치정보 허용 여부 확인
        if (!checkLocationServicesStatus())
            showDialogForLocationServiceSetting();
        else
            checkRunTimePermission();

        //커스텀쓰레기통 클릭시 말풍선 Adapter 등록
        mapView.setCalloutBalloonAdapter(new CustomCallOutBalloonAdapter());

        //커스텀 쓰레기통 불러와서 띄우기
        //커스텀 쓰레기통 목록 GET
        Call<List<CustomTrash>> call_userinfo = retrofitAPI.getCustomTrashList();
        call_userinfo.enqueue(new Callback<List<CustomTrash>>() {
            @Override
            public void onResponse(Call<List<CustomTrash>> call, Response<List<CustomTrash>> response) {
                //통신 실패
                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG, String.valueOf(response.code()));
                    return;
                }

                //통신 성공시 커스텀마커 (커스텀 쓰레기통) 추가
                List<CustomTrash> result = response.body();
                Log.i(LOG_TAG,"커스텀 쓰레기통 목록 Get 성공 "+result.size()+"개");

                for(CustomTrash ct : result)
                {


                    MapPOIItem customMarker = new MapPOIItem();
                    customMarker.setUserObject(ct);
                    customMarker.setItemName("Custom Marker");
                    customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(ct.getLatitude()),Double.parseDouble(ct.getLongitude())));
                    customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.

                    switch (ct.getKind())
                    {
                        case "GENERAL":
                            customMarker.setCustomImageResourceId(R.drawable.trash_general_blue); // 마커 이미지.
                            break;
                        case "RECYCLE":
                            customMarker.setCustomImageResourceId(R.drawable.trash_recycle_blue); // 마커 이미지.
                            break;
                        case "SMOKING":
                            customMarker.setCustomImageResourceId(R.drawable.trash_smoking_blue); // 마커 이미지.
                            break;
                    }
                    customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                    customMarker.setCustomImageAnchor(0.5f, 1.0f); //   마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                    mapView.addPOIItem(customMarker);
                }



            }

            @Override
            public void onFailure(Call<List<CustomTrash>> call, Throwable t) {
                //통신 실패
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });


        return rootView;
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Log.i(LOG_TAG,"onPOIItemSelected");

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }



    // CustomCallOutBalloonAdapter 인터페이스 구현
    // 커스텀 쓰레기통 클릭시 띄우는 말풍선
    class CustomCallOutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCallOutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.adapter_custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {

            //커스텀 마크 내부에 저장한 CustomTrash 정보 참조
            CustomTrash customTrash  = (CustomTrash) poiItem.getUserObject();

            //커스텀 쓰레기통 등록자 정보 조회
            //커스텀 쓰레기통 등록자 정보 GET
            Call<UserInfo> call_userinfo = retrofitAPI.getCustomTrashUser(customTrash.getCustomTrashAddressId());
            call_userinfo.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    //통신 실패
                    if (!response.isSuccessful()) {
                        Log.e(LOG_TAG, String.valueOf(response.code()));
                        return;
                    }

                    //통신 성공시 커스텀마커 (커스텀 쓰레기통) 추가
                    UserInfo result = response.body();
                    Log.i(LOG_TAG,"커스텀 쓰레기통 등록자 정보 Get 성공 UserId:"+result.getUserId()+" NickName: "+result.getNickName()+" heart: "+result.getHeart());

                    //쓰레기통 등록자의 프로필
                    ImageView imageView = ((ImageView) mCalloutBalloon.findViewById(R.id.custom_trash_balloon_imageView));
                    Glide.with(mCalloutBalloon).load(result.getProfileURL()).into(imageView);


                    //쓰레기통 등록자의 닉네임
                    ((TextView) mCalloutBalloon.findViewById(R.id.custom_trash_balloon_nickName)).setText(result.getNickName());

                    //쓰레기통 등록자의 좋아요개수
                    ((TextView) mCalloutBalloon.findViewById(R.id.custom_trash_balloon_heart)).setText("좋아요: "+ result.getHeart());

                    Log.i(LOG_TAG,"설정완료");
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    //통신 실패
                    Log.e(LOG_TAG, t.getLocalizedMessage());
                }
            });

            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return  null;
        }
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                Log.d(LOG_TAG, "start");
                //위치 값을 가져올 수 있음
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            }
        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    //GPS_PROVIDER & NETWORK_PROVIDER 가 제공되어 있는지 확인
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.i(LOG_TAG, "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        //지도화면을 길게 누를 경우 쓰레기통 등록 여부를 물어보는 AlertDialog 생성
        //쓰레기통을 생성하려 할 경우 activity 이동
        UserInfo userInfo = (UserInfo)getActivity().getIntent().getSerializableExtra("userInfo");
        MapPoint.GeoCoordinate geoCoordinate = mapPoint.getMapPointGeoCoord();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("쓰레기통 등록하기");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Log.i(LOG_TAG,"쓰레기통 등록 Activity 이동");
                Intent intent = new Intent(getActivity(),CustomTrashAdd.class);
                intent.putExtra("latitude",geoCoordinate.latitude);
                intent.putExtra("longitude",geoCoordinate.longitude);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Log.i(LOG_TAG,"쓰레기통 등록 취소");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Log.i(LOG_TAG,geoCoordinate.latitude+" "+geoCoordinate.longitude);
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}