package it.unibo.ds.presentation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class AuthRequest {

    @Expose
    @SerializedName("method")
    private String method;

    @Expose
    @SerializedName("args")
    private List<Object> args;

    public AuthRequest() {
    }

    public AuthRequest(String method, List<Object> args) {
        this.method = method;
        this.args = args;
    }

    public AuthRequest(String method, Object... args) {
        this(method, Arrays.asList(args));
    }


    public String getMethod() {
        return method;
    }

    public List<Object> getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthRequest request = (AuthRequest) o;
        return Objects.equals(method, request.method) && Objects.equals(args, request.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, args);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", args=" + args +
                '}';
    }
}
