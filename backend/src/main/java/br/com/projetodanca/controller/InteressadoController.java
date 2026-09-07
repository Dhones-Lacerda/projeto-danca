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

import java.util.List;

@RestController
@RequestMapping("/api/interessados")
public class InteressadoController {

    private final InteressadoService service;

    public InteressadoController(InteressadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InteressadoResponse> cadastrar(
            @Valid @RequestBody InteressadoRequest request
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

    @GetMapping("/{id}")
    public ResponseEntity<InteressadoResponse> buscarPorId(
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

    @PutMapping("/{id}")
    public ResponseEntity<InteressadoResponse> atualizar(
            @PathVariable Long id,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
