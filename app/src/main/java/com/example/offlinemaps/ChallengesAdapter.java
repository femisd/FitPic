package com.example.offlinemaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ChallengesViewHolder> {
    private ArrayList<Challenges> mChallangesList;
    private Context context;
    private DatabaseReference userRef;
    private String mCurrentUser = FirebaseAuth.getInstance().getUid();
    public int currentPoints;

    public static class ChallengesViewHolder extends RecyclerView.ViewHolder{

        public TextView challengeNameText;
        public TextView progressText;
        public TextView challengeLimitText;
        public TextView challengeFormatText;
        public Button claimButton;
        public LinearLayout challengeLayout;
        public RelativeLayout completionLayout;



        public ChallengesViewHolder(@NonNull View itemView) {
            super(itemView);
            challengeNameText = itemView.findViewById(R.id.challengeNameText);
            progressText = itemView.findViewById(R.id.currentStepView);
            challengeLimitText = itemView.findViewById(R.id.challengeLimitText);
            challengeFormatText = itemView.findViewById(R.id.challengeFormatText);
            claimButton = itemView.findViewById(R.id.claimBtn);
            challengeLayout = itemView.findViewById(R.id.challengeElementLayout);
            completionLayout = itemView.findViewById(R.id.completion_layout);

            claimButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){

                    }
                }
            });

        }
    }


    public ChallengesAdapter(ArrayList<Challenges> challengesList){
        //Pass the list from the list from the adapter.
        mChallangesList = challengesList;

    }


    @NonNull
    @Override
    public ChallengesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reward_element, viewGroup, false);
        ChallengesViewHolder viewHolder = new ChallengesViewHolder(v);
        context = viewGroup.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChallengesViewHolder challengesViewHolder, int i) {
        final Challenges currentItem = mChallangesList.get(i);
        challengesViewHolder.challengeNameText.setText(currentItem.getChallengeName());
        challengesViewHolder.progressText.setText(String.valueOf(currentItem.getProgress()));
        challengesViewHolder.challengeLimitText.setText(String.valueOf(currentItem.getChallengeLimit()));
        challengesViewHolder.challengeFormatText.setText(currentItem.getChallengeFormat());







        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);

        if( Integer.valueOf(currentItem.getProgress()) > Integer.valueOf(currentItem.getChallengeLimit()) ){
            challengesViewHolder.claimButton.setBackgroundResource(R.drawable.buttonstyle);

            challengesViewHolder.claimButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, " Congratulations!\n"+ currentItem.getPoints() + " added!", Toast.LENGTH_LONG).show();

                     //   challengesViewHolder.challengeLayout.setVisibility(View.GONE);
                        challengesViewHolder.completionLayout.setVisibility(View.VISIBLE);
                        challengesViewHolder.claimButton.setBackgroundResource(R.drawable.buttonstlye_dead);
                        challengesViewHolder.claimButton.setEnabled(false);

                       userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               User user = dataSnapshot.getValue(User.class);
                           //    currentPoints = user.get
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                        userRef.child("mPoints").setValue(currentItem.getPoints());
                        
                        
                }
            });
        }else{

            challengesViewHolder.claimButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Challenge not yet complete\nKeep it up!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public int getItemCount() {

        return mChallangesList.size();
    }
}
