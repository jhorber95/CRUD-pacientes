package com.software.service.dto;

import com.software.domain.enumeration.TipoIdentificacion;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.software.domain.Paciente} entity.
 */
@Data
public class PacienteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombres;

    @NotNull
    private String apellidos;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private TipoIdentificacion tipoIdentificacion;

    @NotNull
    private String numeroIdentificacion;

    private String direccion;

}
