package com.degsoft.appium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.degsoft.utils.LoggerUtil.*;

public class Server extends Thread {
    private Process process;
    private String address;
    private String serverPort;


    public Server(String address, String port){
        this.address = address;
        this.serverPort = port;
    }

    @Override
    public void run() {
        try {
            if (isRunning()) {
                System.out.println("Appium server is already running! Stopping it...");

                killAppium();

                while (isRunning()) ;
                printDebug("Appium server stopped.");
            }
            System.out.println("Starting Appium server...");

            try {
                String[] macCommand = new String[]{
                        "/usr/local/bin/appium",
                        "-a", address,
                        "-p", serverPort,
                        "--command-timeout", "120",
                        "--full-reset"
                };

                String[] winCommand = new String[]{
                        "\"C:\\Program Files (x86)\\Appium\\node.exe\"",
                        "\"C:\\Program Files (x86)\\Appium\\node_modules\\appium\\bin\\appium.js\"",
                        "-a", address,
                        "-p", serverPort,
                        "--command-timeout", "120",
                        "--full-reset"
                };

                String [] command;
                if(System.getProperty("os.name").toLowerCase().contains("windows")){
                    command = winCommand;
                }else{
                    command = macCommand;
                }

                process = Runtime.getRuntime().exec(command);

                BufferedReader read = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));

                String line = "";
                while ((line = read.readLine()) != null) {
                    if (line != null) {
                        System.out.println("SERVER LOG: " + line);
                    }
                }

                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    printError(e.getMessage());
                }
                while (read.ready()) {
                    printDebug(read.readLine());
                }
            } catch (IOException e) {
                printError(e.getMessage());
            }
        } catch (IOException e) {
            printError("Cannot start appium server!: " + e.getMessage());
        }
    }

    public boolean isRunning() {
        try {
            int port = Integer.parseInt(serverPort);

            new Socket(address, port).close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void killAppium() throws IOException {
        String [] command;
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            command = new String []{"taskkill", "/F", "/IM", "node.exe"};
        }else{
            command = new String []{"killall", "node"};
        }

        ProcessBuilder builder = new ProcessBuilder(command);
                Process proc = builder.start();
                BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    System.out.println(line);
                }

    }

    public void end() {
        if(process != null){
            try {
                process.destroy();
                process.waitFor();
                printInfo("Appium server stopped!");
            } catch (InterruptedException e) {
                printError("Cannot stop appium server!: " + e.getMessage());
            }
        }
    }
}
