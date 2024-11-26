package be.ucll.se.groep26backend.rent.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.repo.RentRepository;
import be.ucll.se.groep26backend.rents.service.RentService;
import be.ucll.se.groep26backend.rents.service.RentServiceException;

@ExtendWith(MockitoExtension.class)
public class RentServiceTest {
    @Mock
    RentRepository rentRepository;

    @InjectMocks
    RentService rentService;
    private Rent rent1;
    private Rent rent2;
    private Rent rent3;

    @BeforeEach
    public void setup() throws RentServiceException{
        rent1 = new Rent("0491257948", "jago.onzea@student.ucll.be", "01.01.01-001.01", LocalDate.of(2000, 1, 1), "0000000000", null, 0, 0);
        rent2 = new Rent("0491257955", "quentin.gillis@student.ucll.be", "01.01.01-001.02", LocalDate.of(2000, 1, 2), "0000000001", null, 0, 0);
    }

    @Test
    public void givenNoRents_whenGetAllRents_thenThrowRentServiceException() throws RentServiceException{
        // given
        // when
        // then
        assertThrows(RentServiceException.class, () -> rentService.getAllRents());
    }

    @Test
    public void givenNoRents_whenCancelRent_thenThrowRentServiceException() throws RentServiceException{
        // given
        // when
        // then
        assertThrows(RentServiceException.class, () -> rentService.cancelRent(1));
    }
}