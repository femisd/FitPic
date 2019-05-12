package com.example.offlinemaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        ImageView image = (ImageView) listItem.findViewById(R.id.iv_profile_pic);
        //image.setImageResource((String) currentUser.getmProfilePicture());
        if (!currentUser.getmProfilePicture().isEmpty()) {
            Picasso.get().load(currentUser.getmProfilePicture()).placeholder(R.drawable.ic_user_placeholder).into(image);
        } else {
            Picasso.get().load(R.drawable.ic_user_placeholder).placeholder(R.drawable.ic_user_placeholder).into(image);
        }

        if (currentUser.getmVIP()) {
            ImageView vip = (ImageView) listItem.findViewById(R.id.iv_vip);
            vip.setImageResource(R.drawable.vip_ticket);
        }

        TextView name = (TextView) listItem.findViewById(R.id.tv_username);
        name.setText(currentUser.getmUsername());

        TextView location = (TextView) listItem.findViewById(R.id.tv_location);
        location.setText(currentUser.getmLocation());

        TextView points = (TextView) listItem.findViewById(R.id.tv_point_counter);
        points.setText("" + currentUser.getmPoints());

        TextView calories = (TextView) listItem.findViewById(R.id.tv_calories_burnt);
        calories.setText(currentUser.getmCaloriesBurned() + "");


        return listItem;
    }
}
