package com.vincent.smartenergymeter;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMeterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDbRef;
    private FirebaseDatabase mFirebaseInstance;
    private Button add_meter;
    private EditText meter_id;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meter);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //initialize_auth
        mAuth = FirebaseAuth.getInstance();
        //initialize firebase
        mFirebaseInstance = FirebaseDatabase.getInstance();

        meter_id = (EditText) findViewById(R.id.meter_id);
        add_meter =(Button) findViewById(R.id.add_meter);
        add_meter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showProgressDialog("adding meter...");
                if(meter_id.getText().toString().isEmpty()){
                    hideProgressDialog();
                    meter_id.setError("Meter Id Must be provided");
                }
                else
                {
                    String uid = mAuth.getCurrentUser().getUid();
                    //saving meter  infos
                    mFirebaseDbRef = mFirebaseInstance.getReference("meter");
                    Meter mMeter = new Meter(meter_id.getText().toString(), 0.0, uid);
                    mFirebaseDbRef.push().setValue(mMeter);
                    hideProgressDialog();
                    meter_id.setText("");
                    Snackbar.make(v, "Meter Number Added Successful", Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }
    public void showProgressDialog(String message){
        if (!pDialog.isShowing())
            pDialog.setMessage(message);
            pDialog.show();
    };
    public void hideProgressDialog(){
        if (pDialog.isShowing())
            pDialog.dismiss();
    };
}
