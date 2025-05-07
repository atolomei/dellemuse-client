package dellemuse.client;

import java.util.HashMap;
import java.util.Map;

import dellemuse.model.ArtWorkModel;
import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.InstitutionTypeModel;
import dellemuse.model.PersonModel;

public class Endpoint {

    // public static final String ROOT                      = "dellemuse";

    public static final String API_PING[]                   = { "ping"                                      };
    public static final String API_METRICS[]                = { "metrics"                                   };
    public static final String API_SYSTEM_INFO[]            = { "systeminfo"                                };
    
    /**
     *  Site
     */
    public static final String API_SITE_LISTBYINSTITUTION_GET[]    = { "institution", "sites", "{institutionid}"    };
    public static String[] getEndPointSitesByInstitution() {return API_SITE_LISTBYINSTITUTION_GET;}
    
    
    public static final String API_ARTEXHIBITIONS_BY_SITE_GET[]    = { "site", "artexhibitions", "{siteid}"    };
    public static String[] getEndPointArtExhibitionsBySite() {return API_ARTEXHIBITIONS_BY_SITE_GET;}
    
                                
    public static final String API_SITE_BY_SHORTNAME_GET[]    = { "site", "shortname", "{shortname}"    };
    
    public static String[] getEndPointSitebyShortName() {
        return API_SITE_BY_SHORTNAME_GET;
    }

    
    public static final String API_ARTEXHIBITIONGUIDE_CONTENTS_GET[]    = { "artexhibitionguide", "contents", "{guideid}"    };
    
    
    public static String[] getArtExhibitionGuideContens(String guideid) {
        return put( API_ARTEXHIBITIONGUIDE_CONTENTS_GET, guideid);
    }
    

    public static final String API_ARTEXHIBITIONGUIDES_BY_ARTEXHIBITION_GET[]    = { "artexhibition", "artexhibitionguides", "{artexhibitionid}"    };
    public static String[] getEndPointArtExhibitionGuidesByArtExhibition() {
        return API_ARTEXHIBITIONGUIDES_BY_ARTEXHIBITION_GET;
    }

                                    
    public static final String API_FLOORS_BY_SITE_GET[]    = { "site", "floors", "{siteid}"    };
    public static String[] getEndPointFloorsBySite() {
        return API_FLOORS_BY_SITE_GET;
    }

                                            
    public static final String API_ROOMS_BY_FLOOR_GET[]    = { "site", "floor", "rooms", "{floorid}"    };
    public static String[] getEndPointRoomsByFloor() {
        return API_ROOMS_BY_FLOOR_GET;
    }

    /**
     *  ArtExhibitionGuideContents
     */
    
    //public static String[] getEndPointArtExhibitionsBySite() {return API_SITE_LISTBYINSTITUTION_GET;}
    
    
    
    
    static Map<Class<?>, Map<String, String[]>> map = new HashMap<Class<?>, Map<String, String[]>>();
    
    public static String[] getEndPointId(Class<?> clientClass) {
        String [] arr = {clientClass.getSimpleName().toLowerCase().replaceAll("(?i)(.*)model$", "$1"), "get", "{id}"};
        return arr;
    }

    public static String[] getEndPointName(Class<?> clientClass) {
        String [] arr = {clientClass.getSimpleName().toLowerCase().replaceAll("(?i)(.*)model$", "$1"), "getbyname", "name}"};
        return arr;
    }
    
    public static String[] getEndPointList(Class<?> clientClass) {
        String [] arr = {clientClass.getSimpleName().toLowerCase().replaceAll("(?i)(.*)model$", "$1"), "list"};
        return arr;
    }

    public static String[] getEndPointExists(Class<?> clientClass) {
        String [] arr = {clientClass.getSimpleName().toLowerCase().replaceAll("(?i)(.*)model$", "$1"), "exists", "{id}"};
        return arr;
    }

    public static String[] getEndPointDelete(Class<?> clientClass) {
        String [] arr = {clientClass.getSimpleName().toLowerCase().replaceAll("(?i)(.*)model$", "$1"), "delete", "{id}"};
        return arr;
    }

    public static String[] getEndPointCreate(Class<?> clientClass) {
        String [] arr = {clientClass.getSimpleName().toLowerCase().replaceAll("(?i)(.*)model$", "$1"), "create", "{name}"};
        return arr;
    }
    public static String[] getEndPointUpdate(Class<?> clientClass) {
        String [] arr = {clientClass.getSimpleName().toLowerCase().replaceAll("(?i)(.*)model$", "$1"), "update" };
        return arr;
    }

    public static String[] getPingEndPoint() {
        return API_PING;
    }


    private static String[] put(String[] arr, String value) {
        String path[] = new String[arr.length];
        for (int n=0; n<path.length; n++)
            path[n]=arr[n];
        path[path.length-1]=value;
        return path;
    }



    
}

