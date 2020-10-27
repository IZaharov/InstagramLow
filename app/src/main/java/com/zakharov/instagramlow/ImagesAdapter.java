package com.zakharov.instagramlow;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import static com.zakharov.instagramlow.MainActivity.list;
public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LikeController likeController;
    final int VIEW_TYPE_IMAGE = 0;
    final int VIEW_TYPE_LOADING = 1;

    public ImagesAdapter(Context context){
        likeController = new LikeController(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
            return new ImagesViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImagesViewHolder){
            ((ImagesViewHolder) holder).bind(list.get(position));

        }
        else if (holder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder) holder, position);
        }

//        //костыль, чтобы картинки подгружались, когда лента заканчивается
//        Log.d("AAA", "position="+position);
//        if (position >= (list.size() - 15)){
//            //Log.d("AAA", "position="+position);
//            String str = ""+MainActivity.list.size();
//            Log.d("AAA", "position="+position+" ,list.size="+str);
//            new MainActivity.AsynkDownload().execute();
//        }
//
//        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_IMAGE;
    }

    class ImagesViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView likeImageView;

        String _currentImage;
        Integer _currentIndex;

        public ImagesViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView1);
            likeImageView = itemView.findViewById(R.id.likeImageView);
            likeController.CreateDataBase();
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SetLike().execute();
                }
            });
        }

        private class SetLike extends AsyncTask<Void, Void, Void>{
            boolean like = false;

            @Override
            protected void onPreExecute() {
                like = likeController.FindImageFromDataBase(_currentImage);
                if (like){
                    Log.d("LIKE", "likeoff "+ _currentIndex + " " + _currentImage);
                    Picasso.get().load(R.drawable.heartoff).into(likeImageView);

                }
                else{
                    Log.d("LIKE", "likeon "+ _currentIndex + " " + _currentImage);
                    Picasso.get().load(R.drawable.hearton).into(likeImageView);
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (like){
                    likeController.DeleteFromDataBase(_currentImage);
                }
                else{
                    likeController.InsertIntoDataBase(_currentImage);
                }
                return null;
            }
        }

        void bind(String url){
            //Загрузка картинки в ViewHolder
            Picasso.get().load(url).into(imageView);

            //
            _currentIndex = getAdapterPosition();
            _currentImage = list.get(_currentIndex);
            Log.d("LIKE", _currentIndex + " " + _currentImage);
            if (likeController.FindImageFromDataBase(_currentImage)){
                Log.d("LIKE", "лайкнуто " + _currentImage);
                Picasso.get().load(R.drawable.hearton).into(likeImageView);
            }
            else Picasso.get().load(R.drawable.heartoff).into(likeImageView);
        }

    }

    class LoadingViewHolder extends  RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position){
        //progressBar animation
    }

}
