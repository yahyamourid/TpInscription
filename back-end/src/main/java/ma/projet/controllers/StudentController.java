package ma.projet.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.projet.entities.Filiere;
import ma.projet.entities.Role;
import ma.projet.entities.Student;
import ma.projet.repository.RoleRepository;
import ma.projet.services.StudentService;

@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
	@Autowired
	private StudentService studentservice;
	
	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("")
	public List<Student> getAllStudent() {
		return studentservice.findAll();
	}


	@GetMapping("/filiere/{id}")
	public List<Student> getStudentsByFiliere(@PathVariable Long id) {
		Filiere filiere = new Filiere();
		filiere.setId(id);
		return studentservice.findStudentsByFiliere(filiere);
	}


	@GetMapping("/{id}")
	public Student getById(@PathVariable Long id) {
		return studentservice.findById(id);

	}

	@PostMapping("")
	public Student createStudent(@RequestBody Student student) {
//		Role role = roleRepository.findById(student.getRoles().get(0).getId()).get();
//		System.out.println(role.getName());
//		student.getRoles().add(role);
		return studentservice.create(student);
	}

	@PutMapping("/{id}")
	public ResponseEntity updateStudent(@PathVariable Long id, @RequestBody Student student) {

		if (student == null) {
			return new ResponseEntity<Object>("student avec ID " + id + " n exite pas", HttpStatus.BAD_REQUEST);
		} else {
			studentservice.update(student);
			return ResponseEntity.ok("UPDATE AVEC SUCCEs");
			// return ResponseEntity.ok("");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		Student student = studentservice.findById(id);
		
		if (student == null) {
			return new ResponseEntity<Object>("student avec ID " + id + " n exite pas", HttpStatus.BAD_REQUEST);
		} else {
			studentservice.delete(student);
			return ResponseEntity.ok(" supression avec succes ");

		}
	}
}
