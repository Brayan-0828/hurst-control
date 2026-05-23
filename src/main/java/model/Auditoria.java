package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    public enum Accion { INSERT, UPDATE, DELETE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long idLog;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "tabla_afectada", nullable = false, length = 60)
    private String tablaAfectada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Accion accion;

    @Column(name = "registro_id")
    private Long registroId;

    @Column(name = "valor_anterior", columnDefinition = "TEXT")
    private String valorAnterior;

    @Column(name = "valor_nuevo", columnDefinition = "TEXT")
    private String valorNuevo;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora = LocalDateTime.now();

    public Auditoria() {}

    public Auditoria(Usuario usuario, String tablaAfectada, Accion accion,
                     Long registroId, String valorAnterior, String valorNuevo) {
        this.usuario = usuario;
        this.tablaAfectada = tablaAfectada;
        this.accion = accion;
        this.registroId = registroId;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.fechaHora = LocalDateTime.now();
    }

    public Long getIdLog() { return idLog; }
    public void setIdLog(Long idLog) { this.idLog = idLog; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getTablaAfectada() { return tablaAfectada; }
    public void setTablaAfectada(String tablaAfectada) { this.tablaAfectada = tablaAfectada; }

    public Accion getAccion() { return accion; }
    public void setAccion(Accion accion) { this.accion = accion; }

    public Long getRegistroId() { return registroId; }
    public void setRegistroId(Long registroId) { this.registroId = registroId; }

    public String getValorAnterior() { return valorAnterior; }
    public void setValorAnterior(String valorAnterior) { this.valorAnterior = valorAnterior; }

    public String getValorNuevo() { return valorNuevo; }
    public void setValorNuevo(String valorNuevo) { this.valorNuevo = valorNuevo; }

    public String getIpOrigen() { return ipOrigen; }
    public void setIpOrigen(String ipOrigen) { this.ipOrigen = ipOrigen; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
