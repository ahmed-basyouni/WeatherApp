package com.ark.android.weatherapp.ui.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.manager.BookmarkManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>{

    private final Context context;
    private final int width;
    private final int height;
    private final BookmarkManager bookmarkManager;
    private List<BookMarksObject> bookmarks;

    public BookmarkAdapter(List<BookMarksObject> bookmarks, Context context){
        this.bookmarks = bookmarks;
        this.context = context;
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        bookmarkManager = new BookmarkManager();
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.bookmark_item, parent, false);
        return new BookmarkViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        final BookMarksObject bookMarksObject = bookmarks.get(position);
        holder.bookmarkTitle.setText(bookMarksObject.getTitle());
        CardView cardView = (CardView) holder.timeImage.getParent();
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) cardView.getLayoutParams();
        params.height = height / 3;
        if(bookMarksObject.isUpdating()){
            holder.bookmarkGeoProgress.setVisibility(View.VISIBLE);
            holder.bookmarkGeoAddress.setVisibility(View.GONE);
            holder.weatherTitle.setVisibility(View.GONE);
            holder.bookmarkDegree.setVisibility(View.GONE);
            holder.retry.setVisibility(View.GONE);
        }else if(!bookMarksObject.isUpdating() && bookMarksObject.isUpdateFailed()){
            holder.bookmarkGeoProgress.setVisibility(View.GONE);
            holder.bookmarkGeoAddress.setVisibility(View.GONE);
            holder.weatherTitle.setVisibility(View.GONE);
            holder.bookmarkDegree.setVisibility(View.GONE);
            holder.retry.setVisibility(View.VISIBLE);
        }else if(!bookMarksObject.isUpdating() && !bookMarksObject.isUpdateFailed() && (bookMarksObject.getWeatherObj() == null
                || bookMarksObject.getWeatherObj().getWeatherInfoObject() == null || bookMarksObject.getWeatherObj().getWeatherInfoObject().getTemp() == 0)){
            bookmarkManager.updateBookmarkObjectData(bookMarksObject);
        }else{
            holder.bookmarkGeoAddress.setVisibility(View.VISIBLE);
            holder.bookmarkDegree.setVisibility(View.VISIBLE);
            holder.weatherTitle.setVisibility(View.VISIBLE);
            if(bookMarksObject.getWeatherObj() != null && !bookMarksObject.getWeatherObj().getWeather().isEmpty())
                holder.weatherTitle.setText(bookMarksObject.getWeatherObj().getWeather().get(0).getWeatherTitle());

            holder.bookmarkGeoAddress.setText(bookMarksObject.getGeoAddress());
            holder.bookmarkDegree.setText(String.valueOf(bookMarksObject.getWeatherObj().getWeatherInfoObject().getTemp()) + "\u00B0");
            holder.bookmarkGeoProgress.setVisibility(View.GONE);
            holder.retry.setVisibility(View.GONE);
        }

        if(bookMarksObject.getWeatherObj() != null
                && !bookMarksObject.getWeatherObj().getWeather().isEmpty() && bookMarksObject.getWeatherObj().getWeather().get(0).getWeatherTitle() != null)
            holder.timeImage.setImageResource(getImageForWeather(bookMarksObject.getWeatherObj().getWeather().get(0).getWeatherTitle()));

        holder.retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               bookmarkManager.updateBookmarkObjectData(bookMarksObject);
            }
        });

        if(bookMarksObject.isDefault())
            holder.defaultMark.setVisibility(View.VISIBLE);
        else
            holder.defaultMark.setVisibility(View.GONE);
    }

    private int getImageForWeather(String weatherTitle) {
        int drawableInt = R.drawable.day_clearsky;

        if(weatherTitle.contains("Rain") || weatherTitle.contains("Extreme")
                || weatherTitle.contains("Additional") || weatherTitle.contains("Thunderstorm") || weatherTitle.contains("Drizzle")){
            drawableInt = R.drawable.day_rain;
        } else if(weatherTitle.contains("Snow")){
            drawableInt = R.drawable.day_snow;
        }else if(weatherTitle.contains("Atmosphere")){
            drawableInt = R.drawable.day_fog;
        }else if(weatherTitle.contains("Clouds")){
            drawableInt = R.drawable.day_cloudy;
        }

        return drawableInt;
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public void updateList(List<BookMarksObject> bookmarks) {
        this.bookmarks = bookmarks;
    }

    class BookmarkViewHolder extends RecyclerView.ViewHolder{

        TextView bookmarkTitle;
        TextView bookmarkGeoAddress;
        TextView weatherTitle;
        ProgressBar bookmarkGeoProgress;
        TextView bookmarkDegree;
        ImageButton retry;
        ImageView timeImage;
        ImageView defaultMark;

        BookmarkViewHolder(View itemView) {
            super(itemView);
            bookmarkTitle = (TextView) itemView.findViewById(R.id.bookmarkTitle);
            bookmarkGeoAddress = (TextView) itemView.findViewById(R.id.bookmarkGeoAddress);
            bookmarkGeoProgress = (ProgressBar) itemView.findViewById(R.id.progressBar);
            bookmarkDegree = (TextView) itemView.findViewById(R.id.degree);
            retry = (ImageButton) itemView.findViewById(R.id.retry);
            timeImage = (ImageView) itemView.findViewById(R.id.timeImage);
            defaultMark = (ImageView) itemView.findViewById(R.id.defaultBookmark);
            weatherTitle = (TextView) itemView.findViewById(R.id.weatherTitle);
        }
    }
}
