package dellemuse.client;

import java.util.List;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.DelleMuseModelObject;

public interface IBaseClientHandler<T extends DelleMuseModelObject>  {

    public T update(T item)             throws DelleMuseClientException;
    public T get(Long id)               throws DelleMuseClientException;
    public boolean exists(Long id)      throws DelleMuseClientException;
    public T findByName(String name)    throws DelleMuseClientException;
    public List<T> findAll()            throws DelleMuseClientException;
    public void delete(Long id)         throws DelleMuseClientException;
    public T create(String name)        throws DelleMuseClientException;
    
    
}
