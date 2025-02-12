package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Notificacao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private String titulo;

    @NotNull
    private Instant prazo;

    private CompromissoDTO compromisso;

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

    public Instant getPrazo() {
        return prazo;
    }

    public void setPrazo(Instant prazo) {
        this.prazo = prazo;
    }

    public CompromissoDTO getCompromisso() {
        return compromisso;
    }

    public void setCompromisso(CompromissoDTO compromisso) {
        this.compromisso = compromisso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificacaoDTO)) {
            return false;
        }

        NotificacaoDTO notificacaoDTO = (NotificacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificacaoDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", prazo='" + getPrazo() + "'" +
            ", compromisso=" + getCompromisso() +
            "}";
    }
}
