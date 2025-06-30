package it.unibo.ds.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) return null;
        JsonObject object = new JsonObject();
        object.addProperty("year", src.getYear());
        object.addProperty("month", src.getMonthValue());
        object.addProperty("day", src.getDayOfMonth());
        return object;
    }
}
