package be.ucll.se.groep26backend.wallet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep26backend.user.service.UserServiceException;
import be.ucll.se.groep26backend.wallet.model.Wallet;
import be.ucll.se.groep26backend.wallet.service.WalletService;
import be.ucll.se.groep26backend.wallet.service.WalletServiceException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/wallets")
public class WalletRestController {
    @Autowired
    private WalletService walletService;

    @GetMapping("")
    public List<Wallet> getAllWallets() throws WalletServiceException {
        return walletService.getAllWallets();
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())")    
    @PutMapping("/{id}/addBalance/{Amount}")
    public Wallet addBalance(@PathVariable long id, @PathVariable double Amount) throws WalletServiceException {
        return walletService.addBalance(id, Amount);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())")    
    @PutMapping("/{id}/subtractBalance/{Amount}")
    public Wallet subtractBalance(@PathVariable long id, @PathVariable double Amount) throws WalletServiceException {
        return walletService.subtractBalance(id, Amount);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())") 
    @DeleteMapping("/{id}")
    public void deleteWallet(@PathVariable long id) throws WalletServiceException {
        walletService.deleteWallet(id);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({
    MethodArgumentNotValidException.class})
    public Map<String, String>
    handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({ UserServiceException.class})
    public Map<String, String>
    handleServiceExceptions(UserServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }


}
