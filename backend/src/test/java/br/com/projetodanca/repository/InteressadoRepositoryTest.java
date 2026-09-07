package br.com.projetodanca.repository;

import br.com.projetodanca.entity.Interessado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.projetodanca.enums.EstiloDanca;
import br.com.projetodanca.enums.NivelExperiencia;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class InteressadoRepositoryTest {

    @Autowired
    private InteressadoRepository repository;

    @Test
    void deveVerificarSeEmailExiste() {
    Interessado interessado = new Interessado(
        "Maria Silva",
        "maria.teste@example.com",
        "11999999999",
        LocalDate.of(1995, 5, 20),
        NivelExperiencia.INICIANTE,
        EstiloDanca.SALSA,
        "Teste do repository"
      );
    repository.save(interessado);

    boolean existe = repository.existsByEmail("maria.teste@example.com");

    assertTrue(existe);
    }
}
