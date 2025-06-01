package com.employee_service.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//package com.salary_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Salary {

    @Id
    private long eid; // Primary key

    private Long id;  // Not primary key here, since `eid` is used

    private String ename = "Na";
    private String jan = "Na";
    private String feb = "Na";
    private String mar = "Na";
    private String apr = "Na";
    private String may = "Na";
    private String june = "Na";
    private String july = "Na";
    private String aug = "Na";
    private String sep = "Na";
    private String oct = "Na";

    @Column(name = "`nov`")
    private String nov = "Na";

    @Column(name = "`dec`")
    private String dec = "Na";

    @Column(name = "advancc")
    private String advancc = "Na";

   
    }

