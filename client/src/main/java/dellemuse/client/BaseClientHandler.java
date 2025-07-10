package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.InstitutionModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

public abstract class BaseClientHandler<T extends DelleMuseModelObject> implements IBaseClientHandler<T> {
                
    private static final Logger logger = Logger.getLogger(BaseClientHandler.class.getName());
    
    private final DelleMuseClient client;
    
    private final Class<?> clientClass;
    
    public BaseClientHandler(DelleMuseClient client, Class<?> clientClass) {
        this.client=client;
        this.clientClass=clientClass;
    }
    
    /**
     * 
     * 
     * 
     * 
     */
    @SuppressWarnings("unchecked")    
    public T update(T item) throws DelleMuseClientException {
        
        Check.requireNonNullArgument(item, "item is null");
        
        String str = null;
        
        byte data[] = item.toJSON().getBytes();
        int len = data.length;
        
        String endPoint [] = Endpoint.getEndPointUpdate(getClientClass());
        String path[] = new String[endPoint.length + 1];
        int counter = 0;
        for (String s: endPoint)
            path[counter++]=s;
        
        try (Response response = getClient().executePost(
                                        path,
                                        Optional.empty(),
                                        Optional.empty(),
                                        Optional.of (data), 
                                        len, 
                                        false)) {
            
            str = response.body().string();
            
     
        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage()));
        }
        
        try {

            return (T) getClient().getObjectMapper().readValue(str, getClientClass());
            
        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1",
                            "error parsing server response" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));
        } 
        
        
        
    }
    
    /**
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    public T get(Long id) throws DelleMuseClientException {

            Check.requireNonNullArgument(id, "id is null or empty");
            String str = null;

            String endPoint [] = Endpoint.getEndPointId(getClientClass());
            
            try (Response response = getClient().executeGetById(endPoint, id)) {

                str = response.body().string();

            } catch (IOException e) {
                logger.debug(e);
                throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                        ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                        + "| id:" + id.toString());
            }

            try {
                
                return (T) getClient().getObjectMapper().readValue(str, getClientClass());

            } catch (JsonProcessingException e) {
                logger.debug(e);
                throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                        ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                        + e.getClass().getSimpleName() + " - " + e.getMessage() + "| id:" + id.toString()));
            }
        }
    
    /**
     * 
     * @param id
     * @return
     * @throws DelleMuseClientException
     */
    public boolean exists(Long id) throws DelleMuseClientException {
        
        Check.requireNonNullArgument(id, "id is null or empty");
        String str = null;

        String endPoint [] = Endpoint.getEndPointExists(getClientClass());
        
        try (Response response = getClient().executeGetById(endPoint, id)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| id:" + id.toString());
        }

        try {
            
            return getClient().getObjectMapper().readValue(str, Boolean.class).booleanValue();

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| id:" + id.toString()));
        }
    }

    
    /**
     * 
     * 
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    public T findByName(String name) throws DelleMuseClientException {
        
        Check.requireNonNullStringArgument(name, "name is null or empty");

        String str = null;

        String endPoint [] = Endpoint.getEndPointName(getClientClass());
        
        try (Response response = getClient().executeGetByName(endPoint, name)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| name:" + name);
        }

        try {
            
            return (T) getClient().getObjectMapper().readValue(str, getClientClass());

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| name:" + name));
        }

        
    }
    
    
    /**
     * 
     * 
     * 
     * 
     */
    public List<T> findAll() throws DelleMuseClientException  { 
        
        String str = null;

        String endPoint [] = Endpoint.getEndPointList(getClientClass());
        
        try (Response response = getClient().executeGetReq(endPoint)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage()));
        }

        try {

            return getClient().getObjectMapper().readValue(str, getTypeReference() );
            
        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1",
                            "error parsing server response" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));
        } 
    }

    
    /**
     * 
     * 
     * 
     * 
     */
    public void delete(Long id) throws DelleMuseClientException {
        Check.requireNonNullArgument(id, "id is null");
        String endPoint [] = Endpoint.getEndPointDelete(getClientClass());
        String str = null;
        try (Response response = getClient().executeDeleteById(endPoint, id)) {
            str = response.body().string();
        } catch (IOException e) {
            logger.debug(e, str);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage()));
        }
    }
    

    /**
     * 
     * 
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    public T create(String name)  throws DelleMuseClientException {
        
        Check.requireNonNullStringArgument(name, "name is null");
        
        String str = null;
        
        byte data[] = "".getBytes();
        int len = data.length;
        
        String endPoint [] = Endpoint.getEndPointCreate(getClientClass());
        String path[] = new String[endPoint.length + 1];
        int counter = 0;
        for (String s: endPoint)
            path[counter++]=s;
        path[counter]=name;
        
        try (Response response = getClient().executePost(
                                        path,
                                        Optional.empty(),
                                        Optional.empty(),
                                        Optional.of (data), 
                                        len, 
                                        false)) {
            
            str = response.body().string();
            
     
        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage()));
        }
        
        try {

            return (T) getClient().getObjectMapper().readValue(str, getClientClass());
            
        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1",
                            "error parsing server response" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));
        } 
    }

    
    /**
     * 
     * 
     * 
     * 
     */
    public Class<?> getClientClass() {
        return clientClass;
    }
    
    
    /**
     * 
     * 
     * 
     * 
     */
    public abstract TypeReference<List<T>> getTypeReference();
    
    public DelleMuseClient getClient() {
        return this.client;
    }

    
    
    
}
