package ru.otus.model;

import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ObjectForMessage{ data={");
        data.forEach(item -> builder.append(item).append(", "));
        builder.append("}}");
        return builder.toString();
    }

}
