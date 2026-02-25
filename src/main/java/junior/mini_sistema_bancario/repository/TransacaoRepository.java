package junior.mini_sistema_bancario.repository;

import junior.mini_sistema_bancario.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
