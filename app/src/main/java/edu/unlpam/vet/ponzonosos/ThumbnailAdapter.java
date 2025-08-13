package edu.unlpam.vet.ponzonosos;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.unlpam.vet.ponzonosos.util.Measures;

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

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();

        // Dependiendo si el item es el seleccionado el thumbnail tiene un tamaño diferente

        if(position == selectedPosition){

            params.height = (int) Measures.dpToPx(holder.itemView.getContext(), 85);
            params.width = (int) Measures.dpToPx(holder.itemView.getContext(), 85);

        }
        else{

            params.height = (int) Measures.dpToPx(holder.itemView.getContext(), 72);
            params.width = (int) Measures.dpToPx(holder.itemView.getContext(), 72);

        }
        holder.itemView.setLayoutParams(params);


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

