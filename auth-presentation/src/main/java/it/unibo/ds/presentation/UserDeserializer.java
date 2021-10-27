package it.unibo.ds.presentation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDeserializer implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();
            var fullName = object.has("full_name") ? object.get("full_name").getAsString() : null;
            var username = object.has("username") ? object.get("username").getAsString() : null;
            var password = object.has("password") ? object.get("password").getAsString() : null;
            Role role = object.has("role") ? context.deserialize(object.get("role"), Role.class) : null;
            LocalDate birthDate = object.has("birth_date") ? context.deserialize(object.get("birth_date"), LocalDate.class) : null;
            var emailsArray = object.getAsJsonArray("email_addresses");
            List<String> emails = new ArrayList<>(emailsArray.size());
            for (var item : emailsArray) {
                emails.add(item.getAsString());
            }
            return new User(fullName, username, password, birthDate, role, emails);
        } catch (ClassCastException e) {
            throw new JsonParseException("Invalid user: " + json, e);
        }
    }
}
