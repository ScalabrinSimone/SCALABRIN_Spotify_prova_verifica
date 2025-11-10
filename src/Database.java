import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instace = null;
    private Connection connection = null;

    private Database()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:database/spotify.db");
        }
        catch (SQLException e)
        {
            System.err.println("Errore di connessione SQLite al database: " + e.getMessage());

            System.exit(-1);
        }
    }

    public static Database getInstance() //synchronized
    {
        if(instace == null)
        {
            return new Database();
        }
        else
            return null;
    }
}
