package junior.mini_sistema_bancario.dto.conta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AtualizarContaDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
}