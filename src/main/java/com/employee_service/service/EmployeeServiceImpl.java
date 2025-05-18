package com.employee_service.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.HashMap;

//package com.employee_service.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

//import com.employee_managemnet.employee_dto.EmployeeDTO;
import com.employee_service.entity.Employee;
import com.employee_service.entity.EmployeeDTO;
import com.employee_service.entity.EmployeeDocument;
import com.employee_service.entity.EmployeeDocumentDTO;
import com.employee_service.entity.FatherDocument;
import com.employee_service.entity.MotherDocument;
import com.employee_service.entity.SiblingsDocument;
import com.employee_service.repo.EmployeeDocumentRepository;
import com.employee_service.repo.EmployeeRepo;
import com.employee_service.repo.FatherDocumentRepo;
import com.employee_service.repo.MotherDocumentRepo;
import com.employee_service.repo.SiblingsDocumentRepo;
//import com.employee_managemnet.employee_dto.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  @Autowired
  private EmployeeRepo erepo;
  @Autowired
  EmployeeDocumentRepository edrepo;
@Autowired
EmployeeDocumentRepository  employeeDocumentRepository;
  // Save Employee entity


@Autowired
FatherDocumentRepo frepo;
@Autowired
MotherDocumentRepo mrepo;
@Autowired
SiblingsDocumentRepo srepo;
  @Override
  public void saveEmploye(Employee e) {
      erepo.save(e);
  }

  // Get a single employee by ID and convert to DTO
  @Override
  public EmployeeDTO getEmployeeById(int id) {
      Employee emp = erepo.findById(id)
              .orElseThrow(() -> new RuntimeException("Employee not found"));
      return new EmployeeDTO(emp.getEid(), emp.getEname(), emp.getPhone(),
                             emp.getEmail(), emp.getHiredate(), emp.getJobTitle(), emp.getSalary());
  } 

  // Get all employees and convert to DTO list
  @Override
  public List<EmployeeDTO> getAllEmployees() {
      List<Employee> list = erepo.findAll();
      System.out.println(list);
      return list.stream()
              .map(emp -> new EmployeeDTO(emp.getEid(), emp.getEname(), emp.getPhone(),
                                          emp.getEmail(), emp.getHiredate(), emp.getJobTitle(), emp.getSalary()))
              .collect(Collectors.toList());
  }

@Override
public void saveDocument(EmployeeDocument ed) {
	// TODO Auto-generated method stub
	   edrepo.save(ed);
}

@Override
public List<EmployeeDocument> getAllDocument() {
	// TODO Auto-generated method stub
	return edrepo.findAll();
}


@Override
public Map<Long, EmployeeDocumentDTO> getAllEmployeesDocumentsStatus() {
    List<EmployeeDocument> allDocs = employeeDocumentRepository.findAll();

    Map<Long, EmployeeDocumentDTO> resultMap = new HashMap<>();

    for (EmployeeDocument doc : allDocs) {
        EmployeeDocumentDTO dto = new EmployeeDocumentDTO();
        dto.setEid(doc.getEmployee().getEid());

        dto.setHasAadhaar(isPresent(doc.getAadhaarPic()));
        dto.setHasPan(isPresent(doc.getPanPic()));
        dto.setHasSslc(isPresent(doc.getSslcPic()));
        dto.setHasEmployeePhoto(isPresent(doc.getEmployeePhoto()));
        
   

        resultMap.put(doc.getEmployee().getEid(), dto);
    }

    return resultMap;
}


private boolean isPresent(byte[] data) {
    return data != null && data.length > 0;
}

@Override
public Map<Long, EmployeeDocumentDTO> getAllFatherDocumentsStatus() {
	// TODO Auto-generated method stub

    List<FatherDocument> allDocs = frepo.findAll();

    Map<Long, EmployeeDocumentDTO> resultMap = new HashMap<>();

    for (FatherDocument doc : allDocs) {
        EmployeeDocumentDTO dto = new EmployeeDocumentDTO();
        dto.setEid(doc.getEmployee().getEid());

        dto.setFatherHasAadhaar(isPresent(doc.getAadhaarPic()));
        dto.setFatherHasPan(isPresent(doc.getPanPic()));
      
        dto.setFatherHasPhoto(isPresent(doc.getEmployeePhoto()));
        
   

        resultMap.put(doc.getEmployee().getEid(), dto);
    }

    return resultMap;

}

@Override
public Map<Long, EmployeeDocumentDTO> getAllMotheDocumentsStatus() {
	 List<MotherDocument> allDocs = mrepo.findAll();

	    Map<Long, EmployeeDocumentDTO> resultMap = new HashMap<>();

	    for (MotherDocument doc : allDocs) {
	        EmployeeDocumentDTO dto = new EmployeeDocumentDTO();
	        dto.setEid(doc.getEmployee().getEid());

	        dto.setMotherHasAadhaar(isPresent(doc.getAadhaarPic()));
	        dto.setMotherHasPan(isPresent(doc.getPanPic()));
	      
	        dto.setMotherHasPhoto(isPresent(doc.getEmployeePhoto()));
	        
	   

	        resultMap.put(doc.getEmployee().getEid(), dto);
	    }

	    return resultMap;
}

@Override
public Map<Long, EmployeeDocumentDTO> getAllSiblingdDocumentsStatus() {
	 List<SiblingsDocument> allDocs = srepo.findAll();

	    Map<Long, EmployeeDocumentDTO> resultMap = new HashMap<>();

	    for (SiblingsDocument doc : allDocs) {
	        EmployeeDocumentDTO dto = new EmployeeDocumentDTO();
	        dto.setEid(doc.getEmployee().getEid());

	        dto.setSiblingsHasAadhaar(isPresent(doc.getAadhaarPic()));
	        dto.setSiblingsHasPan(isPresent(doc.getPanPic()));
	      
	        dto.setSiblingsHasPhoto(isPresent(doc.getEmployeePhoto()));
	        
	   

	        resultMap.put(doc.getEmployee().getEid(), dto);
	    }

	    return resultMap;
}




}

