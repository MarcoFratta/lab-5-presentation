package it.unibo.ds.presentation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class RequestDeserializer implements JsonDeserializer<Request<?>> {
    @Override
    public Request<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();
            var method = object.getAsJsonPrimitive("method").getAsString();
            switch (method) {
                case "register":
                    return context.deserialize(json, RegisterRequest.class);
                case "authorize":
                    return context.deserialize(json, AuthorizeRequest.class);
                default:
                    throw new JsonParseException("Invalid request: " + json);
            }
        } catch (NullPointerException | ClassCastException e) {
            throw new JsonParseException("Invalid request: " + json);
        }
    }
}
