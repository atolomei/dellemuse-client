package dellemuse.client;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.PersonModel;
import dellemuse.model.UserModel;
import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.HttpStatus;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import okhttp3.Response;

public class UserClientHandler extends BaseClientHandler<UserModel> {
            
    private static final Logger logger = Logger.getLogger(UserClientHandler.class.getName());

    public UserClientHandler(DelleMuseClient client) {
        super(client, UserModel.class);
    }
    
    public TypeReference<List<UserModel>> getTypeReference() {
        return new TypeReference<List<UserModel>>() {};
    }

    
    
}


