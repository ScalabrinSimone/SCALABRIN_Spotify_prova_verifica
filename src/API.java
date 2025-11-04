import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.PreparedStatement;

public class API {
    private HttpClient client = null;
    private final String ENDPOINT_BASE = "http://localhost:4567/api/";

    public API() {
        client = HttpClient.newHttpClient();
    }

    public String fetchArtisti() {
        //Build richiesta
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT_BASE + "artisti"))
                .GET()
                .build();

        //Build risposta: il client manda la richiesta
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //BodyHandelers è come gestitre il corpo.
            //String serve per dire che ritorna una stringa.

            Gson deserializzatore = new Gson();
            Artista[] artisti = deserializzatore.fromJson(response.body(), Artista[].class);

            StringBuilder result = new StringBuilder();
            for (Artista artista : artisti) { //Foreach in java
                result.append(artista.toString());
            }

            return result.toString();
        }catch (IOException | InterruptedException e) //Carattere pipe |
        {
            System.err.println("Errore in riocheista API: " + e.getMessage());
            return null;
        }

    }

    public String fetchArtista(int id) {
        //Build richiesta
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT_BASE + "artisti/" + id))
                .GET()
                .build();

        //Build risposta: il client manda la richiesta
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //BodyHandelers è come gestitre il corpo.
            //String serve per dire che ritorna una stringa.

            Gson deserializzatore = new Gson();
            Artista artista = deserializzatore.fromJson(response.body(), Artista.class);

            return artista.toString();
        }catch (IOException | InterruptedException e) //Carattere pipe |
        {
            System.err.println("Errore in riocheista API: " + e.getMessage());
            return null;
        }

    }
}
