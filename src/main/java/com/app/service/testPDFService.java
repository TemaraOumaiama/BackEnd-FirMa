package com.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class testPDFService {

    private final String apiKey = "ttest.docma@gmail.com_b46d44be41adc6b8fe72817c2dd4ec6651644e5fe072be877a11ea5126c3bd0528498d68";
    private final String baseUrl = "https://api.pdf.co/v1";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public testPDFService() {
        httpClient = HttpClients.createDefault();
        objectMapper = new ObjectMapper();
    }

    public void makeSearchablePDF(String sourceFilePath, String destinationFilePath, String pages, String password, String language) throws IOException {
        String uploadedFileUrl = uploadFile(sourceFilePath);
        if (uploadedFileUrl != null) {
            processSearchablePDF(uploadedFileUrl, destinationFilePath, pages, password, language);
        }
    }

    private String uploadFile(String filePath) throws IOException {
        String url = baseUrl + "/file/upload/get-presigned-url?contenttype=application/octet-stream&name=" + new File(filePath).getName();
        HttpGet request = new HttpGet(url);
        request.addHeader("x-api-key", apiKey);

        ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                JsonNode jsonResponse = objectMapper.readTree(inputStream);
                if (!jsonResponse.get("error").asBoolean()) {
                    String uploadUrl = jsonResponse.get("presignedUrl").asText();
                    String uploadedFileUrl = jsonResponse.get("url").asText();

                    HttpPut uploadRequest = new HttpPut(uploadUrl);
                    uploadRequest.addHeader("x-api-key", apiKey);
                    uploadRequest.addHeader("content-type", "application/octet-stream");

                    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                        File file = new File(filePath);
                        uploadRequest.setEntity(new FileEntity(file, ContentType.create("application/octet-stream")));

                        ClassicHttpResponse uploadResponse = (ClassicHttpResponse) httpClient.execute(uploadRequest);
                        if (uploadResponse.getCode() == 200) {
                            return uploadedFileUrl;
                        } else {
                            System.out.println("Upload request error: " + uploadResponse.getCode() + " " + uploadResponse.getReasonPhrase());
                        }
                    }
                } else {
                    System.out.println("Upload file error: " + jsonResponse.get("message").asText());
                }
            }
        } else {
            System.out.println("Upload request error: No response entity");
        }

        return null;
    }

    private void processSearchablePDF(String uploadedFileUrl, String destinationFilePath, String pages, String password, String language) throws IOException {
        String url = baseUrl + "/pdf/makesearchable";
        HttpPost request = new HttpPost(url);
        request.addHeader("x-api-key", apiKey);
        request.setHeader("Content-Type", "application/json");

        JsonNode requestBody = objectMapper.createObjectNode()
                .put("name", new File(destinationFilePath).getName())
                .put("password", password)
                .put("pages", pages)
                .put("lang", language)
                .put("url", uploadedFileUrl);

        request.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

        ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                JsonNode jsonResponse = objectMapper.readTree(inputStream);
                if (!jsonResponse.get("error").asBoolean()) {
                    String resultFileUrl = jsonResponse.get("url").asText();
                    downloadResultFile(resultFileUrl, destinationFilePath);
                } else {
                    System.out.println("Make searchable PDF error: " + jsonResponse.get("message").asText());
                }
            }
        } else {
            System.out.println("Make searchable PDF request error: No response entity");
        }
    }

    private void downloadResultFile(String resultFileUrl, String destinationFilePath) throws IOException {
        HttpGet request = new HttpGet(resultFileUrl);
        ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream inputStream = entity.getContent();
                 FileOutputStream outputStream = new FileOutputStream(destinationFilePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Result file saved as \"" + destinationFilePath + "\" file.");
            }
        } else {
            System.out.println("Download result file error: No response entity");
        }
    }
}
