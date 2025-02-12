package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.StatusEtapa;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Etapa.
 */
@Entity
@Table(name = "etapa")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etapa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEtapa status;

    @NotNull
    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "etapas", "usuario" }, allowSetters = true)
    private Compromisso compromisso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etapa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Etapa descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusEtapa getStatus() {
        return this.status;
    }

    public Etapa status(StatusEtapa status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusEtapa status) {
        this.status = status;
    }

    public Integer getOrdem() {
        return this.ordem;
    }

    public Etapa ordem(Integer ordem) {
        this.setOrdem(ordem);
        return this;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Compromisso getCompromisso() {
        return this.compromisso;
    }

    public void setCompromisso(Compromisso compromisso) {
        this.compromisso = compromisso;
    }

    public Etapa compromisso(Compromisso compromisso) {
        this.setCompromisso(compromisso);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etapa)) {
            return false;
        }
        return getId() != null && getId().equals(((Etapa) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etapa{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", status='" + getStatus() + "'" +
            ", ordem=" + getOrdem() +
            "}";
    }
}
