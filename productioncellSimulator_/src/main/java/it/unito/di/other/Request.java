package it.unito.di.other;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    private String request;




    public Request(String request) {
        this.request = request;
    }





    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request1 = (Request) o;
        return Objects.equals(request, request1.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request);
    }

    @Override
    public String toString() {
        return "Request{" +
                "request='" + request + '\'' +
                '}';
    }
}
