package it.unibo.ds.presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

public class GsonUtils {
    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(Request.class, new RequestDeserializer())
                .registerTypeAdapter(User.class, new UserDeserializer())
                .registerTypeAdapter(User.class, new UserSerializer())
                .registerTypeAdapter(Token.class, new TokenSerializer())
                .registerTypeAdapter(Token.class, new TokenDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(Role.class, new RoleSerializer())
                .registerTypeAdapter(Role.class, new RoleDeserializer())
                .registerTypeAdapter(Credentials.class, new CredentialsSerializer())
                .registerTypeAdapter(Credentials.class, new CredentialsDeserializer())
                .registerTypeAdapter(EmptyResponse.class, new EmptyResponseSerializer())
                .registerTypeAdapter(AuthorizeResponse.class, new AuthorizeResponseSerializer())
                .registerTypeAdapter(Response.class, new ResponseDeserializer())
                .registerTypeAdapter(Void.class, new VoidTypeAdapter())
                .create();
    }
}
