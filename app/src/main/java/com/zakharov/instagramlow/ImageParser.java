package com.zakharov.instagramlow;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

import java.util.ArrayList;

import static com.flickr4java.flickr.photos.SearchParameters.RELEVANCE;

public class ImageParser {
    private Flickr flickr;
    private String apiKey = "482c6c617a17d28a98b9b4b461864a10";
    private String sharedSecret = "3bb3cae063e0ad84";
    private String searchTile = "cat";
    private Integer itemInPage = 25;
    private Integer page = 1;

    public ImageParser(){
        this.flickr = new Flickr(this.apiKey, this.sharedSecret, new REST());
    }

    public void downloadImages(ArrayList<String> list){
        PhotosInterface photos = flickr.getPhotosInterface();
        SearchParameters params = new SearchParameters();
        try {
            params.setMedia("photos"); // One of "photos", "videos" or "all"
            params.setText(searchTile);
            params.setSort(RELEVANCE);
            params.setExtras(Extras.ALL_EXTRAS);
            PhotoList<Photo> resultsPhoto = photos.search(params, itemInPage, page++);
            for (Photo p: resultsPhoto){
                list.add(p.getSmallUrl());
            }

        } catch (FlickrException e) {
            e.printStackTrace();
        }
    }
}
