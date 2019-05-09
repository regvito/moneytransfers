package com.reggiev.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.reggiev.interview.rest.RestEndpoints;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

/**
 * Main entry point to the money transfer REST service.
 */
public class App
{

    public static void main(String[] args) {
        int port = 7000;
        if (args.length == 1)
            port = Integer.parseInt(args[0]);
        Javalin app = Javalin.create().start(port);
        new RestEndpoints().configure(app);

        // enable pretty print when returning json output
        ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        JavalinJackson.configure(om);

    }

}
