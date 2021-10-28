package it.unibo.ds.presentation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Response<T> {
    public enum Status {
        OK, BAD_CONTENT, CONFLICT, WRONG_CREDENTIALS;
    }

    @Expose
    @SerializedName("status")
    private Status status;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("result")
    private T result;

    public Response() {
    }

    public Response(Status status, String message) {
        this(status, message, null);
    }

    public Response(Status status, String message, T result) {
        throw new Error("not implemented");
    }
}
