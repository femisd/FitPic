package com.example.offlinemaps;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.friends_listview, parent, false);

        User currentUser = userList.get(position);

        ImageView image = (ImageView) listItem.findViewById(R.id.iv_profile_pic);
        image.setImageResource((int) currentUser.getmProfilePicture());

        TextView name = (TextView) listItem.findViewById(R.id.tv_username);
        name.setText(currentUser.getmUsername());

        TextView release = (TextView) listItem.findViewById(R.id.tv_location);
        release.setText(currentUser.getmLocation());

        return listItem;
    }
}
