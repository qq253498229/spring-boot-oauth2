package com.example.centeroauth.java;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Package com.example.centeroauth.java
 * Module
 * Project center-oauth
 * Email 253498229@qq.com
 * Created on 2018/5/13 下午4:22
 *
 * @author wangbin
 */
public class JavaTest {
  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Test
  public void test() {
    System.out.println(encoder.encode("password"));
    System.out.println(encoder.encode("secret"));
  }
}
