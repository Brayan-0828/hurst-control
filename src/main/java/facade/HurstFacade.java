package facade;

import dao.*;
import model.*;
import tree.ArbolOrganizacional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HurstFacade {

    private static HurstFacade instancia;

    public static synchronized HurstFacade getInstance() {
        if (instancia == null) {
            instancia = new HurstFacade();
        }
        return instancia;
    }

    private final EstudianteDAO   estudianteDAO;
    private final DocenteDAO      docenteDAO;
    private final UniversidadDAO  universidadDAO;
    private final CronogramaDAO   cronogramaDAO;
    private final ServicioDAO     servicioDAO;
    private final RegistroAccesoDAO registroAccesoDAO;
    private final AlertaLogDAO    alertaLogDAO;
    private final AuditoriaDAO    auditoriaDAO;

    private final ArbolOrganizacional arbol;

    private HurstFacade() {
        estudianteDAO   = new EstudianteDAO();
        docenteDAO      = new DocenteDAO();
        universidadDAO  = new UniversidadDAO();
        cronogramaDAO   = new CronogramaDAO();
        servicioDAO     = new ServicioDAO();
        registroAccesoDAO = new RegistroAccesoDAO();
        alertaLogDAO    = new AlertaLogDAO();
        auditoriaDAO    = new AuditoriaDAO();
        arbol           = new ArbolOrganizacional();
    }

    public void registrarEstudiante(Estudiante estudiante) {
        estudianteDAO.save(estudiante);
        refrescarArbol();
    }

    public void eliminarEstudiante(Long id) {
        estudianteDAO.delete(id);
        refrescarArbol();
    }

    public List<Estudiante> getEstudiantes() {
        return estudianteDAO.findAll();
    }

    public List<Estudiante> getEstudiantesActivos() {
        return estudianteDAO.findAll().stream()
                .filter(Estudiante::isActivo)
                .collect(Collectors.toList());
    }

    public List<Estudiante> getEstudiantesConVigenciaProxima(int diasUmbral) {
        LocalDate limite = LocalDate.now().plusDays(diasUmbral);
        return estudianteDAO.findAll().stream()
                .filter(e -> e.isActivo()
                        && (e.getArlVigencia().isBefore(limite)
                            || e.getSeguroVigencia().isBefore(limite)))
                .collect(Collectors.toList());
    }

    public Estudiante buscarEstudiantePorId(Long id) {
        return estudianteDAO.findById(id);
    }

    public void registrarDocente(Docente docente) {
        docenteDAO.save(docente);
        refrescarArbol();
    }

    public void eliminarDocente(Long id) {
        docenteDAO.delete(id);
        refrescarArbol();
    }

    public List<Docente> getDocentes() {
        return docenteDAO.findAll();
    }

    public List<Docente> getDocentesActivos() {
        return docenteDAO.findAll().stream()
                .filter(Docente::isActivo)
                .collect(Collectors.toList());
    }

    public void registrarUniversidad(Universidad universidad) {
        universidadDAO.save(universidad);
        refrescarArbol();
    }

    public void eliminarUniversidad(Long id) {
        universidadDAO.delete(id);
        refrescarArbol();
    }

    public List<Universidad> getUniversidades() {
        return universidadDAO.findAll();
    }

    public List<Universidad> getUniversidadesConConvenio() {
        return universidadDAO.findAll().stream()
                .filter(Universidad::isConvenioActivo)
                .collect(Collectors.toList());
    }

    public void registrarCronograma(Cronograma cronograma) {
        cronogramaDAO.save(cronograma);
    }

    public void eliminarCronograma(Long id) {
        cronogramaDAO.delete(id);
    }

    public List<Cronograma> getCronogramas() {
        return cronogramaDAO.findAll();
    }

    public List<Cronograma> getCronogramasPendientes() {
        return cronogramaDAO.findAll().stream()
                .filter(c -> c.getEstado() == Cronograma.Estado.PENDIENTE)
                .collect(Collectors.toList());
    }

    public void registrarServicio(Servicio servicio) {
        servicioDAO.save(servicio);
    }

    public void eliminarServicio(Long id) {
        servicioDAO.delete(id);
    }

    public List<Servicio> getServicios() {
        return servicioDAO.findAll();
    }

    public void registrarAcceso(RegistroAcceso registro) {
        registroAccesoDAO.save(registro);
    }

    public List<RegistroAcceso> getRegistrosAcceso() {
        return registroAccesoDAO.findAll();
    }

    public void registrarAlerta(AlertaLog alerta) {
        alertaLogDAO.save(alerta);
    }

    public List<AlertaLog> getAlertas() {
        return alertaLogDAO.findAll();
    }

    public void registrarAuditoria(Auditoria auditoria) {
        auditoriaDAO.save(auditoria);
    }

    public List<Auditoria> getAuditorias() {
        return auditoriaDAO.findAll();
    }

    public ArbolOrganizacional getArbol() {
        if (arbol.getRaiz().getHijos().isEmpty()) {
            refrescarArbol();
        }
        return arbol;
    }

    public void refrescarArbol() {
        arbol.construir(
                universidadDAO.findAll(),
                docenteDAO.findAll(),
                estudianteDAO.findAll()
        );
    }

    public void imprimirArbol() {
        System.out.println(arbol.imprimirArbol());
    }
}
