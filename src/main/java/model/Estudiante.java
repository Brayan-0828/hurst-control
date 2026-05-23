package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    public enum EstadoInduccion { PENDIENTE, REALIZADA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Long idEstudiante;

    @Column(nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String programa;

    @Column(nullable = false)
    private int semestre;

    @ManyToOne
    @JoinColumn(name = "id_universidad", nullable = false)
    private Universidad universidad;

    @ManyToOne
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente docente;

    @Column(name = "arl_inicio", nullable = false)
    private LocalDate arlInicio;

    @Column(name = "arl_vigencia", nullable = false)
    private LocalDate arlVigencia;

    @Column(name = "seguro_inicio", nullable = false)
    private LocalDate seguroInicio;

    @Column(name = "seguro_vigencia", nullable = false)
    private LocalDate seguroVigencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_induccion", nullable = false)
    private EstadoInduccion estadoInduccion = EstadoInduccion.PENDIENTE;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public Estudiante() {}

    public Estudiante(String cedula, String nombre, String programa, int semestre,
                      Universidad universidad, Docente docente,
                      LocalDate arlInicio, LocalDate arlVigencia,
                      LocalDate seguroInicio, LocalDate seguroVigencia) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.programa = programa;
        this.semestre = semestre;
        this.universidad = universidad;
        this.docente = docente;
        this.arlInicio = arlInicio;
        this.arlVigencia = arlVigencia;
        this.seguroInicio = seguroInicio;
        this.seguroVigencia = seguroVigencia;
        this.estadoInduccion = EstadoInduccion.PENDIENTE;
        this.activo = true;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Long idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    public Universidad getUniversidad() { return universidad; }
    public void setUniversidad(Universidad universidad) { this.universidad = universidad; }

    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }

    public LocalDate getArlInicio() { return arlInicio; }
    public void setArlInicio(LocalDate arlInicio) { this.arlInicio = arlInicio; }

    public LocalDate getArlVigencia() { return arlVigencia; }
    public void setArlVigencia(LocalDate arlVigencia) { this.arlVigencia = arlVigencia; }

    public LocalDate getSeguroInicio() { return seguroInicio; }
    public void setSeguroInicio(LocalDate seguroInicio) { this.seguroInicio = seguroInicio; }

    public LocalDate getSeguroVigencia() { return seguroVigencia; }
    public void setSeguroVigencia(LocalDate seguroVigencia) { this.seguroVigencia = seguroVigencia; }

    public EstadoInduccion getEstadoInduccion() { return estadoInduccion; }
    public void setEstadoInduccion(EstadoInduccion estadoInduccion) { this.estadoInduccion = estadoInduccion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() { return nombre + " (" + cedula + ")"; }
}
