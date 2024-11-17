package com.example.transportpro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.transportpro.databinding.SignUpPageBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// for password hashing
import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignUpPage extends AppCompatActivity {

    SignUpPageBinding binding;
    String fullname, username, email, contact, password, Rpassword;
    FirebaseDatabase db;
    DatabaseReference reference;
    private CheckBox privacyCheckbox;
    // Dialog Variables
    Dialog showDialog;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignUpPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        privacyCheckbox = findViewById(R.id.privacy_checkbox);
        TextView termsText = findViewById(R.id.terms_text);

        termsText.setOnClickListener(v -> showPrivacyPoliciesDialog());

        // Initialize Firebase Database reference
        reference = FirebaseDatabase.getInstance().getReference("User");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        binding.signUpButton.setOnClickListener(v -> {
            // Check if the terms and conditions checkbox is checked
            if (privacyCheckbox.isChecked()) {
                // Proceed with the sign-up logic
                fullname = Objects.requireNonNull(binding.nameInput.getText()).toString();
                username = Objects.requireNonNull(binding.usernameInput.getText()).toString().toLowerCase(Locale.ROOT);
                email = Objects.requireNonNull(binding.emailInput.getText()).toString();
                contact = Objects.requireNonNull(binding.phoneInput.getText()).toString();
                password = Objects.requireNonNull(binding.passwordInput.getText()).toString();
                Rpassword = Objects.requireNonNull(binding.reenterPasswordInput.getText()).toString();

                // Validate input fields
                if (!validateInputs()) {
                    showAlertDialog(R.layout.sign_up_fail, 2);
//                    return; // Stop execution if validation fails
                }

                // Disable button to prevent multiple submissions
                binding.signUpButton.setEnabled(false);

                // Check if the email already exists in Firebase Authentication
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                boolean isEmailExistsInAuth = !Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();
                                if (isEmailExistsInAuth) {
                                    // Email exists in Firebase Authentication
                                    Toast.makeText(SignUpPage.this, "Email is already used in Firebase Authentication, please try another", Toast.LENGTH_SHORT).show();
                                    binding.signUpButton.setEnabled(true); // Re-enable the button
                                    return;
                                }

                                // Email not found in Firebase Authentication, now check Firebase Realtime Database
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
                                database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // Email exists in Firebase Realtime Database
                                            Toast.makeText(SignUpPage.this, "Email is already used in the system, please try another", Toast.LENGTH_SHORT).show();
                                            binding.signUpButton.setEnabled(true); // Re-enable the button
                                        } else {
                                            // Email doesn't exist in both Firebase Authentication and Realtime Database, create the user
                                            createUser(firebaseAuth);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(SignUpPage.this, "Error checking email in database: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        binding.signUpButton.setEnabled(true); // Re-enable button in case of error
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpPage.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                binding.signUpButton.setEnabled(true); // Re-enable the button on failure
                            }
                        });
            } else {
                // Show a warning if terms and conditions are not accepted
                Toast.makeText(SignUpPage.this, "Please agree to the Privacy Policies before proceeding.", Toast.LENGTH_SHORT).show();
            }
        });

        // Password Validation
        // Get reference to the TextInputLayout and TextInputEditText for password
        TextInputLayout layoutPassword = findViewById(R.id.textInputLayoutPassword);
        TextInputEditText eTextPassword = findViewById(R.id.passwordInput);
        eTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if (password.length() >= 8) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(password);
                    boolean isPwdContainSpeChar = matcher.find();
                    layoutPassword.setHelperText("");
                    if (isPwdContainSpeChar) {
                        layoutPassword.setError("");
                    } else {
                        layoutPassword.setError("Weak Password. Please include a minimum of 1 special character");
                    }
                } else {
                    layoutPassword.setHelperText("***Password must be a minimum of 8 characters");
                    layoutPassword.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Validate Re-enter password
        // Get reference to the TextInputLayout and TextInputEditText for reenter password
        TextInputLayout textInputLayoutReenterPassword = findViewById(R.id.textInputLayoutRepeatPassword);
        TextInputEditText reenterPasswordInput = findViewById(R.id.reenterPasswordInput);

        reenterPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = Objects.requireNonNull(eTextPassword.getText()).toString();
                String reenterPassword = Objects.requireNonNull(reenterPasswordInput.getText()).toString();

                // If password does not match reenter password, error message displayed
                if (password.equals(reenterPassword)) {
                    textInputLayoutReenterPassword.setError(null);
                } else {
                    textInputLayoutReenterPassword.setError("Password does not match");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        TextInputLayout textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        TextInputEditText emailInput = findViewById(R.id.emailInput);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = Objects.requireNonNull(emailInput.getText()).toString();

                if (email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                    textInputLayoutEmail.setError(null);
                } else {
                    textInputLayoutEmail.setError("Invalid Email");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        TextInputLayout textInputLayoutContact = findViewById(R.id.textInputLayoutPhoneNumber);
        TextInputEditText contactInput = findViewById(R.id.phoneInput);

        contactInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String contact = Objects.requireNonNull(contactInput.getText()).toString();

                if (contact.matches("^\\+60\\d{7,9}$")){
                    textInputLayoutContact.setError(null);
                } else {
                    textInputLayoutContact.setError("Invalid Email");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean validateInputs() {
        if (fullname.isEmpty() || username.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty() || Rpassword.isEmpty()) {
            Toast.makeText(SignUpPage.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(Rpassword)) {
            Toast.makeText(SignUpPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Email validation
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(emailPattern)) {
            Toast.makeText(SignUpPage.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Phone number validation
        String phonePattern = "^\\+60\\d{7,9}$";
        if (!contact.matches(phonePattern)) {
            Toast.makeText(SignUpPage.this, "Invalid phone number format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createUser(FirebaseAuth firebaseAuth) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            Toast.makeText(SignUpPage.this, "User created successfully. Please verify your email.", Toast.LENGTH_SHORT).show();
                                            // Fetch last userId, increment it, and assign it to the new user
                                            incrementAndSaveUserId(fullname, username, email, contact, password, privacyCheckbox.isChecked());
                                        } else {
                                            showAlertDialog(R.layout.sign_up_fail, 2);
                                            Toast.makeText(SignUpPage.this, "Error sending verification email.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(SignUpPage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to increment and save new userId
    private void incrementAndSaveUserId(String fullname, String username, String email, String contact, String password, boolean hasAgreedToPrivacyPolicy) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("User");

        // Fetch all users to find the maximum userId
        usersReference.get().addOnCompleteListener(task -> {

            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

            if (task.isSuccessful() && task.getResult().exists()) {
                int maxUserId = 0;

                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    int currentUserId = userSnapshot.child("userId").getValue(Integer.class);
                    if (currentUserId > maxUserId) {
                        maxUserId = currentUserId;
                    }
                }
                int newUserId = maxUserId + 1;

                // Create new user with the incremented userId
                User user = new User(0, 0, newUserId, fullname, username, email, contact, hashedPassword, hasAgreedToPrivacyPolicy);
                setUserInDatabase(username, user);
                showAlertDialog(R.layout.sign_up_successful, 1);
            } else {
                // If there are no users, start with userId = 1
                int initialUserId = 1;
                User user = new User(0, 0, initialUserId, fullname, username, email, contact, hashedPassword, hasAgreedToPrivacyPolicy);
                setUserInDatabase(username, user);
                showAlertDialog(R.layout.sign_up_successful, 1);
            }
        });
    }
    public void backLoginPage(View view) {
        Intent loginPage = new Intent(this, LoginPage.class);
        startActivity(loginPage);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showAlertDialog(int myLayout, int type) {
        showDialog = new Dialog(SignUpPage.this);
        View layoutView = getLayoutInflater().inflate(myLayout, null);

        showDialog.setContentView(layoutView);
        showDialog.setCancelable(false);
        Objects.requireNonNull(showDialog.getWindow()).getAttributes().windowAnimations = R.style.animation;
        // Sign up successful
        if (type == 1) {
            showDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_button_success));

            Button done = showDialog.findViewById(R.id.done);

            done.setOnClickListener(this::backLoginPage);
        } else {
            showDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_button_fail));
            Button signUpFail = layoutView.findViewById(R.id.tryAgainButton);
            // Click on the try again button
            signUpFail.setOnClickListener(v -> showDialog.dismiss());
        }

        showDialog.show();
    }

    private void setUserInDatabase(String username, User user) {
        reference.child(username).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("activity", "User register");
                mFirebaseAnalytics.logEvent("Update_profile", bundle);

                // Log the success
                Log.d("Firebase", "User data saved successfully.");

                // Reset the input fields after successful write
                binding.nameInput.setText("");
                binding.usernameInput.setText("");
                binding.emailInput.setText("");
                binding.phoneInput.setText("");
                binding.passwordInput.setText("");

                // Redirect to the login page and pass the success message
                showAlertDialog(R.layout.sign_up_successful, 1);

                // Optionally, you can finish the current SignUpPage activity to prevent users from navigating back
                finish();
            } else {
                // Handle the error
                Log.e("Firebase", "Failed to save user data: " + task.getException().getMessage());
                showAlertDialog(R.layout.sign_up_fail, 2);
            }
        });
    }

    private void showPrivacyPoliciesDialog() {
        // Define your Privacy Policies text
        String privacyPoliciesText = "Privacy Policy\n" +
                "This privacy policy is applicable to the TransportPro app (hereinafter referred to as \"Application\") for mobile devices, which was developed by (hereinafter referred to as \"Service Provider\") as a Free service. This service is provided \"AS IS\".\n\n" +

                "What information does the Application obtain and how is it used?\n" +
                "User Provided Information\n" +
                "The Application acquires the information you supply when you download and register the Application. Registration with the Service Provider is not mandatory. However, bear in mind that you might not be able to utilize some of the features offered by the Application unless you register with them.\n\n" +
                "The Service Provider may also use the information you provided them to contact you from time to time to provide you with important information, required notices and marketing promotions.\n\n" +

                "Automatically Collected Information\n" +
                "In addition, the Application may collect certain information automatically, including, but not limited to, the type of mobile device you use, your mobile devices unique device ID, the IP address of your mobile device, your mobile operating system, the type of mobile Internet browsers you use, and information about the way you use the Application.\n\n" +

                "Does the Application collect precise real time location information of the device?\n" +
                "This Application does not gather precise information about the location of your mobile device.\n\n" +

                "Do third parties see and/or have access to information obtained by the Application?\n" +
                "Only aggregated, anonymized data is periodically transmitted to external services to aid the Service Provider in improving the Application and their service. The Service Provider may share your information with third parties in the ways that are described in this privacy statement.\n\n" +

                "Please note that the Application utilizes third-party services that have their own Privacy Policy about handling data. Below are the links to the Privacy Policy of the third-party service providers used by the Application:\n\n" +
                "Google Play Services\nAdMob\nGoogle Analytics for Firebase\nFirebase Crashlytics\nFacebook\n\n" +

                "The Service Provider may disclose User Provided and Automatically Collected Information:\n" +
                "- as required by law, such as to comply with a subpoena, or similar legal process;\n" +
                "- when they believe in good faith that disclosure is necessary to protect their rights, protect your safety or the safety of others, investigate fraud, or respond to a government request;\n" +
                "- with their trusted services providers who work on their behalf, do not have an independent use of the information we disclose to them, and have agreed to adhere to the rules set forth in this privacy statement.\n\n" +

                "What are my opt-out rights?\n" +
                "You can halt all collection of information by the Application easily by uninstalling the Application. You may use the standard uninstall processes as may be available as part of your mobile device or via the mobile application marketplace or network.\n\n" +

                "Data Retention Policy, Managing Your Information\n" +
                "The Service Provider will retain User Provided data for as long as you use the Application and for a reasonable time thereafter. The Service Provider will retain Automatically Collected information for up to 24 months and thereafter may store it in aggregate. If you'd like the Service Provider to delete User Provided Data that you have provided via the Application, please contact them at transportpro@gmail.com and we will respond in a reasonable time. Please note that some or all of the User Provided Data may be required in order for the Application to function properly.\n\n" +

                "Children\n" +
                "The Service Provider does not use the Application to knowingly solicit data from or market to children under the age of 13.\n\n" +

                "The Application does not address anyone under the age of 13. The Service Provider does not knowingly collect personally identifiable information from children under 13 years of age. In the case the Service Provider discover that a child under 13 has provided personal information, the Service Provider will immediately delete this from their servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact the Service Provider (transportpro@gmail.com) so that they will be able to take the necessary actions.\n\n" +

                "Security\n" +
                "The Service Provider are concerned about safeguarding the confidentiality of your information. The Service Provider provide physical, electronic, and procedural safeguards to protect information we process and maintain. For example, we limit access to this information to authorized employees and contractors who need to know that information in order to operate, develop or improve their Application. Please be aware that, although we endeavor provide reasonable security for information we process and maintain, no security system can prevent all potential security breaches.\n\n" +

                "Changes\n" +
                "This Privacy Policy may be updated from time to time for any reason. The Service Provider will notify you of any changes to the Privacy Policy by updating this page with the new Privacy Policy. You are advised to consult this Privacy Policy regularly for any changes, as continued use is deemed approval of all changes.\n\n" +

                "This privacy policy is effective as of 2024-11-11\n\n" +

                "Your Consent\n" +
                "By using the Application, you are giving your consent to the Service Provider processing of your information as set forth in this Privacy Policy now and as amended by us. \"Processing,” means using cookies on a computer/hand held device or using or touching information in any way, including, but not limited to, collecting, storing, deleting, using, combining and disclosing information.\n\n" +

                "Contact us\n" +
                "If you have any questions regarding privacy while using the Application, or have questions about the practices, please contact the Service Provider via email at transportpro@gmail.com.";

        // Create a SpannableString for styling
        SpannableString spannableString = new SpannableString(privacyPoliciesText);

        // Headers to be bolded
        String[] headers = {
                "Privacy Policy",
                "What information does the Application obtain and how is it used?",
                "User Provided Information",
                "Automatically Collected Information",
                "Does the Application collect precise real time location information of the device?",
                "Do third parties see and/or have access to information obtained by the Application?",
                "What are my opt-out rights?",
                "Data Retention Policy, Managing Your Information",
                "Children",
                "Security",
                "Changes",
                "Your Consent",
                "Contact us"
        };

        // Apply bold style to headers
        for (String header : headers) {
            int start = privacyPoliciesText.indexOf(header);
            if (start != -1) {
                int end = start + header.length();
                spannableString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // Display the formatted text in an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Privacy Policy");

        // TextView with styled text
        TextView termsTextView = new TextView(this);
        termsTextView.setText(spannableString);
        termsTextView.setPadding(20, 20, 20, 20);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());

        // Set TextView in dialog
        builder.setView(termsTextView);
        builder.setPositiveButton("I Agree", (dialog, which) -> {
            // Store consent in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("hasConsentedTerms", true);
            editor.apply();
            privacyCheckbox.setChecked(true);
            dialog.dismiss();
            Toast.makeText(this, "Thank you for agreeing to the Privacy Policy", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}