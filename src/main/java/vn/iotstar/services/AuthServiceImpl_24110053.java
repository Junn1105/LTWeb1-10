package vn.iotstar.services;

import vn.iotstar.entity.User_24110053;
import vn.iotstar.repository.LibraryRepositoryImpl_24110053;
import vn.iotstar.repository.LibraryRepository_24110053;
import java.time.LocalDateTime;
import java.util.Optional;

public class AuthServiceImpl_24110053 implements AuthService_24110053 {
    private final LibraryRepository_24110053 repository = new LibraryRepositoryImpl_24110053();
    @Override public Optional<User_24110053> authenticate(String email, String password) {
        Optional<User_24110053> user = repository.findUserByEmail(email)
                .filter(item -> item.getPasswd().equals(password));
        user.ifPresent(item -> { repository.updateLastLogin(item.getId()); item.setLastLogin(LocalDateTime.now()); });
        return user;
    }
    @Override public boolean emailExists(String email) { return repository.findUserByEmail(email).isPresent(); }
    @Override public void register(User_24110053 user) {
        user.setSignupDate(LocalDateTime.now());
        user.setAdmin(false);
        repository.saveUser(user);
    }
}
