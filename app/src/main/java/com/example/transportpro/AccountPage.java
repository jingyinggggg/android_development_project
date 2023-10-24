package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transportpro.databinding.ActivityAccountPageBinding;
import com.example.transportpro.databinding.BookingPageBinding;
import com.example.transportpro.databinding.HomepageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AccountPage extends AppCompatActivity {
    public ImageButton setting;
    TextView username_text;
    TextView userid_text;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference reference;
    ActivityAccountPageBinding binding;
    String fullname,mobile, email, postcode, add1, add2, add3;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    String[] state = {"  - - Select State - - ","Pulau Pinang","Selangor", "Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan", "Pahang", "Perak",
            "Perlis", "Terengganu" ,"Sabah" , "Sarawak" };
    Spinner state_spinner;
    String selectedState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        state_spinner = findViewById(R.id.state_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, state);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(adapter);
        state_spinner.setSelection(0);

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedState = state[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(userid != null && username != null){
            username_text = findViewById(R.id.username_text);
            userid_text = findViewById(R.id.userid);

            userid_text.setText("User Id: " + userid);
            username_text.setText(username.toUpperCase());

            db = FirebaseDatabase.getInstance();
            reference = db.getReference("Address");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    EditText name_text = findViewById(R.id.getName);
                    EditText mobile_text = findViewById(R.id.getMobile);
                    EditText email_text = findViewById(R.id.getEmail);
                    EditText postcode = findViewById(R.id.getPostCode);
                    EditText add1 = findViewById(R.id.getAddress1);
                    EditText add2 = findViewById(R.id.getAddress2);
                    EditText add3 = findViewById(R.id.getAddress3);

                    if(snapshot.hasChild(username)){
                        fullname = snapshot.child(username).child("fullname").getValue(String.class);
                        mobile = snapshot.child(username).child("contact").getValue(String.class);
                        email = snapshot.child(username).child("email").getValue(String.class);

                        final String state_text = snapshot.child(username).child("state").getValue(String.class);
                        final String postcode_text = snapshot.child(username).child("postcode").getValue(String.class);
                        final String add1_text = snapshot.child(username).child("address1").getValue(String.class);
                        final String add2_text = snapshot.child(username).child("address2").getValue(String.class);
                        final String add3_text = snapshot.child(username).child("address3").getValue(String.class);

                        int index = 0; // Default to 0 if not found
                        for (int i = 0; i < state.length; i++) {
                            if (state[i].equals(state_text)) {
                                index = i;
                                break; // Found the index, exit the loop
                            }
                        }

                        name_text.setText(fullname);
                        mobile_text.setText(mobile);
                        email_text.setText(email);
                        state_spinner.setSelection(index);
                        postcode.setText(postcode_text);
                        add1.setText(add1_text);
                        add2.setText(add2_text);
                        add3.setText(add3_text);



                    }
                    else{
                        reference = db.getReference("User");

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(username)){
                                    fullname = snapshot.child(username).child("fullname").getValue(String.class);
                                    mobile = snapshot.child(username).child("contact").getValue(String.class);
                                    email = snapshot.child(username).child("email").getValue(String.class);

                                    name_text.setText(fullname);
                                    mobile_text.setText(mobile);
                                    email_text.setText(email);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Button update_address = findViewById(R.id.update_address);

            update_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int user_id = Integer.parseInt(userid);

                    fullname = binding.getName.getText().toString();
                    mobile = binding.getMobile.getText().toString();
                    email = binding.getEmail.getText().toString();
                    postcode = binding.getPostCode.getText().toString();
                    add1 = binding.getAddress1.getText().toString();
                    add2 = binding.getAddress2.getText().toString();
                    add3 = binding.getAddress3.getText().toString();

                    if(!fullname.isEmpty() && !mobile.isEmpty() && !email.isEmpty() && !selectedState.equals("  - - Select State - - ") && !postcode.isEmpty() && !add1.isEmpty() && !add2.isEmpty() && !add3.isEmpty()){
                        reference = db.getReference("Address");

                        AddressClass addressClass = new AddressClass(user_id,username,fullname,mobile,email, selectedState, postcode,add1,add2,add3);

                        reference.child(username).setValue(addressClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AccountPage.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else{
                        Toast.makeText(AccountPage.this, "Please enter all the field", Toast.LENGTH_SHORT).show();
                    }



                }
            });




        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottombar);

        bottomNavigationView.setSelectedItemId(R.id.bar_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bar_home){
                Intent intent = new Intent(AccountPage.this,HomePage.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_payment) {
                Intent intent = new Intent(AccountPage.this,Payment.class).putExtra("from","profile");
                startActivity(intent);
            } else if (item.getItemId() == R.id.bar_wallet) {
                Intent intent = new Intent(AccountPage.this,WalletPage.class).putExtra("from","profile");
                startActivity(intent);
            }
            return true;
        });
    }

    public void backPreviousActivity(View view){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottombar);

        if(getIntent().getStringExtra("from").equals("homepage")){
            bottomNavigationView.setSelectedItemId(R.id.bar_home);
        }
        else if(getIntent().getStringExtra("from").equals("payment")){
            bottomNavigationView.setSelectedItemId(R.id.bar_payment);
        }
        else if(getIntent().getStringExtra("from").equals("wallet")){
            bottomNavigationView.setSelectedItemId(R.id.bar_wallet);
        }

        finish();
    }
}