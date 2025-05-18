package com.employee_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
	
	@Id 
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private long eid;
	private String ename;
	private long phone;
	
	private String email;
	private String Hiredate;
	private String jobTitle;
	private String salary;


}
