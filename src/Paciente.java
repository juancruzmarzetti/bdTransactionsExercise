import java.time.LocalDate;

public class Paciente {
    private final Integer ID;
    private final String NOMBRE;
    private final String APELLIDO;
    private final String DOMICILIO;
    private final Integer DNI;
    private final LocalDate FECHA_DE_ALTA;
    private final String USUARIO;
    private String password;

    public Paciente(Integer ID, String NOMBRE, String APELLIDO, String DOMICILIO, Integer DNI, LocalDate FECHA_DE_ALTA, String USUARIO, String password) {
        this.ID = ID;
        this.NOMBRE = NOMBRE;
        this.APELLIDO = APELLIDO;
        this.DOMICILIO = DOMICILIO;
        this.DNI = DNI;
        this.FECHA_DE_ALTA = FECHA_DE_ALTA;
        this.USUARIO = USUARIO;
        this.password = password;
    }
    public Integer getId() {
        return ID;
    }
    public String getNombre() {
        return NOMBRE;
    }
    public String getApellido() {
        return APELLIDO;
    }
    public String getDomicilio() {
        return DOMICILIO;
    }
    public Integer getDni() {
        return DNI;
    }
    public LocalDate getFechaDeAlta() {
        return FECHA_DE_ALTA;
    }
    public String getUsuario() {
        return USUARIO;
    }
    public String getPassword() {
        return password;
    }
    public String setPassword(String password) {
        this.password = password;
        return password;
    }
}
