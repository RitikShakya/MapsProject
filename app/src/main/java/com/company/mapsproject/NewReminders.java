package com.company.mapsproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.company.mapsproject.models.AppPermissions;
import com.company.mapsproject.models.ContantData;
import com.company.mapsproject.models.LatLong;
import com.company.mapsproject.models.PlaceModel;
import com.company.mapsproject.models.Reminder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;


public class NewReminders extends Fragment implements OnMapReadyCallback {

    Context context;



    FloatingActionButton mapType,trafficbtn,currentBtn;
    ChipGroup placesGroup;
    ExtendedFloatingActionButton savBtn,setAlarmBtn;
    final static int REQUEST_CODE = 1 ;

    private EditText title,desc,location;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    // for maps and location current
    private GoogleMap mMap;
    private boolean isLocationOk,isTrafficEnabled;

    private AppPermissions appPermissions;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private Marker marker;

    ArrayList<Address> Locationlist;

    //second way of current
    double current_location_latitude = 0;
    double current_location_longitutde = 0;

    double alarm_location_latitude = 0;
    double alarm_location_longitutde = 0;
    boolean state = false ;
    LatLng location1 ;


    //alarm
    Circle circle;
//    public LocationManager lm ;
//    LatLng location1 ;

    //private ArrayList<Object> Locationlist;


    //for nearby places add this code
//    RetrofitApi retrofitAPI;
//
//
//    int radius=5000;
//    private List<GooglePlaceModel> googlePlaceModelList;
//   private PlaceModel selectedPlaceModel;


    //private GooglePlaceAdapter googlePlaceAdapter;



    public NewReminders(Context context) {
        this.context=context;
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new_reminders, container, false);

        appPermissions = new AppPermissions();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("datauser");

        savBtn = view.findViewById(R.id.saveReminder);
        title= (EditText) view.findViewById(R.id.title);
        desc= (EditText) view.findViewById(R.id.description);
        location= (EditText) view.findViewById(R.id.location);

        mapType = view.findViewById(R.id.btnmapType);
        trafficbtn = view.findViewById(R.id.enableTraffic);
        currentBtn = view.findViewById(R.id.btncurrentloc);

        placesGroup = view.findViewById(R.id.placesGroup);

        Locationlist = new ArrayList<>();

        setAlarmBtn= view.findViewById(R.id.setAlarm);


        //for nearby places
//         retrofitAPI = RetrofitClient.getRetrofitClient().create(RetrofitApi.class);

        //places List


        for(PlaceModel placeModel : ContantData.nearByPlaces){
            Chip chip = new Chip(requireContext());
            chip.setText(placeModel.getName());
            chip.setId(placeModel.getId());
            chip.setTextColor(getResources().getColor(R.color.white));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            chip.setChipIcon(ResourcesCompat.getDrawable(getResources(),placeModel.getDrawerId(),null));
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            placesGroup.addView(chip);
        }

        /// map ttype
        mapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(requireContext(),v);
                popupMenu.getMenuInflater().inflate(R.menu.map_type,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.normal:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;
                            case R.id.satellite:
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case R.id.terrain:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            default:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        //traffic on map
        trafficbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isTrafficEnabled){
                    if(mMap!=null){
                        mMap.setTrafficEnabled(false);
                        isTrafficEnabled=false;
                    }
                }else {
                    if(mMap!=null){
                        mMap.setTrafficEnabled(true);
                        isTrafficEnabled=true;
                    }
                }

            }
        });

        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });


        // for nearby places add the code

//placesGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
//    @Override
//    public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
//        if (checkedId != -1) {
//            PlaceModel placeModel = ContantData.nearByPlaces.get(checkedId - 1);
//            location.setText(placeModel.getName());
//            selectedPlaceModel = placeModel;
//            getPlaces(placeModel.getPlacetype());
//        }
//
//    }
//});


        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapNew);

        supportMapFragment.getMapAsync(this);


        savBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saveTitle=title.getText().toString();
                String saveDesc= desc.getText().toString();
                String saveloc= location.getText().toString();

                if(!saveTitle.isEmpty()){
                    title.setError(null);
                    if(!saveDesc.isEmpty()){
                        desc.setError(null);

                        if(!saveloc.isEmpty()){
                            location.setError(null);



                            LatLong latLong = new LatLong(Locationlist.get(0).getLatitude(),Locationlist.get(0).getLongitude(),saveTitle);
                            alarm_location_latitude= Locationlist.get(0).getLatitude();
                            alarm_location_longitutde= Locationlist.get(0).getLongitude();
                            Reminder reminder = new Reminder(saveTitle,saveTitle,saveDesc,saveloc,latLong);
                            //list.add(reminder);
                            databaseReference.child("user").child(String.valueOf(firebaseAuth.getUid())).child("reminders").child(saveTitle).setValue(reminder);

                            //databaseReference.child("user").child(String.valueOf(firebaseAuth.getUid())).child("reminders").child(saveTitle).setValue(latLng);

                            title.setText("");
                            desc.setText("");
                            location.setText("");
                            Toast.makeText(getContext(),"added success",Toast.LENGTH_SHORT).show();
                        }else{
                            location.setError("enter location");
                        }
                    }else{
                        desc.setError("please enter description");
                    }
                }else{
                    title.setError("please enter title");
                }


            }
        });

        setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlaram(v);



            }
        });

    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        mMap = googleMap;
        isLocationOk=true;

        //custom marker check
//        LatLng jdplat = new LatLng(28.7041, 77.1025);
//        MarkerOptions markerOptions = new MarkerOptions().position(jdplat).title("delhi");
//        mMap.addMarker(markerOptions);
//
//        //move to the location
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(jdplat));
//        //zzooom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jdplat, 10f));
//        Toast.makeText(requireContext(), "move", Toast.LENGTH_SHORT).show();
//    }


//         code for current location
        if(appPermissions.isLocationOk(requireContext())){
            setUpGoogleMap();
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(requireContext()).setTitle("Location Permission").setMessage("We require permission to access location").
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocation();
                        }
                    }).create().show();

        }else{
            requestLocation();
        }





        // setting clicklistener on the map
        String getlocation =location.getText().toString();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {


                if(Locationlist.size()==0){
                    setMarker(latLng);
                }
                else{
                    location.setError("already added");
                }


            }
        });


        location1 = new LatLng(current_location_latitude, current_location_longitutde);


    }

    // add a new marker to get the locatin for the textbox

    private void setMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Clicked here");
        mMap.addMarker(markerOptions);
        Geocoder geocoder =new Geocoder(requireContext());
        try {

            Locationlist = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            Log.d("add", Locationlist.get(0).getAddressLine(0));
            location.setText(String.valueOf(Locationlist.get(0).getAddressLine(0)));
            //databaseReference.child("user").child(String.valueOf(firebaseAuth.getUid())).child("reminders").child(saveTitle).setValue(reminder);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//    public  void getMyLocation() {
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
//        } else {
//            @SuppressLint("MissingPermission") Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            current_location_latitude = loc.getLatitude();
//            current_location_longitutde = loc.getLongitude();
//            // Toast.makeText(getApplicationContext(),current_location_latitude+" , "+ current_location_longitutde , Toast.LENGTH_SHORT).show();
//        }
//    }


 // code for current location request
    private void requestLocation(){

        requestPermissions(
                new String[] {
                        Manifest.permission
                                .ACCESS_FINE_LOCATION,
                        Manifest.permission
                                .ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION },
                100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                setUpGoogleMap();
                isLocationOk=true;
            }
            else{
                isLocationOk=false;
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // setup map with current location
    @SuppressLint("MissingPermission")
    private void setUpGoogleMap() {

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
        &&ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            isLocationOk=false;
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);

        setUpLocation();
    }

    // current location setup
    @SuppressLint("MissingPermission")
    private void setUpLocation() {

         locationRequest=   LocationRequest.create();
         locationRequest.setInterval(1000);
         locationRequest.setFastestInterval(5000);
         locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

         locationCallback = new LocationCallback(){
             @Override
             public void onLocationResult(@NonNull LocationResult locationResult) {
                 if(locationResult!=null){
                     for(Location location : locationResult.getLocations()){
                         Log.d("location",location.getLatitude() +"  " +location.getLongitude());

                         current_location_latitude=location.getLatitude();
                         current_location_longitutde=location.getLongitude();
                     }
                 }

                 super.onLocationResult(locationResult);
             }

         };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

         startLocation();


    }


    @SuppressLint("MissingPermission")
    private void startLocation(){

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            isLocationOk=false;
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(requireContext(), "Location update ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        getCurrentLocation();

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireContext());

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            isLocationOk=false;
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                currentLocation = location;

                moveCameraToLocation(currentLocation);
            }
        });
    }

    // camera to current location
    private void moveCameraToLocation(Location location) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),17);

        MarkerOptions markerOptions= new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                .title("currentLocation");

        if(marker!=null){
            marker.remove();
        }

        marker= mMap.addMarker(markerOptions);
        mMap.animateCamera(cameraUpdate);

    }


    private  void stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d("tag", "stop location update");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(fusedLocationProviderClient!=null)
        stopLocationUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(fusedLocationProviderClient!=null){
            startLocation();
            if(marker!=null){
                marker.remove();
            }
        }
    }





    // for nearb places after integrating places api use this code

    private void getPlaces(String placeName) {}
//
//        if (isLocationOk) {
//
//            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
//                    + currentLocation.getLatitude() + "," + currentLocation.getLongitude()
//                    + "&radius=" + radius + "&type=" + placeName + "&key=" +
//                    getResources().getString(R.string.google_api_key);
//
//            if (currentLocation != null) {
//                retrofitAPI.getNearByPlaces(url).enqueue(new Callback<GoogleResponseModel>() {
//                    @Override
//                    public void onResponse(Call<GoogleResponseModel> call, Response<GoogleResponseModel> response) {
//                        Gson gson = new Gson();
//                        String res = gson.toJson(response.body());
//                        Log.d("TAG", "onResponse: " + res);
//                        if (response.errorBody() == null) {
//                            if (response.body() != null) {
//                                if (response.body().getGooglePlaceModelList() != null && response.body().getGooglePlaceModelList().size() > 0) {
//
//                                    googlePlaceModelList.clear();
//                                    mMap.clear();
//
//                                    for (int i = 0; i < response.body().getGooglePlaceModelList().size(); i++) {
//
////                                        if (userSavedLocationId.contains(response.body().getGooglePlaceModelList().get(i).getPlaceId())) {
////                                            response.body().getGooglePlaceModelList().get(i).setSaved(true);
////                                        }
//                                        googlePlaceModelList.add(response.body().getGooglePlaceModelList().get(i));
//                                        addMarker(response.body().getGooglePlaceModelList().get(i),i);
//                                    }
//
//                                    //googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
//
//
//                                } else {
//
//                                    mMap.clear();
//                                    googlePlaceModelList.clear();
//                                    //googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
//                                    radius += 1000;
//                                    Log.d("TAG", "onResponse: " + radius);
//                                    getPlaces(placeName);
//
//                                }
//                            }
//
//                        } else {
//                            Log.d("TAG", "onResponse: " + response.errorBody());
//                            Toast.makeText(requireContext(), "Error : " + response.errorBody(), Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<GoogleResponseModel> call, Throwable t) {
//
//                        Log.d("TAG", "onFailure: " + t);
//                    }
//                });
//            }
//        }
//
//    }
//
//    private void addMarker(GooglePlaceModel googlePlaceModel,int position) {
//
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(new LatLng(googlePlaceModel.getGeometry().getLocation().getLat(),
//                        googlePlaceModel.getGeometry().getLocation().getLng()))
//                .title(googlePlaceModel.getName())
//                .snippet(googlePlaceModel.getVicinity());
//        mMap.addMarker(markerOptions).setTag(position);
//    }



    // Checks whether user is inside of circle or not
    public boolean IsInCircle(){
        float distance[] ={0,0,0};
        Location.distanceBetween( current_location_latitude,current_location_longitutde,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);
        if( distance[0] > circle.getRadius())
            return false;
        else
            return true;
    }



    //------------ Sends user to Set Alarm
    public void addAlaram(View v){
        //getMyLocation();
        Intent i = new Intent(requireContext(), Alarm.class);
//        i.putExtra("longitude" ,current_location_longitutde );
//        i.putExtra("latitude" ,current_location_latitude );

        i.putExtra("longitude" ,alarm_location_longitutde );
        i.putExtra("latitude" ,alarm_location_latitude );
        startActivityForResult(i, REQUEST_CODE);
    }

    //-----------After Alarm Set ---------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2 && requestCode == REQUEST_CODE)
        {
            if (data.hasExtra("alarm_location_latitude") && data.hasExtra("alarm_location_longitude")) {
                state = true;
                alarm_location_latitude = data.getExtras().getDouble("alarm_location_latitude");
                alarm_location_longitutde = data.getExtras().getDouble("alarm_location_longitude");

                location1 = new LatLng(alarm_location_latitude, alarm_location_longitutde);
                mMap.addMarker(new MarkerOptions().position(location1).title("Alarm Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
                // Add a circle of radius 50 meter
                circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(alarm_location_latitude, alarm_location_longitutde))
                        .radius(50).strokeColor(Color.RED).fillColor(Color.BLUE));

                //--------------- Check user is in Range or Not after 5 Seconds --------
                final Handler handler = new Handler();
                final int delay = 5000; //milliseconds
                handler.postDelayed(new Runnable(){
                    public void run(){
                        //do something
                        getCurrentLocation();
                        if(IsInCircle()){
                            if(state==true)
                            {
                                Intent intent = new Intent(requireContext(), MyBroadCastReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                        requireContext(), 234324243, intent, 0);
                                //Context contex= requireContext();
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                                        + ( 100), pendingIntent);
                                state = false;
                            }
                        }
                        handler.postDelayed(this, delay);
                    }
                }, delay);


            }
        }

    }

}