package com.zakharov.instagramlow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

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
        new AsynkDownload().execute();

        imagesRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        imagesRecyclerView.setLayoutManager(layoutManager);
        ImagesAdapter adapter = new ImagesAdapter(this);
        imagesRecyclerView.setAdapter(adapter);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                        //loadMore();
                        isLoading = true;
                        new AsynkNewDownload().execute();
                    }
                }
            }
        });

    }

    private void loadMore(){
        list.add(null);
        imagesRecyclerView.getAdapter().notifyItemInserted(list.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.remove(list.size() - 1);
                int scrollPosition = list.size();
                imagesRecyclerView.getAdapter().notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;
                while (currentSize - 1 < nextLimit) {
                    list.add("https://live.staticflickr.com/5598/14934282524_344c84246b_c.jpg");
                    currentSize++;
                }
                imagesRecyclerView.getAdapter().notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    public class AsynkNewDownload extends AsyncTask<Void ,Void, Void>{
        int index = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            index = list.size();
            list.add(null);
            imagesRecyclerView.getAdapter().notifyItemInserted(index);
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

    public static class AsynkDownload extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            imageParser.downloadImages(list);
            return null;
        }
    }
}
