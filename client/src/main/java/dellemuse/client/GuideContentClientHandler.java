package dellemuse.client;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtWorkArtistModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

/***
 * 
 * 
 */
public class GuideContentClientHandler extends BaseClientHandler<GuideContentModel> {

    private static final Logger logger = Logger.getLogger(GuideContentClientHandler.class.getName());

    public GuideContentClientHandler(DelleMuseClient client) {
        super(client, GuideContentModel.class);
    }

    public TypeReference<List<GuideContentModel>> getTypeReference() {
        return new TypeReference<List<GuideContentModel>>() {};
    }

    
        

}
