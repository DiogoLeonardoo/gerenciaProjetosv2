package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CompromissoClassificacao;
import com.mycompany.myapp.domain.enumeration.StatusCompromisso;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Compromisso.
 */
@Entity
@Table(name = "compromisso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Compromisso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "data_horario", nullable = false)
    private Instant dataHorario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao", nullable = false)
    private CompromissoClassificacao classificacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusCompromisso status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compromisso")
    @JsonIgnoreProperties(value = { "compromisso" }, allowSetters = true)
    private Set<Etapa> etapas = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compromisso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Compromisso titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Compromisso descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataHorario() {
        return this.dataHorario;
    }

    public Compromisso dataHorario(Instant dataHorario) {
        this.setDataHorario(dataHorario);
        return this;
    }

    public void setDataHorario(Instant dataHorario) {
        this.dataHorario = dataHorario;
    }

    public CompromissoClassificacao getClassificacao() {
        return this.classificacao;
    }

    public Compromisso classificacao(CompromissoClassificacao classificacao) {
        this.setClassificacao(classificacao);
        return this;
    }

    public void setClassificacao(CompromissoClassificacao classificacao) {
        this.classificacao = classificacao;
    }

    public StatusCompromisso getStatus() {
        return this.status;
    }

    public Compromisso status(StatusCompromisso status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusCompromisso status) {
        this.status = status;
    }

    public Set<Etapa> getEtapas() {
        return this.etapas;
    }

    public void setEtapas(Set<Etapa> etapas) {
        if (this.etapas != null) {
            this.etapas.forEach(i -> i.setCompromisso(null));
        }
        if (etapas != null) {
            etapas.forEach(i -> i.setCompromisso(this));
        }
        this.etapas = etapas;
    }

    public Compromisso etapas(Set<Etapa> etapas) {
        this.setEtapas(etapas);
        return this;
    }

    public Compromisso addEtapa(Etapa etapa) {
        this.etapas.add(etapa);
        etapa.setCompromisso(this);
        return this;
    }

    public Compromisso removeEtapa(Etapa etapa) {
        this.etapas.remove(etapa);
        etapa.setCompromisso(null);
        return this;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Compromisso usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compromisso)) {
            return false;
        }
        return getId() != null && getId().equals(((Compromisso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compromisso{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataHorario='" + getDataHorario() + "'" +
            ", classificacao='" + getClassificacao() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
