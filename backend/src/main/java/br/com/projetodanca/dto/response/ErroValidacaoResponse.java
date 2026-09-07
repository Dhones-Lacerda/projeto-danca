package br.com.projetodanca.dto.response;

import java.util.List;

public record ErroValidacaoResponse(
        int status,
        String codigo,
        String mensagem,
        List<String> erros
) {
}
