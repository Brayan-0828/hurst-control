package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_log")
public class AlertaLog {

    public enum TipoAlerta {
        ARL_VENCIDA, ARL_POR_VENCER, INDUCCION_PENDIENTE,
        DOCENTE_AUSENTE, SOBRECUPO, SALIDA_NO_REGISTRADA, ACCESO_DENEGADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long idAlerta;

    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante; // Puede ser null para alertas globales

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", nullable = false)
    private TipoAlerta tipoAlerta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(nullable = false)
    private boolean atendida = false;

    public AlertaLog() {}

    public AlertaLog(Estudiante estudiante, TipoAlerta tipoAlerta, String descripcion) {
        this.estudiante = estudiante;
        this.tipoAlerta = tipoAlerta;
        this.descripcion = descripcion;
        this.fechaHora = LocalDateTime.now();
        this.atendida = false;
    }

    public Long getIdAlerta() { return idAlerta; }
    public void setIdAlerta(Long idAlerta) { this.idAlerta = idAlerta; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public TipoAlerta getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(TipoAlerta tipoAlerta) { this.tipoAlerta = tipoAlerta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public boolean isAtendida() { return atendida; }
    public void setAtendida(boolean atendida) { this.atendida = atendida; }

    @Override
    public String toString() { return "[" + tipoAlerta + "] " + descripcion; }
}
