package junior.mini_sistema_bancario.dto.transacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositarDTO {

    @NotNull(message = "ID da conta é obrigatório")
    private Long contaId;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor mínimo é R$ 0,01")
    private BigDecimal valor;
}