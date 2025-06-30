package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CredentialsSerializer implements JsonSerializer<Credentials> {

    @Override
    public JsonElement serialize(Credentials src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", src.getUserId());
        object.addProperty("password", src.getPassword());
        return object;
    }
}
