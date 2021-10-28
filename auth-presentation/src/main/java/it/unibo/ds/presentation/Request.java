package it.unibo.ds.presentation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Request<T> {

    @Expose
    @SerializedName("method")
    private String method;

    @Expose
    @SerializedName("argument")
    private T argument;

    public Request() {
    }

    public Request(String method, T argument) {
        this.method = method;
        this.argument = argument;
    }
}
