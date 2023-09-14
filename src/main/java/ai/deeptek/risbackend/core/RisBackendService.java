package ai.deeptek.risbackend.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RisBackendService {

    @Autowired
    PatientRepository patientRepository;


    //Simple CRUD Operations on the Repository
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public void addPatient(Patient patient) {
        patientRepository.save(patient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).get();
    }

    public Patient updatePatientById(Patient updatedPatient, Long id) {
        return patientRepository.findById(id).map(patient->{
            patient.setName(updatedPatient.getName());
            patient.setDob(updatedPatient.getDob());
            patient.setEmail(updatedPatient.getEmail());
            patient.setGender(updatedPatient.getGender());
            return patientRepository.save(patient);
        }).orElseGet(()->{
            updatedPatient.setId(id);
            return patientRepository.save(updatedPatient);
        });
    }

    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }

    public void setPathToNull(Long id) {
        patientRepository.findById(id).map(patient->{
            patient.setFilePath(null);
            return patientRepository.save(patient);
        });
    }


    //Paging Implementation to improve efficiency
    public Page<Patient> getPatientsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return patientRepository.findAll(pageable);
    }

    public Page<Patient> getPatientsPageByEmail(int page, int size, String searchTerm){
        Pageable pageable = PageRequest.of(page - 1, size);
        return patientRepository.findByEmailIgnoreCase(searchTerm, pageable);
    }

    public Page<Patient> getPatientsPageById(int page, int size, Long id){
        Pageable pageable = PageRequest.of(page - 1, size);
        return  patientRepository.findByIdPage(id,pageable);
    }

}