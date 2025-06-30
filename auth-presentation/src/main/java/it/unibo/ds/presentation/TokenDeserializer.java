package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class TokenDeserializer implements JsonDeserializer<Token> {
    @Override
    public Token deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonObject) {
            JsonObject object = json.getAsJsonObject();
            String username = object.get("username").getAsString();
            Role role = context.deserialize(object.get("role"), Role.class);
            return new Token(username, role);
        }
        throw new JsonParseException("Expected token object");
    }
}
