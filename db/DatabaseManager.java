package ro.clinicrezervari.db;
import java.sql.* ;

public final class DatabaseManager {
    private static final String DB_URL =
            "jdbc:derby:./src/main/java/dbdata.clinicdb;create=true";
    private DatabaseManager() {}
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }
    public static void initSchema() {
        try (Connection con = getConnection(); Statement st = con.createStatement()) {


            if (!tableExists(con, "DOCTOR")) {
                st.executeUpdate("CREATE TABLE DOCTOR (" + " id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," + " nume VARCHAR(120) NOT NULL," + " specializare VARCHAR(120) NOT NULL," + " program_disponibil VARCHAR(500)" + ")");
            }

            if (!tableExists(con, "PACIENT")) {
                st.executeUpdate("CREATE TABLE PACIENT (" + " id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," + " nume VARCHAR(120) NOT NULL," + " email VARCHAR(200) NOT NULL UNIQUE," + " parola VARCHAR(200) NOT NULL," + " is_admin SMALLINT NOT NULL DEFAULT 0" + ")");
            }

            if (!tableExists(con, "PROGRAMARE")) {
                st.executeUpdate("CREATE TABLE PROGRAMARE (" + " id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," + " pacient_id INT NOT NULL," + " doctor_id INT NOT NULL," + " data_programare DATE NOT NULL," + " ora_programare TIME NOT NULL," + " durata_min INT NOT NULL DEFAULT 30," + " status VARCHAR(20) NOT NULL DEFAULT 'ACTIVA'," + " CONSTRAINT fk_prog_pac FOREIGN KEY (pacient_id) REFERENCES PACIENT(id)," + " CONSTRAINT fk_prog_doc FOREIGN KEY (doctor_id) REFERENCES DOCTOR(id)" + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare initSchema: " + e.getMessage(), e);
        }
    }
    private static boolean tableExists(Connection con, String tableName) throws SQLException {
        DatabaseMetaData md = con.getMetaData();
        try(ResultSet rs = md.getTables(null,null,tableName.toUpperCase(), null)){
            return rs.next();

        }
    }
}
