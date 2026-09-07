package br.com.projetodanca.repository;

import br.com.projetodanca.entity.Interessado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteressadoRepository extends JpaRepository<Interessado, Long> {

    boolean existsByEmail(String email);
}