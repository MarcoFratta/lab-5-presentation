package it.unibo.ds.presentation;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDeserializer implements JsonDeserializer<User> {

    private String getPropertyAsString(JsonObject object, String name) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return value.getAsString();
        }
        return null;
    }

    private <T> T getPropertyAs(JsonObject object, String name, Class<T> type, JsonDeserializationContext context) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return context.deserialize(value, type);
        }
        return null;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonObject) {
            JsonObject object = json.getAsJsonObject();
            String fullName = getPropertyAsString(object, "full_name");
            String username = getPropertyAsString(object, "username");
            String password = getPropertyAsString(object, "password");
            
            List<String> emailAddresses = new ArrayList<>();
            if (object.has("email_addresses")) {
                JsonArray emails = object.getAsJsonArray("email_addresses");
                for (JsonElement email : emails) {
                    emailAddresses.add(email.getAsString());
                }
            }
            
            Role role = getPropertyAs(object, "role", Role.class, context);
            LocalDate birthDate = getPropertyAs(object, "birth_date", LocalDate.class, context);
            
            return new User(fullName, username, password, birthDate, role, emailAddresses);
        }
        throw new JsonParseException("Expected user object");
    }
}
