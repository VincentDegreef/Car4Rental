package be.ucll.se.groep26backend.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.se.groep26backend.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    public User findUserByUsername(String username);

    public User findUserByEmail(String email);

    public User findUserById(long id);

    public User findUserByWalletId(long walletId);
}
