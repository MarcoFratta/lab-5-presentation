package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("username", src.getUsername());
        object.addProperty("full_name", src.getFullName());
        object.addProperty("password", src.getPassword());
        JsonArray emails = new JsonArray();
        for (String email : src.getEmailAddresses()) {
            emails.add(new JsonPrimitive(email));
        }
        object.add("email_addresses", emails);
        object.add("role", context.serialize(src.getRole()));
        object.add("birth_date", context.serialize(src.getBirthDate()));
        return object;
    }
}
