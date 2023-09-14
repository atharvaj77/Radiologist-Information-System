package ai.deeptek.risbackend.core;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DicomUploadController {

    private final PatientRepository patientRepository;

    public DicomUploadController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostMapping("/upload/{patientId}")
    public ResponseEntity<String> uploadFile(@PathVariable Long patientId, @RequestParam("file") MultipartFile file){
        LocalDate today = LocalDate.now();
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty!");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(()-> new IllegalArgumentException("Patient not found"));


        String uploadDir="/Users/atharvajoshi/Documents/deeptek-ris/files/";

        try {
            String randomFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + randomFileName);

            if(Files.exists(filePath)){
                return ResponseEntity.badRequest().body("File with the same name already exists!");
            }

            Files.copy(file.getInputStream(),filePath);

            patient.setFilePath(filePath.toString());
            patient.setUploadDate(today);
            patientRepository.save(patient);

            return ResponseEntity.ok("File uploaded successfully");
        }

        catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file");
        }
    }



}