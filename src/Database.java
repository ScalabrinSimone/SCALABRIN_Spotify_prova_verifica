import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Database instace = null;
    private Connection connection = null;

    private Database()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:database/spotify.db");
            System.out.println("Connected to database successfully");
            // IMPORTANTE: abilita foreign key
            connection.createStatement().execute("PRAGMA foreign_keys = ON;");
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

    public void close() {
        try
        {
            if (connection != null && !connection.isClosed())
                connection.close();
        }
        catch (SQLException e)
        {
            System.err.println("Errore di informazione sulla connessione: " + e.getMessage());
            System.exit(-1);
        }

    }
}
