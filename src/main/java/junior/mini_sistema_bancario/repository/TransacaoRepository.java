package junior.mini_sistema_bancario.repository;

import junior.mini_sistema_bancario.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    // Busca transações onde a conta é origem OU destino (para transferências)
    @Query("SELECT t FROM Transacao t WHERE t.conta.id = :contaId OR t.contaDestino.id = :contaId ORDER BY t.dataHora DESC")
    List<Transacao> findExtratoCompleto(@Param("contaId") Long contaId);
}
