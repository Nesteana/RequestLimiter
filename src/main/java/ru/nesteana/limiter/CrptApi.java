package ru.nesteana.limiter;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CrptApi {

    public static ObjectMapper objectMapper = new ObjectMapper();
    public static List<Document> documentList = new ArrayList<>();

    private TimeUnit timeUnit;
    private int requestLimit;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        if (requestLimit <= 0) {
            requestLimit = 1;
        }
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();

    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equals(t.getRequestMethod()) && "/api/v3/lk/documents/create".equals(t.getRequestURI().getPath())) {
                String body = new String(t.getRequestBody().readAllBytes());
                Document document = objectMapper.readValue(body, Document.class);
                createDocument(document);

                String response = "Документ сохранён";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                t.sendResponseHeaders(404, 0);
                t.close();
            }
        }
    }

    public static void createDocument(Document document){
        documentList.add(document);
    }

    @Data
    @NoArgsConstructor
    static class Document {
        public Description description;

        public String doc_id;
        public String doc_status;
        public String doc_type;
        public Boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;
        public String production_date;
        public String production_type;

        List<Product> products;

        public String reg_date;
        public String reg_number;
    }

    @Data
    @NoArgsConstructor
    static class Description {
        public String participantInn;
    }

    @Data
    @NoArgsConstructor
    static class Product {
        public String certificate_document;
        public String certificate_document_date;
        public String certificate_document_number;
        public String owner_inn;
        public String producer_inn;
        public String production_date;
        public String tnved_code;
        public String uit_code;
        public String uitu_code;
    }
}
