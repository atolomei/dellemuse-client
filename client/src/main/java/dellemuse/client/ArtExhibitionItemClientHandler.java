package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionItemModel;
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
public class ArtExhibitionItemClientHandler extends BaseClientHandler<ArtExhibitionItemModel> {

    private static final Logger logger = Logger.getLogger(ArtExhibitionItemClientHandler.class.getName());

    public ArtExhibitionItemClientHandler(DelleMuseClient client) {
        super(client, ArtExhibitionItemModel.class);
    }
    
    public TypeReference<List<ArtExhibitionItemModel>> getTypeReference() {
        return new TypeReference<List<ArtExhibitionItemModel>>() {};
    }
  
}
