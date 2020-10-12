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

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {



    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForImage = R.layout.image_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForImage, parent, false);

        return new ImagesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, int position) {
        if (position == (MainActivity.list.size() - 8)){
            String str = ""+MainActivity.list.size();
            Log.d("DOC", "position="+position+" ,list.size="+str);
            new MainActivity.NewThread().execute();
        }

        holder.bind(MainActivity.list.get(position));
    }

    @Override
    public int getItemCount() {
        return MainActivity.list.size();
    }

    class ImagesViewHolder extends  RecyclerView.ViewHolder{

        ImageView imageView;
        ImageView likeImageView;

        public ImagesViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView1);
            likeImageView = itemView.findViewById(R.id.likeImageView);

            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetLike();
                }

                private void SetLike() {

                    Picasso.get().load(R.drawable.hearton).into(likeImageView);
                }
            });
        }

        void bind(String url){
            Picasso.get().load(url).into(imageView);
            Picasso.get().load(R.drawable.heartoff).into(likeImageView);
        }

    }
}
