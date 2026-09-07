package br.com.projetodanca.dto.response;

import br.com.projetodanca.enums.EstiloDanca;
import br.com.projetodanca.enums.NivelExperiencia;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InteressadoResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento,
        NivelExperiencia nivelExperiencia,
        EstiloDanca estiloDanca,
        String observacoes,
        LocalDateTime dataCadastro
) {
}
