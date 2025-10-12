package cl.duoc.animals.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.animals.api.controller.dto.LoginRequest;
import cl.duoc.animals.api.controller.dto.LoginResponse;
import cl.duoc.animals.api.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtUtil jwt;

  public AuthController(AuthenticationManager authManager, JwtUtil jwt) {
    this.authManager = authManager;
    this.jwt = jwt;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
    );
    UserDetails user = (UserDetails) auth.getPrincipal();
    String role = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("ROLE_USER");
    String token = jwt.generateToken(user.getUsername(), role);
    return ResponseEntity.ok(new LoginResponse(token, role));
  }
}
