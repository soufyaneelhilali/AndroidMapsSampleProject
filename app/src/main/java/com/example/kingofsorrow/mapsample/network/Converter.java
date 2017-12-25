package com.example.kingofsorrow.mapsample.network;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

/**
 * Created by kingofsorrow on 25/04/2017.
 */
public class Converter<T> implements JsonDeserializer<T> {

    private String[] mTagToRemove;

    public Converter() {
        mTagToRemove = null;
    }

    public Converter(String... tags) {
        mTagToRemove = tags;
    }

    /**
     * Concept: remove all unneeded attributes whether objects or titles from the response json and accessing data directly
     * response: {
     * object1:{
     * object2:{
     * data
     * }
     * }
     * }
     **/


    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        try {
            if (!ArrayUtils.isEmpty(mTagToRemove)) {
                for (String tag : mTagToRemove) {
                    if (!StringUtils.isEmpty(tag)) {
                        JsonElement element = json.getAsJsonObject().get(tag);
                        if (element != null) {
                            json = element;
                        }
                    }
                }
            }
            return gson.fromJson(json, typeOfT);
        } catch (IllegalStateException e) {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            return gson.fromJson(json, typeOfT);
        }
    }
}
