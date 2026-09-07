package br.com.projetodanca.entity;

import br.com.projetodanca.enums.EstiloDanca;
import br.com.projetodanca.enums.NivelExperiencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interessado")
public class Interessado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "nivel_experiencia",
            nullable = false,
            length = 30
    )
    private NivelExperiencia nivelExperiencia;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "estilo_danca",
            nullable = false,
            length = 50
    )
    private EstiloDanca estiloDanca;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Column(
            name = "data_cadastro",
            nullable = false,
            updatable = false,
            insertable = false
    )
    private LocalDateTime dataCadastro;

    protected Interessado() {
    }

    public Interessado(
            String nome,
            String email,
            String telefone,
            LocalDate dataNascimento,
            NivelExperiencia nivelExperiencia,
            EstiloDanca estiloDanca,
            String observacoes
    ) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.nivelExperiencia = nivelExperiencia;
        this.estiloDanca = estiloDanca;
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public NivelExperiencia getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(NivelExperiencia nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public EstiloDanca getEstiloDanca() {
        return estiloDanca;
    }

    public void setEstiloDanca(EstiloDanca estiloDanca) {
        this.estiloDanca = estiloDanca;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}
