package ai.deeptek.risbackend.core;
import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RisBackendController {
    @Autowired
    RisBackendService risBackendService;

    // All elements CRUD
    @GetMapping("/patients")
    public List<Patient> getPatients(){
        return risBackendService.getAllPatients();
    }

    @PostMapping("/patients")
    public void createPatient(@RequestBody Patient newPatient) {
        risBackendService.addPatient(newPatient);
    }

    @GetMapping("/patientsByPage")
    public ResponseEntity<Page<Patient>> getPatientsByPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<Patient> patients = risBackendService.getPatientsByPage(page, size);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patientsPageByEmail")
    public ResponseEntity<Page<Patient>> getPatientsPageByEmail(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "searchEmail") String searchEmail
    )
    {
        Page<Patient> patientsByEmail = risBackendService.getPatientsPageByEmail(page,size,searchEmail);
        return ResponseEntity.ok(patientsByEmail);
    }

    @GetMapping("/patientsPageById")
    public ResponseEntity<Page<Patient>> getPatientsPageById(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "searchId") Long searchId
    )
    {
        Page<Patient> patientsById = risBackendService.getPatientsPageById(page,size,searchId);
        return ResponseEntity.ok(patientsById);
    }



    //Single Element CRUD
    @GetMapping("/patients/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public Patient getPatientById(@PathVariable Long id) {
        //System.out.println(risBackendService.getPatientById(id));
        return risBackendService.getPatientById(id);
    }

    @PutMapping("/patients/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public Patient updatePatient(@RequestBody Patient updatedPatient, @PathVariable Long id) {
        return risBackendService.updatePatientById(updatedPatient,id);
    }

    @DeleteMapping("/patients/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public void deletePatient(@PathVariable Long id) {
        risBackendService.deletePatientById(id);
    }

    @PatchMapping("/patients/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public void patchPatient(@PathVariable Long id) {
        System.out.println("Patch Request Called");
        risBackendService.setPathToNull(id);
    }


    //DICOM Specific Calls
    @GetMapping("/dicom/{fileName}")
    @CrossOrigin(origins = "http://localhost:5173")
    public FileSystemResource getFile(@PathVariable String fileName) {
        System.out.println(fileName);
        String uploadDir = "/Users/atharvajoshi/Documents/deeptek-ris/files/";

        String filePathString = uploadDir+fileName;
        return new FileSystemResource(filePathString);

    }

    @DeleteMapping("/dicom/{fileName}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        //System.out.println("Here on deleteFile Method");
        try {
            String uploadDir="/Users/atharvajoshi/Documents/deeptek-ris/files/";
            Resource resource = new FileSystemResource(uploadDir+fileName);

            if(resource.exists()) {
                File file = resource.getFile();
                if(file.delete()) {
                    return ResponseEntity.ok("File Deleted Successfully");
                }
                else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to delete the file");
                }
            }

            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: "+e.getMessage());
        }
    }


}
