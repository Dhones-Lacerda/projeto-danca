package br.com.projetodanca.dto.request;

import br.com.projetodanca.enums.EstiloDanca;
import br.com.projetodanca.enums.NivelExperiencia;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record InteressadoRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(
                max = 150,
                message = "O nome deve ter no máximo 150 caracteres."
        )
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail deve ter um formato válido.")
        @Size(
                max = 150,
                message = "O e-mail deve ter no máximo 150 caracteres."
        )
        String email,

        @NotBlank(message = "O telefone é obrigatório.")
        @Size(
                max = 20,
                message = "O telefone deve ter no máximo 20 caracteres."
        )
        String telefone,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve estar no passado.")
        LocalDate dataNascimento,

        @NotNull(message = "O nível de experiência é obrigatório.")
        NivelExperiencia nivelExperiencia,

        @NotNull(message = "O estilo de dança é obrigatório.")
        EstiloDanca estiloDanca,

        @Size(
                max = 500,
                message = "As observações devem ter no máximo 500 caracteres."
        )
        String observacoes
) {
}
