package model;

import jakarta.persistence.*;

@Entity
@Table(name = "universidades")
public class Universidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_universidad")
    private Long idUniversidad;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String nit;

    @Column(nullable = false, length = 80)
    private String ciudad;

    @Column(name = "convenio_activo", nullable = false)
    private boolean convenioActivo = true;

    public Universidad() {}

    public Universidad(String nombre, String nit, String ciudad) {
        this.nombre = nombre;
        this.nit = nit;
        this.ciudad = ciudad;
        this.convenioActivo = true;
    }

    public Long getIdUniversidad() { return idUniversidad; }
    public void setIdUniversidad(Long idUniversidad) { this.idUniversidad = idUniversidad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public boolean isConvenioActivo() { return convenioActivo; }
    public void setConvenioActivo(boolean convenioActivo) { this.convenioActivo = convenioActivo; }

    @Override
    public String toString() { return nombre; }
}
