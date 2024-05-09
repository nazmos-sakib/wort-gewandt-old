package com.example.wortgewant.Utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadMP3Task {
    public byte[] downloadMP3(String mp3Url) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(mp3Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Check if the connection was successful
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                return readBytesFromStream(inputStream);
            } else {
                throw new IOException("Failed to download MP3. HTTP response code: " + responseCode);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private byte[] readBytesFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, bytesRead);
        }
        return byteBuffer.toByteArray();
    }
}