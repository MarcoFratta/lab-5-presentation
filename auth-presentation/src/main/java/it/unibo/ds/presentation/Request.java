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
    public String getMethod() {
        return method;
    }

    public T getArgument() {
        return argument;
    }
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Request)) return false;
        Request<?> request = (Request<?>) o;
        return Objects.equals(this.method, request.method) && Objects.equals(this.argument, request.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.method, this.argument);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", argument=" + argument +
                '}';
    }
}
