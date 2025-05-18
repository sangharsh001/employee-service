package com.employee_service.controller;



//import com.employee_managemnet.employee_dto.EmployeeDTO;

import com.employee_service.client.SalaryClient;



import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

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
import com.employee_service.service.EmployeeServiceImpl;
//import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
// import com.salary_service.entity.Salary;
//import com.itextpdf.text.pdf.PdfWriter;
//import jakarta.servlet.http.HttpServletResponse;

import feign.FeignException;
import jakarta.ws.rs.GET;

//import com.salary_service.entity.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import javax.swing.text.Document;



@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl empservice;
  @Autowired
  private EmployeeRepo emprepo;
    @Autowired
    private SalaryClient salaryClient;
    @Autowired
    private EmployeeDocumentRepository docsrepo;
  @Autowired
  private FatherDocumentRepo frepo;
  
  @Autowired
  private MotherDocumentRepo mrepo;
  
  
  @Autowired 
  private SiblingsDocumentRepo srepo;
  
    // 1. Show add employee form
    @GetMapping("/addemployee")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee()); // empty object for form binding
        model.addAttribute("activePage", "addemployee");
         return "addemployee"; // goes to addemployee.html
    }

    // 2. Save employee to DB
    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee emp) {
        empservice.saveEmploye(emp); // Service layer call
        return "addemployee"; // Or redirect to list page
    }

    // 3. Static table page
    @GetMapping("/documentstable")
    public String documentsTable() {
        return "documenttable";
    }

    // 4. Get employee salary details (via Feign)
//    @GetMapping("/salary/{id}")
//    public String getEmployeeDetails(@PathVariable long id, Model model) {
//        Salary salary = salaryClient.getSalaryByEmployeeId(id);
//        model.addAttribute("salary", salary);
//        return "addsalary"; // Thymeleaf page
//    }

    // 5. REST: Get one employee DTO (optional if you’re using RestController)
    @ResponseBody
    @GetMapping("/{eid}")
    public EmployeeDTO getEmployeeById(@PathVariable int eid) {
        return empservice.getEmployeeById(eid); // Call service to return DTO
    }

    // 6. REST: Get all employees as DTO list
    @ResponseBody
    @GetMapping("/alle")
    public List<EmployeeDTO> getAllEmployees() {
        return empservice.getAllEmployees(); // Call service to return list
    }
    
	/*
	 * @GetMapping("/allemp") public String getAll(Model model) { List<EmployeeDTO>
	 * emps=empservice.getAllEmployees(); // List<EmployeeDocument>
	 * empdocs=docsrepo.findAll(); Map<Long, EmployeeDocumentDTO> empdocs=
	 * empservice.getAllEmployeesDocumentsStatus(); Map<Long, EmployeeDocumentDTO>
	 * farherdocs=empservice.getAllFatherDocumentsStatus(); Map<Long,
	 * EmployeeDocumentDTO> motherdocs=empservice.getAllMotheDocumentsStatus();
	 * Map<Long, EmployeeDocumentDTO>
	 * siblingsdocs=empservice.getAllSiblingdDocumentsStatus();
	 * model.addAttribute("emplist",emps);
	 * model.addAttribute("documentsStatus",empdocs);
	 * model.addAttribute("fatherdocumentsStatus", farherdocs);
	 * model.addAttribute("motherdocumentsStatus", motherdocs);
	 * model.addAttribute("siblingsdocumentsStatus", siblingsdocs); return
	 * "documenttable"; }
	 */
    
    @GetMapping("/allemp")
    public String getAll(Model model) {
        List<EmployeeDTO> emps = empservice.getAllEmployees(); 
        Map<Long, EmployeeDocumentDTO> empdocs = empservice.getAllEmployeesDocumentsStatus();
        Map<Long, EmployeeDocumentDTO> farherdocs = empservice.getAllFatherDocumentsStatus();
        Map<Long, EmployeeDocumentDTO> motherdocs = empservice.getAllMotheDocumentsStatus();
        Map<Long, EmployeeDocumentDTO> siblingsdocs = empservice.getAllSiblingdDocumentsStatus();

        model.addAttribute("emplist", emps);
        model.addAttribute("documentsStatus", empdocs);
        model.addAttribute("fatherdocumentsStatus", farherdocs);
        model.addAttribute("motherdocumentsStatus", motherdocs);
        model.addAttribute("siblingsdocumentsStatus", siblingsdocs);
        
        model.addAttribute("activePage", "employee"); // 👈 for navbar highlighting

        return "documenttable";
    }

    
    
    @PostMapping("/upload-docs")
    public String uploadDocs(@RequestParam(value = "aadhaarPic", required = false) MultipartFile aadhaarPic,
                             @RequestParam(value = "panPic", required = false) MultipartFile panPic,
                             @RequestParam(value = "sslcPic", required = false) MultipartFile sslcPic,
                             @RequestParam(value = "employeePhoto", required = false) MultipartFile employeePhoto,
                             @RequestParam("employeeId") int employeeId) throws IOException {

        Employee employee = emprepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + employeeId));

        EmployeeDocument existingDoc = docsrepo.findDocByEmployeeId(employeeId);

        EmployeeDocument ed;

        if (existingDoc == null) {
            // No existing document, create new one
            ed = new EmployeeDocument();
            ed.setEmployee(employee);
        } else {
            // Use the existing one
            ed = existingDoc;
        }

        // Update fields if new file is uploaded, else retain existing or null
        if (aadhaarPic != null && !aadhaarPic.isEmpty()) {
            ed.setAadhaarPic(aadhaarPic.getBytes());
        }

        if (panPic != null && !panPic.isEmpty()) {
            ed.setPanPic(panPic.getBytes());
        }

        if (sslcPic != null && !sslcPic.isEmpty()) {
            ed.setSslcPic(sslcPic.getBytes());
        }

        if (employeePhoto != null && !employeePhoto.isEmpty()) {
            ed.setEmployeePhoto(employeePhoto.getBytes());
        }

        empservice.saveDocument(ed);

        return "redirect:/employee/allemp";
    }

    
    
//    @GetMapping("/alldocs")
//    public String getAllDocumnet(Model model) {
//    	 List<EmployeeDocument> docs=empservice.getAllDocument();
//    	 
//    	 model.addAttribute("empdocs",docs);
//    	 return "documenttable";
//    }
//    
    
    @GetMapping("/aadhaar-as-pdf")
    public ResponseEntity<byte[]> getAadhaarAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            EmployeeDocument doc = docsrepo.findDocByEmployeeId(employeeId);
            if (doc == null || doc.getAadhaarPic() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getAadhaarPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    @GetMapping("/pan-as-pdf")
    public ResponseEntity<byte[]> getPanAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            EmployeeDocument doc = docsrepo.findDocByEmployeeId(employeeId);
            if (doc == null || doc.getPanPic() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getPanPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }

    
    //sslc as a pdf
    @GetMapping("/sslc-as-pdf")
    public ResponseEntity<byte[]> getSslcAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            EmployeeDocument doc = docsrepo.findDocByEmployeeId(employeeId);
            if (doc == null || doc.getSslcPic() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getSslcPic() ;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    //10th as a pdf
    
    
    @GetMapping("/photo-as-pdf")
    public ResponseEntity<byte[]> getPhotoAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            EmployeeDocument doc = docsrepo.findDocByEmployeeId(employeeId);
            if (doc == null || doc.getEmployeePhoto() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getEmployeePhoto() ;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    
    //father details
    @PostMapping("/upload-father-docs")
    public String uploadFatherDocs(@RequestParam(value = "aadhaarPic", required = false) MultipartFile aadhaarPic,
                             @RequestParam(value = "panPic", required = false) MultipartFile panPic,
//                             @RequestParam(value = "sslcPic", required = false) MultipartFile sslcPic,
                             @RequestParam(value = "employeePhoto", required = false) MultipartFile employeePhoto,
                             @RequestParam("employeeId") int employeeId) throws IOException {

        Employee employee = emprepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + employeeId));

      FatherDocument existingDoc = frepo.findFatherDocByEmployeeId(employeeId);

      FatherDocument ed;

        if (existingDoc == null) {
            // No existing document, create new one
            ed = new FatherDocument();
            ed.setEmployee(employee);
        } else {
            // Use the existing one
            ed = existingDoc;
        }

        // Update fields if new file is uploaded, else retain existing or null
        if (aadhaarPic != null && !aadhaarPic.isEmpty()) {
            ed.setAadhaarPic(aadhaarPic.getBytes());
        }

        if (panPic != null && !panPic.isEmpty()) {
            ed.setPanPic(panPic.getBytes());
        }

      
        if (employeePhoto != null && !employeePhoto.isEmpty()) {
            ed.setEmployeePhoto(employeePhoto.getBytes());
        }

         frepo.save(ed);

        return "redirect:/employee/allemp";
    }
    
    
    //father pan as pdf
    
    
    @GetMapping("/fatheradhar-as-pdf")
    public ResponseEntity<byte[]> getFatherAdharAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            FatherDocument doc =frepo.findFatherDocByEmployeeId(employeeId);
            if (doc == null || doc.getAadhaarPic() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getAadhaarPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    
    
    @GetMapping("/fatherpan-as-pdf")
    public ResponseEntity<byte[]> getFatherPanAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            FatherDocument doc =frepo.findFatherDocByEmployeeId(employeeId);
            if (doc == null || doc.getPanPic()== null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getPanPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    @GetMapping("/fatherphoto-as-pdf")
    public ResponseEntity<byte[]> getFatherPhotoAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            FatherDocument doc =frepo.findFatherDocByEmployeeId(employeeId);
            if (doc == null || doc.getEmployeePhoto()!=null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getEmployeePhoto();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    //mother documnets
    
    @PostMapping("/upload-mother-docs")
    public String uploadMotherDocs(@RequestParam(value = "aadhaarPic", required = false) MultipartFile aadhaarPic,
                             @RequestParam(value = "panPic", required = false) MultipartFile panPic,
//                             @RequestParam(value = "sslcPic", required = false) MultipartFile sslcPic,
                             @RequestParam(value = "employeePhoto", required = false) MultipartFile employeePhoto,
                             @RequestParam("employeeId") int employeeId) throws IOException {

        Employee employee = emprepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + employeeId));

      MotherDocument existingDoc = mrepo.findMotherDocByEmployeeId(employeeId);

      MotherDocument ed;

        if (existingDoc == null) {
            // No existing document, create new one
            ed = new MotherDocument();
            ed.setEmployee(employee);
        } else {
            // Use the existing one
            ed = existingDoc;
        }

        // Update fields if new file is uploaded, else retain existing or null
        if (aadhaarPic != null && !aadhaarPic.isEmpty()) {
            ed.setAadhaarPic(aadhaarPic.getBytes());
        }

        if (panPic != null && !panPic.isEmpty()) {
            ed.setPanPic(panPic.getBytes());
        }

      
        if (employeePhoto != null && !employeePhoto.isEmpty()) {
            ed.setEmployeePhoto(employeePhoto.getBytes());
        }

         mrepo.save(ed);

        return "redirect:/employee/allemp";
    }
    
    
    
    
    
    
    
    
    @GetMapping("/motheradhar-as-pdf")
    public ResponseEntity<byte[]> getMotherAdharAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            MotherDocument doc =mrepo.findMotherDocByEmployeeId(employeeId);
            if (doc == null || doc.getAadhaarPic() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getAadhaarPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    
    
    @GetMapping("/motherpan-as-pdf")
    public ResponseEntity<byte[]> getMotherPanAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
        	   MotherDocument doc =mrepo.findMotherDocByEmployeeId(employeeId);
            if (doc == null || doc.getPanPic()== null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getPanPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    @GetMapping("/motherphoto-as-pdf")
    public ResponseEntity<byte[]> getMotherPhotoAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
        	  MotherDocument doc =mrepo.findMotherDocByEmployeeId(employeeId);
            if (doc == null || doc.getEmployeePhoto()!=null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getEmployeePhoto();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    
    
 //siblings
    
    @PostMapping("/upload-siblings-docs")
    public String uploadSiblingsDocs(@RequestParam(value = "aadhaarPic", required = false) MultipartFile aadhaarPic,
                             @RequestParam(value = "panPic", required = false) MultipartFile panPic,
//                             @RequestParam(value = "sslcPic", required = false) MultipartFile sslcPic,
                             @RequestParam(value = "employeePhoto", required = false) MultipartFile employeePhoto,
                             @RequestParam("employeeId") int employeeId) throws IOException {

        Employee employee = emprepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + employeeId));

      SiblingsDocument existingDoc =srepo.findSiblingsDocByEmployeeId(employeeId);

    		  SiblingsDocument ed;

        if (existingDoc == null) {
            // No existing document, create new one
            ed = new SiblingsDocument();
            ed.setEmployee(employee);
        } else {
            // Use the existing one
            ed = existingDoc;
        }

        // Update fields if new file is uploaded, else retain existing or null
        if (aadhaarPic != null && !aadhaarPic.isEmpty()) {
            ed.setAadhaarPic(aadhaarPic.getBytes());
        }

        if (panPic != null && !panPic.isEmpty()) {
            ed.setPanPic(panPic.getBytes());
        }

      
        if (employeePhoto != null && !employeePhoto.isEmpty()) {
            ed.setEmployeePhoto(employeePhoto.getBytes());
        }

         srepo.save(ed);

        return "redirect:/employee/allemp";
    }
    
    
    
    
    
    
    
    
    @GetMapping("/siblingsadhar-as-pdf")
    public ResponseEntity<byte[]> getSiblingsAdharAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
            SiblingsDocument doc =srepo.findSiblingsDocByEmployeeId(employeeId);
//            SiblingsDocument existingDoc =
            if (doc == null || doc.getAadhaarPic() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getAadhaarPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    
    
    @GetMapping("/siblingspan-as-pdf")
    public ResponseEntity<byte[]> getSiblingsPanAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
        	   SiblingsDocument doc =srepo.findSiblingsDocByEmployeeId(employeeId);
            if (doc == null || doc.getPanPic()== null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getPanPic();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    @GetMapping("/siblingsphoto-as-pdf")
    public ResponseEntity<byte[]> getSiblingsPhotoAsPdf(@RequestParam("employeeId") int employeeId) {
        try {
        	  SiblingsDocument doc =srepo.findSiblingsDocByEmployeeId(employeeId);
            if (doc == null || doc.getEmployeePhoto()!=null) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = doc.getEmployeePhoto();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDoc, baos);
            pdfDoc.open();

            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(500, 700);
            pdfDoc.add(image);
            pdfDoc.close();

            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=aadhaar.pdf")
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace(); // or log it
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
    
    
 //THROGH FIEGN 
    
//   @GetMapping("/getallsalries")
//   public void getEmployeesAllSalries()
//   {
//	   salaryClient.getAllEmployeeSalaries();
//   }
//    
    
    
    @GetMapping("/getallemployeesdeatails")
    public String  getAllEmployeesDetails(Model model)
    {
    List<Employee> allemps=	emprepo.findAll();
    model.addAttribute("allempslist", allemps);
    model.addAttribute("activePage", "allemployees");
    	return "allemployees";
    	 
    	
    }
    
//    @GetMapping("/delete/{id}")
//    public String DeleteEmployee(@PathVariable("id") int  id)
//    {
////	    salaryClient.deleteSalary(id);
//    	emprepo.deleteById(id);
//       
//    	
//    	return "redirect:/employee/getallemployeesdeatails";
//    }
    
//    @GetMapping("/delete/{id}")
//    public String DeleteEmployee(@PathVariable("id") int id, Model model) {
//        try {
//            ResponseEntity<String> response = salaryClient.deleteSalary(id);
//            if (!response.getStatusCode().is2xxSuccessful()) {
//                model.addAttribute("error", "Salary deletion failed: " + response.getBody());
//                return "redirect:/employee/getallemployeesdeatails"; // show error on same page
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Error deleting salary: " + e.getMessage());
//            return "redirect:/employee/getallemployeesdeatails";
//        }
//
//        emprepo.deleteById(id);
//        return "redirect:/employee/getallemployeesdeatails"; // reload employee list
//    }

    @DeleteMapping("/deleterec/{eid}")
    @ResponseBody
    public ResponseEntity<String> deleteEmp(@PathVariable int eid) {
        try {
            salaryClient.deleteSalary(eid);
        } catch (FeignException.NotFound ex) {
            // Log or ignore if already deleted
        }
        emprepo.deleteById(eid);
        return ResponseEntity.ok("Deleted");
    }
    
    @GetMapping("/edit/{id}")
    public String updateempById(@PathVariable int id, Model model) {
        Optional<Employee> emp = emprepo.findById(id);
        if (emp.isPresent()) {
            model.addAttribute("updateemp", emp.get());
            return "editemployee";
        } else {
            // Redirect or error page if not found
        	return "redirect:/error";// or a custom error view
        }
    }
    
    
    @PostMapping("/updateemployee/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmp) {
        Optional<Employee> empOptional = emprepo.findById(id);
        if (empOptional.isPresent()) {
            Employee emp = empOptional.get();
            emp.setEname(updatedEmp.getEname());
            emp.setPhone(updatedEmp.getPhone());
            emp.setEmail(updatedEmp.getEmail());
            emp.setHiredate(updatedEmp.getHiredate());
            emp.setJobTitle(updatedEmp.getJobTitle());
            emp.setSalary(updatedEmp.getSalary());
            emprepo.save(emp);
            return ResponseEntity.ok("Employee updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }


    
    

    
    
    
}
