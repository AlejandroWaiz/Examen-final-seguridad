// controller/PetController.java
package cl.duoc.animals.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.animals.api.entity.Pet;
import cl.duoc.animals.api.repository.PetRepository;

@RestController @RequestMapping("/api/pets")
public class PetController {
  private final PetRepository repo;
  public PetController(PetRepository repo){ this.repo=repo; }

  @GetMapping public List<Pet> all(){ return repo.findAll(); }

  @PostMapping public Pet create(@RequestBody Pet p){ return repo.save(p); }

  @PutMapping("/{id}")
  public ResponseEntity<Pet> update(@PathVariable Long id, @RequestBody Pet p){
    return repo.findById(id).map(db -> {
      db.setName(p.getName()); db.setSpecies(p.getSpecies()); db.setAge(p.getAge()); db.setAdopted(p.isAdopted());
      return ResponseEntity.ok(repo.save(db));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    if (!repo.existsById(id)) return ResponseEntity.notFound().build();
    repo.deleteById(id); return ResponseEntity.noContent().build();
  }
}
