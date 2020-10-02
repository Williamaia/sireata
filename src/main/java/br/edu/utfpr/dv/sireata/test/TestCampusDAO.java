package br.edu.utfpr.dv.sireata.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;

import br.edu.utfpr.dv.sireata.dao.CampusDAO;
import br.edu.utfpr.dv.sireata.model.Campus;

public class TestCampusDAO {
	
	@Test
	public void testmethodBuscarPorId() {

			Campus campusDAOTest = new Campus();
			campusDAOTest.setIdCampus(1);
			
			@Mock
			CampusDAO campusTest = new Mock.(CampusDAO.class);
		
			when(campusTest.buscarPorId(1)).thenReturn(campusTest);    
			
			Campus campusResult = campusTest.buscarPorId(1);
			
			assertEquals(1, campusResult.getIdCampus());
			
	}
	
	@Test
	public void testmethodBuscarDepartamento() {

			Campus campusDAOTest = new Campus();
			campusDAOTest.setIdCampus(1);
			
			@Mock
			CampusDAO campusTest = new Mock.(CampusDAO.class);
		
			when(campusTest.buscarPorId(1)).thenReturn(campusTest);    
			
			Campus campusResult = campusTest.buscarPorDepartamento(1);
			
			assertEquals(1, campusResult.getIdCampus());
			
	}
}