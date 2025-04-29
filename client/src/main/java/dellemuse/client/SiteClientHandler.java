package dellemuse.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.InstitutionModel;
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
public class SiteClientHandler extends BaseClientHandler<SiteModel> {

    private static final Logger logger = Logger.getLogger(SiteClientHandler.class.getName());

    public SiteClientHandler(DelleMuseClient client) {
        super(client, SiteModel.class);
    }

    
    public TypeReference<List<SiteModel>> getTypeReference() {
        return new TypeReference<List<SiteModel>>() {};
    }

    
    public List<ArtExhibitionModel> findArtExhibition(SiteModel site) throws DelleMuseClientException {
     
        Check.requireNonNullArgument(site, "Site is null");
        String str = null;

        String endPoint [] = Endpoint.getEndPointSiteListByInstitution();

        String path[] = new String[endPoint.length + 1];
        int counter = 0;
        for (String s: endPoint)
            path[counter++]=s;
        path[counter]=site.getId().toString();
        
        try (Response response = getClient().executeGetReq(path)) {

            str = response.body().string();

        } catch (IOException e ) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| site:" + site.getId().toString());
        } 
        
        try {
            
            return getClient().getObjectMapper().readValue(str, new TypeReference<List<ArtExhibitionModel>>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| site:" + site.getId().toString()));
        }
    }


    
    
   /**
    public List<ArtExhibitionGuideModel> findArtExhibitionGuide(ArtExhibitionModel artExhibition) throws DelleMuseClientException {
        return null;
    }
    
    
    public List<ArtExhibitionItemModel> findArtExhibitionItem(ArtExhibitionModel artExhibition) throws DelleMuseClientException {
        return null;
    }
   **/  
    
    
    public List<SiteModel> findSites(InstitutionModel institution) throws DelleMuseClientException {

        Check.requireNonNullArgument(institution, "institution is null");
        String str = null;

        String endPoint [] = Endpoint.getEndPointSiteListByInstitution();
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
            return getClient().getObjectMapper().readValue(str, getTypeReference() );

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| institution:" + institution.getId().toString()));
        }
    }
}

