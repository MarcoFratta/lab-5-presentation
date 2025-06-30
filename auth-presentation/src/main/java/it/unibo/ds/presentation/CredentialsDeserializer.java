package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CredentialsDeserializer implements JsonDeserializer<Credentials> {
    @Override
    public Credentials deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonObject) {
            JsonObject object = json.getAsJsonObject();
            String userId = object.get("userId").getAsString();
            String password = object.get("password").getAsString();
            return new Credentials(userId, password);
        }
        throw new JsonParseException("Expected credentials object");
    }
}
