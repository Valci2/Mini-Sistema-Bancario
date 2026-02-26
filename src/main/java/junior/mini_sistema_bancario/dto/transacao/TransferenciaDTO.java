package junior.mini_sistema_bancario.dto.transacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferenciaDTO {

    @NotNull(message = "ID da conta de origem é obrigatório")
    private Long contaOrigemId;

    @NotNull(message = "ID da conta de destino é obrigatório")
    private Long contaDestinoId;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
    private BigDecimal valor;

    @NotNull(message = "Chave de transação da conta origem é obrigatória")
    private String chaveTransacaoOrigem;
}