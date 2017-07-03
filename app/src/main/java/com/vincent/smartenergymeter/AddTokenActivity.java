package com.vincent.smartenergymeter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class AddTokenActivity extends AppCompatActivity {

    private EditText mSpinner;
    private EditText token_voucher;
    private Button recharge;
    private ProgressDialog pDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDbRef;
    private FirebaseDatabase mFirebaseInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_token);
        //initialize_auth
        mAuth = FirebaseAuth.getInstance();
        //initialize firebase
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //spinner
        mSpinner = (EditText) findViewById(R.id.spinner);
        token_voucher = (EditText) findViewById(R.id.token_voucher);

        recharge = (Button) findViewById(R.id.recharge);
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog("Checking token...");

                mFirebaseDbRef = mFirebaseInstance.getReference();
                mFirebaseDbRef.child("vouchers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot adress : dataSnapshot.getChildren()) {
                            if(adress.getValue(Voucher.class).getVoucherCode().equals(token_voucher.getText().toString())) {

                                final Double voucher_unit = adress.getValue(Voucher.class).getVoucherValue();
                                Log.d("VOUCHER_UNITE", adress.getValue(Voucher.class).getVoucherValue().toString());
                                Log.d("SELECTED_METER",mSpinner.getText().toString());
                                mFirebaseDbRef.child("meter").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot address : dataSnapshot.getChildren()) {
                                            if(address.getValue(Meter.class).getMeterId().equals(mSpinner.getText().toString())){
                                                Double old_value = address.getValue(Meter.class).getPower();
                                                Log.d("OLD_VALUE", address.getValue(Meter.class).getPower().toString());
                                                address.child("power").getRef().setValue(old_value+voucher_unit);
                                                mSpinner.setText("");
                                                token_voucher.setText("");
                                                Intent mIntent = new Intent(getApplicationContext(),MeterActivity.class);
                                                startActivity(mIntent);
                                                break;

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                hideProgressDialog();
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
