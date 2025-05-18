package com.employee_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employee_service.entity.Employee;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
	
//	public void deleteByEid(long eid);  
	
	 
}
