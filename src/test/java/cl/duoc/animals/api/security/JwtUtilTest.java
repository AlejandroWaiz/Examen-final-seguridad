package cl.duoc.animals.api.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

  // 64+ chars para HS256
  private static final String SECRET = "Wm9tYmllTGFyZ2VTZWNyZXRLZXlGb3JUZWFtbGFiQW5pbWFsczEwMjM0NTY3ODkwMTIzNDU2";
  private static final long EXP = 60_000L;

  @Test
  void generate_validate_and_parse_username() {
    JwtUtil jwt = new JwtUtil(SECRET, EXP);
    String token = jwt.generateToken("admin", "ROLE_ADMIN");

    assertNotNull(token);
    assertTrue(jwt.validate(token));
    assertEquals("admin", jwt.getUsername(token));
  }

  @Test
  void invalid_token_is_rejected() {
    JwtUtil jwt = new JwtUtil(SECRET, EXP);
    String token = "xxx.yyy.zzz";
    assertFalse(jwt.validate(token));
  }
}
