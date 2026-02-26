package junior.mini_sistema_bancario.controller;

import junior.mini_sistema_bancario.dto.transacao.DepositarDTO;
import junior.mini_sistema_bancario.dto.transacao.SaqueDTO;
import junior.mini_sistema_bancario.dto.transacao.TransferenciaDTO;
import junior.mini_sistema_bancario.entity.Transacao;
import junior.mini_sistema_bancario.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/deposito")
    public ResponseEntity<Transacao> depositar(@RequestBody @Valid DepositarDTO dto) {
        return ResponseEntity.ok(transacaoService.depositar(dto));
    }

    @PostMapping("/saque")
    public ResponseEntity<Transacao> sacar(@RequestBody @Valid SaqueDTO dto) {
        return ResponseEntity.ok(transacaoService.sacar(dto));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Transacao> transferir(@RequestBody @Valid TransferenciaDTO dto) {
        return ResponseEntity.ok(transacaoService.transferir(dto));
    }

    @GetMapping("/extrato/{contaId}")
    public ResponseEntity<List<Transacao>> extrato(@PathVariable Long contaId) {
        return ResponseEntity.ok(transacaoService.extrato(contaId));
    }
}