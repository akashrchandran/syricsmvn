package in.akashrchandran.syrics.api.helper;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

/**
 * The type Http client manager.
 */
public class HttpClientManager {
    private final CloseableHttpClient httpClient;
    /**
     * The Basic cookie store.
     */
    CookieStore basicCookieStore;
    private String authToken;

    /**
     * Instantiates a new Http client manager.
     */
    public HttpClientManager() {
        basicCookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).addRequestInterceptorFirst((request, $1, $2) -> {
            request.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36");
            request.setHeader("App-platform", "WebPlayer");
        }).build();
    }

    /**
     * Sets basic cookie store.
     *
     * @param key    the key
     * @param value  the value
     * @param domain the domain
     * @param path   the path
     */
    public void setBasicCookieStore(String key, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        basicCookieStore.addCookie(cookie);
    }

    /**
     * Close.
     *
     * @throws IOException the io exception
     */
    public void close() throws IOException {
        httpClient.close();
    }

    /**
     * Sets auth token.
     *
     * @param token the token
     */
    public void setAuthToken(String token) {
        this.authToken = token;
    }

    /**
     * Get string.
     *
     * @param url the url
     * @return the string
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
// Method to perform a GET request
    public String get(String url) throws IOException, ParseException {
        HttpGet get = new HttpGet(url);
        if (authToken != null) {
            get.setHeader("Authorization", "Bearer " + authToken);
        }
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            return EntityUtils.toString(response.getEntity());
        }
    }
}