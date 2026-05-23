package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "cronogramas")
public class Cronograma {

    public enum Estado { PENDIENTE, APROBADO, RECHAZADO, MODIFICADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cronograma")
    private Long idCronograma;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente docente;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @Column(name = "motivo_excepcion", columnDefinition = "TEXT")
    private String motivoExcepcion;

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga = LocalDateTime.now();

    public Cronograma() {}

    public Cronograma(Estudiante estudiante, Docente docente, Servicio servicio,
                      LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.estudiante = estudiante;
        this.docente = docente;
        this.servicio = servicio;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = Estado.PENDIENTE;
        this.fechaCarga = LocalDateTime.now();
    }

    public Long getIdCronograma() { return idCronograma; }
    public void setIdCronograma(Long idCronograma) { this.idCronograma = idCronograma; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getMotivoExcepcion() { return motivoExcepcion; }
    public void setMotivoExcepcion(String motivoExcepcion) { this.motivoExcepcion = motivoExcepcion; }

    public LocalDateTime getFechaCarga() { return fechaCarga; }
    public void setFechaCarga(LocalDateTime fechaCarga) { this.fechaCarga = fechaCarga; }

    @Override
    public String toString() {
        return estudiante.getNombre() + " | " + servicio.getNombre() + " | " + fecha + " " + horaInicio;
    }
}
