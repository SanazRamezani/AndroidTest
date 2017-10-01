package com.tiva.androidtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.compoundicontextview.CompoundIconTextView;
import com.squareup.picasso.Picasso;
import com.tiva.androidtest.model.User;
import com.tiva.androidtest.util.Invariants;
import com.tiva.androidtest.util.JSONHelper;
import com.squareup.picasso.Callback;
import com.vansuita.materialabout.views.CircleImageView;

import java.util.ArrayList;


/**
 * Created by mojtaba on 17/9/29 AD.
 */
public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private TextView mName, mHobbies;
    private CircleImageView mImage;
    private CompoundIconTextView mAge, mEmail;
    private ArrayList<User> users;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.second_page));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mName = (TextView) findViewById(R.id.name);
        mImage = (CircleImageView) findViewById(R.id.profile_image);
        mAge = (CompoundIconTextView) findViewById(R.id.age);
        mHobbies = (TextView) findViewById(R.id.sub_title);
        mEmail = (CompoundIconTextView) findViewById(R.id.email);


        JSONHelper jsonHelper = new JSONHelper(this, Invariants.USERS_DETAILS_JSON);
        users = jsonHelper.parseJSONUserDetails(Invariants.USERS_DETAILS_JSON);

        user = Invariants.user;
        int searchListLength = users.size();
        for (int i = 0; i < searchListLength; i++) {
            if (users.get(i).getId() == Invariants.user.getId() && users.get(i).getName().equals(Invariants.user.getName())) {
                user = users.get(i);
            }
        }


        mName.setText(Invariants.user.getName());
        if (user.getAge() != 0) {
            mAge.setText(getResources().getString(R.string.age) +" "+ user.getAge());
        } else {
            mAge.setText(getResources().getString(R.string.age)+" - ");
        }

        if (user.getEmail() != null) {
            mEmail.setText(getResources().getString(R.string.email) +" "+ user.getEmail());
        } else {
            mEmail.setText(getResources().getString(R.string.email) +" - ");
        }


        if (user.getImage() != null && isOnline()) {
            Picasso.with(context).load(user.getImage()).into(mImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    mImage.setBackground(context.getResources().getDrawable(R.drawable.profile_picture));
                }
            });
        } else {
            mImage.setBackground(context.getResources().getDrawable(R.drawable.profile_picture));
        }

        if (user.getHobbies() != null) {
            if (user.getHobbies().size() == 1) {
                mHobbies.setText(getResources().getString(R.string.hobbies) +" " + user.getHobbies().get(0));
            } else {
                String s = user.getHobbies().get(0);
                for (int i = 1; i < user.getHobbies().size(); i++) {
                    s = s + ", " + user.getHobbies().get(i);
                }
                mHobbies.setText(getResources().getString(R.string.hobbies) +" " + s);
            }
        } else {
            mHobbies.setText("");
        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (user.getEmail() != null) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
                    i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject));
                    i.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.body));
                    try {
                        startActivity(Intent.createChooser(i, getResources().getString(R.string.send_email)));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, getResources().getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.no_email_address), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
