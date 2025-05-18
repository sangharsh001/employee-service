package com.employee_service.entity;

import lombok.Data;

@Data
public class EmployeeDocumentDTO {

    private Long eid;

    private boolean hasAadhaar;
    private boolean hasPan;
    private boolean hasSslc;
    private boolean hasEmployeePhoto;

    private boolean fatherHasAadhaar;
    private boolean fatherHasPan;
    private boolean fatherHasPhoto;
    
    private boolean motherHasAadhaar;
    private boolean motherHasPan;
    private boolean motherHasPhoto;
    
    
    private boolean siblingsHasAadhaar;
    private boolean siblingsHasPan;
    private boolean siblingsHasPhoto;

}
