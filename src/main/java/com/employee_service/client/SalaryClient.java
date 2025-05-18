package com.employee_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import  com.salary_service.entity.Salary;
@FeignClient(name = "salary-service" )
public interface SalaryClient {
//	@GetMapping("/salary/{employeeId}")
//    Salary getSalaryByEmployeeId(@PathVariable("employeeId") long employeeId);

	@GetMapping("/salary/all")
	public String getAllEmployeeSalaries(Model model);
	
	@DeleteMapping("/salary/deleteSalary/{id}")
    void deleteSalary(@PathVariable("id") int id);
//	@DeleteMapping("/salary/deleteslary/{id}")
//	public ResponseEntity<String> deleteSalary(@PathVariable("id") int id);

}
