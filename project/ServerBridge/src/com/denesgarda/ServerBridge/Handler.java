package com.denesgarda.ServerBridge;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String fileContent = "Failed to load file. Check the console for details.";
        try {
            fileContent = Files.readString(Path.of(Config.path), StandardCharsets.US_ASCII);
            fileContent = "Viewing " + Config.path + " on " + InetAddress.getLocalHost().getHostAddress() + ": \n\n" + fileContent;
            fileContent = fileContent.replaceAll("\n", "<br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String head;
        if (Config.refresh == 0) {
            head = "<head></head>";
        } else {
            head = "<head><meta http-equiv=\"refresh\" content=\"" + Config.refresh + "\"></head>";
        }
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                head +
                "    <body>" + fileContent + "</body>\n" +
                "</html>";
        exchange.sendResponseHeaders(200, content.length());
        OutputStream os = exchange.getResponseBody();
        os.write(content.getBytes());
        os.close();
    }
}
