package com.example.centeroauth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

/**
 * Package com.example.centeroauth.token
 * Module
 * Project center-oauth
 * Email 253498229@qq.com
 * Created on 2018/5/13 下午3:50
 *
 * @author wangbin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class TokenTest {
  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate template;
  private String client = "client";
  private String secret = "secret";
  private String username = "user";
  private String password = "password";
  private String BASE_URL = "http://localhost:{port}";
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void testPasswordType() throws IOException {
    Map<String, Object> map = new HashMap<>();
    map.put("port", port);
    map.put("username", username);
    map.put("password", password);
    ResponseEntity<String> response;
    response = template.withBasicAuth(client, secret)
            .postForEntity(BASE_URL + "/oauth/token" +
                            "?grant_type=password&username={username}&password={password}",
                    null, String.class, map);
    assertEquals(response.getStatusCode(), OK);

    testResult(response);
  }

  @Test
  public void testCodeTest() throws IOException {
    Map<String, Object> map = new HashMap<>();
    map.put("port", port);
    map.put("client_id", client);
    map.put("redirect_uri", "http://www.baidu.com");
    ResponseEntity<String> response;
    response = template.withBasicAuth(username, password)
            .postForEntity(BASE_URL + "/oauth/authorize" +
                            "?response_type=code&client_id={client_id}&redirect_uri={redirect_uri}"
                    , null, String.class, map);
    assertEquals(response.getStatusCode(), OK);

    String cookie = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", cookie);
    response = template.withBasicAuth(username, password)
            .postForEntity(BASE_URL + "/oauth/authorize" +
                            "?response_type=code&client_id={client_id}&redirect_uri={redirect_uri}&user_oauth_approval=true&authorize=Authorize"
                    , new HttpEntity<>(headers), String.class, map);
    assertEquals(response.getStatusCode(), FOUND);


    String query = response.getHeaders().getLocation().getQuery();
    response = template.withBasicAuth(client, secret)
            .postForEntity(BASE_URL + "/oauth/token" +
                            "?" + query + "&grant_type=authorization_code&redirect_uri={redirect_uri}"
                    , null, String.class, map);
    assertEquals(response.getStatusCode(), OK);
    assertNotNull(response.getBody());

    testResult(response);

  }

  private void testResult(ResponseEntity<String> response) throws IOException {
    Map resultMap = mapper.readValue(response.getBody(), HashMap.class);
    String token = (String) resultMap.get("access_token");
    String userInfoJson = new String(Base64.getDecoder().decode(token.split("\\.")[1]), "utf-8");
    Map userInfoMap = mapper.readValue(userInfoJson, HashMap.class);
    assertEquals(username, userInfoMap.get("user_name"));
    assertEquals(client, userInfoMap.get("client_id"));
  }

}
