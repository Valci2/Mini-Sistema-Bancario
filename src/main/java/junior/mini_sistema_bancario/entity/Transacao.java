package junior.mini_sistema_bancario.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacao")
@Getter
@Setter
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Column(unique = true, nullable = false)
    private String codigoTransacao;

    // MUITAS transações pertencem a UMA conta (conta de origem/dono)
    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    @JsonIgnore
    private Conta conta;

    // Para transferências: conta de destino (pode ser null em depósitos/saques)
    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    public enum TipoTransacao {
        DEPOSITO, SAQUE, TRANSFERENCIA_ENVIADA, TRANSFERENCIA_RECEBIDA
    }

    @PrePersist
    protected void onCreate() {
        dataHora = LocalDateTime.now();
        if (this.codigoTransacao == null) {
            this.codigoTransacao = UUID.randomUUID().toString();
        }
    }
}