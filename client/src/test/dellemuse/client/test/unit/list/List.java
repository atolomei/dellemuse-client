package dellemuse.client.test.unit.list;


import org.junit.jupiter.api.Test;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.client.test.base.BaseTest;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.FloorModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.PersonModel;
import dellemuse.model.RoomModel;
import dellemuse.model.SiteModel;
import dellemuse.model.UserModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;

public class List extends BaseTest {
            
    private static final Logger logger = Logger.getLogger(List.class.getName());

    public List() {
    }
    
    @Test
    @Override
    public void executeTest() {

         ping();

         logger.info(Constant.SEPARATOR);

         listPersons();
         listUsers();
         
         
         listInstitutions();
         listSites();
         listSitePlan();
         listArtExhibitions();
         
         
         listArtExhibitionGuides();
         listArtExhibitionGuideContents();

         
         
         listSiteContents();
         
         showResults();
         
         
         SiteModel site;
         
         try {
           site = getSiteByShortName("MNBA");
         
           
           
        } catch (DelleMuseClientException e) {
            error(e);
        }

         
         //getSiteHome(site);
         
         
         
         
         /**----------------------------------------
          * Site 
          * 
          * 
          * 
          * getSiteNews();
          * 
          * getSiteExhibitions();
          * 
          * Exhibition -> description list of guides
          * 
          * Guide Page.
          * Description
          * Room
          * List of 
          * 
          */
         
    }

    /**
    private void getSiteHome(SiteModel site) {
        
        getClient().getSiteClientHandler().getSiteMainContent();
        
    }
**/
    
    
    private void testHome() {
    
        
        
    }
    
    
    
    private SiteModel getSiteByShortName(String string) throws DelleMuseClientException {
        SiteModel model = getClient().getSiteClientHandler().getByShortName(string);
        return model;
    }

    /**
     * 
     * 
     * 
     */
    private void listSitePlan() {
        
        int n_site = 0;
        int n_floor = 0;
        int n_room = 0;
        
        logger.info("list Site Plan");
        
        try {
            for (SiteModel model : getClient().listSites() ) {

                logger.info( String.valueOf(++n_site) + " - Site ->  id: " + model.getId() + " title: " + model.getTitle());
                
                java.util.List<FloorModel> floors = getClient().getSiteClientHandler().listFloorsBySite(model);
                
                n_floor = 0;

                for (FloorModel floor: floors) {
                
                        logger.info(String.valueOf(n_site) + String.valueOf(++n_floor) + " - Floor ->  id: " + floor.getId() + " title: " + floor.getTitle());
                        
                        java.util.List<RoomModel> rooms = getClient().getSiteClientHandler().listRoomsByFloor(floor);
                        n_room = 0;
                        
                        for (RoomModel room: rooms)
                            logger.info(String.valueOf(n_site) + String.valueOf(n_floor) + String.valueOf(++n_room) + " - Room ->  id: " + room.getId() + " title: " + room.getTitle());
                }
                logger.info("");
            }
        } catch (DelleMuseClientException e) {
                error(e);
        }
        logger.info(Constant.SEPARATOR);
        getMap().put("list ArtExhibition Guide Contents", "ok");
    }

    /**
     * 
     * 
     * 
     */
    private void listSiteContents() {
        
        int n_site = 0;
        int n_exhibition = 0;
        int n_guide = 0;
        int n_content = 0;
        
        
        logger.info("list Site Contents");
        try {
            for (SiteModel model : getClient().listSites() ) {

                logger.info( String.valueOf(++n_site) + " - Site ->  id: " + model.getId() + " title: " + model.getTitle());
                
                java.util.List<ArtExhibitionModel> exhibitions = getClient().getSiteClientHandler().listArtExhibitionsBySite(model);
                
                n_exhibition = 0;
                
                for (ArtExhibitionModel ae: exhibitions) {
                
                        logger.info( String.valueOf(n_site) + String.valueOf(++n_exhibition) + " - ArtExhibition ->  id: " + ae.getId() + " title: " + ae.getTitle());
                        
                        java.util.List<ArtExhibitionGuideModel> guides = getClient().getArtExhibitionClientHandler().listArtExhibitionGuides(ae);
                        
                        n_guide = 0;
                        
                        for (ArtExhibitionGuideModel guide: guides) {
                            
                            logger.info(String.valueOf(n_site) + String.valueOf(n_exhibition) + String.valueOf(++n_guide)  + " - Art Exhibition Guide -> " + guide.toString());
                            
                            n_content = 0;
                            
                            for (GuideContentModel content: getClient().getArtExhibitionGuideClientHandler().listGuideContentsByArtExhibition(guide)) {

                                logger.info( String.valueOf(n_site) + String.valueOf(n_exhibition) + String.valueOf(n_guide) + String.valueOf(++n_content) + " - Guide Content -> " + content.toString());
                            }
                        }
                }
                logger.info("");
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        getMap().put("list Site ArtExhibition Guide Contents", "ok");
    }
    
    /**
     * 
     * 
     */
    private void listArtExhibitionGuideContents() {
        
        logger.info("list ArtExhibition Guide Contents");
        try {
            for (ArtExhibitionGuideModel model : getClient().listArtExhibitionGuides() ) {

                logger.info(" ArtExhibition Guide id -> " + model.getId());
                java.util.List<GuideContentModel> list = getClient().listArtExhibitionGuideContents(model);
                for (GuideContentModel content : list ) {
                    logger.info(content.toString());
                }
                logger.info("");
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        getMap().put("list ArtExhibition Guide Contents", "ok");
    }
    

    /**
     * 
     * 
     * 
     */
    private void listArtExhibitionGuides() {

        logger.info("list ArtExhibition Guides");
        try {
            for (ArtExhibitionGuideModel model : getClient().listArtExhibitionGuides() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        getMap().put("list ArtExhibition Guides", "ok");

        
    }

    /**
     * 
     * 
     * 
     */
    private void listArtExhibitions() {

        logger.info("list ArtExhibitions");
        try {
            for (ArtExhibitionModel model : getClient().listArtExhibitions() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        getMap().put("list ArtExhibitions", "ok");
    }
    

    /**
     * 
     * 
     * 
     */
    private void listSites() {
        logger.info("list Sites");
        try {
            for (SiteModel model : getClient().listSites() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        getMap().put("list sites", "ok");
        
    }

    /**
     * 
     * 
     * 
     */
    private void listInstitutions() {
        logger.info("list Institutions");
        try {
            for (InstitutionModel model : getClient().listInstitutions() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        
        getMap().put("list institutions", "ok");
        

        
    }

    /**
     * 
     * 
     * 
     */
    private void listPersons() {
        logger.info("list Persons");
        try {
            for (PersonModel model : getClient().listPersons() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        
        getMap().put("list persons", "ok");
        

        
    }

    private void listUsers() {
        logger.info("list Users");
        try {
            for (UserModel model : getClient().listUsers() ) {
                logger.info(model.toString());
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        
        getMap().put("list users", "ok");
    }
    
    
    
    private void listArtExhibitionGuidesDetail() {

        try {
            for (ArtExhibitionGuideModel model : getClient().listArtExhibitionGuides() ) {
                
                

                
                
                
            }
        } catch (DelleMuseClientException e) {
                error(e);
        };
        logger.info(Constant.SEPARATOR);
        getMap().put("list ArtExhibition Guides", "ok");

    }
    

    

    
}
