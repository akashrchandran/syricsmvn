package in.akashrchandran.syrics;

import in.akashrchandran.syrics.api.Spotify;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class Syrics {

    private Spotify spotify;

    public Syrics(String SP_DC) {
        spotify = new Spotify(SP_DC);
        login();
    }

    public void login() {
        try {
            spotify.login();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
