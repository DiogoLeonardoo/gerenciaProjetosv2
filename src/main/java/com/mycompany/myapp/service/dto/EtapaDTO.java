package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.StatusEtapa;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Etapa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtapaDTO implements Serializable {

    private Long id;

    @NotNull
    private String descricao;

    @NotNull
    private StatusEtapa status;

    @NotNull
    private Integer ordem;

    private CompromissoDTO compromisso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusEtapa getStatus() {
        return status;
    }

    public void setStatus(StatusEtapa status) {
        this.status = status;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
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
        if (!(o instanceof EtapaDTO)) {
            return false;
        }

        EtapaDTO etapaDTO = (EtapaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, etapaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtapaDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", status='" + getStatus() + "'" +
            ", ordem=" + getOrdem() +
            ", compromisso=" + getCompromisso() +
            "}";
    }
}
