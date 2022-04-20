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

        try {
            Socket socket = new Socket(url.getHost(), 80);
            InputStream in = socket.getInputStream();
            String request = sCurlParm.getMethod() + getMethod2(sCurlParm.getMethod()) + "\n"
                + "Host: " + url.getHost() + "\n"
                +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36 \n"
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

            if (sCurlParm.isVerbose()) {
                System.out.println(request);
                System.out.println(message2[0]);
            }

            System.out.println(message2[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMethod2(String method) {
        if (method.equals("GET")) {
            return " /get HTTP/1.1 ";
        }
        return " /post HTTP/1.1 ";
    }
}