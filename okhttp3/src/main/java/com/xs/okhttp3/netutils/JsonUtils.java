package com.xs.okhttp3.netutils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Json工具类
 */
public class JsonUtils {
    private static final Gson gson = new Gson();

    /**
     * 对象转Json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Json字符串转对象
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    /**
     * Json转List
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T> clz) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Json转List的第二种方式
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> List<T> js2List(String json, Class<T> clz) {
        List<T> resultList = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            resultList.add(gson.fromJson(jsonElement, clz));
        }
        return resultList;
    }

    /**
     * Json转Map
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> toMap(String json, Class<T> clz) {
        Type type = new TypeToken<Map<String, T>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * json转Map类型的List
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> toMapList(String json, Class<T> clz) {
        Type type = new TypeToken<List<Map<String, T>>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
