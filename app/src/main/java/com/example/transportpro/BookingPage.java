package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.transportpro.databinding.BookingPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BookingPage extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference reference;
    BookingPageBinding binding;
    String trackNumber, category, deliveryBy, quantity, description;
    SharedPreferences sharedPreferences;
    //sharedpreferences
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BookingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String userid = sharedPreferences.getString(KEY_ID,null);
        String username = sharedPreferences.getString(KEY_USERNAME,null);

        //Show message for user when they submit a booking
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackNumber = binding.trackingNumberInput.getText().toString();
                category = binding.categoryInput.getText().toString();
                deliveryBy = binding.deliveryInput.getText().toString();
                quantity = binding.quantityInput.getText().toString();
                description = binding.descriptionInput.getText().toString();

                if(!trackNumber.isEmpty() && !category.isEmpty() && !quantity.isEmpty()){
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Booking");
                    int user_id = Integer.parseInt(userid);
                    int amount = Integer.parseInt(quantity);
                    int collected = 0;
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = dateFormat.format(date);
                    int isChecked = 0;
                    double weight = 0.000;
                    int isPackOrder = 0;


                    BookingClass booking = new BookingClass(user_id, trackNumber, category, deliveryBy, amount, description, collected, formattedDate, isChecked, weight, isPackOrder);

                    reference.child(username).child(trackNumber).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showToast("Booking submitted!");
                            Intent home = new Intent(BookingPage.this, HomePage.class);
                            startActivity(home);
                        }
                    });
                }
            }
        });
    }

    public void showToast(String text){
        Toast.makeText(BookingPage.this, text, Toast.LENGTH_SHORT).show();
    }

    public void backLoginPage(View view){
        Intent loginPage = new Intent(this, LoginPage.class);
        startActivity(loginPage);
    }

    public void backHomePage(View view){
        Intent homePage = new Intent(BookingPage.this, HomePage.class);
        startActivity(homePage);
    }
}