package com.example.offlinemaps;

import android.content.Context;
import android.util.Log;
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

public class FriendAdapterClass extends ArrayAdapter {
    private Context mContext;
    private List<User> userList = new ArrayList<>();

    public FriendAdapterClass(@NonNull Context context, ArrayList<User> list) {
        super(context, 0, list);
        mContext = context;
        userList = list;
    }

    /**
     * Get the friends list view.
     *
     * @param position = position in the ListView.
     * @return listItem.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.friends_listview, parent, false);

        //Get user and set variables.
        User currentUser = userList.get(position);

        if (currentUser != null) {
            Log.d("FriendAdapter", currentUser.toString());
            if (currentUser.getmVIP()) {
                ImageView vip = listItem.findViewById(R.id.iv_vip);
                vip.setImageResource(R.drawable.vip_ticket);
            }

            ImageView image = listItem.findViewById(R.id.iv_profile_pic);
            if (!currentUser.getmProfilePicture().isEmpty()) {
                Picasso.get().load(currentUser.getmProfilePicture()).placeholder(R.drawable.ic_user_placeholder).into(image);
            } else {
                Picasso.get().load(R.drawable.ic_user_placeholder).placeholder(R.drawable.ic_user_placeholder).into(image);
            }

            TextView name = listItem.findViewById(R.id.tv_username);
            name.setText(currentUser.getmUsername());

            TextView release = listItem.findViewById(R.id.tv_location);
            release.setText(currentUser.getmLocation());
        }
        return listItem;
    }
}
