package in.akashrchandran.syrics.api.helper;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class HttpClientManager {
    private static HttpClientManager instance;
    private final CloseableHttpClient httpClient;
    CookieStore basicCookieStore;

    public HttpClientManager() {
        basicCookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).build();

    }

    public static synchronized HttpClientManager getInstance() {
        if (instance == null) {
            instance = new HttpClientManager();
        }
        return instance;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setBasicCookieStore(String key, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        basicCookieStore.addCookie(cookie);
    }

    public CookieStore getCookieStore() {
        return basicCookieStore;
    }

    public void close() throws IOException {
        httpClient.close();
    }

    // Method to perform a POST request
    public String post(String url) throws IOException, ParseException {
        HttpPost post = new HttpPost(url);

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    // Method to perform a GET request
    public String get(String url) throws IOException, ParseException {
        HttpGet get = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            return EntityUtils.toString(response.getEntity());
        }
    }
}