package in.innovateria.whatsappstatus.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.innovateria.whatsappstatus.Activities.ShowImageActivity;
import in.innovateria.whatsappstatus.Models.ItemModel;
import in.innovateria.whatsappstatus.R;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ItemsViewHolder> {
    Context context;
    ArrayList<ItemModel> items;

    public RecycleAdapter(Context context, ArrayList<ItemModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_items, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {

        if(items.get(position).image){
            holder.playButton.setVisibility(View.GONE);
            holder.videoPreview.setVisibility(View.GONE);
            holder.mainImage.setVisibility(View.VISIBLE);
            holder.pauseButton.setVisibility(View.GONE);
            Glide.with(context).load(items.get(position).uri).centerCrop().into(holder.mainImage);
        }
        else
        {
            holder.playButton.setVisibility(View.VISIBLE);
            holder.videoPreview.setVisibility(View.GONE);
            holder.mainImage.setVisibility(View.VISIBLE);
            holder.pauseButton.setVisibility(View.GONE);

            Glide.with(context).load(items.get(position).uri).centerCrop().into(holder.mainImage);
            holder.videoPreview.setVideoURI(items.get(position).uri);

            holder.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.videoPreview.setVisibility(View.VISIBLE);
                    holder.mainImage.setVisibility(View.GONE);
                    holder.playButton.setVisibility(View.GONE);
                    holder.videoPreview.start();
                    holder.pauseButton.setVisibility(View.VISIBLE);
                }
            });

            holder.pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.playButton.setVisibility(View.VISIBLE);
                    holder.videoPreview.pause();
                    holder.pauseButton.setVisibility(View.GONE);

                    holder.videoPreview.setVisibility(View.GONE);
                    holder.mainImage.setVisibility(View.VISIBLE);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Aryan","Button is clicked");
                Intent intent = new Intent(context, ShowImageActivity.class);
                Log.d("Aryan","Values are " + items.get(position).uri);
                intent.putExtra("Data",items.get(position).uri);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        ImageView playButton, pauseButton, mainImage;
        VideoView videoPreview;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage=itemView.findViewById(R.id.mainImage);
            videoPreview=itemView.findViewById(R.id.videoPreview);
            playButton=itemView.findViewById(R.id.playButton);
            pauseButton=itemView.findViewById(R.id.pauseButton);
        }

    }
}
