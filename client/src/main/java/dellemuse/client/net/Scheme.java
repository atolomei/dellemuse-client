package dellemuse.client.net;

/**
 * <p>HTTP schemes</p>
 * 
 *  @author atolomei@novamens.com (Alejandro Tolomei)
 *  
 */
public enum Scheme {
  HTTP("http"), 
  HTTPS("https");
  private final String value;

  private Scheme(String value) {
    this.value = value;
  }

  /**
   * <p>Returns Scheme enum of given string</p>
   */
  public static Scheme fromString(String scheme) {
    if (scheme == null) {
      throw new IllegalArgumentException("null scheme");
    }

    for (Scheme s : Scheme.values()) {
      if (scheme.equalsIgnoreCase(s.value)) {
        return s;
      }
    }

    throw new IllegalArgumentException("invalid HTTP scheme '" + scheme + "'");
  }
}