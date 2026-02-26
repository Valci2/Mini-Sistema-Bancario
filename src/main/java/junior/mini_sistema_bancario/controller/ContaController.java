package junior.mini_sistema_bancario.controller;

import jakarta.validation.Valid;
import junior.mini_sistema_bancario.dto.conta.AtualizarContaDTO;
import junior.mini_sistema_bancario.dto.conta.CriarContaDTO;
import junior.mini_sistema_bancario.entity.Conta;
import junior.mini_sistema_bancario.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conta") // indica a rota que o controler vai atuar
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<Conta> criar(@RequestBody @Valid CriarContaDTO conta) {
        return ResponseEntity.ok(contaService.criar(conta));
    }

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        return ResponseEntity.ok(contaService.listarContas());
    }

    @GetMapping("{id}")
    public ResponseEntity<Conta> procurarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contaService.procurarPorId(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<Conta> atualizarConta(@PathVariable("id") Long id, @RequestBody @Valid AtualizarContaDTO conta){
        return ResponseEntity.ok(contaService.atualizarConta(id, conta));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<List<Conta>> deletarConta(@PathVariable("id") Long id){
        return ResponseEntity.ok(contaService.deletarConta(id));
    }
}
