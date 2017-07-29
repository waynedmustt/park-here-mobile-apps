package com.dmustt.mdewantara.parkhere.core;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmustt.mdewantara.R;
import com.dmustt.mdewantara.stadium.schema.StadiumSchema;

import java.util.ArrayList;

import Utilities.ParkHereUtilities;

/**
 * Created by mdewantara on 7/21/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StadiumSchema> stadiumSchemas;
    private ParkHereUtilities utilities;

    public CustomAdapter(Context context, ArrayList<StadiumSchema> stadiumSchemas) {
        this.context = context;
        this.stadiumSchemas = stadiumSchemas;
        this.utilities = new ParkHereUtilities();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dashboard, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(stadiumSchemas.get(position).getName());
        Glide.with(context).
                load(utilities.base64Decode(stadiumSchemas.get(position).getImage()))
                .into(holder.image);
        holder.stadiumCardView.setTag(position);
        holder.capacity.setText(stadiumSchemas.get(position).getCapacity().toString());
    }

    @Override
    public int getItemCount() {
        return stadiumSchemas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView capacity;
        public ImageView image;
        public CardView stadiumCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.stadium_name);
            image = (ImageView) itemView.findViewById(R.id.stadium_image);
            stadiumCardView = (CardView) itemView.findViewById(R.id.stadium_card_view);
            capacity = (TextView) itemView.findViewById(R.id.capacity_count);
        }
    }

}
