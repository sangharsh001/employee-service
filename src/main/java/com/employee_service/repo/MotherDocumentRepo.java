package com.employee_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import com.employee_service.entity.FatherDocument;
import com.employee_service.entity.MotherDocument;
@Repository
public interface MotherDocumentRepo extends JpaRepository<MotherDocument,Integer>{
	
	
	
		@Query(value = "SELECT * FROM mother_document WHERE employee_id = :employeeId", nativeQuery = true)
		MotherDocument findMotherDocByEmployeeId(@Param("employeeId") int employeeId);
	


}
