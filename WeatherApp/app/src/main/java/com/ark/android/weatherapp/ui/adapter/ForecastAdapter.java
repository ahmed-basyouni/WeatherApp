package com.ark.android.weatherapp.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ark.android.weatherapp.R;
import com.ark.android.weatherapp.data.model.ForcastObj;
import com.ark.android.weatherapp.data.model.WeatherObj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * Created by Ark on 6/25/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>{

    private final Context context;
    private final ForcastObj forecastObj;
    private SimpleDateFormat simpleDateFormat;

    public ForecastAdapter(Context context, ForcastObj forcastObj){
        this.context = context;
        this.forecastObj = forcastObj;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        WeatherObj weatherObj = forecastObj.getWeatherObjs().get(holder.getAdapterPosition());
        holder.weatherDescription.setText(weatherObj.getWeather().get(0).getDescription());
        try {
            final Date date = simpleDateFormat.parse(weatherObj.getDateText());
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            holder.date.setText(calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH));
            holder.time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.date.setText(context.getString(R.string.NA));
            holder.time.setText(context.getString(R.string.NA));
        }

        holder.weatherInfo.setCompoundDrawablesWithIntrinsicBounds(getDrawableId(weatherObj.getWeather().get(0).getIcon()), 0, 0, 0);
        holder.weatherInfo.setText(String.format(context.getString(R.string.minMax), String.valueOf(weatherObj.getWeatherInfoObject().getTempMax()), String.valueOf(weatherObj.getWeatherInfoObject().getTempMin())));

    }

    private int getDrawableId(String name) {
        Resources resources = context.getResources();
        return resources.getIdentifier("weather"+name, "drawable",
                context.getPackageName());
    }

    @Override
    public int getItemCount() {
        return forecastObj.getWeatherObjs().size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView time;
        TextView weatherInfo;
        TextView weatherDescription;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            weatherInfo = (TextView) itemView.findViewById(R.id.maxMinTemp);
            weatherDescription = (TextView) itemView.findViewById(R.id.weatherDescription);
        }
    }
}
