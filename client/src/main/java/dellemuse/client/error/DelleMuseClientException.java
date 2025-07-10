package dellemuse.client.error;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;


/**
 *	<p>Exception thrown by the client library.<br/> 
 *It contains three main parts:
 *</p> 
 *<ul>
 *  <li>The code of the {@link HttpStatus} status ({@code int})</li>
 * <li>The code of the{@link  ErrorCode ErrorCode} ({@code int})</li>
 * <li>Application level error message ({@code String})</li>
 *</ul>
 * 
 *
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
public class DelleMuseClientException extends Exception {

	private static final long serialVersionUID = 1L;

	static private ObjectMapper mapper = new ObjectMapper();
	
	static  {
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}	
	
	@JsonProperty("httpStatus")
	private int httpStatus;
	
	@JsonProperty("applicationCode")
	private int applicationErrorCode;

	@JsonProperty("applicationErrorMessage")
	private String applicationErrorMessage;
	
	@JsonProperty("context")
	private Map<String, String> context = new HashMap<String, String>();

	public DelleMuseClientException(Exception e) {
		super(e);
		this.httpStatus=0;
		this.applicationErrorCode=ErrorCode.GENERAL_CLIENT_ERROR.value();
		this.applicationErrorMessage= e.getClass().getName() +  (e.getMessage()!=null? ( " | " + e.getMessage() ) :"" );
	}
	
	public DelleMuseClientException(int errorCode, String errorMessage) {
		super(errorMessage);
      this.httpStatus=0;
        this.applicationErrorCode=errorCode;
		this.applicationErrorMessage=errorMessage;
	}

	public DelleMuseClientException(int httpStatus, int errorCode, String errorMessage) {
		super(errorMessage);
		this.httpStatus=httpStatus;
		this.applicationErrorCode=errorCode;
		this.applicationErrorMessage=errorMessage;
	}

	public String getMessage() {					
		return applicationErrorMessage;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public int getErrorCode() {
		return applicationErrorCode;
	}

	public void setErrorCode(int errorCode) {
		this.applicationErrorCode = errorCode;
	}

	public void setContext(Map<String, String> s) {
		context=s;
	}
	
	public Map<String, String> getContext() {
		return context;
	}

	@Override
	public String toString() {
			StringBuilder str = new StringBuilder();
			str.append(this.getClass().getSimpleName() +"{");
			str.append("\"httpStatus\":"  + String.valueOf(httpStatus));
			str.append(", \"errorCode\": "  + String.valueOf(applicationErrorCode));
			str.append(", \"errorMessage\": \""  + String.valueOf(Optional.ofNullable(applicationErrorMessage).orElse("null"))+"\"");
			str.append("}");
			return str.toString();
	}
	
	public String toJSON() {
	   try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
					return "\"error\":\"" + e.getClass().getName()+ " | " + e.getMessage()+"\""; 
		}
	}
}
