package net.crud.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.crud.springboot.exception.ResourceNotFoundException;
import net.crud.springboot.model.Employee;
import net.crud.springboot.repository.EmployeeRepository;

import org.springframework.http.MediaType;



@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {
	
@Autowired	
private EmployeeRepository employeeRepository;

@GetMapping("employees")
public List<Employee> getAllEmployee(){
	return this.employeeRepository.findAll();
}

@GetMapping("/employees/{id}")
public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
		throws ResourceNotFoundException {
	Employee employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
	return ResponseEntity.ok().body(employee);
}

@PostMapping(value="/employees",consumes = MediaType.APPLICATION_JSON_VALUE)
public Employee createEmployee(@RequestBody Employee employee) {
	return employeeRepository.save(employee);
}
@PutMapping(value = "/employees/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Employee> updateEmployee(
        @PathVariable("id") Long employeeId,
        @RequestBody Employee employeeDetails
) throws ResourceNotFoundException {
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

    employee.setEmail(employeeDetails.getEmail());
    employee.setLastName(employeeDetails.getLastName());
    employee.setFirstName(employeeDetails.getFirstName());

    final Employee updatedEmployee = employeeRepository.save(employee);
    return ResponseEntity.ok(updatedEmployee);
}


@DeleteMapping("/employees/{id}")
public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
		throws ResourceNotFoundException {
	Employee employee = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

	employeeRepository.delete(employee);
	Map<String, Boolean> response = new HashMap<>();
	response.put("deleted", Boolean.TRUE);
	return response;
}
}