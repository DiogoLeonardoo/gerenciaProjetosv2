package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.CompromissoClassificacao;
import com.mycompany.myapp.domain.enumeration.StatusCompromisso;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Compromisso} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompromissoDTO implements Serializable {

    private Long id;

    @NotNull
    private String titulo;

    @NotNull
    private String descricao;

    @NotNull
    private Instant dataHorario;

    @NotNull
    private CompromissoClassificacao classificacao;

    @NotNull
    private StatusCompromisso status;

    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataHorario() {
        return dataHorario;
    }

    public void setDataHorario(Instant dataHorario) {
        this.dataHorario = dataHorario;
    }

    public CompromissoClassificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(CompromissoClassificacao classificacao) {
        this.classificacao = classificacao;
    }

    public StatusCompromisso getStatus() {
        return status;
    }

    public void setStatus(StatusCompromisso status) {
        this.status = status;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompromissoDTO)) {
            return false;
        }

        CompromissoDTO compromissoDTO = (CompromissoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compromissoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompromissoDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataHorario='" + getDataHorario() + "'" +
            ", classificacao='" + getClassificacao() + "'" +
            ", status='" + getStatus() + "'" +
            ", usuario=" + getUsuario() +
            "}";
    }
}
