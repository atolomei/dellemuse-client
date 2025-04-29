package dellemuse.client.net;

/**
 * HTTP methods.
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 * 
 */
public enum Method {
  GET("GET"), 
  HEAD("HEAD"), 
  POST("POST"), 
  PUT("PUT"), 
  DELETE("DELETE");

	@SuppressWarnings("unused")
	private final String value;

	private Method(String value) {
		this.value = value;
	}
}