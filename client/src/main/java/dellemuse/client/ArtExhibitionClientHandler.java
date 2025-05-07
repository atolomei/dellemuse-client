package dellemuse.client;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

/***
 * 
 */
public class ArtExhibitionClientHandler extends BaseClientHandler<ArtExhibitionModel> {

    private static final Logger logger = Logger.getLogger(ArtExhibitionClientHandler.class.getName());

    public ArtExhibitionClientHandler(DelleMuseClient client) {
        super(client, ArtExhibitionModel.class);
    }
    
    public TypeReference<List<ArtExhibitionModel>> getTypeReference() {
        return new TypeReference<List<ArtExhibitionModel>>() {};
    }

    public List<ArtExhibitionGuideModel> listArtExhibitionGuides(ArtExhibitionModel artExhibition) throws DelleMuseClientException {
        
        Check.requireNonNullArgument(artExhibition, "artExhibition is null");
        String str = null;

        String endPoint [] = Endpoint.getEndPointArtExhibitionGuidesByArtExhibition();

        String path[] = new String[endPoint.length];
        for (int n=0; n<path.length; n++)
            path[n]=endPoint[n];
        path[path.length-1]=artExhibition.getId().toString();
        
        try (Response response = getClient().executeGetReq(path)) {

            str = response.body().string();

        } catch (IOException e ) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| artExhibition:" + artExhibition.getId().toString());
        } 
        
        try {
            
            return getClient().getObjectMapper().readValue(str, new TypeReference<List<ArtExhibitionGuideModel>>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| artExhibition:" + artExhibition.getId().toString()));
        }
    }

    
}
