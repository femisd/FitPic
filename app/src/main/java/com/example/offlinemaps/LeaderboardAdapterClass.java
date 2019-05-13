package com.example.offlinemaps;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapterClass extends ArrayAdapter {
    private Context mContext;
    private List<User> userList = new ArrayList<User>();

    public LeaderboardAdapterClass(@NonNull Context context, ArrayList<User> list) {
        super(context, 0, list);
        mContext = context;
        userList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.leaderboard_listview, parent, false);
        //Get the user and populate the variables.
        User currentUser = userList.get(position);

        ImageView medal = listItem.findViewById(R.id.iv_medal);
        if (position == 0) {
            medal.setImageResource(R.drawable.first);
        } else if (position == 1) {
            medal.setImageResource(R.drawable.second);
        } else if (position == 2) {
            medal.setImageResource(R.drawable.third);
        } else {
            //No medal
        }

        TextView leaderboardNumber = listItem.findViewById(R.id.tv_medal);
        for (int i = 3; i < userList.size(); i++) {
            if (position == i) {
                leaderboardNumber.setText(i + 1 + "");
            }
        }

        ImageView image = listItem.findViewById(R.id.iv_profile_pic);
        //image.setImageResource((String) currentUser.getmProfilePicture());
        if (!currentUser.getmProfilePicture().isEmpty()) {
            Picasso.get().load(currentUser.getmProfilePicture()).placeholder(R.drawable.ic_user_placeholder).into(image);
        } else {
            Picasso.get().load(R.drawable.ic_user_placeholder).placeholder(R.drawable.ic_user_placeholder).into(image);
        }

        if (currentUser.getmVIP()) {
            ImageView vip = listItem.findViewById(R.id.iv_vip);
            vip.setImageResource(R.drawable.vip_ticket);
        }

        TextView name = listItem.findViewById(R.id.tv_username);
        if (!currentUser.getmUid().equals(FirebaseAuth.getInstance().getUid())) {
            name.setText(currentUser.getmUsername());
            listItem.setBackgroundColor(Color.WHITE);
        } else {
            name.setText("You");
            listItem.setBackgroundColor(Color.GREEN);
        }

        TextView location = listItem.findViewById(R.id.tv_location);
        location.setText(currentUser.getmLocation());

        TextView points = listItem.findViewById(R.id.tv_point_counter);
        points.setText("" + currentUser.getmPoints());

        TextView calories = listItem.findViewById(R.id.tv_calories_burnt);
        calories.setText(currentUser.getmCaloriesBurned() + "");


        return listItem;
    }
}
