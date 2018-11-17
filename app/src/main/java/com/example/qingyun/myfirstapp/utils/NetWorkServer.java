package com.example.qingyun.myfirstapp.utils;

import java.util.Map;

public class NetWorkServer {
    private String method;
    private String url;
    private Boolean isFrom;
    private Map paramsMap;
    private String response;
    private HttpRequestor httpRequestor = new HttpRequestor();
    private NetWorkServerListener netWorkServerListener;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getFrom() {
        return isFrom;
    }

    public void setFrom(Boolean from) {
        isFrom = from;
    }

    public HttpRequestor getHttpRequestor() {
        return httpRequestor;
    }

    public void setHttpRequestor(HttpRequestor httpRequestor) {
        this.httpRequestor = httpRequestor;
    }

    public Map getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map paramsMap) {
        this.paramsMap = paramsMap;
    }

    public NetWorkServerListener getNetWorkServerListener() {
        return netWorkServerListener;
    }

    public NetWorkServer setNetWorkServerListener(NetWorkServerListener netWorkServerListener) {
        this.netWorkServerListener = netWorkServerListener;
        return this;
    }
    public void request() {
        new Thread() {
            @Override
            public void run() {
                if (method.equalsIgnoreCase("POST")) {
                    try {
                        response = httpRequestor.doPost(NetWorkServer.this.url, NetWorkServer.this.paramsMap);
                        netWorkServerListener.onSuccessed(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //
                        netWorkServerListener.onFailed(e.getMessage());
                    }
                } else {
                    try {
                        response = httpRequestor.doGet(NetWorkServer.this.url);
                        netWorkServerListener.onSuccessed(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        netWorkServerListener.onFailed(e.getMessage());
                    }
                }
            }
        }.start();

    }
    public void request(String method, String url, Map paramsMap){
        this.method = method;
        this.url = url;
        this.paramsMap = paramsMap;
        request();
    }
}
