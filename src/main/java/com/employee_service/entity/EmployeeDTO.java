package com.employee_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
	private long eid;
	private String ename;
	private long phone;
	
	private String email;
	private String Hiredate;
	private String jobTitle;
	private String salary;
}
