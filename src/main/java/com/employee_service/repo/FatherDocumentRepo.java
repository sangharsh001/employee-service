package com.employee_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.employee_service.entity.EmployeeDocument;
import com.employee_service.entity.FatherDocument;

public interface FatherDocumentRepo extends JpaRepository<FatherDocument,Integer>{
	@Query(value = "SELECT * FROM father_document WHERE employee_id = :employeeId", nativeQuery = true)
	FatherDocument findFatherDocByEmployeeId(@Param("employeeId") int employeeId);
}
