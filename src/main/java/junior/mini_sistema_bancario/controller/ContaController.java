package junior.mini_sistema_bancario.controller;

import jakarta.validation.Valid;
import junior.mini_sistema_bancario.dto.conta.AtualizarContaDTO;
import junior.mini_sistema_bancario.entity.Conta;
import junior.mini_sistema_bancario.service.ContaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conta") // indica a rota que o controler vai atuar
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    List<Conta> criar(@RequestBody @Valid Conta conta) {
        return contaService.criar(conta);
    }

    @GetMapping
    List<Conta> listarContas() {
        return contaService.listarContas();
    }

    @GetMapping("{id}")
    Optional<Conta> procurarPorId(@PathVariable("id") Long id) {
        return contaService.procurarPorId(id);
    }

    @PutMapping("{id}")
    List<Conta> atualizarConta(@PathVariable("id") Long id, @RequestBody AtualizarContaDTO conta){
        return contaService.atualizarConta(id, conta);
    }

    @DeleteMapping("{id}")
    List<Conta> deletarConta(@PathVariable("id") Long id){
        return contaService.deletarConta(id);
    }
}
