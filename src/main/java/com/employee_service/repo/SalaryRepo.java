package com.employee_service.repo;

//import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;

import com.employee_service.entity.Salary;

//import com.salary_service.entity.Salary;
//import com.salary_service.entity.SalaryDTO;

public interface SalaryRepo extends JpaRepository<Salary, Integer> {

	Salary findByEid(long eid);
//	public void deleteByEid(long id);
	
  }
