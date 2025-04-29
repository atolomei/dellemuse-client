package dellemuse.client;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.client.net.ClientConstant;
import dellemuse.client.net.HttpRequestBody;
import dellemuse.client.net.InetAddressValidator;
import dellemuse.client.net.Method;
import dellemuse.client.net.Scheme;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.PersonModel;
import dellemuse.model.SiteModel;
import dellemuse.model.UserModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.ErrorProxy;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.model.util.Constant;
import dellemuse.model.util.RandomIDGenerator;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;



public class DelleMuseClient {

    private static final Logger logger = Logger.getLogger(DelleMuseClient.class.getName());

    private final RandomIDGenerator rand = new RandomIDGenerator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private OkHttpClient httpClient;
    private Scheme scheme = Scheme.HTTP;

    private HttpUrl httpUrl;

    private boolean isSSL = false;
    private boolean acceptAllCertificates = false;

    private String accessKey;
    private String secretKey;

    private String userAgent = ClientConstant.DEFAULT_USER_AGENT;

    private PersonClientHandler personClientHandler;

    private InstitutionClientHandler institutionClientHandler;


    private ArtWorkClientHandler artWorkClientHandler;
    private ArtWorkArtistClientHandler artWorkArtistClientHandler;
    private ArtWorkTypeClientHandler artWorkTypeClientHandler;
    private SiteClientHandler siteClientHandler;

    private ArtExhibitionClientHandler artExhibitionClientHandler;
    private ArtExhibitionGuideClientHandler artExhibitionGuideClientHandler;
    private ArtExhibitionItemClientHandler artExhibitionItemClientHandler;
    

    private UserClientHandler userClientHandler;
    
    
    /**
     * 
     * @param endpoint
     * @param port
     * @param accessKey
     * @param secretKey
     */
    public DelleMuseClient(String endpoint, int port, String accessKey, String secretKey) {
        this(endpoint, port, accessKey, secretKey, false);
    }

    public DelleMuseClient(String endpoint, int port, String accessKey, String secretKey, boolean secure) {
        this(endpoint, port, accessKey, secretKey, secure, false);
    }

    public DelleMuseClient(String endpoint, int port, String accessKey, String secretKey, boolean isSecure,
            boolean acceptAllCertificates) {

        Check.requireNonNullStringArgument(endpoint, "endpoint is null or emtpy");
        Check.requireNonNullStringArgument(accessKey, "accessKey is null or emtpy");
        Check.requireNonNullStringArgument(secretKey, "secretKey is null or emtpy");

        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("port must be in range of 1 to 65535 -> " + String.valueOf(port));

        //if (!this.isValidEndpoint(endpoint))
        //    throw new IllegalArgumentException("invalid host -> " + endpoint);

        this.acceptAllCertificates = acceptAllCertificates;
        this.isSSL = isSecure;

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.registerModule(new Jdk8Module());

        this.scheme = (isSecure) ? Scheme.HTTPS : Scheme.HTTP;
        
        List<Protocol> protocol = new ArrayList<>();
        protocol.add(Protocol.HTTP_1_1);

        Cache cache = new Cache(new File(getCacheWorkDir()), ClientConstant.HTTP_CACHE_SIZE);

        this.accessKey = accessKey;
        this.secretKey = secretKey;

        this.httpClient = new OkHttpClient();

        this.httpClient = this.httpClient.newBuilder().connectTimeout(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS).
                protocols(protocol).cache(cache).build();

        HttpUrl url = HttpUrl.parse(endpoint);

        if (url != null) {
            
            if (!"/".equals(url.encodedPath()))
                throw new IllegalArgumentException("no path allowed in endpoint -> " + endpoint);


            HttpUrl.Builder urlBuilder = url.newBuilder();

            urlBuilder.scheme(getScheme().toString());

            if (port > 0)
                urlBuilder.port(port);

            this.httpUrl = urlBuilder.build();

            if (isSSL() && isAcceptAllCertificates()) {
                try {
                    ignoreCertCheck();
                } catch (KeyManagementException | NoSuchAlgorithmException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
            
        initHandlers();
    }

    /** Ping */
    public String ping() throws DelleMuseClientException {
        
        String str = null;

        String endPoint [] = Endpoint.getPingEndPoint();
        
        try (Response response = executeGetReq(endPoint)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage()));
        }

        
        return str;
        
    }
    
    
    
    /** Person */
    public PersonModel getPerson(Long id)                   throws DelleMuseClientException  { return getPersonClientHandler().get(id); }
    public List<PersonModel>   listPersons()                 throws DelleMuseClientException  { return getPersonClientHandler().findAll(); }
    
    /** ArtWork */
    public ArtWorkModel getArtWork(Long id)                 throws DelleMuseClientException  { return getArtWorkClientHandler().get(id); }
    public List<ArtWorkModel> listArtWorks()                 throws DelleMuseClientException  {return getArtWorkClientHandler().findAll();}
    
    /** Institution */
    public InstitutionModel         getInstitution(Long id) throws DelleMuseClientException  { return getInstitutionClientHandler().get(id); }
    public List<InstitutionModel>   listInstitutions()       throws DelleMuseClientException  { return getInstitutionClientHandler().findAll();  }

    /** Site */
    public SiteModel         getSite(Long id)                           throws DelleMuseClientException  { return getSiteClientHandler().get(id); }
    public List<SiteModel>   listSites()                                 throws DelleMuseClientException  { return getSiteClientHandler().findAll();}
    public List<SiteModel>   listSite(InstitutionModel institution)     throws DelleMuseClientException  { return getSiteClientHandler().findSites(institution);}
    
    
    /** ArtExhibition */
    public ArtExhibitionModel         getArtExhibition(Long id)  throws DelleMuseClientException  { return getArtExhibitionClientHandler().get(id); }
    public List<ArtExhibitionModel>   listArtExhibitions()        throws DelleMuseClientException  { return getArtExhibitionClientHandler().findAll();}


    /** ArtExhibitionGuide (Site) */
    public ArtExhibitionGuideModel         getArtExhibitionGuide(Long id)  throws DelleMuseClientException  { return getArtExhibitionGuideClientHandler().get(id); }
    public List<ArtExhibitionGuideModel>   listArtExhibitionGuides()        throws DelleMuseClientException  { return getArtExhibitionGuideClientHandler().findAll();}
    
    /** List<ArtExhibitionItem> (Site) */
    public ArtExhibitionItemModel         getArtExhibitionItem(Long id)      throws DelleMuseClientException  {return getArtExhibitionItemClientHandler().get(id);   }
    public List<ArtExhibitionItemModel>   listArtExhibitionItems()            throws DelleMuseClientException  {return getArtExhibitionItemClientHandler().findAll(); }


    /** User */
    public UserModel           getUser(Long id)     throws DelleMuseClientException   {return getUserClientHandler().get(id);   }
    public List<UserModel>    listUsers()            throws DelleMuseClientException  {return getUserClientHandler().findAll(); }

    
    
    
    /**
     * 
     * 
     */
    protected void initHandlers() {
        
        this.personClientHandler = new PersonClientHandler( this );

        this.artWorkClientHandler       = new ArtWorkClientHandler ( this );
        this.artWorkArtistClientHandler = new ArtWorkArtistClientHandler ( this );
        this.artWorkTypeClientHandler  = new ArtWorkTypeClientHandler ( this );
        
        this.institutionClientHandler = new InstitutionClientHandler ( this );
        this.siteClientHandler = new  SiteClientHandler( this );
        
        this.userClientHandler = new UserClientHandler( this );
    
        this.artExhibitionClientHandler = new ArtExhibitionClientHandler( this ) ;
        this.artExhibitionGuideClientHandler = new ArtExhibitionGuideClientHandler( this );
        this.artExhibitionItemClientHandler =new ArtExhibitionItemClientHandler( this );
    }
    
    
    /**
     * <p>
     * Ignores check on server certificate for HTTPS connection.
     * </p>
     * <b>Example:</b><br>
     * 
     * <pre>{@code
     *  client.ignoreCertCheck();
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
        if (this.getHttpClient() != null) {
            if (this.getHttpClient().cache() != null)
                try {
                    this.getHttpClient().cache().close();
                } catch (Exception e) {
                    throw new IOException(e);
                }
            this.getHttpClient().connectionPool().evictAll();
            this.getHttpClient().dispatcher().executorService().shutdown();
        }
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
    
    /**
     * <p>
     * Checks whether port should be omitted in Host header. HTTP Spec (rfc2616)
     * defines that port should be omitted in Host header when port and service
     * matches (i.e HTTP -> 80, HTTPS -> 443)
     * </p>
     * 
     * @param url Url object
     */
    private boolean shouldOmitPortInHostHeader(HttpUrl url) {
        return (url.scheme().equals("http") && url.port() == 80) || (url.scheme().equals("https") && url.port() == 443);
    }


    public String calculateSHA256String(final byte [] data) throws IOException, NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
        return String.format("%64s", new BigInteger(1,  hash).toString(16)).replace(' ', '0');
    }
    
    private HttpUrl getHttpUrl() {
        return httpUrl;
    }
    
    private Scheme getScheme() {
        return scheme;
    }


    public boolean isSSL() {
        return this.isSSL;
    }

    public boolean isAcceptAllCertificates() {
        return this.acceptAllCertificates;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    protected OkHttpClient getHttpClient() {
        return httpClient;
    }

    
    protected Response executeDeleteById(String relativePath[], Long id) throws DelleMuseClientException {
        Check.requireNonNullArgument(relativePath, "relativePath is null");
        Check.requireNonNullArgument(id, "id is null");
        String[] path = new String[ relativePath.length ];
        int n=0;
        for( String s:relativePath)
            path[n++]=s;
        path[n]=id.toString();
        
        byte data[] = "".getBytes();
        int len = data.length;
        
        return executeReq(relativePath, Method.DELETE, Optional.empty(), Optional.empty(), Optional.of(data), len, false);
    }

    
    
    protected Response executePost( String relativePath[], 
                                    Optional<Map<String, String>> headerMap, 
                                    Optional<Map<String, String>> queryParamMap, 
                                    Optional<byte[]> data, 
                                    int length, 
                                    boolean multiPart) throws DelleMuseClientException {
        return execute(relativePath, Method.POST, headerMap, queryParamMap, data, length, multiPart);
    }
    
    protected Response execute(   String relativePath[], 
                                Method method,
                                Optional<Map<String, String>> oheaderMap, 
                                Optional<Map<String, String>> oqueryParamMap, 
                                Optional<byte[]> obody,
                                int length, 
                                boolean multiPart) throws DelleMuseClientException {


        Optional<Multimap<String, String>> queryParamMultiMap;

        if (oqueryParamMap.isPresent())
            queryParamMultiMap = Optional.of(Multimaps.forMap(oqueryParamMap.get()));
        else
            queryParamMultiMap = Optional.empty();

        Optional<Multimap<String, String>> headerMultiMap;

        if (oheaderMap.isPresent())
            headerMultiMap = Optional.of(Multimaps.forMap(oheaderMap.get()));
        else
            headerMultiMap = Optional.empty();

        return executeReq(relativePath, method, headerMultiMap, queryParamMultiMap, obody, length,  multiPart);

    }

    
    
    protected Response executeGetById(String relativePath[], Long id) throws DelleMuseClientException {
        Check.requireNonNullArgument(relativePath, "relativePath is null");
        Check.requireNonNullArgument(id, "id is null");
        String[] path = new String[ relativePath.length ];
        for(int n=0; n<relativePath.length-1;n++)
              path[n]=relativePath[n];
        path[relativePath.length-1]=id.toString();
        
        return executeReq(path, Method.GET, Optional.empty(), Optional.empty(), Optional.empty(), -1, false);
    }
    

    protected Response executeGetByName(String relativePath[], String name) throws DelleMuseClientException {
        Check.requireNonNullArgument(relativePath, "relativePath is null");
        Check.requireNonNullArgument(name, "id is null");
        String[] path = new String[ relativePath.length ];
        for(int n=0; n<relativePath.length-1;n++)
              path[n]=relativePath[n];
        path[relativePath.length-1]=name;
        return executeReq(path, Method.GET, Optional.empty(), Optional.empty(), Optional.empty(), -1, false);
    }


    
    protected Response executeGetReq(String relativePath[]) throws DelleMuseClientException  {
        return executeReq(relativePath, Method.GET, Optional.empty(), Optional.empty(), Optional.empty(), -1, false);
        
    }
    protected Response executeGetReq(String relativePath[], Optional<Multimap<String, String>> headerMap, Optional<Multimap<String, String>> queryParamMap) throws DelleMuseClientException  {
        return executeReq(relativePath, Method.GET, headerMap, queryParamMap, Optional.empty(), -1, false);
    }

    
    /**
     * Response is not closed if the Request is successful
     * 
     * @param relativePath
     * @param method
     * @param headerMap
     * @param queryParamMap
     * @param o_body
     * @param length
     * @param multiPart
     * @return
     * @throws DelleMuseClientException
     */
    protected Response executeReq (
            String relativePath[], 
            Method method, 
            Optional<Multimap<String, String>> headerMap, 
            Optional<Multimap<String, String>> queryParamMap, 
            Optional<byte[]> o_body, 
            int length, 
            boolean multiPart) throws DelleMuseClientException {

        String contentType = null;

        if (headerMap.isPresent()) {
            if (headerMap.get().get("Content-Type") != null) 
                contentType = String.join(" ", headerMap.get().get("Content-Type"));
        }

        Request request = null;

        try {
            
            request = createRequest(relativePath, method, headerMap, queryParamMap, contentType, o_body, length, multiPart);

        } catch (NoSuchAlgorithmException | IOException e) {
            
            logger.error("before sending the Request. Caused by -> request = createRequest(...)");

            throw new DelleMuseClientException(HttpStatus.HTTP_NOT_EXECUTED.value(), 
                    ErrorCode.GENERAL_CLIENT_ERROR.getCode(), 
                    ErrorCode.GENERAL_CLIENT_ERROR.getMessage().replace("%1", "error creating request" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("---------START-HTTP---------");
            String encodedPath = request.url().encodedPath();
            String encodedQuery = request.url().encodedQuery();
            if (encodedQuery != null)
                encodedPath += "?" + encodedQuery;
            logger.debug(request.method() + " " + encodedPath + " HTTP/1.1");
            String headers = request.headers().toString().replaceAll("Signature=([0-9a-f]+)", "Signature=*REDACTED*")
                    .replaceAll("Credential=([^/]+)", "Credential=*REDACTED*");
            logger.debug(headers);
            logger.debug();
        }

        Response response = null;

        boolean error = false;
        
        try {
            
            response = getHttpClient().newCall(request).execute();
            
        } catch (ConnectException e) {
            error = true;
            throw new DelleMuseClientException(HttpStatus.SERVICE_UNAVAILABLE.value(), 
                    ErrorCode.SERVER_UNREACHEABLE.getCode(), 
                    ErrorCode.SERVER_UNREACHEABLE.getMessage().replace("%1", "connection error" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));

        } catch (IOException e) {
            error = true;
            throw new DelleMuseClientException(HttpStatus.SERVICE_UNAVAILABLE.value(), 
                    ErrorCode.INTERNAL_ERROR.getCode(), 
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));
        }
        finally {
            if (error && (response!=null))
                response.close();
        }

        if (logger.isDebugEnabled()) {
            logger.debug(response.protocol().toString().toUpperCase(Locale.US) + " " + response.code());
            logger.debug(response.headers());
        }
        
        
        if (response.isSuccessful()) {
            if (logger.isDebugEnabled())
                logger.debug(ClientConstant.END_HTTP);
            
            return response;
        }

        
        /**
         * if response is not successful -> throw Exception
         * ----------------------------
         */
        

        int httpCode = 0;
        String str = null;
        try {

            str = response.body().string();
            httpCode = response.code();
            
        } catch (IOException e) {
            
            throw new DelleMuseClientException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    ErrorCode.INTERNAL_ERROR.getCode(), 
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", e.getClass().getSimpleName() + " - " + e.getMessage()));
        }
        finally {
            response.close();
        }
        
        if (httpCode == HttpStatus.UNAUTHORIZED.value())
            throw new DelleMuseClientException(HttpStatus.UNAUTHORIZED.value(), 
                    ErrorCode.AUTHENTICATION_ERROR.getCode(), 
                    ErrorCode.AUTHENTICATION_ERROR.getMessage().replace("%1", response.toString()));

        if (httpCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            throw new DelleMuseClientException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    ErrorCode.INTERNAL_ERROR.getCode(), 
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", response.toString()));
        }
        if (httpCode == HttpStatus.FORBIDDEN.value()) {
            throw new DelleMuseClientException(HttpStatus.FORBIDDEN.value(), 
                    ErrorCode.ACCESS_DENIED.value(), 
                    ErrorCode.ACCESS_DENIED.getMessage().replace("%1", response.toString()));
        }

        try {
            ErrorProxy proxy = getObjectMapper().readValue(str, ErrorProxy.class);
            DelleMuseClientException ex = new DelleMuseClientException(proxy.getHttpStatus(), proxy.getErrorCode(), proxy.getMessage());
            Map<String, String> context = new HashMap<String, String>();
            if (queryParamMap.isPresent()) {
                queryParamMap.get().asMap().forEach((k, v) -> context.put(k, v.toString()));
            }
            ex.setContext(context);
            throw (ex);

        } catch (JsonProcessingException e) {
            throw new DelleMuseClientException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    ErrorCode.CLIENT_ERROR_JSON_PARSE_ERROR.getCode(), 
                    ErrorCode.CLIENT_ERROR_JSON_PARSE_ERROR.getMessage().replace("%1", e.getClass().getSimpleName() + " - " + e.getMessage()));
        }
    }
    
    /**
     * 
     * @param relativePath
     * @param method
     * @param headerMap
     * @param queryParamMap
     * @param contentType
     * @param o_body
     * @param length
     * @param multiPart
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    protected Request createRequest(
            
            String relativePath[],
            Method method, 
            Optional<Multimap<String, String>> headerMap, 
            Optional<Multimap<String, String>> queryParamMap, 
            final String contentType, 
            Optional<byte[]> o_body,
            int length, 
            boolean multiPart
            
            ) throws NoSuchAlgorithmException, IOException {

        HttpUrl.Builder urlBuilder = this.getHttpUrl().newBuilder();

        
        //urlBuilder.addEncodedPathSegment(Endpoint.ROOT);
        
        for (String str : relativePath)
             urlBuilder.addEncodedPathSegment(str);
        
        if (queryParamMap.isPresent()) {
            // for (Map.Entry<String, String> entry : queryParamMap.entries())
            //     urlBuilder.addEncodedQueryParameter(entry.getKey(), entry.getValue());
            queryParamMap.get().entries().forEach(entry -> urlBuilder.addEncodedQueryParameter(entry.getKey(), entry.getValue()));
        }

        HttpUrl url = urlBuilder.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.header("Authorization", "Basic " + Base64.getEncoder().encodeToString((getAccessKey() + ":" + getSecretKey()).getBytes()));
        requestBuilder.header("Host", this.shouldOmitPortInHostHeader(url) ? url.host() : (url.host() + ":" + url.port()));
        requestBuilder.header("User-Agent", this.userAgent);
        requestBuilder.header("Accept", ClientConstant.APPLICATION_JSON);
        requestBuilder.header("Accept-Charset", "utf-8");
        requestBuilder.header("Accept-Encoding", "gzip, deflate");
        requestBuilder.header("Date", ClientConstant.HTTP_DATE.format(OffsetDateTime.now()));
        // requestBuilder.header("Content-Encoding", "gzip, deflate");
        if (multiPart) 
            requestBuilder.header("Transfer-Encoding", "gzip, chunked");

        if (headerMap.isPresent()) 
            headerMap.get().entries().forEach( entry -> requestBuilder.header(entry.getKey(), entry.getValue()));

        if (o_body.isPresent()) {
            requestBuilder.header("ETag", calculateSHA256String(o_body.get()));
            RequestBody requestBody = new HttpRequestBody(contentType, o_body.get(), length);
            
            if (multiPart) {
                String fileName = queryParamMap.get().get("filename").toString();
                MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM) // Header to show we are
                                                                                                      // sending a Multipart Form
                                                                                                      // Data
                        .addFormDataPart("file", fileName, requestBody) // file param
                        .addFormDataPart("Content-Type", contentType) // other string params can be like userId, name or something
                        .build();
                requestBuilder.method(method.toString(), multipartBody);
            } else {
                requestBuilder.method(method.toString(), requestBody);
            }
        } else {
            requestBuilder.method(method.toString(), null);
        }
        return requestBuilder.build();
    }
    
    protected boolean isLinux() {
        if (System.getenv("OS") != null && System.getenv("OS").toLowerCase().contains("windows"))
            return false;
        return true;
    }


    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected PersonClientHandler getPersonClientHandler() {
        return personClientHandler;
    }

    protected void setPersonClientHandler(PersonClientHandler personClientHandler) {
        this.personClientHandler = personClientHandler;
    }

    protected ArtWorkClientHandler getArtWorkClientHandler() {
        return artWorkClientHandler;
    }

    protected void setArtWorkClientHandler(ArtWorkClientHandler artWorkClientHandler) {
        this.artWorkClientHandler = artWorkClientHandler;
    }

    protected static Logger getLogger() {
        return logger;
    }

    protected RandomIDGenerator getRand() {
        return rand;
    }

    protected String getUserAgent() {
        return userAgent;
    }

    protected InstitutionClientHandler getInstitutionClientHandler() {
        return institutionClientHandler;
    }

    protected SiteClientHandler getSiteClientHandler() {
        return this.siteClientHandler;
    }
    
    protected void setInstitutionClientHandler(InstitutionClientHandler institutionClientHandler) {
        this.institutionClientHandler = institutionClientHandler;
    }
    
    protected ArtWorkArtistClientHandler getArtWorkArtistClientHandler() {
        return artWorkArtistClientHandler;
    }

    protected void setArtWorkArtistClientHandler(ArtWorkArtistClientHandler artWorkArtistClientHandler) {
        this.artWorkArtistClientHandler = artWorkArtistClientHandler;
    }

    public ArtWorkTypeClientHandler getArtWorkTypeClientHandler() {
        return artWorkTypeClientHandler;
    }

    public void setArtWorkTypeClientHandler(ArtWorkTypeClientHandler artWorkTypeClientHandler) {
        this.artWorkTypeClientHandler = artWorkTypeClientHandler;
    }

    public ArtExhibitionClientHandler getArtExhibitionClientHandler() {
        return artExhibitionClientHandler;
    }

    public void setArtExhibitionClientHandler(ArtExhibitionClientHandler artExhibitionClientHandler) {
        this.artExhibitionClientHandler = artExhibitionClientHandler;
    }

    public ArtExhibitionItemClientHandler getArtExhibitionItemClientHandler() {
        return artExhibitionItemClientHandler;
    }

    public void setArtExhibitionItemClientHandler(ArtExhibitionItemClientHandler artExhibitionItemClientHandler) {
        this.artExhibitionItemClientHandler = artExhibitionItemClientHandler;
    }

    public ArtExhibitionGuideClientHandler getArtExhibitionGuideClientHandler() {
        return artExhibitionGuideClientHandler;
    }

    public void setArtExhibitionGuideClientHandler(ArtExhibitionGuideClientHandler artExhibitionGuideClientHandler) {
        this.artExhibitionGuideClientHandler = artExhibitionGuideClientHandler;
    }

    public UserClientHandler getUserClientHandler() {
        return userClientHandler;
    }

    public void setUserClientHandler(UserClientHandler userClientHandler) {
        this.userClientHandler = userClientHandler;
    }
    
    private String getCacheWorkDir() {
        return getHomeDirAbsolutePath() + File.separator + "tmp" + File.separator + rand.randomString(6);
    }

    private String getHomeDirAbsolutePath() {
        if (isLinux())
            return ClientConstant.linux_home;
        return ClientConstant.windows_home;
    }


}
