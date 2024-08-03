package in.akashrchandran.syrics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.akashrchandran.syrics.api.Spotify;
import in.akashrchandran.syrics.exception.SyricsHttpException;
import in.akashrchandran.syrics.exception.SyricsParseException;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public String getLyricsLrc(String trackId) throws SyricsHttpException, SyricsParseException {
        JsonObject response = getRawLyrics(trackId);
        if (response.get("syncType").getAsString().equals("LINE_SYNCED")) {
            return formatLrc(response.get("lines").getAsJsonArray());
        } else {
            return unsyncLyrics(response.get("lines").getAsJsonArray());
        }
    }

    private String formatLrc(JsonArray lines) {
        List<String> lyrics = new ArrayList<>();
        lines.forEach(line -> {
            JsonObject lineObject = line.getAsJsonObject();
            String text = lineObject.get("words").getAsString();
            int startTime = lineObject.get("startTimeMs").getAsInt();
            lyrics.add(String.format("[%s] %s", formatMS(startTime), text));
        });
        return lyrics.stream().reduce((a, b) -> a + "\n" + b).orElse("");
    }

    public String unsyncLyrics(JsonArray lines) {
        List<String> lyrics = new ArrayList<>();
        lines.forEach(line -> {
            JsonObject lineObject = line.getAsJsonObject();
            String text = lineObject.get("words").getAsString();
            lyrics.add(text);
        });
        return lyrics.stream().reduce((a, b) -> a + "\n" + b).orElse("");
    }

    private static String formatMS(int milliseconds) {
        int totalSeconds = milliseconds / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        int hundredths = (milliseconds % 1000) / 10;
        return String.format("%02d:%02d.%02d", minutes, seconds, hundredths);
    }
}
