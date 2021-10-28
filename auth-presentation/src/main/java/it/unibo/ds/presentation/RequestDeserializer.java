package it.unibo.ds.presentation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class RequestDeserializer implements JsonDeserializer<Request<?>> {
    @Override
    public Request<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        throw new Error("not implemented");
    }
}
