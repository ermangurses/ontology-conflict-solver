package edu.arizona.biosemantics.conflictsolver;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

 public class TermOptionsAdapter extends RecyclerView.Adapter<TermOptionsAdapter.ViewHolder>{

    private static final String TAG = "TermOptionsAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mDefinitions = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    private int selectedPosition = -1;

    public TermOptionsAdapter(Context context, ArrayList<String> images, ArrayList<String> imageNames, ArrayList<String> definitions){
        mImageNames = imageNames;
        mImageNames.add("Not above, I enter my own");
        mDefinitions = definitions;
        mDefinitions.add("");
        mImages = images;
        mImages.add("");
        mContext = context;
    }
     @NonNull
     @Override
     public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
         SharedPreferencesManager.getInstance(mContext).setSelectedOption(-1);
         return holder;
     }

     @Override
     public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");
        Glide.with(mContext)
                .load(mImages.get(position))
                .placeholder(R.drawable.noimage).into(holder.image);

         holder.imageName.setText(mImageNames.get(position));
         holder.definition.setText(mDefinitions.get(position));

         // Set visible the selected position
         if(selectedPosition == position){
             holder.ok.setVisibility(View.VISIBLE);
             SharedPreferencesManager.getInstance(mContext).setSelectedOption(position);
         } else {
             holder.ok.setVisibility(View.INVISIBLE);
         }

         holder.parentLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
                 selectedPosition = position;
                 notifyDataSetChanged();
                 Toast.makeText(mContext, "You selected: "+mImageNames.get(position), Toast.LENGTH_SHORT).show();
             }
         });
     }

     @Override
     public int getItemCount() {

        return mImageNames.size();
     }

     public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        ImageView ok;
        TextView imageName;
        TextView definition;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            ok = itemView.findViewById(R.id.OK);
            imageName = itemView.findViewById(R.id.image_name);
            definition = itemView.findViewById(R.id.definition);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }




}