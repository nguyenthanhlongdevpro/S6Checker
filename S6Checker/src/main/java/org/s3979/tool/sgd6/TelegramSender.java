package org.s3979.tool.sgd6;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TelegramSender {

    private static final String BOT_TOKEN = "7521455664:AAF3o4aO_LiwlAfREc17gKFI-Onv5C6jMtA";
    private static final String CHAT_ID = "-4591433928";

    public static void sendMessage(String message) {
        try {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage").newBuilder();
            urlBuilder.addQueryParameter("chat_id", CHAT_ID);
            urlBuilder.addQueryParameter("text", message);
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}