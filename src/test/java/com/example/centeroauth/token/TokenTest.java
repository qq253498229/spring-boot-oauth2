package com.example.centeroauth.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
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

  @Test
  public void testPasswordType() {
    Map<String, Object> map = new HashMap<>();
    map.put("port", port);
    map.put("username", username);
    map.put("password", password);
    ResponseEntity<String> response;
    response = template.withBasicAuth(client, secret)
            .postForEntity(BASE_URL + "/oauth/token" +
                            "?grant_type=password&username={username}&password={password}",
                    null, String.class, map);
    log.info(response.getBody());
    assertEquals(response.getStatusCode(), OK);
  }

}
