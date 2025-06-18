package dellemuse.client;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtWorkArtistModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

/***
 * 
 * 
 */
public class ArtWorkClientHandler extends BaseClientHandler<ArtWorkModel> {

    private static final Logger logger = Logger.getLogger(ArtWorkClientHandler.class.getName());

    public ArtWorkClientHandler(DelleMuseClient client) {
        super(client, ArtWorkModel.class);
    }

    public TypeReference<List<ArtWorkModel>> getTypeReference() {
        return new TypeReference<List<ArtWorkModel>>() {};
    }

    
    
    
    
    
        

}
