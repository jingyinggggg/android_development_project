package com.example.transportpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class PostAnnouncement extends AppCompatActivity {

    private Spinner uploadImage;
    private ImageView imageView;
    private Button post;
    EditText announcement_title,announcement_content;
    int userId;
    String username;
    String selectedItem;
    int drawableResId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_announcement);

        announcement_title = findViewById(R.id.announcement_title);
        announcement_content = findViewById(R.id.announcement_content);

        imageView = findViewById(R.id.imageView);
        uploadImage = findViewById(R.id.uploadImage);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.announcement_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uploadImage.setAdapter(adapter);

        uploadImage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                selectedItem = parent.getItemAtPosition(position).toString();
                // Use the selected value
                Toast.makeText(PostAnnouncement.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                if (selectedItem.equals("Maintenance")){
                    drawableResId = R.drawable.maintenance;
                    imageView.setImageResource(drawableResId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        post = findViewById(R.id.post_button);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = announcement_title.getText().toString();
                String content = announcement_content.getText().toString();
                if (!title.isEmpty() && !content.isEmpty() && drawableResId != 0) {
                    saveAnnouncementToDatabase(drawableResId);
                } else {
                    Toast.makeText(PostAnnouncement.this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveAnnouncementToDatabase(int drawableResId) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");

        ArrayList<String> user = new ArrayList<>();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    int isAdmin = dataSnapshot.child("isAdminAcc").getValue(Integer.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    if (isAdmin == 0){
                        user.add(username);
                    }
                }
                for (String currentUser : user){

                    String type = getResources().getResourceEntryName(drawableResId);
                    String title = announcement_title.getText().toString(); // Retrieve text from EditText
                    String content = announcement_content.getText().toString(); // Retrieve text from EditText
                    String image = String.valueOf(drawableResId);

                    int is_read = 0;

                    DatabaseReference systemNotificationReference = FirebaseDatabase.getInstance().getReference("SystemNotification");

                    SystemNotification systemNotificationClass = new SystemNotification(image, title, type, content, is_read);
                    systemNotificationReference.child(currentUser).child(type).setValue(systemNotificationClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showPostedDialog();
                            Toast.makeText(PostAnnouncement.this, "Announcement Post Successful!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure to post announcement
                            Toast.makeText(PostAnnouncement.this, "Announcement not posted Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showPostedDialog() {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.posted_announcement_dialog, null);

        // Find views in the custom layout
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);

        // Customize the dialog content (e.g., set image and message)
        dialogImage.setImageResource(R.drawable.posted_done_icon);
        dialogMessage.setText("You have successfully posted an announcement");

        // Create the custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PostAnnouncement.this, AdminHomePage.class);
                        startActivity(intent);
                    }
                });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // back admin homepage
    public void backAdminHomepage(View view) {
        Intent intent = new Intent(PostAnnouncement.this, AdminHomePage.class);
        startActivity(intent);
    }
}
