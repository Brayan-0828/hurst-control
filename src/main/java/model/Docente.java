package model;

import jakarta.persistence.*;

@Entity
@Table(name = "docentes")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Long idDocente;

    @Column(nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 120)
    private String correo;

    @ManyToOne
    @JoinColumn(name = "id_universidad", nullable = false)
    private Universidad universidad;

    @Column(nullable = false)
    private boolean activo = true;

    public Docente() {}

    public Docente(String cedula, String nombre, String correo, Universidad universidad) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.universidad = universidad;
        this.activo = true;
    }

    public Long getIdDocente() { return idDocente; }
    public void setIdDocente(Long idDocente) { this.idDocente = idDocente; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Universidad getUniversidad() { return universidad; }
    public void setUniversidad(Universidad universidad) { this.universidad = universidad; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() { return nombre + " (" + cedula + ")"; }
}
