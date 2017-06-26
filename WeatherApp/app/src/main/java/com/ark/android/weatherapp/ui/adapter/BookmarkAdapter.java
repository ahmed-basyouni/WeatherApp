package com.ark.android.weatherapp.ui.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.data.cache.BookMarksUtils;
import com.ark.android.weatherapp.data.model.BookMarksObject;
import com.ark.android.weatherapp.manager.BookmarkManager;
import com.ark.android.weatherapp.manager.BookmarkUpdateManager;
import com.ark.android.weatherapp.mvpContract.BookmarksListContract;
import com.ark.android.weatherapp.ui.fragment.DetailsFragment;
import com.ark.android.weatherapp.utils.TempUtils;
import com.ark.android.weatherapp.utils.WeatherImageUtils;

import java.util.ArrayList;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> implements BookmarksListContract.IBookmarkAdapterView{

    private final Context context;
    private final int height;
    private final BookmarkManager bookmarkManager;
    private final BookmarksListContract.IBookmarkAdapterPresenter bookmarkAdapterPresenter;
    private final boolean isTab;
    private boolean isFaherhiet;
    private ArrayList<BookMarksObject> bookmarks;

    public BookmarkAdapter(ArrayList<BookMarksObject> bookmarks, BookmarksListContract.IBookmarkAdapterPresenter iBookmarkAdapterPresenter){
        this.bookmarks = bookmarks;
        this.bookmarkAdapterPresenter = iBookmarkAdapterPresenter;
        this.context = iBookmarkAdapterPresenter.getActivityContext();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        bookmarkManager = new BookmarkManager();
        checkFahrenhiet();
        isTab = context.getResources().getBoolean(R.bool.isTab);
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.bookmark_item, parent, false);
        return new BookmarkViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final BookmarkViewHolder holder, final int position) {

        final BookMarksObject bookMarksObject = bookmarks.get(holder.getAdapterPosition());
        holder.bookmarkTitle.setText(bookMarksObject.getTitle());
        CardView cardView = (CardView) holder.timeImage.getParent();
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) cardView.getLayoutParams();
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && !isTab)
            params.height = height / 3;
        else if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && isTab)
            params.height = height / 4;
        else if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTab)
            params.height = (int)(height/1.5);
        else if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && isTab)
            params.height = (int)(height/3);

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
                || bookMarksObject.getWeatherObj().getWeatherInfoObject() == null
                || bookMarksObject.getWeatherObj().getWeatherInfoObject().getTemp() == 0)){
            bookmarkManager.updateBookmarkObjectData(bookMarksObject);
        }else{
            if(bookMarksObject.isForceUpdate())
                new BookmarkUpdateManager().silentUpdate(bookMarksObject);
            holder.bookmarkGeoAddress.setVisibility(View.VISIBLE);
            holder.bookmarkDegree.setVisibility(View.VISIBLE);
            holder.weatherTitle.setVisibility(View.VISIBLE);
            if(bookMarksObject.getWeatherObj() != null && !bookMarksObject.getWeatherObj().getWeather().isEmpty())
                holder.weatherTitle.setText(bookMarksObject.getWeatherObj().getWeather().get(0).getWeatherTitle());

            holder.bookmarkGeoAddress.setText(bookMarksObject.getGeoAddress());
            holder.bookmarkDegree.setText(String.format(bookmarkAdapterPresenter.getActivityContext().getString(R.string.tempWithUnit),String.valueOf(
                    isFaherhiet ? TempUtils.convertToFahrenheit(bookMarksObject.getWeatherObj().getWeatherInfoObject().getTemp())
                    : bookMarksObject.getWeatherObj().getWeatherInfoObject().getTemp())));
            holder.bookmarkGeoProgress.setVisibility(View.GONE);
            holder.retry.setVisibility(View.GONE);
        }

        if(bookMarksObject.getWeatherObj() != null
                && !bookMarksObject.getWeatherObj().getWeather().isEmpty() && bookMarksObject.getWeatherObj().getWeather().get(0).getWeatherTitle() != null)
            holder.timeImage.setImageResource(WeatherImageUtils.getImageForWeather(bookMarksObject.getWeatherObj().getWeather().get(0).getWeatherTitle()));

        holder.retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               bookmarkManager.updateBookmarkObjectData(bookMarksObject);
            }
        });

        if(bookMarksObject.isDefault()) {
            holder.defaultMark.setVisibility(View.VISIBLE);
            holder.deleteCheckBox.setVisibility(View.GONE);
        }else {
            holder.defaultMark.setVisibility(View.GONE);
            holder.deleteCheckBox.setVisibility(View.VISIBLE);
        }

        if(bookmarkAdapterPresenter.isItemAtPositionSelected(holder.getAdapterPosition()))
            holder.deleteCheckBox.setChecked(true);
        else
            holder.deleteCheckBox.setChecked(false);

        holder.deleteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkAdapterPresenter.selectBookmarkAtPosition(holder.getAdapterPosition(), holder.deleteCheckBox.isChecked());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(DetailsFragment.BOOKMARK_OBJ,getBookmarks().get(holder.getAdapterPosition()));
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(bundle);
                bookmarkAdapterPresenter.didSelectItemAtPosition(holder.getAdapterPosition());
                if(isTab){
                    bookmarkAdapterPresenter.getActivityContext().getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.masterViewDetails, detailsFragment).commit();
                }else{
                    bookmarkAdapterPresenter.getActivityContext().getFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .add(R.id.fragmentContainer, detailsFragment).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public void updateList(ArrayList<BookMarksObject> bookmarks) {
        this.bookmarks = bookmarks;
    }

    class BookmarkViewHolder extends RecyclerView.ViewHolder{

        final TextView bookmarkTitle;
        final TextView bookmarkGeoAddress;
        final TextView weatherTitle;
        final ProgressBar bookmarkGeoProgress;
        final TextView bookmarkDegree;
        final ImageButton retry;
        final ImageView timeImage;
        final ImageView defaultMark;
        final CheckBox deleteCheckBox;

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
            deleteCheckBox = (CheckBox) itemView.findViewById(R.id.deleteCheckBox);
        }
    }

    @Override
    public ArrayList<BookMarksObject> getBookmarks() {
        return bookmarks;
    }

    @Override
    public void checkFahrenhiet() {
        isFaherhiet = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BookMarksUtils.IS_Fahrenheit, false);
    }
}
