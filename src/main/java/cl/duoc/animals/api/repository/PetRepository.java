// repository/PetRepository.java
package cl.duoc.animals.api.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.animals.api.entity.Pet;
public interface PetRepository extends JpaRepository<Pet, Long> {}
