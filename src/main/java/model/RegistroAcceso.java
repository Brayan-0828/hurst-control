package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "registros_acceso")
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Long idRegistro;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_cronograma", nullable = false)
    private Cronograma cronograma;

    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalDateTime horaSalida; // NULL mientras está dentro

    public RegistroAcceso() {}

    public RegistroAcceso(Estudiante estudiante, Servicio servicio, Cronograma cronograma) {
        this.estudiante = estudiante;
        this.servicio = servicio;
        this.cronograma = cronograma;
        this.horaEntrada = LocalDateTime.now();
    }

    public Double getHorasCumplidas() {
        if (horaSalida == null) return null;
        long minutos = ChronoUnit.MINUTES.between(horaEntrada, horaSalida);
        return Math.round(minutos / 60.0 * 100.0) / 100.0;
    }

    public Long getIdRegistro() { return idRegistro; }
    public void setIdRegistro(Long idRegistro) { this.idRegistro = idRegistro; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Cronograma getCronograma() { return cronograma; }
    public void setCronograma(Cronograma cronograma) { this.cronograma = cronograma; }

    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalDateTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }
}
