package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class NotificacaoDTO implements Serializable {

    private Long id;
    private String titulo;
    private Instant prazo;
    private Set<String> convidados; // Aqui é onde os convidados entram
    private Long compromisso;

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

    public Set<String> getConvidados() {
        return convidados;
    }

    public void setConvidados(Set<String> convidados) {
        this.convidados = convidados;
    }

    public Long getCompromisso() {
        return compromisso;
    }

    public void setCompromisso(Long compromisso) {
        this.compromisso = compromisso;
    }
    // Outros métodos, como equals, hashCode, toString
}
