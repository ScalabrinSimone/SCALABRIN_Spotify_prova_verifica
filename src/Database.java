import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Database manager using SQLite (Xerial).
 *
 * Responsabilità:
 * - Garantire unica connessione
 * - Creare schema se non esiste
 * - Offrire semplici metodi CRUD per la collezione locale di Artistai
 *
 * Nota: il DB file viene creato nella working directory con nome 'database'
 */
public class Database {
    private static Database instance;
    private Connection conn;

    private Database() {
        try {
            // Connessione in modalità embedded SQLite
            conn = DriverManager.getConnection("jdbc:sqlite:database/spotify.db");
            initSchema();
        } catch (SQLException e) {
            throw new RuntimeException("Impossibile inizializzare DB", e);
        }
    }

    // Singleton getInstance
    public static synchronized Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    // Creazione tabelle (semplice)
    private void initSchema() throws SQLException {
        String createArtistas = """
                CREATE TABLE IF NOT EXISTS Artistas (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  api_id INTEGER,       -- id originale dell'API (se salvato da API)
                  nome TEXT NOT NULL,
                  paese TEXT,
                  genere TEXT
                );
                """;
        try (Statement st = conn.createStatement()) {
            st.execute(createArtistas);
        }
    }

    // Inserimento (CREATE) - restituisce id locale (autoincrement)
    public int saveArtista(Artista a) throws SQLException {
        String sql = "INSERT INTO Artistas(api_id, nome, paese, genere) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (a.id != null) ps.setInt(1, a.id); else ps.setNull(1, Types.INTEGER);
            ps.setString(2, a.nome);
            ps.setString(3, a.paese);
            ps.setString(4, a.genere);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            else throw new SQLException("Impossibile ottenere id generato");
        }
    }

    // READ all saved Artistas (collezione locale)
    public List<Artista> getAllSavedArtistas() throws SQLException {
        List<Artista> list = new ArrayList<>();
        String sql = "SELECT id, api_id, nome, paese, genere FROM Artistas";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Artista a = new Artista();
                a.id = rs.getInt("api_id") == 0 ? null : rs.getInt("api_id"); // id dell'API se salvato
                // NOTE: qui `id` dell'oggetto Artista = api_id (se presente). Se vuoi separare, crea DTO.
                a.nome = rs.getString("nome");
                a.paese = rs.getString("paese");
                a.genere = rs.getString("genere");
                list.add(a);
            }
        }
        return list;
    }

    // UPDATE saved Artista: aggiorna record locale tramite nome+api_id se presente
    public boolean updateSavedArtista(int localId, Artista a) throws SQLException {
        String sql = "UPDATE Artistas SET nome = ?, paese = ?, genere = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.nome);
            ps.setString(2, a.paese);
            ps.setString(3, a.genere);
            ps.setInt(4, localId);
            int changed = ps.executeUpdate();
            return changed > 0;
        }
    }

    // DELETE saved Artista (locale) by local id
    public boolean deleteSavedArtista(int localId) throws SQLException {
        String sql = "DELETE FROM Artistas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, localId);
            int changed = ps.executeUpdate();
            return changed > 0;
        }
    }

    // Chiudi connessione (utile a fine programma)
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            // ignore
        }
    }
}
