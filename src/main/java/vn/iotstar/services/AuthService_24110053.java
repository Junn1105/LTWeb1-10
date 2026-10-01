package vn.iotstar.services;

import vn.iotstar.entity.User_24110053;
import java.util.Optional;

public interface AuthService_24110053 {
    Optional<User_24110053> authenticate(String email, String password);
    boolean emailExists(String email);
    void register(User_24110053 user);
}
