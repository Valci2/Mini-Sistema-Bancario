package junior.mini_sistema_bancario.repository;

import junior.mini_sistema_bancario.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByEmail(String email);
    boolean existsByEmail(String email);
}
