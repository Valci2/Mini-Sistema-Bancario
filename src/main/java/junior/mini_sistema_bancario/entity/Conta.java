package junior.mini_sistema_bancario.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conta")
@NoArgsConstructor
@Getter
@Setter
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank
    private String nome;

    @NonNull
    @NotBlank
    private String senha;

    @NonNull
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    @Column(name = "data_nascimento", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @DecimalMin(value = "0.00")
    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(unique = true, nullable = false)
    private String chaveTransacao;


    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Uma conta tem MUITAS transações
    @JsonIgnore // Evita loop infinito na serialização
    private List<Transacao> transacoes = new ArrayList<>();

    // Método helper para adicionar transação
    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
        transacao.setConta(this);
    }

    public Conta(String nome, String senha, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }
}
