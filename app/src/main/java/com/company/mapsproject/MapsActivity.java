package com.company.mapsproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.company.mapsproject.databinding.ActivityMapsBinding;
import com.company.mapsproject.models.LatLong;
import com.company.mapsproject.models.Reminder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    ArrayList<LatLong> latLngList ;
    ArrayList<Reminder> reminderList;
    Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        reminderList = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        firebaseAuth= FirebaseAuth.getInstance();
        mMap= googleMap;
        firebaseDatabase = FirebaseDatabase.getInstance();
        latLngList= new ArrayList<>();
        databaseReference =firebaseDatabase.getReference("datauser").child("user").child(firebaseAuth.getUid()).child("reminders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reminder reminder = dataSnapshot.getValue(Reminder.class);
                    latLngList.add(reminder.getLatLong());

                    Log.d(String.valueOf(dataSnapshot), "res");
                    //Log.d(String.valueOf(latLngList),"hel");
                    //Log.d(String.valueOf( latLngList.size()),"ss");


                    Toast.makeText(getApplicationContext(), String.valueOf(latLngList.size()), Toast.LENGTH_SHORT).show();

                    for(int i=0;i<latLngList.size();i++){
                        LatLng latLng= new LatLng(latLngList.get(i).getLatitude(),latLngList.get(i).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(latLngList.get(i).getSaveTitle()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }

                    Toast.makeText(getApplicationContext(), String.valueOf(latLngList.size()), Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





//
//        LatLng jdplat= new LatLng(28.7041, 77.1025);
//        MarkerOptions markerOptions = new MarkerOptions().position(jdplat).title("delhi");
//        mMap.addMarker(markerOptions);
//
//        //move to the location
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(jdplat));
//        //zzooom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jdplat,10f));
//        Toast.makeText(this, "move", Toast.LENGTH_SHORT).show();
//
//
//                //  adding a circle
//        mMap.addCircle(new CircleOptions().center(jdplat).radius(1000).fillColor(Color.GREEN).strokeColor(Color.DKGRAY));



//        Reminder r1= new Reminder("1","title1","desc1","Location1");
//
//        Reminder r2= new Reminder("1","title1","desc1","Location1");
//
//        Reminder r3= new Reminder("1","title1","desc1","Location1");
//
//        Reminder r4= new Reminder("1","title1","desc1","Location1");
//
//        Reminder r5= new Reminder("1","title1","desc1","Location1");
//        dataList.add(r1);
//        dataList.add(r2);
//        dataList.add(r3);
//        dataList.add(r4);
//        dataList.add(r5);








    }
}