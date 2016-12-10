package com.valdemar;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(path= "/", method = RequestMethod.GET, produces = "application/json")
    public String greeting() throws FileNotFoundException {

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("atlassian-connect.json").getFile());

        FileReader fileReader = new FileReader(file);

        Template tmpl = Mustache.compiler().compile(fileReader);
        Map<String, String> data = new HashMap<String, String>();
        data.put("localBaseUrl", "https://d32789d5.ngrok.io");
        System.out.println(tmpl.execute(data));


        return tmpl.execute(data);
    }

    @RequestMapping(path="/installable", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void installable(@RequestBody String body) {


        return;

    }


}