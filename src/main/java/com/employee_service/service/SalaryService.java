package com.employee_service.service;

import java.util.List;

import com.employee_service.entity.Salary;



//import com.salary_service.entity.EmployeeDTO;
//import com.employee_managemnet.employee_dto.EmployeeDTO;
//import com.salary_service.entity.Salary;

public interface SalaryService {
    public void addSalary(Salary salaryList);
    public List<Salary> getAllEmployeeSalaries();
}
