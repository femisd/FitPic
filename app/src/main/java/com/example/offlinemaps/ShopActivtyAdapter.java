package com.example.offlinemaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class ShopActivtyAdapter extends ArrayAdapter {
    private Context mContext;
    private List<ShopItem> userList = new ArrayList<>();

    public ShopActivtyAdapter(@NonNull Context context, ArrayList<ShopItem> list) {
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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.shop_item_listview, parent, false);

        //Get user and set variables.
        final ShopItem item = userList.get(position);

        if (item != null) {

            TextView itemCost = listItem.findViewById(R.id.tv_item_cost);
            itemCost.setText(item.getItemCost() + " Points");

            TextView itemName = listItem.findViewById(R.id.tv_item_name);
            itemName.setText(item.getItemName());

            /*
                Get users current points and check if they can afford the shop item.
             */
            Button buyItem = listItem.findViewById(R.id.bt_claim_item);
            buyItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mCurrentUser = FirebaseAuth.getInstance().getUid();
                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user.getmPoints() >= item.getItemCost()) {
                                if (!user.getmVIP()) {
                                    userRef.child("mVIP").setValue(true);
                                    userRef.child("mPoints").setValue(user.getmPoints() - item.getItemCost());
                                    Toast.makeText(mContext, item.getItemName() + " Purchased. Congratulations!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "You are already a VIP!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "You need more points!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
        return listItem;
    }
}
