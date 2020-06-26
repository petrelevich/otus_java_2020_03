package ru.otus.server.utils;

import org.eclipse.jetty.http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Optional;

public final class HttpUrlConnectionHelper {
    private HttpUrlConnectionHelper() {
    }

    public static HttpURLConnection sendRequest(String url, HttpMethod method) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod(method.asString());
        return connection;
    }

    public static String readResponseFromConnection(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }

    public static String buildUrl(String host, String path, List<String> pathParams) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(host);
        stringBuilder.append(path);
        Optional.ofNullable(pathParams).ifPresent(paramsMap -> paramsMap.forEach(p -> stringBuilder.append("/").append(p)));
        return stringBuilder.toString();
    }

}
