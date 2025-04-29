package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
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
public class ArtExhibitionClientHandler extends BaseClientHandler<ArtExhibitionModel> {

    private static final Logger logger = Logger.getLogger(ArtExhibitionClientHandler.class.getName());

    public ArtExhibitionClientHandler(DelleMuseClient client) {
        super(client, ArtExhibitionModel.class);
    }
    
    public TypeReference<List<ArtExhibitionModel>> getTypeReference() {
        return new TypeReference<List<ArtExhibitionModel>>() {};
    }

    
    public List<ArtExhibitionGuideModel> findArtExhibitionGuide(ArtExhibitionModel artExhibition) throws DelleMuseClientException {
        return null;
    }
    
    

    
}
