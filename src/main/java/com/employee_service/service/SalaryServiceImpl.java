package com.employee_service.service;

import java.util.List;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.employee_service.entity.Salary;
import com.employee_service.repo.SalaryRepo;

//import com.employee_managemnet.employee_dto.EmployeeDTO;
//import com.salary_service.client.EmployeeClient;
//import com.salary_service.entity.EmployeeDTO;
//import com.employee_managemnet.employee_dto.Employee;
//import com.salary_service.client.EmployeeClient;
//import com.salary_service.entity.Salary;
////import com.salary_service.entity.SalaryDTO;
//import com.salary_service.repo.SalaryRepo;
@Service
public class SalaryServiceImpl implements SalaryService {
@Autowired
SalaryRepo srepo;

//@Autowired
//private EmployeeClient employeeClient;

	@Override
	public void addSalary(Salary salaryList) {
		// TODO Auto-generated method stub
	    srepo.save(salaryList);
		
	}
	
	
	


    @Override
    public List<Salary> getAllEmployeeSalaries() {
        // Get all employee data from employee-service
        
    
        List<Salary> sal=srepo.findAll();
        // Add employee list to model to be displayed on the Thymeleaf page
//        model.addAttribute("employees", employees);
        
        // "addsalary.html" should be the Thymeleaf template
        return sal;
    }
}
