package cl.duoc.animals.api.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class UserPojoTest {

  @Test
  void getters_setters_ok() {
    User u = new User();
    u.setId(99L);
    u.setUsername("admin");
    u.setPassword("secret");
    u.setRole("ADMIN");

    assertEquals(99L, u.getId());
    assertEquals("admin", u.getUsername());
    assertEquals("secret", u.getPassword());
    assertEquals("ADMIN", u.getRole());
  }
}
