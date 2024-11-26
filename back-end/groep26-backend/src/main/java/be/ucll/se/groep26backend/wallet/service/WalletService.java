package be.ucll.se.groep26backend.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;
import be.ucll.se.groep26backend.wallet.model.Wallet;
import be.ucll.se.groep26backend.wallet.repo.WalletRepository;
import be.ucll.se.groep26backend.wallet.service.WalletServiceException;

import jakarta.validation.Valid;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet createWallet() {
        Wallet wallet = new Wallet(0);
        return walletRepository.save(wallet);
        
    }

    public Wallet getWalletById(long id) {
        return walletRepository.findById(id).get();
    }


    public Wallet addBalance(long id, double Amount) throws WalletServiceException {
        Wallet OldWallet = walletRepository.findById(id)
            .orElseThrow(() -> new WalletServiceException("Wallet not found", "walletId"));
        OldWallet.addBalance(Amount);
        if(userRepository.findUserByWalletId(id) == null){
            throw new WalletServiceException("User not found", "userId");
        }
        User user = userRepository.findUserByWalletId(id);

        walletRepository.save(OldWallet);
        user.setWallet(OldWallet);
        userRepository.save(user);
        return walletRepository.save(OldWallet);
    }
    
    public Wallet subtractBalance(long id, double Amount) throws WalletServiceException {
        Wallet OldWallet = walletRepository.findById(id)
            .orElseThrow(() -> new WalletServiceException("Wallet not found", "walletId"));
        OldWallet.subtractBalance(Amount);
        if(userRepository.findUserByWalletId(id) == null){
            throw new WalletServiceException("User not found", "userId");
        }
        User user = userRepository.findUserByWalletId(id);

        walletRepository.save(OldWallet);
        user.setWallet(OldWallet);
        userRepository.save(user);
        return walletRepository.save(OldWallet);
    }

    public void deleteWallet(long id) {
        walletRepository.deleteById(id);
    }


}
