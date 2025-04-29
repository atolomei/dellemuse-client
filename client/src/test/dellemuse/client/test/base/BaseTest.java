/*
 * Odilon Object Storage
 * (C) Novamens 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dellemuse.client.test.base;


import dellemuse.client.DelleMuseClient;
import dellemuse.model.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

/**
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
public abstract class BaseTest {

	private static final Logger logger = Logger.getLogger(BaseTest.class.getName());

	public String endpoint = "localhost";
	
	public int port = 9876;

	private String accessKey = "dellemuse";
	private String secretKey = "dellemuse";
	private DelleMuseClient client;

	private int max = 1000;
	
	private long max_length = 500 * 100 * 10000; // 500 MB
	public boolean isSSL = false;
	public boolean isAcceptAllCertificates = true;
	
	public void close() {
		if (this.client!=null) {
			try {
				this.client.close();
			} catch (Exception e) {
				error(e.getClass().getName() +( e.getMessage()!=null ? (" | " + e.getMessage()) : ""));
			}
		}
	}
	/**
	 * <p>Used by stand alone test</p>
	 */
	public BaseTest() {
		this(null);
	}
	
	/**
	 * <p>Used by RegressionTest</p>
	 * 
	 * @param client
	 */
	public BaseTest(DelleMuseClient client) {
		
		logger.info("Start -> " + this.getClass().getName());
		setClient(client);
		
	}

	
	public void setClient(DelleMuseClient client) {
		this.client = client;
	}

	
	
	
	public void error(Exception e) {
		error(e.getClass().getName() +( e.getMessage()!=null ? (" | " + e.getMessage()) : ""));
	}
	
	public void error(String string) {
		logger.error(string);
		System.exit(1);
	}
	
	
	@Test
	public abstract void executeTest();
	
	public boolean isPdf(String filename) {
		return filename.toLowerCase().matches("^.*\\.(pdf)$"); 
	}
	 

	public void setSSL( boolean b) {
		this.isSSL=b;
	}
	
	public boolean isSSL() {
		return this.isSSL;
	}

	public boolean isAcceptAllCertificates() {
		return this.isAcceptAllCertificates;
	}
	
	public void setAcceptAllCertificates(boolean b) {
		this.isAcceptAllCertificates=b;
	}
	
	
	public DelleMuseClient getClient() {
		try {
			if (client==null) {
					this.client = new DelleMuseClient((isSSL()?"https":"http") + "://" + endpoint, port, accessKey, secretKey, isSSL(), isAcceptAllCertificates());
			        logger.debug(this.client.toString());
			}
	        
		} catch (Exception e) {
			error(e.getClass().getName() +( e.getMessage()!=null ? (" | " + e.getMessage()) : ""));
		}
		return client;
	}

	
	private Map<String, String> map = new TreeMap<String, String>();
	
    public void ping() {

        try {
            String p=getClient().ping();
            if (p==null || !p.equals("ok"))
                error("ping  -> " + p!=null?p:"null");
            else {
                getMap().put("ping", "ok");
            }
        } catch (Exception e)   {
            error(e.getClass().getName() + " | " + e.getMessage());
        }
        
    }

    public Map<String, String> getMap() {
        return map;
    }
    
	
    public void showResults() {
        logger.info("Results");
        logger.info("-------");
        getMap().forEach((k,v) -> logger.info(k+" -> "+ v));
        logger.info("done");
        
    }

    
    
	public String randomString(int size) {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength =  size;
	    Random random = new Random();
	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    return generatedString;
	}

	protected long dateTimeDifference(Temporal d1, Temporal d2, ChronoUnit unit) {
        return unit.between(d1, d2);
    }
	
	protected int getMax() {
		return max;
	}

	protected void setMax(Integer n) {
		this.max=n.intValue();
	}

	protected void setMaxLength(Long n) {
		this.max_length=n.longValue();
	}


	
	   
	protected long getMaxLength() {return this.max_length;}
	
}
 