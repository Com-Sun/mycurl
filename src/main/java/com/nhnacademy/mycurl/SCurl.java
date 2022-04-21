package com.nhnacademy.mycurl;

import com.beust.jcommander.JCommander;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class SCurl {
    public static void main(String[] args) throws MalformedURLException {

        SCurlParm sCurlParm = new SCurlParm();
        JCommander.newBuilder()
            .addObject(sCurlParm)
            .build()
            .parse(args);

        String stringUrl;
        stringUrl = sCurlParm.getParameters().get(0);
        if (sCurlParm.isMethod()) {
            stringUrl = sCurlParm.getParameters().get(1);
        }
        if (sCurlParm.isHeader()) {
            stringUrl = sCurlParm.getParameters().get(1);
        }
        if (sCurlParm.isData()) {
            stringUrl = sCurlParm.getParameters().get(sCurlParm.getCount() + 3);
        }

        URL url = new URL(stringUrl);
        String[] afterHost = stringUrl.split(url.getHost());
        try {
            Socket socket = new Socket(url.getHost(), 80);
            InputStream in = socket.getInputStream();
            String request = sCurlParm.getMethod()
                + " " + afterHost[1] + " HTTP/1.1 \n"
                + "Host: " + url.getHost() + "\n"
                + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36 \n"
                + "Content-Type: application/json \n"
                + "Accept: */* \n";
            if (sCurlParm.isHeader()) {
                request += sCurlParm.getParameters().get(0) + "\n";
            }
            if (sCurlParm.isData()) {
                int length = sCurlParm.getParameters().get(1).length();
                System.out.println("length: " + length);
                request += "Content-Length: " + length + "\n";
                request += "\n" +
                    sCurlParm.getParameters().get(1) + "\n"
                    + "\n";
            }

            PrintStream out = new PrintStream(socket.getOutputStream()); // 개행을 기준으로 한줄씩 던짐
            out.println(request);

            byte[] byteArr = new byte[2048];
            int readByteCount = in.read(byteArr);
            String message = new String(byteArr, 0, readByteCount, "UTF-8");
            String[] message2 = message.split("\r\n\r\n");

            // 6번 스펙
            if (message2[0].contains("301") || message2[0].contains("302") ||
                message2[0].contains("307") || message2[0].contains("308")) {
                String[] locationTemp = message2[0].split("\n");
                String location = null;
                for (String s : locationTemp) {
                    if (s.contains("location")) {
                        location = s;
                    }
                }
                String[] locationArr = location.split(":");
                String newRequest = getNewRequest(url, locationArr);
                out.println(newRequest);
                for (int i = 0; i < 3; i ++) {
                    readByteCount = in.read(byteArr);
                    message = new String(byteArr, 0, readByteCount, "UTF-8");
                    message2 = message.split("\r\n\r\n");
                    if (message2[0].contains("301") || message2[0].contains("302") ||
                        message2[0].contains("307") || message2[0].contains("308")) {
                        out.println(newRequest);
                    } else {
                        break;
                    }
                }
            }

            if (sCurlParm.isVerbose()) {
                System.out.println(request);
                System.out.println(message2[0]);
            }
            if (sCurlParm.isLocation()) {
            }
            System.out.println(message2[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getNewRequest(URL url, String[] locationArr) {
        return "GET"
            + locationArr[1].replace("\r", "") + " HTTP/1.1 \n"
            + "Host: " + url.getHost() + "\n"
            +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36 \n"
            + "Content-Type: application/json \n"
            + "Accept: */* \n";
    }

}