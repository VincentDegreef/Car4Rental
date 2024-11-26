package be.ucll.se.groep26backend.wallet.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.se.groep26backend.wallet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long>{

    public List<Wallet> findAll();

    public Wallet findWalletById(long id);
}
