package br.com.projetodanca.controller;

import br.com.projetodanca.dto.request.InteressadoRequest;
import br.com.projetodanca.dto.response.InteressadoResponse;
import br.com.projetodanca.entity.Interessado;
import br.com.projetodanca.service.InteressadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/interessados")
public class InteressadoController {

    private final InteressadoService service;

    public InteressadoController(InteressadoService service) {
        this.service = service;
    }

    @Operation(
        summary = "Cadastrar um novo interessado",
        description = "Endpoint para cadastrar um novo interessado no sistema. Recebe os dados do interessado no corpo da requisição e retorna os dados cadastrados com o ID gerado."
    )

    @ApiResponse (
        responseCode = "201", 
        description = "Interessado cadastrado com sucesso",
        content = @Content (
                schema = @Schema(implementation = InteressadoResponse.class)
        )
    )

    @ApiResponse (
        responseCode = "400", 
        description = "Dados enviados são inválidos. Verifique os dados enviados."
    )

    @ApiResponse (
        responseCode = "409", 
        description = "Já existe um interessado cadastrado com este e-mail."
    )


    @PostMapping
    public ResponseEntity<InteressadoResponse> cadastrar(
            @Valid 
            @io.swagger.v3.oas.annotations.parameters.RequestBody (
                description = "Dados do interessado a ser cadastrado"
            )
            @RequestBody InteressadoRequest request
    ) {
        Interessado interessado = new Interessado(
                request.nome(),
                request.email(),
                request.telefone(),
                request.dataNascimento(),
                request.nivelExperiencia(),
                request.estiloDanca(),
                request.observacoes()
        );

        Interessado salvo = service.cadastrar(interessado);

        InteressadoResponse response = new InteressadoResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getTelefone(),
                salvo.getDataNascimento(),
                salvo.getNivelExperiencia(),
                salvo.getEstiloDanca(),
                salvo.getObservacoes(),
                salvo.getDataCadastro()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation (
        summary = "Listar todos os interessados",
        description = "Retorna todos os interessados cadastrados, ordenados do mais recente para o mais antigo."
    )

    @ApiResponse (
        responseCode = "200",
        description = "Lista de interessados retornada com sucesso",
        content = @Content (
                schema = @Schema(
                        implementation = InteressadoResponse.class
                )
        )
    )

    @GetMapping
    public ResponseEntity<List<InteressadoResponse>> listarTodos() {

        List<Interessado> interessados = service.listarTodos();

        List<InteressadoResponse> response = interessados.stream()
                .map(interessado -> new InteressadoResponse(
                        interessado.getId(),
                        interessado.getNome(),
                        interessado.getEmail(),
                        interessado.getTelefone(),
                        interessado.getDataNascimento(),
                        interessado.getNivelExperiencia(),
                        interessado.getEstiloDanca(),
                        interessado.getObservacoes(),
                        interessado.getDataCadastro()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation (
        summary = "Buscar interessado por ID",
        description = "Retorna os dados de um interessado específico com base no ID fornecido."
    )

        @ApiResponse (
                responseCode = "200",
                description = "Interessado encontrado com sucesso",
                content = @Content (
                        schema = @Schema(
                                implementation = InteressadoResponse.class
                        )
                )
        )

        @ApiResponse (
                responseCode = "404",
                description = "Interessado não encontrado com o ID fornecido"
        )

    @GetMapping("/{id}")
    public ResponseEntity<InteressadoResponse> buscarPorId(
            @Parameter  (
                
                description = "ID do interessado a ser buscado",
                example = "1"
            )

            @PathVariable Long id
    ) {
        Interessado interessado = service.buscarPorId(id);

        InteressadoResponse response = new InteressadoResponse(
                interessado.getId(),
                interessado.getNome(),
                interessado.getEmail(),
                interessado.getTelefone(),
                interessado.getDataNascimento(),
                interessado.getNivelExperiencia(),
                interessado.getEstiloDanca(),
                interessado.getObservacoes(),
                interessado.getDataCadastro()
        );

        return ResponseEntity.ok(response);
    }

    @Operation (
        summary = "Atualizar dados de um interessado",
        description = "Atualiza os dados de um interessado específico com base no ID fornecido."
    )

    @ApiResponse (
        responseCode = "200",
        description = "Interessado atualizado com sucesso",
        content = @Content (
                schema = @Schema(
                        implementation = InteressadoResponse.class
                )
        )
    )

        @ApiResponse (
                responseCode = "400",
                description = "Dados enviados são inválidos."
        )

        @ApiResponse (
                responseCode = "404",
                description = "Interessado não encontrado."
        )

        @ApiResponse (
                responseCode = "409",
                description = "Já existe um interessado cadastrado com este e-mail."
        )

    @PutMapping("/{id}")
    public ResponseEntity<InteressadoResponse> atualizar(
            @Parameter (
                description = "ID do interessado a ser atualizado",
                example = "1"
            ) 

            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody (
                description = "Dados atualizados do interessado"
            )
            @Valid @RequestBody InteressadoRequest request
    ) {
        Interessado dadosAtualizados = new Interessado(
                request.nome(),
                request.email(),
                request.telefone(),
                request.dataNascimento(),
                request.nivelExperiencia(),
                request.estiloDanca(),
                request.observacoes()
        );

        Interessado atualizado = service.atualizar(
                id,
                dadosAtualizados
        );

        InteressadoResponse response = new InteressadoResponse(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getEmail(),
                atualizado.getTelefone(),
                atualizado.getDataNascimento(),
                atualizado.getNivelExperiencia(),
                atualizado.getEstiloDanca(),
                atualizado.getObservacoes(),
                atualizado.getDataCadastro()
        );

        return ResponseEntity.ok(response);
    }

        @Operation (
                summary = "Excluir um interessado",
                description = "Exclui um interessado específico com base no ID fornecido."
        )

        @ApiResponse (
                responseCode = "204",
                description = "Interessado excluído com sucesso"
        )

        @ApiResponse (
                responseCode = "404",
                description = "Interessado não encontrado."
        )

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
        @Parameter (
                        description = "ID do interessado a ser excluído",
                        example = "1"
                )
        @PathVariable Long id
    ) {
        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
