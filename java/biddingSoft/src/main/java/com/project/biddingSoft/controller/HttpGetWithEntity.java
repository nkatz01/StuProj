package com.project.biddingSoft.controller;

import java.net.URI;

import org.apache.http.client.methods.HttpPost;

// this is just used for tests, should be in test folder. Also doesn't need to be a class, should just initialise httppost directly
//  HttpPost httpPost = new HttpPost("http://www.example.com");


public class HttpGetWithEntity extends HttpPost  {
    public final static String METHOD_NAME = "GET";
    
    public HttpGetWithEntity(URI url) {
        super(url);
    }

    public HttpGetWithEntity(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}
