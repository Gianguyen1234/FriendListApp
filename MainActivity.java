package com.example.friendlistapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.friendlistapp.database.UserDatabase;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int UPDATE_USER_REQUEST_CODE = 10;
    private EditText edtUsername;
    private EditText edtAddress;
    private EditText edtPhoneNumber;


    private Button btnAdd;
    private RecyclerView rcvUser;

    private UserAdapter userAdapter;
    private List<User> mListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        userAdapter = new UserAdapter(new UserAdapter.IClickItemUser() {

            @Override
            public void updateUser(User user) {
                startUpdateUserActivity(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);


            }
        });
        mListUser = new ArrayList<>();
        userAdapter.setData(mListUser);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(userAdapter);

       btnAdd.setOnClickListener(v -> addUser());

        loadData();


    }

    private void startUpdateUserActivity(User user) {
        Intent intent = new Intent(MainActivity.this, UpdateUser.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user",user);
        intent.putExtras(bundle);
        startActivityForResult(intent, UPDATE_USER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_USER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
           loadData();

        }
    }

    private void initUi() {
        edtUsername = findViewById(R.id.edt_username);
        edtPhoneNumber = findViewById(R.id.edt_numberPhone);
        edtAddress = findViewById(R.id.edt_address);

        btnAdd = findViewById(R.id.btn_add);
        rcvUser = findViewById(R.id.rcv_user);
        SearchView svSearch = findViewById(R.id.svSearch);
        svSearch.clearFocus();

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }


    private void filterList(String text) {
        List<User> list = new ArrayList<>();
        for(User user : mListUser){
            if (user.getUserName().toLowerCase().contains(text.toLowerCase())){
                list.add(user);
            }

        }
        if (list.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            userAdapter.setData(list);
        }




    }

    private void addUser() {
        String strUsername = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();
        String strPhoneNumber = edtPhoneNumber.getText().toString().trim();


        if(TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress) || TextUtils.isEmpty(strPhoneNumber)  ){
            return;
        }

        User user = new User (strUsername, strPhoneNumber, strAddress);

        if(isUserExist(user)){
            Toast.makeText(this, "User exist", Toast.LENGTH_SHORT).show();
            return;
        }

        UserDatabase.getInstance(this).userDao().insertUser(user);
        Toast.makeText(this, "Add user successfully", Toast.LENGTH_SHORT).show();

        edtUsername.setText("");
        edtPhoneNumber.setText("");
        edtAddress.setText("");



        hideSoftKeyboard();

        loadData();

    }


    private boolean isUserExist(@NonNull User user){
        List<User> list = UserDatabase.getInstance(this).userDao().checkUser(user.getUserName());
        return list != null && !list.isEmpty();

    }
    public void hideSoftKeyboard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    private void loadData(){
        mListUser = UserDatabase.getInstance(this).userDao().getListUser();
        userAdapter.setData(mListUser);

    }

  

    private void clickDeleteUser(User user) {
        new AlertDialog.Builder(this).setTitle("Are you sure you want to delete this item?")
                .setMessage("Think carefully!")
                .setPositiveButton("Yes", (dialog, which) -> {
                    UserDatabase.getInstance(MainActivity.this).userDao().deleteUser(user);
                    Toast.makeText(MainActivity.this, "Delete user successfully", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("No",null)
                .show();
    }
}