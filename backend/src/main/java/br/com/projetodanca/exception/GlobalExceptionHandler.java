package br.com.projetodanca.exception;

import br.com.projetodanca.dto.response.ErroResponse;
import br.com.projetodanca.dto.response.ErroValidacaoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponse> tratarEmailJaCadastrado(
            EmailJaCadastradoException excecao
    ) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.CONFLICT.value(),
                "EMAIL_JA_CADASTRADO",
                excecao.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoResponse> tratarErroValidacao(
            MethodArgumentNotValidException excecao
    ) {
        List<String> erros = excecao.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .toList();

        ErroValidacaoResponse resposta = new ErroValidacaoResponse(
                HttpStatus.BAD_REQUEST.value(),
                "ERRO_VALIDACAO",
                "Existem campos inválidos.",
                erros
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException excecao
    ) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                "JSON_INVALIDO",
                "O JSON enviado possui dados inválidos."
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }

    @ExceptionHandler(InteressadoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarInteressadoNaoEncontrado(
            InteressadoNaoEncontradoException excecao
    ) {
        ErroResponse erro = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                "INTERESSADO_NAO_ENCONTRADO",
                excecao.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }
}
