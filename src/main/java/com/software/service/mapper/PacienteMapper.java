package com.software.service.mapper;


import com.software.domain.*;
import com.software.service.dto.PacienteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {



    default Paciente fromId(Long id) {
        if (id == null) {
            return null;
        }
        Paciente paciente = new Paciente();
        paciente.setId(id);
        return paciente;
    }
}
