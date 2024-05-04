package com.stepanew;

import com.stepanew.handlers.RequestHandler;

public class Application {

    public static void main(String[] args) {
        RequestHandler requestHandler = new RequestHandler(args);
        requestHandler.handleRequest();
    }

}
