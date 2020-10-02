package br.edu.utfpr.dv.sireata.test;

import static org.junit.Assert.assertEquals;
import java.util.Optional;
import javax.ws.rs.client.Client;
import org.junit.Assert;
import org.junit.Test;

import br.edu.utfpr.dv.sireata.dao.CampusDAO;
import br.edu.utfpr.dv.sireata.model.Campus;

public class TestCampusDAO {
	
	@Test
	public void testmethodBuscarPorId() {
		try {
	        Mockito.when(CampusDAO.findById(any(Integer.class)))
	                .thenReturn(Optional.of(Client.builder()        
	                        .id(1)));     
	    } catch (Exception ex) {
	        Assert.fail();
	        
	    }
	}
}