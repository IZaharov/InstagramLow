package com.zakharov.instagramlow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    //String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36";
    public static ArrayList<String> list = new ArrayList();

    RecyclerView imagesList;
    static String url = "http://www.freedigitalphotos.net/images/Business_people_g201.html?p=";
    static int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new NewThread().execute();

        imagesList = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        imagesList.setLayoutManager(layoutManager);
        imagesList.setAdapter(new ImagesAdapter());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static class NewThread extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Document doc;
            //Загрузка картинки с Яндекса в низком разрешении
            /*
            String url = "https://yandex.ru/images/search?text=котики";
            try {
                Log.d("DOC", "Start");
                doc = Jsoup.connect(url)
                        //.userAgent(userAgent)
                        .timeout(5000)
                        .get();
                Log.d("DOC", "End");
                Log.d("DOC", doc.title());
                Elements links = doc.select(".serp-item.serp-item_type_search");
                for (Element e : links) {
                    String src = e.attr("data-bem");
                    JSONObject jsonObj = new JSONObject(src).getJSONObject("serp-item");
                    String imgURL = jsonObj.getString("img_href");
                    Log.d("DOC", imgURL);
                    list.add(imgURL);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            */
            //Загрузка картинки с Яндекса в высоком разрешении
            /*try {
            String url = "https://yandex.ru/images/search?text=котики";
                Log.d("DOC", "Start");
                doc = Jsoup.connect(url)
                        //.userAgent(userAgent)
                        .timeout(5000)
                        .get();
                Log.d("DOC", "End");
                Log.d("DOC", doc.title());
                Elements links = doc.select("[src]");
                for (Element e : links) {
                    if (e.normalName().equals("img")) {
                        String imgURL = "https:".concat(e.attr("src"));
                        Log.d("DOC", imgURL);
                        list.add(imgURL);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            try {
                //Log.d("DOC", "Start");
                doc = Jsoup.connect(url + pageNumber)
                        //.userAgent(userAgent)
                        .get();
                pageNumber++;
                //Log.d("DOC", "End");
                //Log.d("DOC", doc.title());
                Elements links = doc.select(".list-img.clearfix.tallest");
                for (Element e : links.select("img")) {
                    if (e.normalName().equals("img")) {
                        String imgURL = e.attr("abs:src");
                        Log.d("DOC", imgURL);
                        list.add(imgURL);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
