package com.example.centeroauth.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Package com.example.centeroauth.controller
 * Module
 * Project center-oauth
 * Email 253498229@qq.com
 * Created on 2018/5/13 下午4:26
 *
 * @author wangbin
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class ControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testLogin() throws Exception {
    mockMvc.perform(formLogin())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/"))
            .andDo(print());
  }

  @Test
  public void testLogout() throws Exception {
    mockMvc.perform(logout())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/login?logout"))
            .andDo(print())
    ;
  }
}
