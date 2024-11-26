package be.ucll.se.groep26backend.rental.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.ucll.se.groep26backend.rental.model.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    public Rental findRentalById(long id);
    
    public List<Rental> findRentalsByEmail(String email);

    public List<Rental> findRentalsByCarBrand(String brand);

    public List<Rental> findRentalsByCity(String city);

    public List<Rental> findRentalsByStartDate(LocalDate startDate);
    
    public List<Rental> findRentalsByEndDate(LocalDate endDate);
}
