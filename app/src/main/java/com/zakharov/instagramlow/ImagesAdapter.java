package com.zakharov.instagramlow;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import static com.zakharov.instagramlow.MainActivity.list;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {



    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForImage = R.layout.image_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForImage, parent, false);

        return new ImagesViewHolder(view, new LikeController(context));

    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, int position) {
        //костыль, чтобы картинки подгружались, когда лента заканчивается
        if (position == (list.size() - 8)){
            //String str = ""+MainActivity.list.size();
            //Log.d("DOC", "position="+position+" ,list.size="+str);
            new MainActivity.NewThread().execute();
        }

        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ImagesViewHolder extends  RecyclerView.ViewHolder{

        ImageView imageView;
        ImageView likeImageView;
        LikeController _likeController;

        String _currentImage;
        Integer _currentIndex;

        public ImagesViewHolder(View itemView, LikeController likeController) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView1);
            likeImageView = itemView.findViewById(R.id.likeImageView);
            _likeController = likeController;
            _likeController.CreateDataBase();
            Log.d("LIKE", _likeController.SelectAll());
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetLike();
                }

                private void SetLike() {
                    if (_likeController.FindImageFromDataBase(_currentImage)){
                        Log.d("LIKE", "likeoff "+ _currentIndex + " " + _currentImage);
                        Picasso.get().load(R.drawable.heartoff).into(likeImageView);
                        _likeController.DeleteFromDataBase(_currentImage);
                    }
                    else{
                        Log.d("LIKE", "likeon "+ _currentIndex + " " + _currentImage);
                        Picasso.get().load(R.drawable.hearton).into(likeImageView);
                        _likeController.InsertIntoDataBase(_currentImage);
                    }

                }
            });
        }

        void bind(String url){
            Picasso.get().load(url).into(imageView);

            _currentIndex = getAdapterPosition();
            _currentImage = list.get(_currentIndex);
            Log.d("LIKE", _currentIndex + " " + _currentImage);
            if (_likeController.FindImageFromDataBase(_currentImage)){
                Log.d("LIKE", "лайкнуто " + _currentImage);
                Picasso.get().load(R.drawable.hearton).into(likeImageView);
            }
            else Picasso.get().load(R.drawable.heartoff).into(likeImageView);
        }

    }
}
