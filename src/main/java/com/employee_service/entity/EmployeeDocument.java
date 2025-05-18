	package com.employee_service.entity;
	
	//import jakarta.persistence.Column
	
	import jakarta.persistence.Entity;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.JoinColumn;
	import jakarta.persistence.Lob;
	import jakarta.persistence.ManyToOne;
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	
	@Entity
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class EmployeeDocument {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	
	    @Lob
	    private byte[] aadhaarPic;
	
	    @Lob
	    private byte[] panPic;
	
	    @Lob
	    private byte[] sslcPic;
	
	    @Lob
	    private byte[] employeePhoto;
	
	    @ManyToOne
	    @JoinColumn(name = "employee_id")
	    private Employee employee;
	
	    // Getters and setters
	}
