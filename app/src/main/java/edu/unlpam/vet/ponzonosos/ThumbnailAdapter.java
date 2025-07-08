package edu.unlpam.vet.ponzonosos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {

    private final List<String> images;
    private int selectedPosition;
    private final Consumer<Integer> onThumbnailClick;

    public ThumbnailAdapter(List<String> images, int selectedPosition, Consumer<Integer> onThumbnailClick) {
        this.images = images;
        this.selectedPosition = selectedPosition;
        this.onThumbnailClick = onThumbnailClick;
    }

    public void setSelectedPosition(int pos) {
        int prev = selectedPosition;
        selectedPosition = pos;
        notifyItemChanged(prev);
        notifyItemChanged(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(images.get(position)).into(holder.thumb);

        holder.thumb.setBackgroundResource(position == selectedPosition ?
                R.drawable.thumb_border_selected : R.drawable.thumb_border_unselected);

        holder.itemView.setOnClickListener(v -> onThumbnailClick.accept(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.iv_thumb);
        }
    }
}

