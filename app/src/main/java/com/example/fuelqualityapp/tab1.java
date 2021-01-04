package com.example.fuelqualityapp;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab1 extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private ArrayList<Refuel> arrayList;
    private View v;
    private ListView b;
    private int selectedPosition = -1;
    private PageViewModel pageViewModel;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static tab1 newInstance(String param1, String param2) {
        tab1 fragment = new tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        pageViewModel = ViewModelProviders.of(requireActivity()).get(PageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getArguments();
        v = inflater.inflate(R.layout.fragment_tab1, container, false);
        arrayList = new ArrayList<>();
        ImageButton addButton = v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddNewDataActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();

        myRef = database.getReference(user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                showData(snapshot);
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        TextView myMsg = new TextView(getContext());
        myMsg.setText(getString(R.string.alertDeleteQuestion));
        myMsg.setGravity(Gravity.CENTER);
        myMsg.setTextSize(20);

        alert.setView(myMsg);
        alert.setPositiveButton(getString(R.string.yesButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Refuel a = arrayList.get(selectedPosition);
                deleteSelectedItem(a.getKey());
            }
        });
        alert.setNegativeButton(getString(R.string.noButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });




        ImageButton deleteSelected = (ImageButton) v.findViewById(R.id.deleteSelected);
        deleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size() > 0){
                    if(selectedPosition != -1) {
                        ColorDrawable itemBackground = (ColorDrawable) b.getChildAt(selectedPosition).getBackground();
                        if (itemBackground != null) {
                            int colorId = itemBackground.getColor();
                            if (colorId == Color.rgb(214, 208, 252)) {
                                alert.create().show();
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.chooseItem), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.chooseItem), Toast.LENGTH_SHORT).show();
                    }
                } else {
                        Toast.makeText(getContext(), getString(R.string.emptyList), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }


    public float calculateAverageConsumption(){
        float aC = 0;
        int firstTachometerState = (arrayList.get(0)).getTachometer();
        int lastTachometerState = (arrayList.get(arrayList.size() - 1).getTachometer());
        int distance = lastTachometerState - firstTachometerState;
        float quantity = 0;
        for (int i = 0; i < arrayList.size() - 1; i++) {
            Refuel e = arrayList.get(i);
            quantity = quantity + e.getQuantity();
        }
        aC = (quantity / (float) distance) * 100;


        return aC;
    }


    private void showData(DataSnapshot snapshot) {
        arrayList = new ArrayList<>();
        for(DataSnapshot ds : snapshot.getChildren()){
            Refuel r = new Refuel();
            r.setKey(ds.getValue(Refuel.class).getKey());
            r.setGasStation(ds.getValue(Refuel.class).getGasStation().toString());
            r.setQuantity(ds.getValue(Refuel.class).getQuantity());
            r.setTachometer(ds.getValue(Refuel.class).getTachometer());
            r.setDate(ds.getValue(Refuel.class).getDate());
            arrayList.add(r);
        }

    }

    private void loadData(){
        b = v.findViewById(R.id.refuelListView);
        AdapterRefuel c = new AdapterRefuel(getContext(), arrayList);
        b.setAdapter(c);
        b.setClickable(true);
        b.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < b.getChildCount(); i++){
                    if(position == i){
                        selectedPosition = i;
                        selectedItemColor(selectedPosition);
                    } else {
                        b.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });
        pageViewModel.addRefuel(arrayList);
    }

    public void selectedItemColor(int position){
        b.getChildAt(position).setBackgroundColor(Color.rgb(214, 208, 252));
        View item = b.getChildAt(position);
    }

    public void deleteSelectedItem(String objectID){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(user.getUid()).child(objectID);
        dR.removeValue();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}