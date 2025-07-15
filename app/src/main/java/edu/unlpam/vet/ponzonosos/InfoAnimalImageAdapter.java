package edu.unlpam.vet.ponzonosos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class InfoAnimalImageAdapter extends RecyclerView.Adapter<InfoAnimalImageAdapter.ImageViewHolder> {
    private final List<String> imagePaths;
    private final Context context;

    public InfoAnimalImageAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_image_info, parent, false);
        return new ImageViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String path = imagePaths.get(position);
        Glide.with(context).load(path).into(holder.imageView);


        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullscreenGalleryActivity.class);
            intent.putStringArrayListExtra("photos", new ArrayList<>(imagePaths));
            int position2 = holder.getBindingAdapterPosition();
            if (position2 != RecyclerView.NO_POSITION) {
                intent.putExtra("position", position2);
                context.startActivity(intent);
            }

        });

    }


    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_item);
        }
    }
}

