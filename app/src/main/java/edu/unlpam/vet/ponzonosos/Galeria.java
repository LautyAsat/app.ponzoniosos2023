package edu.unlpam.vet.ponzonosos;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import edu.unlpam.vet.ponzonosos.model.Animal;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Galeria extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria);
        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        Long idAnimal = (Long) getIntent().getExtras().get("img");
        Animal animal = Animal.getAnimal(idAnimal);
        if (animal != null) {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            RecyclerView recyclerView = findViewById(R.id.rv_images);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, SpacePhoto.getSpacePhotos(animal.getImages()));
            recyclerView.setAdapter(adapter);
        }else {
            Toast.makeText(getApplicationContext(),
                    "No existe un animal con el id " + idAnimal,
                    Toast.LENGTH_LONG).show();
        }
    }


    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @NonNull
        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.elemento_galeria, parent, false);
            return new ImageGalleryAdapter.MyViewHolder(photoView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            SpacePhoto spacePhoto = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.default_image);
            Glide.with(mContext)
            .load(mContext.getFilesDir()+"/"+spacePhoto.getUrl())
            .apply(options)
            .into(imageView);

        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    SpacePhoto spacePhoto = mSpacePhotos[position];
                    Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                    intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    startActivity(intent);
                }
            }
        }

        private SpacePhoto[] mSpacePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, SpacePhoto[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }
    }
}
