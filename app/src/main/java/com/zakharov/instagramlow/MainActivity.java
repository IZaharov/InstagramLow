package com.zakharov.instagramlow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> list = new ArrayList();
    private static ImageParser imageParser = new ImageParser();
    RecyclerView imagesRecyclerView;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagesRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        imagesRecyclerView.setLayoutManager(layoutManager);
        ImagesAdapter adapter = new ImagesAdapter(this);
        imagesRecyclerView.setAdapter(adapter);
        imagesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) imagesRecyclerView.getLayoutManager();
                if (!isLoading){
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 1){
                        isLoading = true;
                        new DownloadImages().execute();
                    }
                }
            }
        });

        isLoading = true;
        new DownloadImages().execute();
    }

    public class DownloadImages extends AsyncTask<Void ,Void, Void>{
        int index = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            index = list.size();
            list.add(null);
            imagesRecyclerView.getAdapter().notifyItemInserted(index);
            imagesRecyclerView.getAdapter().notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imageParser.downloadImages(list);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            list.remove(index);
            imagesRecyclerView.getAdapter().notifyItemRemoved(index);
            imagesRecyclerView.getAdapter().notifyDataSetChanged();
            isLoading = false;
        }
    }
}
