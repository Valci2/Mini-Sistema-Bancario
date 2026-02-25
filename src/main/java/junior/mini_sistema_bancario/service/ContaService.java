package junior.mini_sistema_bancario.service;

import junior.mini_sistema_bancario.dto.conta.AtualizarContaDTO;
import junior.mini_sistema_bancario.entity.Conta;
import junior.mini_sistema_bancario.repository.ContaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public List<Conta> criar(Conta conta) {

        // 1. Validar email duplicado
        if (contaRepository.existsByEmail(conta.getEmail())) {
            throw new RuntimeException("Já existe uma conta cadastrada com este email!");
        }

        // 2. Valida se é maior de idade
        if (!conta.ehMaiorDeIdade()) {
            throw new RuntimeException("Usuário deve ter pelo menos 18 anos.");
        }

        // 3. Valida se o email e realmente valido
        if (!conta.ehUmEmailValido()) {
            throw new RuntimeException("O Usuário deve adicionar um email valido.");
        }

        // adiciona o saldo e a chave de transação unica
        conta.setSaldo(BigDecimal.ZERO);
        conta.setChaveTransacao(UUID.randomUUID().toString());

        // salva no repositorio
        contaRepository.save(conta);
        return listarContas();
    }

    public List<Conta> listarContas() {
        Sort sort = Sort.by("id").ascending();
        return contaRepository.findAll(sort);
    }

    public Optional<Conta> procurarPorId(Long id) {
        return Optional.ofNullable(contaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com ID: " + id)));
    }

    public List<Conta> atualizarConta(Long id, AtualizarContaDTO contaAtualizada) {

        // verifica se aquele id existe dentro do banco de dados
        Conta contaExistente = contaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com ID: " + id));

        // Validar se o email já existe em OUTRA conta
        if (!contaExistente.getEmail().equals(contaAtualizada.getEmail()) &&
                contaRepository.existsByEmail(contaAtualizada.getEmail())) {
            throw new RuntimeException("Este email já está sendo usado por outra conta!");
        }

        // Validar formato do email
        Conta temp = new Conta();
        temp.setEmail(contaAtualizada.getEmail());
        if (!temp.ehUmEmailValido()) {
            throw new RuntimeException("Email inválido!");
        }

        // 2. Atualizar apenas os campos permitidos
        contaExistente.setNome(contaAtualizada.getNome());
        contaExistente.setSenha(contaAtualizada.getSenha());
        contaExistente.setEmail(contaAtualizada.getEmail());

        // adiciona a mesma chave de transacao e o mesmo saldo.
        contaRepository.save(contaExistente);
        return listarContas();
    }

    public List<Conta> deletarConta(Long id) {

        Conta conta = contaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta com ID " + id + " não encontrada"));

        if (conta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Não é possível deletar uma conta com saldo positivo.");
        }

        contaRepository.deleteById(id);
        return listarContas();
    }
}
