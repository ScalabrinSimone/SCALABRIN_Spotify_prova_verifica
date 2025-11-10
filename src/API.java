import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * API: incapsula tutte le chiamate HTTP verso l'API locale
 * - Usa HttpClient (Java standard)
 * - Usa Gson per serializzazione/deserializzazione
 *
 * NOTA: non è uno "API wrapper" completo in produzione, ma è sufficiente per la verifica.
 */

public class API {
    private final HttpClient client;
    private final Gson gson;
    private final String baseUrl;

    public API(String baseUrl) {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
    }

    // ========== ArtistaI ==========

    // GET /Artistai
    public List<Artista> getAllArtistas() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/Artistai"))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            Type listType = new TypeToken<List<Artista>>(){}.getType();
            return gson.fromJson(resp.body(), listType);
        } else {
            throw new RuntimeException("Errore GET /Artistai: " + resp.statusCode() + " - " + resp.body());
        }
    }

    // GET /Artistai/{id}
    public Artista getArtistaById(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/Artistai/" + id))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return gson.fromJson(resp.body(), Artista.class);
        } else if (resp.statusCode() == 404) {
            return null;
        } else {
            throw new RuntimeException("Errore GET /Artistai/" + id + ": " + resp.statusCode() + " - " + resp.body());
        }
    }

    // GET /Artistai/{id}/canzoni
    public List<Canzone> getCanzonesByArtista(int ArtistaId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/Artistai/" + ArtistaId + "/canzoni"))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            Type listType = new TypeToken<List<Canzone>>(){}.getType();
            return gson.fromJson(resp.body(), listType);
        } else if (resp.statusCode() == 404) {
            return null;
        } else {
            throw new RuntimeException("Errore GET /Artistai/" + ArtistaId + "/canzoni: " + resp.statusCode());
        }
    }

    // POST /Artistai  (crea Artistaa)
    public Artista createArtista(Artista a) throws IOException, InterruptedException {
        String body = gson.toJson(a);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/Artistai"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 201) {
            return gson.fromJson(resp.body(), Artista.class);
        } else {
            throw new RuntimeException("Errore POST /Artistai: " + resp.statusCode() + " - " + resp.body());
        }
    }

    // PUT /Artistai/{id} (aggiorna)
    public Artista updateArtista(int id, Artista a) throws IOException, InterruptedException {
        String body = gson.toJson(a);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/Artistai/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return gson.fromJson(resp.body(), Artista.class);
        } else if (resp.statusCode() == 404) {
            return null;
        } else {
            throw new RuntimeException("Errore PUT /Artistai/" + id + ": " + resp.statusCode() + " - " + resp.body());
        }
    }

    // DELETE /Artistai/{id}
    public boolean deleteArtista(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/Artistai/" + id))
                .DELETE()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 204) {
            return true;
        } else if (resp.statusCode() == 404) {
            return false;
        } else {
            throw new RuntimeException("Errore DELETE /Artistai/" + id + ": " + resp.statusCode());
        }
    }

    // ========== CANZONI ==========

    // GET /canzoni
    public List<Canzone> getAllCanzones() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/canzoni"))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            Type listType = new TypeToken<List<Canzone>>(){}.getType();
            return gson.fromJson(resp.body(), listType);
        } else {
            throw new RuntimeException("Errore GET /canzoni: " + resp.statusCode());
        }
    }

    // GET /canzoni/{id}
    public Canzone getCanzoneById(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/canzoni/" + id))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return gson.fromJson(resp.body(), Canzone.class);
        } else if (resp.statusCode() == 404) {
            return null;
        } else {
            throw new RuntimeException("Errore GET /canzoni/" + id + ": " + resp.statusCode());
        }
    }
}
