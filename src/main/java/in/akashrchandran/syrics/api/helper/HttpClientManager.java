package in.akashrchandran.syrics.api.helper;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class HttpClientManager {
    private static HttpClientManager instance;
    private final CloseableHttpClient httpClient;
    CookieStore basicCookieStore;
    private String authToken;

    public HttpClientManager() {
        basicCookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).addRequestInterceptorFirst((HttpRequestInterceptor) (request, context, random) -> {
            request.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36");
            request.setHeader("App-platform", "WebPlayer");
        }).build();
    }

    public void setBasicCookieStore(String key, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        basicCookieStore.addCookie(cookie);
    }

    public void close() throws IOException {
        httpClient.close();
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    // Method to perform a POST request
    public String post(String url) throws IOException, ParseException {
        HttpPost post = new HttpPost(url);
        if (authToken != null) {
            post.setHeader("Authorization", "Bearer " + authToken);
        }
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

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