package br.com.projetodanca.service;

import br.com.projetodanca.entity.Interessado;
import br.com.projetodanca.exception.EmailJaCadastradoException;
import br.com.projetodanca.exception.InteressadoNaoEncontradoException;
import br.com.projetodanca.repository.InteressadoRepository;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InteressadoService {

    private final InteressadoRepository repository;
    private final EntityManager entityManager;

    public InteressadoService(
        InteressadoRepository repository,
        EntityManager entityManager
    ) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Interessado cadastrar(Interessado interessado) {
        if (repository.existsByEmail(interessado.getEmail())) {
            throw new EmailJaCadastradoException(
                    "Já existe um interessado cadastrado com este e-mail."
            );
        }

        Interessado salvo = repository.save(interessado);

        entityManager.refresh(salvo);

        return salvo;
    }

    public List<Interessado> listarTodos() {
        return repository.findAllByOrderByDataCadastroDesc();
    }

    public Interessado buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new InteressadoNaoEncontradoException(
                        "Interessado não encontrado."
                ));
    }

    public Interessado atualizar(
            Long id,
            Interessado dadosAtualizados
    ) {
        Interessado interessado = buscarPorId(id);

        if (!interessado.getEmail().equals(dadosAtualizados.getEmail())
                && repository.existsByEmail(dadosAtualizados.getEmail())) {

            throw new EmailJaCadastradoException(
                    "Já existe um interessado cadastrado com este e-mail."
            );
        }

        interessado.setNome(dadosAtualizados.getNome());
        interessado.setEmail(dadosAtualizados.getEmail());
        interessado.setTelefone(dadosAtualizados.getTelefone());
        interessado.setDataNascimento(dadosAtualizados.getDataNascimento());
        interessado.setNivelExperiencia(dadosAtualizados.getNivelExperiencia());
        interessado.setEstiloDanca(dadosAtualizados.getEstiloDanca());
        interessado.setObservacoes(dadosAtualizados.getObservacoes());

        return repository.save(interessado);
    }

    public void excluir(Long id) {
        Interessado interessado = buscarPorId(id);

        repository.delete(interessado);
    }
}
