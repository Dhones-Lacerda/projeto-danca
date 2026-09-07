package br.com.projetodanca.service;

import br.com.projetodanca.entity.Interessado;
import br.com.projetodanca.enums.EstiloDanca;
import br.com.projetodanca.enums.NivelExperiencia;
import br.com.projetodanca.exception.EmailJaCadastradoException;
import br.com.projetodanca.exception.InteressadoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class InteressadoServiceTest {

    @Autowired
    private InteressadoService service;

    @Test
    void deveCadastrarInteressado() {
        Interessado interessado = new Interessado(
                "João Silva",
                "joao.teste@example.com",
                "11988888888",
                LocalDate.of(1998, 10, 15),
                NivelExperiencia.INICIANTE,
                EstiloDanca.FORRO,
                "Teste do service"
        );

        Interessado salvo = service.cadastrar(interessado);

        assertNotNull(salvo.getId());
        assertEquals("João Silva", salvo.getNome());
        assertEquals("joao.teste@example.com", salvo.getEmail());
    }

    @Test
    void deveImpedirCadastroComEmailDuplicado() {
        Interessado primeiro = new Interessado(
                "João Silva",
                "duplicado.teste@example.com",
                "11988888888",
                LocalDate.of(1998, 10, 15),
                NivelExperiencia.INICIANTE,
                EstiloDanca.FORRO,
                "Primeiro cadastro"
        );

        service.cadastrar(primeiro);

        Interessado segundo = new Interessado(
                "Maria Silva",
                "duplicado.teste@example.com",
                "11999999999",
                LocalDate.of(1995, 5, 20),
                NivelExperiencia.INICIANTE,
                EstiloDanca.SALSA,
                "Segundo cadastro"
        );

        EmailJaCadastradoException excecao = assertThrows(
                EmailJaCadastradoException.class,
                () -> service.cadastrar(segundo)
        );

        assertEquals(
                "Já existe um interessado cadastrado com este e-mail.",
                excecao.getMessage()
        );
    }

    @Test
    void deveAtualizarInteressado() {
        Interessado interessado = new Interessado(
                "Carlos Silva",
                "carlos.atualizacao@example.com",
                "11977777777",
                LocalDate.of(1990, 3, 10),
                NivelExperiencia.INICIANTE,
                EstiloDanca.SAMBA,
                "Cadastro original"
        );

        Interessado salvo = service.cadastrar(interessado);

        Interessado dadosAtualizados = new Interessado(
                "Carlos Oliveira",
                "carlos.atualizado@example.com",
                "11966666666",
                LocalDate.of(1991, 4, 15),
                NivelExperiencia.INTERMEDIARIO,
                EstiloDanca.FORRO,
                "Dados atualizados"
        );

        Interessado atualizado = service.atualizar(
                salvo.getId(),
                dadosAtualizados
        );

        assertEquals(salvo.getId(), atualizado.getId());
        assertEquals("Carlos Oliveira", atualizado.getNome());
        assertEquals("carlos.atualizado@example.com", atualizado.getEmail());
        assertEquals("11966666666", atualizado.getTelefone());
        assertEquals(
                LocalDate.of(1991, 4, 15),
                atualizado.getDataNascimento()
        );
        assertEquals(
                NivelExperiencia.INTERMEDIARIO,
                atualizado.getNivelExperiencia()
        );
        assertEquals(
                EstiloDanca.FORRO,
                atualizado.getEstiloDanca()
        );
        assertEquals("Dados atualizados", atualizado.getObservacoes());
    }

    @Test
    void deveImpedirAtualizacaoComEmailDuplicado() {
        Interessado primeiro = new Interessado(
                "Joao Silva",
                "joao.atualizacao@example.com",
                "11988888888",
                LocalDate.of(1995, 5, 20),
                NivelExperiencia.INICIANTE,
                EstiloDanca.SALSA,
                "Primeiro interessado"
        );

        Interessado segundo = new Interessado(
                "Maria Silva",
                "maria.atualizacao@example.com",
                "11999999999",
                LocalDate.of(1996, 6, 15),
                NivelExperiencia.INICIANTE,
                EstiloDanca.FORRO,
                "Segundo interessado"
        );

        Interessado primeiroSalvo = service.cadastrar(primeiro);
        service.cadastrar(segundo);

        Interessado dadosAtualizados = new Interessado(
                "Joao Silva Atualizado",
                "maria.atualizacao@example.com",
                "11977777777",
                LocalDate.of(1994, 4, 10),
                NivelExperiencia.INTERMEDIARIO,
                EstiloDanca.SAMBA,
                "Tentativa de usar e-mail existente"
        );

        EmailJaCadastradoException excecao = assertThrows(
                EmailJaCadastradoException.class,
                () -> service.atualizar(
                        primeiroSalvo.getId(),
                        dadosAtualizados
                )
        );

        assertEquals(
                "Já existe um interessado cadastrado com este e-mail.",
                excecao.getMessage()
        );
    }

    @Test
    void deveExcluirInteressado() {
        Interessado interessado = new Interessado(
                "Pedro Silva",
                "pedro.exclusao@example.com",
                "11955555555",
                LocalDate.of(1992, 7, 20),
                NivelExperiencia.INICIANTE,
                EstiloDanca.BACHATA,
                "Teste de exclusao"
        );

        Interessado salvo = service.cadastrar(interessado);

        service.excluir(salvo.getId());

        assertThrows(
                InteressadoNaoEncontradoException.class,
                () -> service.buscarPorId(salvo.getId())
        );
    }

    @Test
    void deveImpedirExclusaoDeInteressadoInexistente() {
        assertThrows(
                InteressadoNaoEncontradoException.class,
                () -> service.excluir(9999L)
        );
    }
}
