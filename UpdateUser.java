package com.example.friendlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.friendlistapp.database.UserDatabase;

public class UpdateUser extends AppCompatActivity {


    private EditText edtUsername;
    private EditText edtPhoneNumber;
    private EditText edtAddress;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user2);

        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        edtPhoneNumber = findViewById(R.id.edt_numberPhone);

        Button btnUpdateUser = findViewById(R.id.btn_update_user);

        mUser = (User) getIntent().getExtras().get("object_user");

        if(mUser != null){
            edtUsername.setText(mUser.getUserName());
            edtPhoneNumber.setText(mUser.getPhoneNumber());
            edtAddress.setText(mUser.getAddress());
        }


        btnUpdateUser.setOnClickListener(v -> updateUser());
    }

    private void updateUser() {
        String strUsername = edtUsername.getText().toString();
        String strAddress = edtAddress.getText().toString();
        String strPhoneNumber = edtPhoneNumber.getText().toString();

        if (  TextUtils.isEmpty(strAddress) || TextUtils.isEmpty(strPhoneNumber)) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update user information
        mUser.setUserName(strUsername);
        mUser.setPhoneNumber(strPhoneNumber);
        mUser.setAddress(strAddress);

        UserDatabase.getInstance(this).userDao().updateUser(mUser);

        // Create an intent to return the updated user back to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updated_user", mUser);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}