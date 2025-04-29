package dellemuse.client.test.unit.list;

import org.junit.jupiter.api.Test;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.client.test.base.BaseTest;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;

public class List extends BaseTest {
            
    private static final Logger logger = Logger.getLogger(List.class.getName());

    public List() {
    }
    
    @Test
    @Override
    public void executeTest() {

        
         ping();
         
         
         listInstitutions();
         
         listSites();
         
         
         listArtExhibitions();

         
         listArtExhibitionGuides();
         
         
         
         
         
         showResults();
        
        
    }

    private void listArtExhibitionGuides() {
        try {
            for (ArtExhibitionGuideModel model : getClient().listArtExhibitionGuides() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        getMap().put("list ArtExhibition Guides", "ok");

        
    }

    private void listArtExhibitions() {
        try {
            for (ArtExhibitionModel model : getClient().listArtExhibitions() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        getMap().put("list ArtExhibitions", "ok");

        
    }

    private void listSites() {
        try {
            for (SiteModel model : getClient().listSites() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        getMap().put("list sites", "ok");
        
    }

    private void listInstitutions() {
        try {
            for (InstitutionModel model : getClient().listInstitutions() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        
        getMap().put("list institutions", "ok");
        

        
    }


}
