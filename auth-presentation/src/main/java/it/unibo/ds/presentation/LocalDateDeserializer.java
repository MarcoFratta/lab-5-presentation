package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) return null;
        
        if (json instanceof JsonObject) {
            JsonObject object = json.getAsJsonObject();
            try {
                int year = object.get("year").getAsInt();
                int month = object.get("month").getAsInt();
                int day = object.get("day").getAsInt();
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                throw new JsonParseException("Invalid date format", e);
            }
        }
        throw new JsonParseException("Expected date object");
    }
}
