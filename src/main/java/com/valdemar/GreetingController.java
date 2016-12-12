package com.valdemar;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.valdemar.model.Capabilities;
import com.valdemar.model.Installable;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.apache.http.auth.Credentials;

@RestController
public class GreetingController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(path= "/", method = RequestMethod.GET, produces = "application/json")
    public String greeting() throws FileNotFoundException {

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("atlassian-connect.json").getFile());

        FileReader fileReader = new FileReader(file);

        Template tmpl = Mustache.compiler().compile(fileReader);
        Map<String, String> data = new HashMap<String, String>();
        data.put("localBaseUrl", "https://e80d5ba2.ngrok.io");
        return tmpl.execute(data);
    }

    @RequestMapping(path="/installable", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void installable(@RequestBody Installable installableBody) {


        //get TokenUrl
        String xxx = restTemplate.getForObject(installableBody.getCapabilitiesUrl(), String.class);

        JsonParser parser = new JsonParser();
        JsonObject top = parser.parse(xxx).getAsJsonObject();
        String tokenUrl = top.getAsJsonObject("capabilities").getAsJsonObject("oauth2Provider").get("tokenUrl").getAsString();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "client_credentials");
        map.add("scope", "send_notification");


            String plainCreds = installableBody.getOauthId() + ":" + installableBody.getOauthSecret();
            byte[] plainCredsBytes = plainCreds.getBytes();
            byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
            String base64Creds = new String(base64CredsBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Creds);

            // Note the body object as first parameter!
            HttpEntity<?> httpEntity = new HttpEntity<Object>(map, headers);

            ResponseEntity<String> model = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, String.class);

        return;

    }

    @RequestMapping(path= "/config", method = RequestMethod.GET)
    public String configure(@RequestHeader HttpHeaders headers) throws FileNotFoundException {


        ClassLoader classLoader = getClass().getClassLoader();
        File config = new File(classLoader.getResource("views/config.hbs").getFile());
        File layout = new File(classLoader.getResource("views/layout.hbs").getFile());

        FileReader configReader = new FileReader(config);
        FileReader layoutReader = new FileReader(layout);


        Template tmpl = Mustache.compiler().compile(configReader);
        Map<String, String> data = new HashMap<String, String>();
        String body = tmpl.execute(data);

        tmpl = Mustache.compiler().escapeHTML(false).compile(layoutReader);
        data.put("title", "Does It Works??");
        data.put("body", body);
        String page = tmpl.execute(data);

        /*
        final File templateDir = config.getParentFile();
        Mustache.Compiler c = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            public Reader getTemplate (String name) throws FileNotFoundException {
                return new FileReader(new File(templateDir, name));
            }
        });

        Map<String, String> data = new HashMap<String, String>();
        data.put("title", "it works?!?!");

        String text = c.compile(fileReader).execute(data);

*/





        System.out.println(page);
       return page;
    }


}