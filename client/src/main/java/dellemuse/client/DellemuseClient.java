package dellemuse.client;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.model.Constant;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.model.util.RandomIDGenerator;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class DellemuseClient {

    private static final Logger logger = Logger.getLogger(DellemuseClient.class.getName());

    public static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 60;
    private static final int HTTP_CACHE_SIZE = 200 * 1024 * 1024; // 200 mb

    public static final String DEFAULT_USER_AGENT = "Dellemuse (" + System.getProperty("os.arch") + "; "
            + System.getProperty("os.arch") + ") odilon-java/" + "1";

    private static final String linux_home = (new File(System.getProperty("user.dir"))).getPath();
    private static final String windows_home = System.getProperty("user.dir");

    private RandomIDGenerator rand = new RandomIDGenerator();

    private final boolean isSSL;
    private final boolean acceptAllCertificates;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private OkHttpClient httpClient;
    private final String urlStr;
    private Scheme scheme = Scheme.HTTP;

    /**
     * private static final String UPLOAD_ID = "uploadId"; the current client
     * instance's base URL.
     */
    private HttpUrl baseUrl;

    /** access key to sign all requests with */
    private String accessKey;

    /** Secret key to sign all requests with */
    private String secretKey;

    private String userAgent = DEFAULT_USER_AGENT;

    public DellemuseClient(String endpoint, int port, String accessKey, String secretKey) {
        this(endpoint, port, accessKey, secretKey, false);
    }

    public DellemuseClient(String endpoint, int port, String accessKey, String secretKey, boolean secure) {
        this(endpoint, port, accessKey, secretKey, secure, false);

    }

    public DellemuseClient(String endpoint, int port, String accessKey, String secretKey, boolean isSecure,
            boolean acceptAllCertificates) {

        Check.requireNonNullStringArgument(endpoint, "endpoint is null or emtpy");
        Check.requireNonNullStringArgument(accessKey, "accessKey is null or emtpy");
        Check.requireNonNullStringArgument(secretKey, "secretKey is null or emtpy");

        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("port must be in range of 1 to 65535 -> " + String.valueOf(port));

        this.acceptAllCertificates = acceptAllCertificates;
        this.isSSL = isSecure;

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.registerModule(new Jdk8Module());

        List<Protocol> protocol = new ArrayList<>();
        protocol.add(Protocol.HTTP_1_1);

        File cacheDirectory = new File(getCacheWorkDir());
        Cache cache = new Cache(cacheDirectory, HTTP_CACHE_SIZE);

        this.httpClient = new OkHttpClient();

        this.httpClient = this.httpClient.newBuilder().connectTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS).protocols(protocol).cache(cache).build();

        this.urlStr = endpoint;
        HttpUrl url = HttpUrl.parse(this.urlStr);
        this.scheme = (isSSL()) ? Scheme.HTTPS : Scheme.HTTP;

        if (url != null) {

            if (!"/".equals(url.encodedPath())) {
                throw new IllegalArgumentException("no path allowed in endpoint -> " + endpoint);
            }

            HttpUrl.Builder urlBuilder = url.newBuilder();

            urlBuilder.scheme(scheme.toString());

            if (port > 0)
                urlBuilder.port(port);

            this.baseUrl = urlBuilder.build();
            this.accessKey = accessKey;
            this.secretKey = secretKey;

            if (isSSL() && isAcceptAllCertificates()) {
                try {

                    ignoreCertCheck();

                } catch (KeyManagementException | NoSuchAlgorithmException e) {
                    throw new IllegalStateException(e);
                }
            }
            return;
        }

        /** endpoint may be a valid hostname, IPv4 or IPv6 address */
        if (!this.isValidEndpoint(endpoint))
            throw new IllegalArgumentException("invalid host -> " + endpoint);

        if (port == 0) {
            this.baseUrl = new HttpUrl.Builder().scheme(scheme.toString()).host(endpoint).build();
        } else {
            this.baseUrl = new HttpUrl.Builder().scheme(scheme.toString()).host(endpoint).port(port).build();
        }
        this.accessKey = accessKey;
        this.secretKey = secretKey;

    }

    public boolean isSSL() {
        return this.isSSL;
    }

    public boolean isAcceptAllCertificates() {
        return this.acceptAllCertificates;
    }

    /**
     * <p>
     * Ignores check on server certificate for HTTPS connection.
     * </p>
     * <b>Example:</b><br>
     * 
     * <pre>{@code
     * odilonClient.ignoreCertCheck();
     * }</pre>
     */
    public void ignoreCertCheck() throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        } };

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        this.httpClient = this.httpClient.newBuilder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                }).build();
    }

    public void close() throws IOException {

        if (this.httpClient != null) {

            if (this.httpClient.cache() != null)
                try {
                    this.httpClient.cache().close();

                } catch (Exception e) {
                    throw new IOException(e);
                }

            this.httpClient.connectionPool().evictAll();

            this.httpClient.dispatcher().executorService().shutdown();

        }

    }

    private static boolean isLinux() {
        if (System.getenv("OS") != null && System.getenv("OS").toLowerCase().contains("windows"))
            return false;
        return true;
    }

    private String getCacheWorkDir() {
        return getHomeDirAbsolutePath() + File.separator + "tmp" + File.separator + rand.randomString(6);
    }

    private String getHomeDirAbsolutePath() {
        if (isLinux())
            return linux_home;
        return windows_home;
    }

    /**
     * @param endpoint
     * @return {@code true} if the endpoint is valid
     *         {@link https://en.wikipedia.org/wiki/Hostname#Restrictions_on_valid_host_names}
     * 
     */
    private boolean isValidEndpoint(String endpoint) {

        Check.requireNonNullStringArgument(endpoint, "endpoint is null or empty");

        if (InetAddressValidator.getInstance().isValid(endpoint))
            return true;

        if (endpoint.length() < 1 || endpoint.length() > 253)
            return false;

        for (String label : endpoint.split("\\.")) {

            if (label.length() < 1 || label.length() > 63)
                return false;

            if (!(label.matches(Constant.valid_endpoint_regex)))
                return false;

        }
        return true;
    }

}
