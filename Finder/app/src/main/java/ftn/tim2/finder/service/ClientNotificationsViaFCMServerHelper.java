package ftn.tim2.finder.service;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientNotificationsViaFCMServerHelper {

    private static String SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static String FCM_ENDPOINT =
            "https://fcm.googleapis.com/v1/projects/finder-129c3/messages:send";

    private static Context context;

    public ClientNotificationsViaFCMServerHelper(Context context) {
        this.context = context;
    }

    public void sendNotification(String notificationTitle, String notificationBody, String receiverId, String fcmToken) {
        sendMessageToFcm(getFCMNotificationMessage(notificationTitle, notificationBody, receiverId, fcmToken));
    }

    // send message to firebase cloud messaging server using okhttp
    private void sendMessageToFcm(String jsonMessage) {
        new AccessToken().execute(jsonMessage);
    }

    private class AccessToken extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... objects) {
            String jsonMessage = objects[0];
            final MediaType mediaType = MediaType.parse("application/json");

            OkHttpClient httpClient = new OkHttpClient();
            try {
                Request request = new Request.Builder().url(FCM_ENDPOINT)
                        .addHeader("Content-Type", "application/json; UTF-8")
                        .addHeader("Authorization", "Bearer " + getAccessToken())
                        .post(RequestBody.create(mediaType, jsonMessage)).build();

                Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                        Log.d("SUCCESS", "Message has been sent to FCM server "
                            + response.body().string());
                }

            } catch (IOException e) {
                Log.d("ERROR","Error in sending message to FCM server " + e);
            }
            return null;
        }
    }

    private static String getAccessToken() throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("firebase-priv-key.json");
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(inputStream)
                .createScoped(Arrays.asList(SCOPE));
        googleCredential.refreshToken();
        String token = googleCredential.getAccessToken();
        return token;
    }

    private String getFCMNotificationMessage(String title, String msg, String receiverId, String fcmToken) {
        JsonObject jsonObj = new JsonObject();
        // client registration key is sent as token in the message to FCM server
        jsonObj.addProperty("token", fcmToken);

        JsonObject data = new JsonObject();
        data.addProperty("USER_ID", receiverId);
        jsonObj.add("data", data);

        JsonObject notification = new JsonObject();
        notification.addProperty("body", msg);
        notification.addProperty("title", title);
        //notification.addProperty("tag","msg");
        //notification.addProperty("sound","true");
        jsonObj.add("notification", notification);

        JsonObject message = new JsonObject();
        message.add("message", jsonObj);

        return message.toString();
    }

    // Firebase SDK registration key from client
    private String getClientToken() {
        //return "cFncIYMV3CU:APA91bH1tj6W3KcngMBZDjvQmYGwcOJdfy-4KBKDL2aMZz_CftdYroeLqAtIDoBfjvnmo_kTpEn6k0ylBLhsEVYjC8YdYy8vRy4dA0GPh8-qkPWkTXjsDCbjJ9E7NuoW4veKFhAGTCiN";
        return "c9fVPD3KrnY:APA91bG679StuPuWkYEPyuhVKxpvTwgrYvGJviBa4IPlB_n0z6RJf5jSgiIWVjrcn09VLkwhpiXKNplK6mVKBc3fkTPS6PcFXPqnbndQZ81md6QhPWTZh2n5cV3gA-krOVSQPYolhrc3";
    }

}
