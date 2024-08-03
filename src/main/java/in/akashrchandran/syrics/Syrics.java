package in.akashrchandran.syrics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.akashrchandran.syrics.api.Spotify;
import in.akashrchandran.syrics.exception.SyricsHttpException;
import in.akashrchandran.syrics.exception.SyricsParseException;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class Syrics {

    private final Spotify spotify;

    public Syrics(String SP_DC) {
        spotify = new Spotify(SP_DC);
    }

    public void login() throws SyricsHttpException, SyricsParseException {
        try {
            spotify.login();
        } catch (IOException e) {
            throw new SyricsHttpException("An error occurred while trying to login to Spotify", e);
        } catch (ParseException e) {
            throw new SyricsParseException("An error occurred while trying to login to Spotify", e);
        }
    }

    public JsonObject getRawLyrics(String trackId) throws SyricsHttpException, SyricsParseException {
        try {
            return spotify.getLyrics(trackId);
        } catch (IOException e) {
            throw new SyricsHttpException("An error occurred while trying to get lyrics", e);
        } catch (ParseException e) {
            throw new SyricsParseException("An error occurred while trying to get lyrics", e);
        }
    }
}
