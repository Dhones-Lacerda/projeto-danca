package br.com.projetodanca.dto.response;

public record ErroResponse(
        int status,
        String codigo,
        String mensagem
) {
}
