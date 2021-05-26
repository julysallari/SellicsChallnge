package com.sellicschallenge.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiWebClient {

    private WebClientConfiguration webClientConfiguration = new WebClientConfiguration();

    public List<String> apiCall(String request) {
        String response =  webClientConfiguration.webClientWithTimeout()
                .get()
                .uri(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return parseResponse(response);

    }

    private List<String> parseResponse(String response) {
        JsonArray jsonArray = new Gson().fromJson(response, JsonArray.class);
        String[] resultList = jsonArray.get(1).toString().replace("[", "").replace("]", "").split(",");
        int fails = jsonArray.get(2).getAsJsonArray().get(0).getAsJsonObject().size();
        if (fails > 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(resultList).map(word -> word.replace("\"", "")).collect(Collectors.toList());
    }

}
