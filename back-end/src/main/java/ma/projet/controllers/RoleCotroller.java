package ma.projet.controllers;

import ma.projet.entities.Role;
import ma.projet.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleCotroller {
    @Autowired
    RoleService roleService;

    @GetMapping("")
    public List<Role> getroles(){
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoleById(@PathVariable Long id){
        Role role = roleService.findById(id);
        if (role != null)
            return ResponseEntity.ok(role);
        else
            return new ResponseEntity<Object>("le role avec l\'id " + id + "n\'existe pas", HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public Role createRole(@RequestBody Role role){
        return roleService.create(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable Long id, @RequestBody Role role){
        Role role1 = roleService.findById(id);
        if(role1 !=null)
            return ResponseEntity.ok(roleService.update(role));
        else
            return new ResponseEntity<Object>("le role avec l\'id " + id + "n\'existe pas", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id){
        Role role = roleService.findById(id);
        if (role != null) {
            roleService.delete(role);
            return ResponseEntity.ok("le role est supprime avec succes");
        }
        else
            return new ResponseEntity<Object>("le role avec l'id " + id + "n'existe pas",HttpStatus.NOT_FOUND);
    }
}
