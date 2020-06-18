package com.software.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.software.domain.Paciente} entity. This class is used
 * in {@link com.software.web.rest.PacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PacienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombres;

    private StringFilter apellidos;

    private LocalDateFilter fechaNacimiento;

    private StringFilter numeroIdentificacion;

    private StringFilter direccion;

    public PacienteCriteria() {
    }

    public PacienteCriteria(PacienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombres = other.nombres == null ? null : other.nombres.copy();
        this.apellidos = other.apellidos == null ? null : other.apellidos.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.numeroIdentificacion = other.numeroIdentificacion == null ? null : other.numeroIdentificacion.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
    }

    @Override
    public PacienteCriteria copy() {
        return new PacienteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombres() {
        return nombres;
    }

    public void setNombres(StringFilter nombres) {
        this.nombres = nombres;
    }

    public StringFilter getApellidos() {
        return apellidos;
    }

    public void setApellidos(StringFilter apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public StringFilter getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(StringFilter numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PacienteCriteria that = (PacienteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombres, that.nombres) &&
            Objects.equals(apellidos, that.apellidos) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(numeroIdentificacion, that.numeroIdentificacion) &&
            Objects.equals(direccion, that.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombres,
        apellidos,
        fechaNacimiento,
        numeroIdentificacion,
        direccion
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombres != null ? "nombres=" + nombres + ", " : "") +
                (apellidos != null ? "apellidos=" + apellidos + ", " : "") +
                (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
                (numeroIdentificacion != null ? "numeroIdentificacion=" + numeroIdentificacion + ", " : "") +
                (direccion != null ? "direccion=" + direccion + ", " : "") +
            "}";
    }

}
