package it.unibo.ds.presentation;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSerializeAndDeserialize {

    protected final User giovanniUser = new User(
            "Giovanni Ciatto",
            "gciatto",
            "password.",
            LocalDate.of(1992, Month.JANUARY, 1),
            Role.USER,
            "giovanni.ciatto@unibo.it",
            "giovanni.ciatto@studio.unibo.it"
    );

    private final Credentials giovanniCredentials = new Credentials(
            giovanniUser.getUsername(),
            giovanniUser.getPassword()
    );

    private final Token giovanniToken = new Token(
            giovanniUser.getUsername(),
            giovanniUser.getRole()
    );

    private final RegisterRequest registerGiovanniRequest = new RegisterRequest(
            giovanniUser
    );

    private final EmptyResponse registerGiovanniResponse = new EmptyResponse(
            Response.Status.OK,
            "ok"
    );

    private final AuthorizeRequest authorizeGiovanniRequest = new AuthorizeRequest(
            giovanniCredentials
    );


    private final AuthorizeResponse authorizeGiovanniResponse = new AuthorizeResponse(
            Response.Status.OK,
            "ok",
            giovanniToken
    );

    private Gson gson;

    @BeforeEach
    public void setup() {
        gson = GsonUtils.createGson();
    }

    @Test
    public void testUser() {
        String serialized = gson.toJson(giovanniUser);
        User deserialized = gson.fromJson(serialized, User.class);
        assertEquals(giovanniUser, deserialized);
    }

    @Test
    public void testCredentials() {
        String serialized = gson.toJson(giovanniCredentials);
        Credentials deserialized = gson.fromJson(serialized, Credentials.class);
        assertEquals(giovanniCredentials, deserialized);
    }

    @Test
    public void testToken() {
        String serialized = gson.toJson(giovanniToken);
        Token deserialized = gson.fromJson(serialized, Token.class);
        assertEquals(giovanniToken, deserialized);
    }

    @Test
    public void testRegisterRequest() {
        String serialized = gson.toJson(registerGiovanniRequest);
        Request<User> deserialized = gson.fromJson(serialized, RegisterRequest.class);
        assertEquals(registerGiovanniRequest, deserialized);
    }

    @Test
    public void testRegisterResponse() {
        String serialized = gson.toJson(registerGiovanniResponse);
        Response<Void> deserialized = gson.fromJson(serialized, EmptyResponse.class);
        assertEquals(registerGiovanniResponse, deserialized);
    }

    @Test
    public void testAuthorizeRequest() {
        String serialized = gson.toJson(authorizeGiovanniRequest);
        Request<Credentials> deserialized = gson.fromJson(serialized, AuthorizeRequest.class);
        assertEquals(authorizeGiovanniRequest, deserialized);
    }

    @Test
    public void testAuthorizeResponse() {
        String serialized = gson.toJson(authorizeGiovanniResponse);
        Response<Token> deserialized = gson.fromJson(serialized, AuthorizeResponse.class);
        assertEquals(authorizeGiovanniResponse, deserialized);
    }

}
