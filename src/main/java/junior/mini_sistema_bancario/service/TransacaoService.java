package junior.mini_sistema_bancario.service;

import junior.mini_sistema_bancario.dto.transacao.DepositarDTO;
import junior.mini_sistema_bancario.dto.transacao.SaqueDTO;
import junior.mini_sistema_bancario.dto.transacao.TransferenciaDTO;
import junior.mini_sistema_bancario.entity.Conta;
import junior.mini_sistema_bancario.entity.Transacao;
import junior.mini_sistema_bancario.repository.ContaRepository;
import junior.mini_sistema_bancario.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public TransacaoService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public Transacao depositar(DepositarDTO dto) {
        // 1. Buscar conta
        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        // 2. Validar valor
        if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo");
        }

        // 3. Atualizar saldo
        conta.setSaldo(conta.getSaldo().add(dto.getValor()));
        contaRepository.save(conta);

        // 4. Criar e salvar transação
        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setValor(dto.getValor());
        transacao.setTipo(Transacao.TipoTransacao.DEPOSITO);
        conta.adicionarTransacao(transacao);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao sacar(SaqueDTO dto) {
        // 1. Buscar conta
        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        // 2. Validar chave de transação
        if (!conta.getChaveTransacao().equals(dto.getChaveTransacao())) {
            throw new IllegalArgumentException("Chave de transação inválida");
        }

        // 3. Validar valor
        if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo");
        }

        // 4. Validar saldo
        if (conta.getSaldo().compareTo(dto.getValor()) < 0) {
            throw new IllegalArgumentException(
                    String.format("Saldo insuficiente! Saldo atual: R$ %.2f", conta.getSaldo())
            );
        }

        // 5. Atualizar saldo
        conta.setSaldo(conta.getSaldo().subtract(dto.getValor()));
        contaRepository.save(conta);

        // 6. Criar e salvar transação
        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setValor(dto.getValor());
        transacao.setTipo(Transacao.TipoTransacao.SAQUE);
        conta.adicionarTransacao(transacao);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao transferir(TransferenciaDTO dto) {
        // 1. Buscar contas
        Conta contaOrigem = contaRepository.findById(dto.getContaOrigemId())
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada"));

        Conta contaDestino = contaRepository.findById(dto.getContaDestinoId())
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada"));

        // 2. Validar se são contas diferentes
        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        // 3. Validar chave de transação
        if (!contaOrigem.getChaveTransacao().equals(dto.getChaveTransacaoOrigem())) {
            throw new IllegalArgumentException("Chave de transação da conta origem inválida");
        }

        // 4. Validar valor
        if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }

        // 5. Validar saldo
        if (contaOrigem.getSaldo().compareTo(dto.getValor()) < 0) {
            throw new IllegalArgumentException(
                    String.format("Saldo insuficiente! Saldo atual: R$ %.2f", contaOrigem.getSaldo())
            );
        }

        // 6. Atualizar saldos
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(dto.getValor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(dto.getValor()));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // 7. Criar transação para a conta de origem (SAÍDA)
        Transacao transacaoOrigem = new Transacao();
        transacaoOrigem.setConta(contaOrigem);
        transacaoOrigem.setContaDestino(contaDestino);
        transacaoOrigem.setValor(dto.getValor());
        transacaoOrigem.setTipo(Transacao.TipoTransacao.TRANSFERENCIA_ENVIADA);
        transacaoRepository.save(transacaoOrigem);
        contaOrigem.adicionarTransacao(transacaoOrigem);

        // 8. Criar transação para a conta de destino (ENTRADA)
        Transacao transacaoDestino = new Transacao();
        transacaoDestino.setConta(contaDestino);
        transacaoDestino.setContaDestino(contaOrigem);
        transacaoDestino.setValor(dto.getValor());
        transacaoDestino.setTipo(Transacao.TipoTransacao.TRANSFERENCIA_RECEBIDA);
        contaDestino.adicionarTransacao(transacaoDestino);

        return transacaoRepository.save(transacaoDestino);
    }

    public List<Transacao> extrato(Long contaId) {
        // Verificar se conta existe
        if (!contaRepository.existsById(contaId)) {
            throw new IllegalArgumentException("Conta não encontrada");
        }

        return transacaoRepository.findExtratoCompleto(contaId);
    }
}