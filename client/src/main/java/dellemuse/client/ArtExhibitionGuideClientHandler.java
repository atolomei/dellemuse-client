package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.InstitutionTypeModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

/***
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
 
    public List<GuideContentModel> findGuideContent(ArtExhibitionGuideModel artExhibitionGuide) throws DelleMuseClientException {
        return null;
    }
    

}
