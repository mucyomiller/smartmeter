package com.vincent.smartenergymeter;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MeterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDbRef;
    private FirebaseDatabase mFirebaseInstance;
    private TextView meter_display;
    private Button addToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        meter_display = (TextView) findViewById(R.id.meter_display);
        meter_display.setTypeface(tf);

        //initialize_auth
        mAuth = FirebaseAuth.getInstance();
        //initialize firebase
        mFirebaseInstance = FirebaseDatabase.getInstance();
        //getting ref
        mFirebaseDbRef = mFirebaseInstance.getReference();
        mFirebaseDbRef.child("meter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot address : dataSnapshot.getChildren()) {
                    if(address.getValue(Meter.class).getUserId().equals(mAuth.getCurrentUser().getUid())) {
//                        Log.d("EMAIL_FOUND", email[0]+" password :"+password);
                        meter_display.setText(address.getValue(Meter.class).getPower().toString()+" KWH");
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addToken = (Button) findViewById(R.id.add_token);
        addToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), AddTokenActivity.class);
                startActivity(mIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_add_meter:
                Intent mIntent = new Intent(getApplicationContext(), AddMeterActivity.class);
                startActivity(mIntent);
                return true;
            case R.id.menu_logout:
                mAuth.signOut();
                Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(myIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
