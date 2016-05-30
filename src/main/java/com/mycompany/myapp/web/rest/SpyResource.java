package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.config.Constants;
import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.MailService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.dto.ManagedUserDTO;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.net.*;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.io.BufferedReader;

@RestController
@RequestMapping("/api/spy")
public class SpyResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;


    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/sendLinks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<List<String>> sendMailLink(@RequestBody List<String> links) {

        Optional<User> user = userRepository
            .findOneByEmail("yana4friends@gmail.com");
        mailService.sendHtmlEmail(user.get(), String.join(",</br> ", links));

        return (ResponseEntity<List<String>>) links;

    }

    @RequestMapping(value = "/{word}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ManagedUserDTO>> spyOn(@PathVariable String word)
        throws URISyntaxException {

        // URL url = new URL("https://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=AngularJS&site=stackoverflow");

        String requestURL = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=" + word + "&site=stackoverflow";
        BufferedReader reader = null;
        try {

            InputStream is = new URL(requestURL).openStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
               // return sb.toString();
                String jsonText = sb.toString();
                JSONObject json = new JSONObject(jsonText);


            Optional<User> user = userRepository
                .findOneByEmail("yana4friends@gmail.com");
            mailService.sendHtmlEmail(user.get(), "Success");

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
