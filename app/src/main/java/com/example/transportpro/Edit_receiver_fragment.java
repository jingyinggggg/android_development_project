package com.example.transportpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_receiver_fragment extends Fragment {
    TextView usethisAcc, clear_text;
    EditText nameEditText,mobileEditText,emailEditText,postcodeEditText,add1EditText,add2EditText,add3EditText;

    // Initialize the SharedViewModel once
    SharedViewModel sharedViewModel;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "localstorage";
    private static final String KEY_USERNAME = "userName";
    private static final String Order_Number = "orderNumber";
    FirebaseDatabase db;
    DatabaseReference reference;
    String[] state = {"  - - Select State - - ","Pulau Pinang","Selangor", "Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan", "Pahang", "Perak",
            "Perlis", "Terengganu" ,"Sabah" , "Sarawak" };
    Spinner state_spinner;
    String selectedState;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receiver, container, false);

        usethisAcc = view.findViewById(R.id.useThisAccountadd);

        SpannableString content = new SpannableString("Use this account address");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        usethisAcc.setText(content);

        state_spinner = view.findViewById(R.id.state_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, state);

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

        nameEditText = view.findViewById(R.id.nameTextBox);
        mobileEditText = view.findViewById(R.id.MobileTextBox);
        emailEditText = view.findViewById(R.id.EmailTextBox);
        postcodeEditText = view.findViewById(R.id.PostcodeTextBox);
        add1EditText = view.findViewById(R.id.add1TextBox);
        add2EditText = view.findViewById(R.id.add2TextBox);
        add3EditText = view.findViewById(R.id.add3TextBox);

        clear_text = view.findViewById(R.id.clear_text);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Check and set clear_text visibility when text changes
                checkAndSetClearTextVisibility();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        // Attach the TextWatcher to all EditText fields
        nameEditText.addTextChangedListener(textWatcher);
        mobileEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        postcodeEditText.addTextChangedListener(textWatcher);
        add1EditText.addTextChangedListener(textWatcher);
        add2EditText.addTextChangedListener(textWatcher);
        add3EditText.addTextChangedListener(textWatcher);

        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the text in all EditText fields
                nameEditText.setText("");
                mobileEditText.setText("");
                emailEditText.setText("");
                state_spinner.setSelection(0);
                postcodeEditText.setText("");
                add1EditText.setText("");
                add2EditText.setText("");
                add3EditText.setText("");
            }
        });

        // Initialize the SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);



        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME,null);
        String order_number = sharedPreferences.getString(Order_Number,null);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("OrderHistory");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(username)){
                    final String name = snapshot.child(username).child(order_number).child("receiver_name").getValue(String.class);
                    final String mobile = snapshot.child(username).child(order_number).child("receiver_contact").getValue(String.class);
                    final String email = snapshot.child(username).child(order_number).child("receiver_email").getValue(String.class);
                    final String state_text = snapshot.child(username).child(order_number).child("receiver_state").getValue(String.class);
                    final String postcode_text = snapshot.child(username).child(order_number).child("receiver_postcode").getValue(String.class);
                    final String add1_text = snapshot.child(username).child(order_number).child("receiver_add1").getValue(String.class);
                    final String add2_text = snapshot.child(username).child(order_number).child("receiver_add2").getValue(String.class);
                    final String add3_text = snapshot.child(username).child(order_number).child("receiver_add3").getValue(String.class);

                    int index = 0; // Default to 0 if not found
                    for (int i = 0; i < state.length; i++) {
                        if (state[i].equals(state_text)) {
                            index = i;
                            break; // Found the index, exit the loop
                        }
                    }

                    int finalIndex = index;

                    nameEditText.setText(name);
                    mobileEditText.setText(mobile);
                    emailEditText.setText(email);
                    state_spinner.setSelection(finalIndex);
                    postcodeEditText.setText(postcode_text);
                    add1EditText.setText(add1_text);
                    add2EditText.setText(add2_text);
                    add3EditText.setText(add3_text);


                }
                else
                {
                    usethisAcc.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        usethisAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = db.getReference("Address");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(username)) {
                            final String name = snapshot.child(username).child("fullname").getValue(String.class);
                            final String mobile = snapshot.child(username).child("contact").getValue(String.class);
                            final String email = snapshot.child(username).child("email").getValue(String.class);
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

                            int finalIndex = index;

                            nameEditText.setText(name);
                            mobileEditText.setText(mobile);
                            emailEditText.setText(email);
                            state_spinner.setSelection(finalIndex);
                            postcodeEditText.setText(postcode_text);
                            add1EditText.setText(add1_text);
                            add2EditText.setText(add2_text);
                            add3EditText.setText(add3_text);


                        } else {
                            usethisAcc.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });





        return view;
    }
    private void checkAndSetClearTextVisibility() {
        boolean hasText = !nameEditText.getText().toString().isEmpty() ||
                !mobileEditText.getText().toString().isEmpty() ||
                !emailEditText.getText().toString().isEmpty() ||
                !postcodeEditText.getText().toString().isEmpty() ||
                !add1EditText.getText().toString().isEmpty() ||
                !add2EditText.getText().toString().isEmpty() ||
                !add3EditText.getText().toString().isEmpty();

        if (hasText) {
            clear_text.setVisibility(View.VISIBLE);
        } else {
            clear_text.setVisibility(View.GONE);
        }
    }
    private void setDataInSharedViewModel() {
        String name = nameEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String postcode = postcodeEditText.getText().toString();
        String add1 = add1EditText.getText().toString();
        String add2 = add2EditText.getText().toString();
        String add3 = add3EditText.getText().toString();

        // Create a ReceiverData object with the EditText data
        ReceiverData receiverData = new ReceiverData(name, mobile, email, postcode, selectedState, add1, add2, add3);

        // Set receiver data in the shared ViewModel
        sharedViewModel.setReceiverData(receiverData);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Automatically call setDataInSharedViewModel when leaving the fragment
        setDataInSharedViewModel();
    }
}
