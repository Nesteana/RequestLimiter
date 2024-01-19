package ru.nesteana.limiter;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CrptApi {
   private TimeUnit timeUnit;
   private int requestLimit;

   public CrptApi(TimeUnit timeUnit, int requestLimit){
       if(requestLimit <= 0){
           requestLimit = 1;
       }
       this.timeUnit = timeUnit;
       this.requestLimit = requestLimit;
   }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();

    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    @Data
    @NoArgsConstructor
    static class Document{
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
    }

    @Data
    @NoArgsConstructor
    static class Description{
        public String participantInn;
    }

    @Data
    @NoArgsConstructor
    static class Product{
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
