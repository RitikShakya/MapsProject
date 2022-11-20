package com.company.mapsproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.mapsproject.adapters.MyAdapter;
import com.company.mapsproject.models.Reminder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourReminders extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Reminder> dataList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public YourReminders() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_reminders, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList= new ArrayList<>();


        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("datauser").child("user").child(firebaseAuth.getUid()).child("reminders");


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

        MyAdapter myAdapter= new MyAdapter(dataList,getContext());
        recyclerView.setAdapter(myAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Reminder reminder =  dataSnapshot.getValue(Reminder.class);
                    dataList.add(reminder);

                   // Toast.makeText(requireContext(), String.valueOf(dataList.size()), Toast.LENGTH_SHORT).show();
                    Log.d(String.valueOf(dataSnapshot),"res");

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}