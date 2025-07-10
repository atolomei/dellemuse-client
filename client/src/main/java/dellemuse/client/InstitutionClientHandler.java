package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.InstitutionModel;
import dellemuse.model.InstitutionTypeModel;
import dellemuse.model.SiteModel;
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
    
    public List<SiteModel> listSitesByInstitution(InstitutionModel institution) throws DelleMuseClientException {

        Check.requireNonNullArgument(institution, "institution is null");
        String str = null;

        String endPoint [] = Endpoint.getEndPointSitesByInstitution();
        String path[] = new String[endPoint.length + 1];
        int counter = 0;
        for (String s: endPoint)
            path[counter++]=s;
        path[counter]=institution.getId().toString();
        
        try (Response response = getClient().executeGetReq(path)) {

            str = response.body().string();

        } catch (IOException e ) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| institution:" + institution.getId().toString());
        } 
        
        try {
            
            return getClient().getObjectMapper().readValue(str, new TypeReference<List<SiteModel>>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| institution:" + institution.getId().toString()));
        }
    }
  
}
