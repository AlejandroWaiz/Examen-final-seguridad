// entity/Pet.java
package cl.duoc.animals.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name="pets")
public class Pet {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, length=80)
  private String name;

  @Column(nullable=false, length=40)
  private String species;

  private int age;

  @Column(nullable=false)
  private boolean adopted=false;

  public Pet(){}

  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public String getSpecies(){return species;} public void setSpecies(String species){this.species=species;}
  public int getAge(){return age;} public void setAge(int age){this.age=age;}
  public boolean isAdopted(){return adopted;} public void setAdopted(boolean adopted){this.adopted=adopted;}
}
