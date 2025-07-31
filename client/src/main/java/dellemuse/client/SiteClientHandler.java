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
import dellemuse.model.ArtWorkModel;
import dellemuse.model.FloorModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.RoomModel;
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

    

    /**
     * 
     * 
     * @param name
     * @return
     * @throws DelleMuseClientException
     */
    public SiteModel getByShortName(String name) throws  DelleMuseClientException {

        Check.requireNonNullArgument(name, "name is null");
        String str = null;

        String endPoint [] = Endpoint.getEndPointSitebyShortName();

        String path[] = new String[endPoint.length];
        for (int n=0; n<path.length-1; n++)
            path[n]=endPoint[n];
        path[path.length-1]=name;
        
        try (Response response = getClient().executeGetReq(path)) {

            str = response.body().string();

        } catch (IOException e ) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| name:" + name);
        } 
        
        try {
            
            return getClient().getObjectMapper().readValue(str, new TypeReference<SiteModel>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| name: " + name));
        }
    }
    
    /**
     * 
     * @param site
     * @return
     * @throws DelleMuseClientException
     */
    
    public List<ArtExhibitionModel> listArtExhibitionsBySite(SiteModel site) throws DelleMuseClientException {
     
        Check.requireNonNullArgument(site, "Site is null");
        String str = null;

        String endPoint [] = Endpoint.getEndPointArtExhibitionsBySite();

        String path[] = new String[endPoint.length];
        for (int n=0; n<path.length-1; n++)
            path[n]=endPoint[n];
        path[path.length-1]=site.getId().toString();
        
        
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
 * 
 * 
 * @param site
 * @return
 * @throws DelleMuseClientException
 */
	public List<GuideContentModel> listGuideContentsBySite(SiteModel site)  throws  DelleMuseClientException {

		   Check.requireNonNullArgument(site, "Site is null");
	        String str = null;

	        String endPoint [] = Endpoint.getEndPointGuideContentsBySite();

	        String path[] = new String[endPoint.length];
	        for (int n=0; n<path.length-1; n++)
	            path[n]=endPoint[n];
	        path[path.length-1]=site.getId().toString();
	        
	        
	        try (Response response = getClient().executeGetReq(path)) {

	            str = response.body().string();

	        } catch (IOException e ) {
	            logger.debug(e);
	            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
	                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
	                    + "| site:" + site.getId().toString());
	        } 
	        
	        try {
	            
	            return getClient().getObjectMapper().readValue(str, new TypeReference<List<GuideContentModel>>() {});

	        } catch (JsonProcessingException e) {
	            logger.debug(e);
	            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
	                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
	                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| site:" + site.getId().toString()));
	        } 
	}

    
    
	/**
	 * 
	 * TODO AT
	 * 
	 * @param site
	 * @return
	 * @throws DelleMuseClientException
	 */
		public List<ArtExhibitionItemModel> listArtExhibitionItemsBySite(SiteModel site)  throws  DelleMuseClientException {

			   Check.requireNonNullArgument(site, "Site is null");
		        String str = null;

		        String endPoint [] = Endpoint.getEndPointArtExhibitionItemsBySite();

		        String path[] = new String[endPoint.length];
		        for (int n=0; n<path.length-1; n++)
		            path[n]=endPoint[n];
		        path[path.length-1]=site.getId().toString();
		        
		        try (Response response = getClient().executeGetReq(path)) {

		            str = response.body().string();

		        } catch (IOException e ) {
		            logger.debug(e);
		            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
		                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
		                    + "| site:" + site.getId().toString());
		        } 
		        
		        try {
		            
		            return getClient().getObjectMapper().readValue(str, new TypeReference<List<ArtExhibitionItemModel>>() {});

		        } catch (JsonProcessingException e) {
		            logger.debug(e);
		            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
		                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
		                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| site:" + site.getId().toString()));
		        } 
		}

	

    
    public List<FloorModel> listFloorsBySite(SiteModel site) throws DelleMuseClientException {
    
        Check.requireNonNullArgument(site, "site is null");
        String str = null;


        String endPoint [] = Endpoint.getEndPointFloorsBySite();
        String path[] = new String[endPoint.length];
        int counter = 0;
        for (String s: endPoint)
            path[counter++]=s;
        path[endPoint.length-1]=site.getId().toString();
        
        try (Response response = getClient().executeGetReq(path)) {

            str = response.body().string();

        } catch (IOException e ) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| site:" + site.getId().toString());
        } 
        
        try {
            
            return getClient().getObjectMapper().readValue(str,  new TypeReference<List<FloorModel>>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| site:" + site.getId().toString()));
        }
    }
    

    public List<RoomModel> listRoomsByFloor(FloorModel floor) throws DelleMuseClientException {
        
        Check.requireNonNullArgument(floor, "floor is null");
        String str = null;


        String endPoint [] = Endpoint.getEndPointRoomsByFloor();
        String path[] = new String[endPoint.length];
        int counter = 0;
        for (String s: endPoint)
            path[counter++]=s;
        path[endPoint.length-1]=floor.getId().toString();
        
        try (Response response = getClient().executeGetReq(path)) {

            str = response.body().string();

        } catch (IOException e ) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
                    + "| floor:" + floor.getId().toString());
        } 
        
        try {
            
            return getClient().getObjectMapper().readValue(str,  new TypeReference<List<RoomModel>>() {});

        } catch (JsonProcessingException e) {
            logger.debug(e);
            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| floor:" + floor.getId().toString()));
        }
    }

    
    public List<SiteModel> listSitesByInstitution(InstitutionModel institution) throws DelleMuseClientException {

        Check.requireNonNullArgument(institution, "institution is null");
        String str = null;

        String endPoint [] = Endpoint.getEndPointSitesByInstitution();
        String path[] = new String[endPoint.length];
        int counter = 0;
        for (String s: endPoint)
            path[counter++]=s;
        path[endPoint.length-1]=institution.getId().toString();
        
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

    public TypeReference<List<SiteModel>> getTypeReference() {
        return new TypeReference<List<SiteModel>>() {};
    }


    /**
     * 
     * 
     * 
     * @param site
     * @return
     * @throws DelleMuseClientException
     */

	public List<ArtWorkModel> listArtWorksBySite(SiteModel site) throws DelleMuseClientException {

		Check.requireNonNullArgument(site, "site is null");
	        String str = null;

	        String endPoint [] = Endpoint.getEndPointArtworksBySite();
	        String path[] = new String[endPoint.length];
	        int counter = 0;
	        for (String s: endPoint)
	            path[counter++]=s;
	        path[endPoint.length-1]=site.getId().toString();
	        
	        try (Response response = getClient().executeGetReq(path)) {

	            str = response.body().string();

	        } catch (IOException e ) {
	            logger.debug(e);
	            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
	                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", getClass().getSimpleName() + " - " + e.getMessage())
	                    + "| site:" + site.getId().toString());
	        } 
	        
	        try {
	            return getClient().getObjectMapper().readValue(str, new TypeReference<List<ArtWorkModel>>() {} );

	        } catch (JsonProcessingException e) {
	            logger.debug(e);
	            throw new DelleMuseClientException(HttpStatus.OK.value(), ErrorCode.INTERNAL_ERROR.getCode(),
	                    ErrorCode.INTERNAL_ERROR.getMessage().replace("%1", "error parsing server response" + " | "
	                    + e.getClass().getSimpleName() + " - " + e.getMessage() + "| institution:" + site.getId().toString()));
	        }
	}
    
}

