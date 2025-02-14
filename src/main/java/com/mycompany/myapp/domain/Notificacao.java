package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * A Notificacao.
 */
@Entity
@Table(name = "notificacao")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notificacao implements Serializable {

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
    @Column(name = "prazo", nullable = false)
    private Instant prazo;

    @ElementCollection
    @CollectionTable(name = "notificacao_convidados", joinColumns = @JoinColumn(name = "notificacao_id"))
    @Column(name = "convidado")
    private Set<String> convidados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "etapas", "usuario" }, allowSetters = true)
    private Compromisso compromisso;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notificacao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Notificacao titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Instant getPrazo() {
        return this.prazo;
    }

    public Notificacao prazo(Instant prazo) {
        this.setPrazo(prazo);
        return this;
    }

    public void setPrazo(Instant prazo) {
        this.prazo = prazo;
    }

    public Compromisso getCompromisso() {
        return this.compromisso;
    }

    public void setCompromisso(Compromisso compromisso) {
        this.compromisso = compromisso;
    }

    public Notificacao compromisso(Compromisso compromisso) {
        this.setCompromisso(compromisso);
        return this;
    }

    public Set<String> getConvidados() {
        return convidados;
    }

    // Setter
    public void setConvidados(Set<String> convidados) {
        this.convidados = convidados;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacao)) {
            return false;
        }
        return getId() != null && getId().equals(((Notificacao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notificacao{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", prazo='" + getPrazo() + "'" +
            "}";
    }
}
