package com.example.demo;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class UserService {

    String url = "https://jsonplaceholder.typicode.com";
    public UserService(String uri) {
        this.url = uri;
    }

    public List<User> getAll() throws UnirestException {
        ArrayList<User> returnUsers = new ArrayList<User>();

        HttpRequest getRequest = Unirest.get(url + "/users");

        HttpResponse<JsonNode> jsonNodeHttpResponse = getRequest.asJson();
        Optional<JsonNode> data = null;

        if (jsonNodeHttpResponse.getStatus() == 200) {
            data = Optional.of(jsonNodeHttpResponse.getBody());
        } else {
            data = Optional.empty();
        }

        if (data != null && data.isPresent()) {
            JSONArray jsonArray = data.get().getArray();
            for (Iterator<Object> it = jsonArray.iterator(); it.hasNext(); ) {
                JSONObject jsonObject = (JSONObject) it.next();
                returnUsers.add(new User());
            }
        }
        return returnUsers;
    }
}
