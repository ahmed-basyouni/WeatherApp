package com.ark.android.weatherapp.data.parsing;

import com.ark.android.weatherapp.data.model.BaseModel;
import com.google.gson.Gson;

/**
 *
 * Created by Ark on 6/24/2017.
 */

public class ParsingManager {

    private static ParsingManager instance;

    private ParsingManager(){
    }

    public static ParsingManager getInstance(){
        if(instance == null)
            instance = new ParsingManager();
        return instance;
    }

    public <T extends BaseModel> T parseModel(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }
}
