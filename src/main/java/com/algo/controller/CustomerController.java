package com.algo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : iyeong-gyo
 * @package : com.sportedu.controller
 * @since : 08.11.23
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

  @GetMapping("/main-dashboard")
  public String userAccess() {
    return "customer/main-dashboard";
  }
}