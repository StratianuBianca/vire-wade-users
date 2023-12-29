package info.wade.users.repository;

import info.wade.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>
{
    User findFirstByEmail(String email);
    User findByEmail(String email);

    Optional<User> findById(UUID uuid);
}
