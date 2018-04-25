package com.tiva.androidtest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mojtaba on 17/9/29 AD.
 */
public class UserDetails {

    private ArrayList<User> userDetails;
    private String status;

    public List<User> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(ArrayList<User> userDetails) {
        this.userDetails = userDetails;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
