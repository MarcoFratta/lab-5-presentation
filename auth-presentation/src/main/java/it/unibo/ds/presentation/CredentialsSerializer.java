package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CredentialsSerializer implements JsonSerializer<Credentials> {

    @Override
    public JsonElement serialize(Credentials src, Type typeOfSrc, JsonSerializationContext context) {
        throw new Error("not implemented");
    }
}
