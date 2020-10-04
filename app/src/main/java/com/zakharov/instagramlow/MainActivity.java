package com.zakharov.instagramlow;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    String url = "https://yandex.ru/images/search?text=котики";
    //String url = "https://flickr.com/explore";
    String src;
    //String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36";
    ArrayList list = new ArrayList();
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv =  (ImageView) findViewById(R.id.imageView);
//        Picasso.get().load(url.toString()).into(iv);
        new NewThread().execute();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Picasso.get().load(list.get(1).toString()).into(iv);
    }

    public class NewThread extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Document doc;
            /*
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

            try {
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