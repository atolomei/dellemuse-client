package dellemuse.client.net;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class ClientConstant {
    
    public static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 60;
    public static final int HTTP_CACHE_SIZE = 200 * 1024 * 1024; // 200 mb

    public static final String APPLICATION_JSON = "application/json";
    
    public static final DateTimeFormatter HTTP_DATE = DateTimeFormatter.RFC_1123_DATE_TIME;
    
    public static final String DEFAULT_USER_AGENT = "Dellemuse (" + System.getProperty("os.arch") + "; "
            + System.getProperty("os.arch") + ") dellemuse-java/" + "1";

    public static final String linux_home = (new File(System.getProperty("user.dir"))).getPath();
    public static final String windows_home = System.getProperty("user.dir");
    
    
    /** private static final String NULL_STRING = "(null)"; */
    public static final String END_HTTP = "----------END-HTTP----------";


}
