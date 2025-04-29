package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
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
public class InstitutionClientHandler extends BaseClientHandler<InstitutionModel> {

    private static final Logger logger = Logger.getLogger(InstitutionClientHandler.class.getName());

    public InstitutionClientHandler(DelleMuseClient client) {
        super(client, InstitutionModel.class);
    }

    
    public TypeReference<List<InstitutionModel>> getTypeReference() {
        return new TypeReference<List<InstitutionModel>>() {};
    }
  
}
