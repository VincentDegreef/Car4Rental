package be.ucll.se.groep26backend.complaints.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.ucll.se.groep26backend.complaints.model.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    // Add custom query methods if needed
}
