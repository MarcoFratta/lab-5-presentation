package it.unibo.ds.presentation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AuthRequest<T> {

    @Expose
    @SerializedName("method")
    private String method;

    @Expose
    @SerializedName("argument")
    private T argument;

    public AuthRequest() {
    }

    public AuthRequest(String method, T argument) {
        this.method = method;
        this.argument = argument;
    }

    public String getMethod() {
        return method;
    }

    public T getArgument() {
        return argument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthRequest<?> that = (AuthRequest<?>) o;
        return Objects.equals(method, that.method) && Objects.equals(argument, that.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, argument);
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "method='" + method + '\'' +
                ", argument=" + argument +
                '}';
    }
}
