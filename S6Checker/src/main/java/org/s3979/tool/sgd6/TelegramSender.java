package org.s3979.tool.sgd6;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TelegramSender {

    private static final String BOT_TOKEN = "7606970085:AAFoaC1dnRM5HD5Ps86hQ6jw9y9ktpLIYVM";
    private static final String CHAT_ID = "-4524303413";
    private static final String CHAT_ID_TEST = "-4537960588";

    private static String chatId = "";

    static {
        chatId = CHAT_ID_TEST;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            chatId = CHAT_ID;
        }
    }

    public static void sendMessage(String message) {
        try {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage").newBuilder();
            urlBuilder.addQueryParameter("chat_id", chatId);
            urlBuilder.addQueryParameter("text", message);
            urlBuilder.addQueryParameter("parse_mode", "Markdown");
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