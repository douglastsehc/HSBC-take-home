package org.hsbc;

import io.muserver.*;
import io.muserver.rest.RestHandlerBuilder;
import org.hsbc.controller.BookingApi;

public class Server {
    public static int port = 9090;

    public static void main(String[] args) {
        BookingApi bookingApi = new BookingApi();
        MuServer server = MuServerBuilder.httpServer().withHttpPort(port)
                .addHandler(RestHandlerBuilder.restHandler(bookingApi))
                .start();
        System.out.println("Server started at: " + server.uri());
    }
}
