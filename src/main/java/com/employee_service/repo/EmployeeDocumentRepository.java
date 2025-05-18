package com.employee_service.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.employee_service.entity.EmployeeDocument;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.CrudRepository; // or JpaRepository
//import java.util.Optional;

public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Integer> {
//	@Query("SELECT d.aadhaar_Pic FROM Employee_Document d WHERE d.employee.id = :employeeId")
//	byte[] getAadhaarPicByEmpId(@Param("employeeId") int employeeId);

//	 EmployeeDocument findByEmployeeId(int employeeId);
	@Query(value = "SELECT * FROM employee_document WHERE employee_id = :employeeId", nativeQuery = true)
	EmployeeDocument findDocByEmployeeId(@Param("employeeId") long employeeId); 
	
	
//	Optional<EmployeeDocument> findByEmpId(Long employeeId);


//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE employee_document SET aadhaar_pic = :aadhaarPic WHERE employee_id = :employeeId", nativeQuery = true)
//	int updateAadhaarPic(@Param("aadhaarPic") byte[] aadhaarPic, @Param("employeeId") long employeeId);


	
//	@Query(value = "SELECT * FROM employee_document WHERE employee_id = :employeeId LIMIT 1", nativeQuery = true)
//	EmployeeDocument findPanByEmployeeId(@Param("employeeId") long employeeId);
}
