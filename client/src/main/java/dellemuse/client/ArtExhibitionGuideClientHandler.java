package dellemuse.client;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

/***
 * 
 * 
 * 
 */
public class ArtExhibitionGuideClientHandler extends BaseClientHandler<ArtExhibitionGuideModel> {
            
    private static final Logger logger = Logger.getLogger(ArtExhibitionGuideClientHandler.class.getName());

    public ArtExhibitionGuideClientHandler(DelleMuseClient client) {
        super(client, ArtExhibitionGuideModel.class);
    }
    
    public TypeReference<List<ArtExhibitionGuideModel>> getTypeReference() {
        return new TypeReference<List<ArtExhibitionGuideModel>>() {};
    }
 
    
    public List<GuideContentModel> listGuideContentsByArtExhibition(ArtExhibitionGuideModel artExhibitionGuide) throws DelleMuseClientException {
        
        Check.requireNonNullArgument(artExhibitionGuide, "artExhibitionGuide is null");
        
        String path[] = Endpoint.getArtExhibitionGuideContens(artExhibitionGuide.getId().toString());
        
        String str = null;
        
        try (Response response = getClient().executeGetReq(path)) {

            str = response.body().string();

        } catch (IOException e ) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| artExhibitionGuide:" + artExhibitionGuide.getId().toString());
        } 
        
        try {
            
            return getClient().getObjectMapper().readValue(str, new TypeReference<List<GuideContentModel>>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| artExhibitionGuide:" + artExhibitionGuide.getId().toString()));
        }
    }
}
