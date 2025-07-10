package dellemuse.client.test;

import java.util.List;

import dellemuse.client.ArtWorkClientHandler;
import dellemuse.client.DelleMuseClient;
import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.InstitutionModel;
import dellemuse.model.logging.Logger;

public class Test {
    
    private static final Logger logger = Logger.getLogger(Test.class.getName());
    
    public static void main(String[] args) {
        Test t = new Test();
        t.run();
    }
    
    
    public Test() {
    }
    
    
    DelleMuseClient client;
    
    public void run() {

        this.client = new  DelleMuseClient("http://localhost", 9876, "dellemuse", "dellemuse");

        try {
            InstitutionModel model = this.client.getInstitution(Long.valueOf(151));
            logger.error(model.toString());
        } catch (DelleMuseClientException e) {
            e.printStackTrace();
        }

        
        try {
            

            this.client.listInstitutions().forEach( item -> {
                logger.error( item.toString() );
            });
            
            
        } catch (DelleMuseClientException e) {
            e.printStackTrace();
        }
        

        
        try {
            this.client.listArtWorks().forEach( item -> {
                logger.error( item.toString() );
            })  ;
        } catch (DelleMuseClientException e) {
            e.printStackTrace();
        }


        try {
            this.client.listPersons().forEach( item -> {
                logger.error( item.toString() );
            })  ;
        } catch (DelleMuseClientException e) {
            e.printStackTrace();
        }

        
        
        
    }

}
