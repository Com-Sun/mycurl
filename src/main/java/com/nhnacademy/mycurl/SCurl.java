package com.nhnacademy.mycurl;

import com.beust.jcommander.JCommander;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class SCurl {
    public static void main(String[] args) throws MalformedURLException {
//$ scurl http://httpbin.org/get
        // 첫번째 인자로 scurl이 들어오고, 두번째로 url이 들어옴.

        SCurlParm sCurlParm = new SCurlParm();
        JCommander.newBuilder()
            .addObject(sCurlParm)
            .build()
            .parse(args);

        String stringUrl = sCurlParm.getParameters().get(0);

        URL url = new URL(stringUrl);

        try {
            url = new URL("http://httpbin.org/get");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        /*
        만약 url이 testvm.com 이면 포트 10000
        scheme = "http" - 포트를 의미하기도 함. 즉 TCP레이어가 될 수 도 있음.
        host: httpbin.org (=서버)
        port: 80
        path = "/ip"
         */

        System.out.println(url.getHost());

        try {
            Socket socket = new Socket(url.getHost(), 80);
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

//            String data =  new String(byteArr);
//            String request;
//            dis.write(request);
//            GET / ip / HTTP/1.1
//            System.out.println(dis.readUTF());

            String request = "GET /get HTTP/1.1 \n"
                + "Host: httpbin.org \n"
                + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36 \n"
                + "Content-Type:  \"text/*"
                + "Accept: */* \n";

            //request 메시지 만들기
            //out.println(request);

            PrintStream out = new PrintStream(socket.getOutputStream()); // 개행을 기준으로 한줄씩 던짐
            out.println(request);

            byte[] byteArr = new byte[2048];
            int readByteCount = in.read(byteArr);
            String message = new String(byteArr, 0, readByteCount, "UTF-8");
            String[] message2 = message.split("\r\n\r\n");
            System.out.println(message2[1]);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}