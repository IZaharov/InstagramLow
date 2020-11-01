package com.zakharov.instagramlow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> list = new ArrayList<String>();
    private static ImageParser imageParser = new ImageParser();
    RecyclerView imagesRecyclerView;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    public class DownloadImages extends AsyncTask<Void ,Void, Void> {
        int index = 0;
        @Override
        protected void onPreExecute() {
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
            list.remove(index);
            imagesRecyclerView.getAdapter().notifyItemRemoved(index);
            imagesRecyclerView.getAdapter().notifyDataSetChanged();
            isLoading = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share_friendly_app:
                //плохой вариант, зато открывается новое приложение
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Hello from InstagramLow");
                String chooserTitle = getString(R.string.chooser);
                Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
                startActivity(chosenIntent);

                //по идее нужный вариант, но открывается в текущем приложении
//                Intent intent = new Intent("com.projectkfc.action.OPEN_FRIENDLY_APP");
//                try{
//                    startActivity(intent);
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
