// repository/UserRepository.java
package cl.duoc.animals.api.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.animals.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
