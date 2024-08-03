package in.akashrchandran.syrics.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import in.akashrchandran.syrics.api.helper.HttpClientManager;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class Spotify {
    HttpClientManager httpClientManager;
    Gson gson = new Gson();

    public Spotify(String SP_DC) {
        httpClientManager = new HttpClientManager();
        httpClientManager.setBasicCookieStore("sp_dc", SP_DC, "open.spotify.com", "/");
    }

    public void login () throws IOException, ParseException {
        String loginUrl = "https://open.spotify.com/get_access_token?reason=transport&productType=web_player";
        JsonObject json = gson.fromJson(httpClientManager.get(loginUrl), JsonObject.class);
        String accessToken = json.get("accessToken").getAsString();
        httpClientManager.setAuthToken(accessToken);
    }

    public JsonObject getMe() throws IOException, ParseException {
        return gson.fromJson(httpClientManager.get("https://api.spotify.com/v1/me"), JsonObject.class);
    }

    public JsonObject getLyrics(String trackId) throws IOException, ParseException {
        String lyricsUrl = String.format("https://spclient.wg.spotify.com/color-lyrics/v2/track/%s?format=json&market=from_token", trackId);
        return gson.fromJson(httpClientManager.get(lyricsUrl), JsonObject.class).get("lyrics").getAsJsonObject();
    }
}
