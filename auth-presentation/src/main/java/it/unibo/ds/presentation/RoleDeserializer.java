package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RoleDeserializer implements JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Role.valueOf(json.getAsString().toUpperCase());
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new JsonParseException("Invalid role: " + json, e);
        }
    }
}
