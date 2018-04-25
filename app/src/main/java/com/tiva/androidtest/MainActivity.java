package com.tiva.androidtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;


import com.tiva.androidtest.adapter.UserAdapter;
import com.tiva.androidtest.model.User;
import com.tiva.androidtest.util.Invariants;
import com.tiva.androidtest.util.JSONHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

//First Page for Showing images
public class MainActivity extends AppCompatActivity {
    private Context context;
    private ArrayList<User> users, userDetails;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserAdapter userAdapter;
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.first_page));

        editSearch = (EditText) findViewById(R.id.search_edit_text);
        editSearch.addTextChangedListener(passwordWatcher);

        recyclerView = (RecyclerView) findViewById(R.id.users_list);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        JSONHelper jsonHelper = new JSONHelper(this, Invariants.USERS_JSON);
        users = jsonHelper.parseJSONUsers(Invariants.USERS_JSON);
        userDetails = jsonHelper.parseJSONUserDetails(Invariants.USERS_DETAILS_JSON);

        for (User  user: users) {
            for(User userDetail: userDetails){
                 if(user.getId()== userDetail.getId() && user.getName().equals(userDetail.getName())){
                    user.setImage(userDetail.getImage());
                 }
            }
        }

        Collections.sort(users, new Comparator<User>() {
            public int compare(User v1, User v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });

        userAdapter = new UserAdapter(users, context, new UserAdapter.OnAdapterItemListener() {
            @Override
            public void onItemClick(int position) {
                Invariants.user = users.get(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(userAdapter);

    }

    private TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            final ArrayList<User> result = new ArrayList<User>();
            int searchListLength = users.size();
            for (int i = 0; i < searchListLength; i++) {
                if (users.get(i).getName().toLowerCase().contains(editSearch.getText().toString().toLowerCase())) {

                    result.add(users.get(i));
                }
            }

            userAdapter = new UserAdapter(result, context, new UserAdapter.OnAdapterItemListener() {
                @Override
                public void onItemClick(int position) {
                    Invariants.user = result.get(position);
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(userAdapter);
        }
    };
}
