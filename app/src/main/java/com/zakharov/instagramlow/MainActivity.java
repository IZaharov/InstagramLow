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

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import static com.flickr4java.flickr.photos.SearchParameters.RELEVANCE;


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

    static Flickr flickr;
    static String apiKey = "482c6c617a17d28a98b9b4b461864a10";
    static String sharedSecret = "3bb3cae063e0ad84";
    Transport t;
    static String searchTile = "cat";
    static Integer itemInPage = 20;
    public static Integer page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = new REST();
        flickr = new Flickr(apiKey, sharedSecret, t);

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

            /*try {
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
            }*/

            PhotosInterface photos = flickr.getPhotosInterface();
            SearchParameters params = new SearchParameters();
            try {
                params.setMedia("photos"); // One of "photos", "videos" or "all"
                params.setText(searchTile);
                params.setSort(RELEVANCE);
                params.setExtras(Extras.ALL_EXTRAS);
                PhotoList<Photo> resultsPhoto = photos.search(params, itemInPage, ++page);
                for (Photo p: resultsPhoto){
                    list.add(p.getSmallUrl());
                }

            } catch (FlickrException e) {
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
