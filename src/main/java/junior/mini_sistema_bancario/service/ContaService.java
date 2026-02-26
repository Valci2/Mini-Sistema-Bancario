package junior.mini_sistema_bancario.service;

import jakarta.transaction.Transactional;
import junior.mini_sistema_bancario.dto.conta.AtualizarContaDTO;
import junior.mini_sistema_bancario.dto.conta.CriarContaDTO;
import junior.mini_sistema_bancario.entity.Conta;
import junior.mini_sistema_bancario.repository.ContaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Transactional
    public Conta criar(CriarContaDTO dto) {

        // 1. Validar email duplicado
        if (contaRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Já existe uma conta cadastrada com este email!");
        }

        // 2. Valida se é maior de idade
        validarMaioridade(dto.getDataNascimento());

        Conta conta = new Conta();
        conta.setNome(dto.getNome());
        conta.setSenha(dto.getSenha());
        conta.setEmail(dto.getEmail());
        conta.setDataNascimento(dto.getDataNascimento());

        // adiciona o saldo e a chave de transação unica
        conta.setSaldo(BigDecimal.ZERO);
        conta.setChaveTransacao(UUID.randomUUID().toString());

        // salva no repositorio
        return contaRepository.save(conta);
    }

    private void validarMaioridade(LocalDate dataNascimento) {
        if (Period.between(dataNascimento, LocalDate.now()).getYears() < 18) {
            throw new RuntimeException("Usuário deve ter pelo menos 18 anos.");
        }
    }

    public List<Conta> listarContas() {
        Sort sort = Sort.by("id").ascending();
        return contaRepository.findAll(sort);
    }

    public Conta procurarPorId(Long id) {
        return contaRepository.findById(id) .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com ID: " + id));
    }

    @Transactional
    public Conta atualizarConta(Long id, AtualizarContaDTO contaAtualizada) {

        // verifica se aquele id existe dentro do banco de dados
        Conta contaExistente = contaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com ID: " + id));

        // Validar se o email já existe em OUTRA conta
        if (!contaExistente.getEmail().equals(contaAtualizada.getEmail()) && contaRepository.existsByEmail(contaAtualizada.getEmail())) {
            throw new RuntimeException("Este email já está sendo usado por outra conta!");
        }

        // 2. Atualizar apenas os campos permitidos
        contaExistente.setNome(contaAtualizada.getNome());
        contaExistente.setSenha(contaAtualizada.getSenha());
        contaExistente.setEmail(contaAtualizada.getEmail());

        // adiciona a mesma chave de transacao e o mesmo saldo.
        return contaRepository.save(contaExistente);
    }

    @Transactional
    public List<Conta> deletarConta(Long id) {

        Conta conta = contaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta com ID " + id + " não encontrada"));

        if (conta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Não é possível deletar uma conta com saldo positivo.");
        }

        contaRepository.deleteById(id);
        return listarContas();
    }
}
