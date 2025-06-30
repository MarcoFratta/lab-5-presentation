package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RequestDeserializer implements JsonDeserializer<Request<?>> {
    @Override
    public Request<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonObject) {
            JsonObject object = json.getAsJsonObject();
            String method = object.get("method").getAsString();
            JsonElement argument = object.get("argument");
            
            switch (method) {
                case "register":
                    User user = context.deserialize(argument, User.class);
                    return new RegisterRequest(user);
                case "authorize":
                    Credentials credentials = context.deserialize(argument, Credentials.class);
                    return new AuthorizeRequest(credentials);
                default:
                    throw new JsonParseException("Unknown request method: " + method);
            }
        }
        throw new JsonParseException("Expected request object");
    }
}
