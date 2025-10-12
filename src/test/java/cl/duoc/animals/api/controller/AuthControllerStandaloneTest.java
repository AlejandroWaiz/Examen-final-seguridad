package cl.duoc.animals.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cl.duoc.animals.api.config.GlobalExceptionHandler;
import cl.duoc.animals.api.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthControllerStandaloneTest {

  @InjectMocks
  AuthController controller;

  @Mock
  AuthenticationManager authenticationManager;

  @Mock
  JwtUtil jwtUtil;

  MockMvc mvc;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders
        .standaloneSetup(controller)
        .setControllerAdvice(new GlobalExceptionHandler())
        .setMessageConverters(new MappingJackson2HttpMessageConverter())
        .build();
  }

  @Test
  void login_ok() throws Exception {
    var ud = User.withUsername("admin").password("1234").authorities("ROLE_ADMIN").build();
    Authentication authenticated = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());

    when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authenticated);
    when(jwtUtil.generateToken("admin", "ROLE_ADMIN")).thenReturn("dummy-token");

    mvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"username":"admin","password":"1234"}
        """))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").value("dummy-token"))
      .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
  }

  @Test
  void login_bad_credentials() throws Exception {
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(new BadCredentialsException("bad creds"));

    mvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {"username":"admin","password":"zzz"}
        """))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.error").value("Bad credentials"));
  }
}
