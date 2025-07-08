package edu.unlpam.vet.ponzonosos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class FullscreenGalleryActivity extends AppCompatActivity {

    private ViewPager2 viewPagerMain;
    private RecyclerView recyclerThumbnails;
    private ThumbnailAdapter thumbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_gallery);

        List<String> imagePaths = getIntent().getStringArrayListExtra("photos");
        int currentPosition = getIntent().getIntExtra("position", 0);

        viewPagerMain = findViewById(R.id.viewPager_main);
        recyclerThumbnails = findViewById(R.id.rv_thumbnails);

        FullscreenAdapter fullscreenAdapter = new FullscreenAdapter(this, imagePaths);
        viewPagerMain.setAdapter(fullscreenAdapter);
        viewPagerMain.setCurrentItem(currentPosition, false);

        thumbAdapter = new ThumbnailAdapter(imagePaths, currentPosition, pos -> viewPagerMain.setCurrentItem(pos, true));

        recyclerThumbnails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerThumbnails.setAdapter(thumbAdapter);

        // Sincronizar scroll de miniaturas al cambiar la imagen grande
        viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                thumbAdapter.setSelectedPosition(position);
                recyclerThumbnails.smoothScrollToPosition(position);
            }
        });
    }
}
