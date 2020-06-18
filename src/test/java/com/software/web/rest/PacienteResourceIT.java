package com.software.web.rest;

import com.software.CrudApp;
import com.software.domain.Paciente;
import com.software.repository.PacienteRepository;
import com.software.service.PacienteService;
import com.software.service.dto.PacienteDTO;
import com.software.service.mapper.PacienteMapper;
import com.software.service.dto.PacienteCriteria;
import com.software.service.PacienteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@SpringBootTest(classes = CrudApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PacienteResourceIT {

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NUMERO_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_IDENTIFICACION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteQueryService pacienteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .numeroIdentificacion(DEFAULT_NUMERO_IDENTIFICACION)
            .direccion(DEFAULT_DIRECCION);
        return paciente;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION);
        return paciente;
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testPaciente.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testPaciente.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testPaciente.getNumeroIdentificacion()).isEqualTo(DEFAULT_NUMERO_IDENTIFICACION);
        assertThat(testPaciente.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    public void createPacienteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombresIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().size();
        // set the field null
        paciente.setNombres(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);


        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().size();
        // set the field null
        paciente.setApellidos(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);


        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaNacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().size();
        // set the field null
        paciente.setFechaNacimiento(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);


        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroIdentificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().size();
        // set the field null
        paciente.setNumeroIdentificacion(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);


        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPacientes() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc.perform(get("/api/pacientes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].numeroIdentificacion").value(hasItem(DEFAULT_NUMERO_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));
    }
    
    @Test
    @Transactional
    public void getPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.numeroIdentificacion").value(DEFAULT_NUMERO_IDENTIFICACION))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION));
    }


    @Test
    @Transactional
    public void getPacientesByIdFiltering() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        Long id = paciente.getId();

        defaultPacienteShouldBeFound("id.equals=" + id);
        defaultPacienteShouldNotBeFound("id.notEquals=" + id);

        defaultPacienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPacienteShouldNotBeFound("id.greaterThan=" + id);

        defaultPacienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPacienteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPacientesByNombresIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombres equals to DEFAULT_NOMBRES
        defaultPacienteShouldBeFound("nombres.equals=" + DEFAULT_NOMBRES);

        // Get all the pacienteList where nombres equals to UPDATED_NOMBRES
        defaultPacienteShouldNotBeFound("nombres.equals=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    public void getAllPacientesByNombresIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombres not equals to DEFAULT_NOMBRES
        defaultPacienteShouldNotBeFound("nombres.notEquals=" + DEFAULT_NOMBRES);

        // Get all the pacienteList where nombres not equals to UPDATED_NOMBRES
        defaultPacienteShouldBeFound("nombres.notEquals=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    public void getAllPacientesByNombresIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombres in DEFAULT_NOMBRES or UPDATED_NOMBRES
        defaultPacienteShouldBeFound("nombres.in=" + DEFAULT_NOMBRES + "," + UPDATED_NOMBRES);

        // Get all the pacienteList where nombres equals to UPDATED_NOMBRES
        defaultPacienteShouldNotBeFound("nombres.in=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    public void getAllPacientesByNombresIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombres is not null
        defaultPacienteShouldBeFound("nombres.specified=true");

        // Get all the pacienteList where nombres is null
        defaultPacienteShouldNotBeFound("nombres.specified=false");
    }
                @Test
    @Transactional
    public void getAllPacientesByNombresContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombres contains DEFAULT_NOMBRES
        defaultPacienteShouldBeFound("nombres.contains=" + DEFAULT_NOMBRES);

        // Get all the pacienteList where nombres contains UPDATED_NOMBRES
        defaultPacienteShouldNotBeFound("nombres.contains=" + UPDATED_NOMBRES);
    }

    @Test
    @Transactional
    public void getAllPacientesByNombresNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombres does not contain DEFAULT_NOMBRES
        defaultPacienteShouldNotBeFound("nombres.doesNotContain=" + DEFAULT_NOMBRES);

        // Get all the pacienteList where nombres does not contain UPDATED_NOMBRES
        defaultPacienteShouldBeFound("nombres.doesNotContain=" + UPDATED_NOMBRES);
    }


    @Test
    @Transactional
    public void getAllPacientesByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where apellidos equals to DEFAULT_APELLIDOS
        defaultPacienteShouldBeFound("apellidos.equals=" + DEFAULT_APELLIDOS);

        // Get all the pacienteList where apellidos equals to UPDATED_APELLIDOS
        defaultPacienteShouldNotBeFound("apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    public void getAllPacientesByApellidosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where apellidos not equals to DEFAULT_APELLIDOS
        defaultPacienteShouldNotBeFound("apellidos.notEquals=" + DEFAULT_APELLIDOS);

        // Get all the pacienteList where apellidos not equals to UPDATED_APELLIDOS
        defaultPacienteShouldBeFound("apellidos.notEquals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    public void getAllPacientesByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where apellidos in DEFAULT_APELLIDOS or UPDATED_APELLIDOS
        defaultPacienteShouldBeFound("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS);

        // Get all the pacienteList where apellidos equals to UPDATED_APELLIDOS
        defaultPacienteShouldNotBeFound("apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    public void getAllPacientesByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where apellidos is not null
        defaultPacienteShouldBeFound("apellidos.specified=true");

        // Get all the pacienteList where apellidos is null
        defaultPacienteShouldNotBeFound("apellidos.specified=false");
    }
                @Test
    @Transactional
    public void getAllPacientesByApellidosContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where apellidos contains DEFAULT_APELLIDOS
        defaultPacienteShouldBeFound("apellidos.contains=" + DEFAULT_APELLIDOS);

        // Get all the pacienteList where apellidos contains UPDATED_APELLIDOS
        defaultPacienteShouldNotBeFound("apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    public void getAllPacientesByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where apellidos does not contain DEFAULT_APELLIDOS
        defaultPacienteShouldNotBeFound("apellidos.doesNotContain=" + DEFAULT_APELLIDOS);

        // Get all the pacienteList where apellidos does not contain UPDATED_APELLIDOS
        defaultPacienteShouldBeFound("apellidos.doesNotContain=" + UPDATED_APELLIDOS);
    }


    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultPacienteShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the pacienteList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultPacienteShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento not equals to DEFAULT_FECHA_NACIMIENTO
        defaultPacienteShouldNotBeFound("fechaNacimiento.notEquals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the pacienteList where fechaNacimiento not equals to UPDATED_FECHA_NACIMIENTO
        defaultPacienteShouldBeFound("fechaNacimiento.notEquals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultPacienteShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the pacienteList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultPacienteShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento is not null
        defaultPacienteShouldBeFound("fechaNacimiento.specified=true");

        // Get all the pacienteList where fechaNacimiento is null
        defaultPacienteShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultPacienteShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the pacienteList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultPacienteShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultPacienteShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the pacienteList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultPacienteShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultPacienteShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the pacienteList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultPacienteShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void getAllPacientesByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultPacienteShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the pacienteList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultPacienteShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }


    @Test
    @Transactional
    public void getAllPacientesByNumeroIdentificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroIdentificacion equals to DEFAULT_NUMERO_IDENTIFICACION
        defaultPacienteShouldBeFound("numeroIdentificacion.equals=" + DEFAULT_NUMERO_IDENTIFICACION);

        // Get all the pacienteList where numeroIdentificacion equals to UPDATED_NUMERO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("numeroIdentificacion.equals=" + UPDATED_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    public void getAllPacientesByNumeroIdentificacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroIdentificacion not equals to DEFAULT_NUMERO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("numeroIdentificacion.notEquals=" + DEFAULT_NUMERO_IDENTIFICACION);

        // Get all the pacienteList where numeroIdentificacion not equals to UPDATED_NUMERO_IDENTIFICACION
        defaultPacienteShouldBeFound("numeroIdentificacion.notEquals=" + UPDATED_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    public void getAllPacientesByNumeroIdentificacionIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroIdentificacion in DEFAULT_NUMERO_IDENTIFICACION or UPDATED_NUMERO_IDENTIFICACION
        defaultPacienteShouldBeFound("numeroIdentificacion.in=" + DEFAULT_NUMERO_IDENTIFICACION + "," + UPDATED_NUMERO_IDENTIFICACION);

        // Get all the pacienteList where numeroIdentificacion equals to UPDATED_NUMERO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("numeroIdentificacion.in=" + UPDATED_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    public void getAllPacientesByNumeroIdentificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroIdentificacion is not null
        defaultPacienteShouldBeFound("numeroIdentificacion.specified=true");

        // Get all the pacienteList where numeroIdentificacion is null
        defaultPacienteShouldNotBeFound("numeroIdentificacion.specified=false");
    }
                @Test
    @Transactional
    public void getAllPacientesByNumeroIdentificacionContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroIdentificacion contains DEFAULT_NUMERO_IDENTIFICACION
        defaultPacienteShouldBeFound("numeroIdentificacion.contains=" + DEFAULT_NUMERO_IDENTIFICACION);

        // Get all the pacienteList where numeroIdentificacion contains UPDATED_NUMERO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("numeroIdentificacion.contains=" + UPDATED_NUMERO_IDENTIFICACION);
    }

    @Test
    @Transactional
    public void getAllPacientesByNumeroIdentificacionNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroIdentificacion does not contain DEFAULT_NUMERO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("numeroIdentificacion.doesNotContain=" + DEFAULT_NUMERO_IDENTIFICACION);

        // Get all the pacienteList where numeroIdentificacion does not contain UPDATED_NUMERO_IDENTIFICACION
        defaultPacienteShouldBeFound("numeroIdentificacion.doesNotContain=" + UPDATED_NUMERO_IDENTIFICACION);
    }


    @Test
    @Transactional
    public void getAllPacientesByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where direccion equals to DEFAULT_DIRECCION
        defaultPacienteShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the pacienteList where direccion equals to UPDATED_DIRECCION
        defaultPacienteShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllPacientesByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where direccion not equals to DEFAULT_DIRECCION
        defaultPacienteShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the pacienteList where direccion not equals to UPDATED_DIRECCION
        defaultPacienteShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllPacientesByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultPacienteShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the pacienteList where direccion equals to UPDATED_DIRECCION
        defaultPacienteShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllPacientesByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where direccion is not null
        defaultPacienteShouldBeFound("direccion.specified=true");

        // Get all the pacienteList where direccion is null
        defaultPacienteShouldNotBeFound("direccion.specified=false");
    }
                @Test
    @Transactional
    public void getAllPacientesByDireccionContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where direccion contains DEFAULT_DIRECCION
        defaultPacienteShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the pacienteList where direccion contains UPDATED_DIRECCION
        defaultPacienteShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllPacientesByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where direccion does not contain DEFAULT_DIRECCION
        defaultPacienteShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the pacienteList where direccion does not contain UPDATED_DIRECCION
        defaultPacienteShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPacienteShouldBeFound(String filter) throws Exception {
        restPacienteMockMvc.perform(get("/api/pacientes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].numeroIdentificacion").value(hasItem(DEFAULT_NUMERO_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));

        // Check, that the count call also returns 1
        restPacienteMockMvc.perform(get("/api/pacientes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPacienteShouldNotBeFound(String filter) throws Exception {
        restPacienteMockMvc.perform(get("/api/pacientes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPacienteMockMvc.perform(get("/api/pacientes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).get();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .numeroIdentificacion(UPDATED_NUMERO_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        restPacienteMockMvc.perform(put("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testPaciente.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testPaciente.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testPaciente.getNumeroIdentificacion()).isEqualTo(UPDATED_NUMERO_IDENTIFICACION);
        assertThat(testPaciente.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void updateNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc.perform(put("/api/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeDelete = pacienteRepository.findAll().size();

        // Delete the paciente
        restPacienteMockMvc.perform(delete("/api/pacientes/{id}", paciente.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
