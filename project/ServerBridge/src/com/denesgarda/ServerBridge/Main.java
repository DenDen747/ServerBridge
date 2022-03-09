package com.denesgarda.ServerBridge;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        System.out.println("Loading config...");
        File file = new File("config.properties");
        if (!file.exists()) {
            System.out.println("Config file not found. Generating new one...");
            try {
                boolean successful = file.createNewFile();
                if (!successful) {
                    throw new Exception();
                }
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("# ServerBridge config file\n\npath=\nport=\nrefresh=");
                bufferedWriter.close();
                System.out.println("Successfully generated new config file. Please modify the file, then relaunch.");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Failed to generate config file.");
                System.exit(-1);
            }
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            System.out.println("Failed to load config.");
            System.exit(-1);
        }
        System.out.println("Successfully loaded config.");
        System.out.println("Processing config...");
        try {
            Config.path = (String) properties.get("path");
            Config.port = Integer.parseInt((String) properties.get("port"));
            Config.refresh = Integer.parseInt((String) properties.get("refresh"));
        } catch (Exception e) {
            System.out.println("Incorrect config format.");
            System.exit(-1);
        }
        System.out.println("Successfully processed config.");
        System.out.println("Loading...");
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(Config.port), 0);
            server.createContext("/", new Handler());
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            System.out.println("Failed to load");
            System.exit(-1);
        }
        System.out.println("Serving \"" + Config.path + "\" on port " + Config.port + ".");
    }
}
