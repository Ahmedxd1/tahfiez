package com.ahmed.ahmed.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.ahmed.R;
import com.ahmed.ahmed.ShowMapsActivity;
import com.ahmed.ahmed.databinding.CustomItemCenterBinding;
import com.ahmed.ahmed.model.CenterJoin;
import com.ahmed.ahmed.ui.Centers.AddCenterActivity;
import com.ahmed.ahmed.ui.Centers.CentersFragment;

import java.util.List;

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.CenterHolder>{

    List<CenterJoin> centers;
    AdapterListener<CenterJoin> listener;
    Context context;
    int userExpiration;

    public CentersAdapter(List<CenterJoin> centers, AdapterListener<CenterJoin> listener , int userExpiration) {
        this.centers = centers;
        this.listener = listener;
        this.userExpiration = userExpiration;
    }


    public List<CenterJoin> getCenters() {
        return centers;
    }

    public void setCenters(List<CenterJoin> centers) {
        this.centers = centers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CenterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_center , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CenterHolder holder, int position) {
        CenterJoin center = centers.get(position);
        holder.bind(center);
    }

    @Override
    public int getItemCount() {
        return centers.size();
    }

    public class CenterHolder extends RecyclerView.ViewHolder{
        CustomItemCenterBinding binding;
        CenterJoin center;
        public CenterHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomItemCenterBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(center);
                }
            });
        }
        public void bind(CenterJoin center){
            this.center = center;
            String city ;
            binding.centerAddress.setText(context.getString(R.string.address)+" : "+center.getCenterAddress());
            binding.centerCity.setText(context.getString(R.string.city)+" : "+center.getCenterSection());
            binding.centerName.setText(context.getString(R.string.center)+" : "+center.getCenterName());
            binding.centerLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , ShowMapsActivity.class);
                    intent.putExtra("lat" , center.getCenterLat());
                    intent.putExtra("long" , center.getCenterLong());
                    context.startActivity(intent);
                }
            });
            binding.centerImage.setImageURI(Uri.parse(center.getCenterImage()));

            binding.cycleActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , CentersFragment.class);
                    intent.putExtra("centerId" , center.getCenterId());
                    context.startActivity(intent);
                }
            });

           if (userExpiration == 1)
               binding.centerSettings.setVisibility(View.VISIBLE);

            binding.centerSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , AddCenterActivity.class);
                    intent.putExtra("Center" , 1001);
                    intent.putExtra("viewCenter" , center);
                    context.startActivity(intent);
                }
            });

        }
    }
}
