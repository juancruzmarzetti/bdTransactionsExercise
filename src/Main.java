import org.apache.log4j.Logger;
import java.sql.*;
import java.time.LocalDate;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    private static final String SQL_CREATE_LOGS = """
            DROP TABLE IF EXISTS LOGS;
            CREATE TABLE LOGS(
                USER_ID VARCHAR(20) NOT NULL,
                DATED DATETIME NOT NULL,
                LOGGER VARCHAR(50) NOT NULL,
                LEVEL VARCHAR(10) NOT NULL,
                MESSAGE VARCHAR(1000) NOT NULL);""";
    private static final String SQL_CREATE_PACIENTES = """
            DROP TABLE IF EXISTS PACIENTES;
            CREATE TABLE PACIENTES(
                ID INT PRIMARY KEY,
                NOMBRE VARCHAR(50) NOT NULL,
                APELLIDO VARCHAR(50) NOT NULL,
                DOMICILIO VARCHAR(100) NOT NULL,
                DNI INT NOT NULL,
                FECHA_DE_ALTA DATE NOT NULL,
                USUARIO VARCHAR(50) NOT NULL,
                PASSWORD VARCHAR(50) NOT NULL);""";
    private static final String SQL_INSERT = """
            INSERT INTO PACIENTES (ID, NOMBRE, APELLIDO, DOMICILIO, DNI, FECHA_DE_ALTA, USUARIO, PASSWORD)
            VALUES (?,?,?,?,?,?,?,?);""";
    private static final String SQL_UPDATE = "UPDATE PACIENTES SET PASSWORD=? WHERE ID=?;";
    private static final String SQL_SELECT_PACIENTES = "SELECT * FROM PACIENTES";

    public static void main(String[] args) {
        Paciente paciente = new Paciente(
                1,
                "Carlos",
                "Espinoza",
                "Palermo 123, Buenos Aires",
                45345678,
                LocalDate.of(2020, 7, 20),
                "Carlos123",
                "carlitos24");

        Connection c = null;
        try{
            c = getConnection();
            Statement stmt = c.createStatement();
            stmt.execute(SQL_CREATE_LOGS);
            Statement stmt2 = c.createStatement();
            stmt2.execute(SQL_CREATE_PACIENTES);


            PreparedStatement pstmtInsert = c.prepareStatement(SQL_INSERT);
            pstmtInsert.setInt(1, paciente.getId());
            pstmtInsert.setString(2, paciente.getNombre());
            pstmtInsert.setString(3, paciente.getApellido());
            pstmtInsert.setString(4, paciente.getDomicilio());
            pstmtInsert.setInt(5, paciente.getDni());
            pstmtInsert.setDate(6, Date.valueOf(paciente.getFechaDeAlta()));
            pstmtInsert.setString(7, paciente.getUsuario());
            pstmtInsert.setString(8, paciente.getPassword());
            pstmtInsert.execute();

            c.setAutoCommit(false);
            PreparedStatement pstmtUpdate = c.prepareStatement(SQL_UPDATE);
            pstmtUpdate.setString(1, paciente.setPassword("Contraseña cambiada"));
            pstmtUpdate.setInt(2, paciente.getId());
            pstmtUpdate.execute();
            c.commit();
            c.setAutoCommit(true);

            Statement stmt3 = c.createStatement();
            ResultSet rd = stmt3.executeQuery(SQL_SELECT_PACIENTES);
            while(rd.next()){
                LOGGER.info("¡Se ha llamado a la base de datos con éxito!");
                System.out.println(
                        "ID: " + rd.getInt(1) + ", "
                      + "NOMBRE: " + rd.getString(2) + ", "
                      + "APELLIDO: " + rd.getString(3) + ", "
                      + "DOMICILIO: " + rd.getString(4) + ", "
                      + "DNI: " + rd.getInt(5) + ", "
                      + "FECHA DE ALTA: " + rd.getDate(6) + ", "
                      + "USUARIO: " + rd.getString(7) + ", "
                      + "CONTRASEÑA: " + rd.getString(8));
            }
        }catch(Exception e){
            LOGGER.error("Error en la comunicación con la base de datos: ", e);
            try{
                assert c != null;
                c.rollback();
            }catch(SQLException e2){
                LOGGER.error("No se pudo hacer el rollback: ", e2);
            }
        }finally{
            try {
                assert c != null;
                c.close();
            }catch (SQLException e3){
                LOGGER.error("No se pudo cerrar la conexión con la base de datos: ", e3);
            }
        }
    }
    private static Connection getConnection(){
        Connection c = null;
        try{
            Class.forName("org.h2.Driver").newInstance();
        }catch(Exception e){
            LOGGER.error("No se pudo crear una instancia de Driver de la base de datos H2: ", e);
        }finally{
            try{
                c = DriverManager.getConnection("jdbc:h2:"+
                        "./Database/my", "sa", "sa");
            }catch (Exception e){
                LOGGER.error("No se pudo establecer conexión con la base de datos: ", e);
            }
        }
      return c;
    };
}