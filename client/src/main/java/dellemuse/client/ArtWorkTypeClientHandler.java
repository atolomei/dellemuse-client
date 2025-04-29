package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

/***
 * 
 */
public class ArtWorkTypeClientHandler extends BaseClientHandler<ArtWorkTypeModel> {

    private static final Logger logger = Logger.getLogger(ArtWorkTypeClientHandler.class.getName());

    public ArtWorkTypeClientHandler(DelleMuseClient client) {
        super(client, ArtWorkTypeModel.class);
    }


    public TypeReference<List<ArtWorkTypeModel>> getTypeReference() {
        return new TypeReference<List<ArtWorkTypeModel>>() {};
    }

    /**
     * @param id
     * @return
     * @throws Exception
     */
    public ArtWorkTypeModel get(Long id) throws DelleMuseClientException {

        Check.requireNonNullArgument(id, "id is null or empty");
        String str = null;

        try (Response response = getClient().executeGetById(Endpoint.getEndPointId(getClientClass()), id)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| id:" + id.toString());
        }

        try {

            return getClient().getObjectMapper().readValue(str, ArtWorkTypeModel.class);

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| id:" + id.toString()));
        }
    }


    /**
     * @return
     * @throws DelleMuseClientException
     */
    public List<ArtWorkTypeModel> findAll() throws DelleMuseClientException {

        String str = null;

        try (Response response = getClient().executeGetReq(Endpoint.getEndPointList(getClientClass()))) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage()));
        }

        try {

            return getClient().getObjectMapper().readValue(str, new TypeReference<List<ArtWorkTypeModel>>() {
            });

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1",
                            "error parsing server response" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));
        }
    }

    @Override
    public ArtWorkTypeModel findByName(String name) throws DelleMuseClientException {

        Check.requireNonNullArgument(name, "name is null or empty");
        
        String str = null;

        try (Response response = getClient().executeGetByName(Endpoint.getEndPointName(getClientClass()), name)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| id:" + name);
        }

        try {

            return getClient().getObjectMapper().readValue(str, ArtWorkTypeModel.class);

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| id:" + name));
        }
    }

    @Override
    public ArtWorkTypeModel create(String name) throws DelleMuseClientException {
        throw new RuntimeException("not done");
    }

    @Override
    public void delete(Long id) throws DelleMuseClientException {

        Check.requireNonNullArgument(id, "id is null or empty");
        String str = null;

        try (Response response = getClient().executeDeleteById(Endpoint.getEndPointDelete(getClientClass()), id)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| id:" + id.toString());
        }
    }

   
    /**
     * 
     * 
     */
    @Override
    public ArtWorkTypeModel update(ArtWorkTypeModel item) throws DelleMuseClientException {
        

        Optional<byte[]> data;
        try {

            data = Optional.of( getClient().getObjectMapper().writeValueAsString(item).getBytes());
            
        } catch (JsonProcessingException e) {
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error serializing object " + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| " + item.toString()));
        }
        
        try ( Response response = getClient().executePost( Endpoint.getEndPointUpdate(getClientClass()),   
                Optional.empty(), 
                Optional.empty(), 
                 data, 
                data.get().length, 
                false) ) {
            
            return item;
            
        }
    }

    
    @Override
    public boolean exists(Long id) throws DelleMuseClientException {
        // TODO Auto-generated method stub
        return false;
    }

}
