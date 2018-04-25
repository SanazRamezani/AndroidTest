package com.tiva.androidtest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tiva.androidtest.R;
import com.tiva.androidtest.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by mojtaba on 17/9/29 AD.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterHolder> {

    private ArrayList<User> users;
    private Context context;
    private OnAdapterItemListener onAdapterItemListener;


    public UserAdapter(ArrayList<User> users, Context context, OnAdapterItemListener onAdapterItemListener) {
        this.users = users;
        this.context = context;
        this.onAdapterItemListener = onAdapterItemListener;

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    @Override
    public UserAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.user_row_layout, parent, false);

        UserAdapterHolder userAdapterHolder = new UserAdapterHolder(itemView, new UserAdapterHolder.OnViewHolderClicks() {
            @Override
            public void onClick(int position) {
                onAdapterItemListener.onItemClick(position);
            }
        });
        return userAdapterHolder;
    }

    @Override
    public void onBindViewHolder(final UserAdapterHolder userAdapterHolder, int i) {

        userAdapterHolder.nameTextView.setText(users.get(i).getName());
        if (users.get(i).getImage() != null && isOnline()) {

            Picasso.with(context).load(users.get(i).getImage()).into(userAdapterHolder.imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    userAdapterHolder.imageView.setImageResource(R.drawable.profile_picture);
                }
            });
        } else {
            userAdapterHolder.imageView.setImageResource(R.drawable.profile_picture);
        }

    }

    public static interface OnAdapterItemListener {
        public void onItemClick(int postion);
    }


    public static class UserAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView nameTextView;
        protected CircleImageView imageView;
        protected RelativeLayout userLayout;

        protected OnViewHolderClicks onViewHolderClicks;


        public UserAdapterHolder(View v,OnViewHolderClicks onViewHolderClicks) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.name_text_view);
            imageView = (CircleImageView) v.findViewById(R.id.profile_image);
            userLayout = (RelativeLayout) v.findViewById(R.id.user_layout);
            userLayout.setOnClickListener(this);

            this.onViewHolderClicks = onViewHolderClicks;

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.user_layout:
                    onViewHolderClicks.onClick(getAdapterPosition());

            }
        }
        public static interface OnViewHolderClicks {
            public void onClick(int position);
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
