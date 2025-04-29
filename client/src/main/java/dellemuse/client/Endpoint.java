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
    
    /** Site
     * 
     */
    public static final String API_SITE_LISTBYINSTITUTION_GET[]    = { "site", "listbyinstitution", "{institutionid}"    };

    
    
    public static String[] getEndPointSiteListByInstitution() {
        return API_SITE_LISTBYINSTITUTION_GET;
    }
    
    
    
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

    
}

