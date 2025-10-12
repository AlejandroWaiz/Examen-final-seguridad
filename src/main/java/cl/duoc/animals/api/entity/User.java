// entity/User.java
package cl.duoc.animals.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name="users")
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true, length=60)
  private String username;

  @Column(nullable=false, length=200)
  private String password;

  @Column(nullable=false, length=30)
  private String role;

  public User() {}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getUsername(){return username;} public void setUsername(String u){this.username=u;}
  public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
  public String getRole(){return role;} public void setRole(String r){this.role=r;}
}
