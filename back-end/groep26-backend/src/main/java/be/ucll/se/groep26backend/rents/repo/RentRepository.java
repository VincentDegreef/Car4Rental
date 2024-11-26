package be.ucll.se.groep26backend.rents.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import be.ucll.se.groep26backend.rents.model.Rent;
public interface RentRepository extends JpaRepository<Rent, Long>{
    public Rent findRentById(long id);

    public List<Rent> findRentsByEmail(String email);
}
