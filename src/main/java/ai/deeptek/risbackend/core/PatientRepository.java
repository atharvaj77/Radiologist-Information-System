package ai.deeptek.risbackend.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Long>{

    @Query("SELECT p FROM Patient p WHERE p.id = :id")
    Page<Patient> findByIdPage(Long id, Pageable page);
    Page<Patient> findByEmailIgnoreCase(String email, Pageable page);

}

