package dellemuse.client;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtWorkArtistModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

/***
 * 
 * 
 */
public class ArtWorkArtistClientHandler extends BaseClientHandler<ArtWorkArtistModel> {

    private static final Logger logger = Logger.getLogger(ArtWorkArtistClientHandler.class.getName());

    public ArtWorkArtistClientHandler(DelleMuseClient client) {
        super(client, ArtWorkArtistModel.class);
    }

    
    public TypeReference<List<ArtWorkArtistModel>> getTypeReference() {
        return new TypeReference<List<ArtWorkArtistModel>>() {};
    }
    
    
    /**
     * @param id
     * @return
     * @throws Exception

    @Override
    public ArtWorkArtistModel get(Long id) throws DelleMuseClientException {
        Check.requireNonNullArgument(id, "id is null or empty");
        String str = null;

        try (Response response = getClient().executeGetById(Endpoint.API_ARTWORKTYPE_GET, id)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                            + "| id:" + id.toString());
        }

        try {

            return getClient().getObjectMapper().readValue(str, ArtWorkArtistModel.class);

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                            + e.getClass().getSimpleName() + " - " + e.getMessage() + "| id:" + id.toString()));
        }
    }
     */
    
    /**
     * @return
     * @throws DelleMuseClientException
    
    public List<ArtWorkArtistModel> findAll() throws DelleMuseClientException {

        String str = null;

        try (Response response = getClient().executeGetReq(Endpoint.API_ARTWORK_LIST)) {

            str = response.body().string();

        } catch (IOException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage()));
        }

        try {

            return getClient().getObjectMapper().readValue(str, new TypeReference<List<ArtWorkArtistModel>>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1",
                            "error parsing server response" + " | " + e.getClass().getSimpleName() + " - " + e.getMessage()));
        }
    }
 */
    
    @Override
    public ArtWorkArtistModel findByName(String name) throws DelleMuseClientException {
        throw new RuntimeException("not done");
    }

    @Override
    public ArtWorkArtistModel create(String name) throws DelleMuseClientException {
        throw new RuntimeException("not done");
    }

    @Override
    public void delete(Long id) throws DelleMuseClientException {
        throw new RuntimeException("not done");        
    }

    @Override
    public ArtWorkArtistModel update(ArtWorkArtistModel item) throws DelleMuseClientException {
        throw new RuntimeException("not done");
    }

    @Override
    public boolean exists(Long id) throws DelleMuseClientException {
        // TODO Auto-generated method stub
        return false;
    }

}
