 package com.employee_service.service;

import java.util.List;
import java.util.Map;

//import com.employee_managemnet.employee_dto.EmployeeDTO;
import com.employee_service.entity.Employee;
import com.employee_service.entity.EmployeeDTO;
import com.employee_service.entity.EmployeeDocument;
import com.employee_service.entity.EmployeeDocumentDTO;

//import com.employee_service.entity.Employee;

public interface EmployeeService {
	public void saveEmploye(Employee e);
	public List<EmployeeDTO> getAllEmployees();
	 public EmployeeDTO getEmployeeById(int id);
	 public void saveDocument(EmployeeDocument ed);
	 public List<EmployeeDocument> getAllDocument();
	 
//	 public EmployeeDocument getDocsById(EmployeeDocument ed);
	 public Map<Long,EmployeeDocumentDTO> getAllEmployeesDocumentsStatus();
	 public Map<Long, EmployeeDocumentDTO> getAllFatherDocumentsStatus();
	 public Map<Long, EmployeeDocumentDTO> getAllMotheDocumentsStatus();
	 public Map<Long, EmployeeDocumentDTO> getAllSiblingdDocumentsStatus();
}
