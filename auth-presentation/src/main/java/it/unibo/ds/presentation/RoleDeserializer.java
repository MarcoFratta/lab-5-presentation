package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RoleDeserializer implements JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonPrimitive) {
            JsonPrimitive prim = json.getAsJsonPrimitive();
            if(prim.isString()) {
                String primAsString = prim.getAsString();
                try {
                    return Role.valueOf(primAsString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException("Invalid Role: " + e);
                }
            }
        }
        throw new JsonParseException("Invalid Role");
    }
}
