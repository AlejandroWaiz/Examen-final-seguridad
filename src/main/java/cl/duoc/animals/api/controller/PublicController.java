// controller/PublicController.java
package cl.duoc.animals.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/public")
public class PublicController {
  @GetMapping("/landing")
  public String landing(){ return "Welcome to United for Animals API"; }

    @GetMapping("/ping")
  public String ping() {
    return "pong";
  }
}
