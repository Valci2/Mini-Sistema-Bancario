package junior.mini_sistema_bancario.repository;

import junior.mini_sistema_bancario.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    boolean existsByEmail(String email);
}
