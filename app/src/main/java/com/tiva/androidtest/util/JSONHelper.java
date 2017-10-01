package com.tiva.androidtest.util;

import android.content.Context;
import android.util.Log;

import com.tiva.androidtest.model.User;
import com.tiva.androidtest.model.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mojtaba on 17/9/29 AD.
 */
public class JSONHelper {
    private Context context;
    private String file;

    public JSONHelper(Context context, String file){
        this.context = context;
        this.file = file;

    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public ArrayList<User> parseJSONUsers(String file){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(file));
            JSONArray usersArray = obj.getJSONArray("users");

            ArrayList<User> users = new ArrayList<>();
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject usersObj = usersArray.getJSONObject(i);
                User user = new User();
                Iterator<String> iter = usersObj.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        user.setId(Integer.parseInt(key));
                        Object value = usersObj.get(key);
                        user.setName(value.toString());
                        users.add(user);
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }
            }


            return users;



        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<User> parseJSONUserDetails(String file){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(file));
            JSONArray usersArray = obj.getJSONArray("users");

            ArrayList<User> users = new ArrayList<User>();
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject usersObj = usersArray.getJSONObject(i);
                User user = new User();
                Iterator<String> iter = usersObj.keys();

                    while (iter.hasNext()) {
                        String key = iter.next();
                        Object value = usersObj.get(key);

                        switch (key) {
                            case "id":
                                user.setId(Integer.parseInt(value.toString()));
                                break;
                            case "name":
                                user.setName(value.toString());
                                break;
                            case "email":
                                user.setEmail(value.toString());
                                break;
                            case "age":
                                user.setAge(Integer.parseInt(value.toString()));
                                break;
                            case "isFemale":
                                user.setFemale(Boolean.parseBoolean(value.toString()));
                                break;
                            case "hobbies":
                                ArrayList<String> listdata = new ArrayList<String>();
                                JSONArray jArray = (JSONArray)value;
                                if (jArray != null) {
                                    for (int j=0;j<jArray.length();j++){
                                        listdata.add(jArray.getString(j));
                                    }
                                }
                                user.setHobbies(listdata);
                            case "image":
                                user.setImage(value.toString());
                                break;
                            case "back":
                                user.setBack(value.toString());
                                break;

                        }



                }

                users.add(user);

            }





            return users;



        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



}
