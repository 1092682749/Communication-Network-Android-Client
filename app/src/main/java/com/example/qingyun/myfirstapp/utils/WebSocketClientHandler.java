package com.example.qingyun.myfirstapp.utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebSocketClientHandler extends WebSocketClient  {
    static WebSocketClientHandler webSocketClientHandler = null;

    public WebSocketClientHandler(URI serverUri) {
        super(serverUri);
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            sslContext.init(new KeyManager[]{}, new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {

                        }


                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLSocketFactory factory = sslContext.getSocketFactory();

        try {
            webSocketClientHandler.setSocket(factory.createSocket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WebSocketClientHandler(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public WebSocketClientHandler(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    public WebSocketClientHandler(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
        super(serverUri, protocolDraft, httpHeaders);
    }

    public WebSocketClientHandler(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    public static WebSocketClientHandler getInstance() throws URISyntaxException {
        if (webSocketClientHandler == null){
            webSocketClientHandler = new WebSocketClientHandler(new URI("wss://dyzhello.club:8080"));
        }else {

        }
        return webSocketClientHandler;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("open");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("close");
    }

    @Override
    public void onError(Exception ex) {

    }
}
