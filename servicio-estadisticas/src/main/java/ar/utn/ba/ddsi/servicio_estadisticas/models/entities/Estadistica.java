package ar.utn.ba.ddsi.servicio_estadisticas.models.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "estadistica")
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "clave", nullable = false)
    private String clave;

    @Column(name = "valor", nullable = false)
    private Long valor;

    @Column(name = "fecha_calculo", nullable = false)
    private LocalDate fechaCalculo;

    public Estadistica() {}

    public String getClave() {
        return clave;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getValor() {
        return valor;
    }

    public LocalDate getFechaCalculo() {
        return fechaCalculo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public void setFechaCalculo(LocalDate fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }
}