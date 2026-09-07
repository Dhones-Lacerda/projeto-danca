package br.com.projetodanca.service;

import br.com.projetodanca.entity.Interessado;
import br.com.projetodanca.exception.EmailJaCadastradoException;
import br.com.projetodanca.exception.InteressadoNaoEncontradoException;
import br.com.projetodanca.repository.InteressadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteressadoService {

    private final InteressadoRepository repository;

    public InteressadoService(InteressadoRepository repository) {
        this.repository = repository;
    }

    public Interessado cadastrar(Interessado interessado) {
        if (repository.existsByEmail(interessado.getEmail())) {
            throw new EmailJaCadastradoException(
                    "Já existe um interessado cadastrado com este e-mail."
            );
        }

        return repository.save(interessado);
    }

    public List<Interessado> listarTodos() {
        return repository.findAll();
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
