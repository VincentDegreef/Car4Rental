package be.ucll.se.groep26backend.wallet.model;


import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.wallet.service.WalletServiceException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    // @OneToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    private double balance;

    public Wallet() throws WalletServiceException{
    }

    public Wallet(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void subtractBalance(double amount) throws WalletServiceException {
        if (this.balance - amount < 0) {
            throw new WalletServiceException("Not enough balance", "balance");
        }
        this.balance -= amount;
    }

    public long getId() {
        return id;
    }

    
}
