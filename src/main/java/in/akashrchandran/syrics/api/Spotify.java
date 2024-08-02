package in.akashrchandran.syrics.api;

import in.akashrchandran.syrics.api.helper.HttpClientManager;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class Spotify {
    HttpClientManager httpClientManager;
    private final String SP_DC;
    private final String LOGIN_URL = "https://open.spotify.com/get_access_token?reason=transport&productType=web_player";

    public Spotify(String SP_DC) {
        this.SP_DC = SP_DC;
        httpClientManager = new HttpClientManager();
        httpClientManager.setBasicCookieStore("sp_dc", SP_DC, "open.spotify.com", "/");
    }

    public String login () throws IOException, ParseException {
        String loginResponse = httpClientManager.get(LOGIN_URL);
        System.out.println(loginResponse);
        return loginResponse;
    }
}
