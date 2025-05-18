package com.employee_service.entity;

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
@NoArgsConstructor
@AllArgsConstructor
public class SiblingsDocument {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long did;
	
	   @Lob
	    private byte[] aadhaarPic;
	
	    @Lob
	    private byte[] panPic;
	
//	    @Lob
//	    private byte[] sslcPic;
	
	    @Lob
	    private byte[] employeePhoto;
	    
	    @ManyToOne
	    @JoinColumn(name = "employee_id")
	    private Employee employee;
	    
	    
	

}