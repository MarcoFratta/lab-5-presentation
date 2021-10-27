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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request<?> that = (Request<?>) o;
        return Objects.equals(method, that.method) && Objects.equals(argument, that.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, argument);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", argument=" + argument +
                '}';
    }
}
