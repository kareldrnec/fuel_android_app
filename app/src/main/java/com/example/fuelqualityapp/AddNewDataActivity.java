package com.example.fuelqualityapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddNewDataActivity extends AppCompatActivity {

    private TextView mDisplayDate;
    private EditText gasStation, editTextQuantity, editTextTachometer;
    private ImageButton gasStationOptions;
    private Button cancelButton, addButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseAuth mAuth;
    private boolean isAllFilled = false;
    private String strGasStation, strQuantity, strTachometer, strDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_data);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference refuelRef = myRef;


        cancelButton = findViewById(R.id.cancelAdding);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        gasStation = findViewById(R.id.editTextGasStation);

        gasStationOptions = findViewById(R.id.gasStationsOptionsMenu);
        gasStationOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AddNewDataActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.gas_station_options, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        gasStation.setText(item.getTitle().toString());
                        Toast.makeText(AddNewDataActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu.show();
            }
        });



        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddNewDataActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextTachometer = findViewById(R.id.editTextTachometer);


        addButton = findViewById(R.id.submitAdding);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGasStation = gasStation.getText().toString();
                strQuantity = editTextQuantity.getText().toString();
                strTachometer = editTextTachometer.getText().toString();
                strDate = mDisplayDate.getText().toString();

                isAllFilled = checkAllBoxes(strGasStation, strQuantity, strTachometer, strDate);

                if(isAllFilled){
                    Refuel refuel = new Refuel();
                    refuel.setGasStation(gasStation.getText().toString());
                    refuel.setQuantity(Float.valueOf(editTextQuantity.getText().toString()));
                    refuel.setTachometer(Integer.valueOf(editTextTachometer.getText().toString()));
                    refuel.setDate(mDisplayDate.getText().toString());
                    String mam = refuelRef.child(user.getUid()).push().getKey();
                    refuel.setKey(mam);
                    refuelRef.child(user.getUid()).child(mam).setValue(refuel);
                    onBackPressed();
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return false;
    }

    public boolean checkAllBoxes(String _strGasStation, String _strQuantity, String _strTachometer, String _strDate){
        boolean allPassed = true;
        if(TextUtils.isEmpty(_strGasStation)){
            gasStation.setError("The item Gas Station cannot be empty.");
            allPassed = false;
        }
        if(TextUtils.isEmpty(_strQuantity)){
            editTextQuantity.setError("The item Number of Litres cannot be empty");
            allPassed = false;
        }
        if(TextUtils.isEmpty(_strTachometer)){
            editTextTachometer.setError("The item Tachometer cannot be empty");
            allPassed = false;
        }
        if(_strDate.equals("Choose date")){
            mDisplayDate.setError("The item Choose date cannot be empty");
            allPassed = false;
        }
        return allPassed;
    }

}