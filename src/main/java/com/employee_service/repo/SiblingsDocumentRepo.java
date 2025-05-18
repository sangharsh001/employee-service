package com.employee_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.employee_service.entity.MotherDocument;
import com.employee_service.entity.SiblingsDocument;
@Repository
public interface SiblingsDocumentRepo extends JpaRepository<SiblingsDocument, Integer>{
	@Query(value = "SELECT * FROM siblings_document WHERE employee_id = :employeeId", nativeQuery = true)
	SiblingsDocument findSiblingsDocByEmployeeId(@Param("employeeId") int employeeId);


}
