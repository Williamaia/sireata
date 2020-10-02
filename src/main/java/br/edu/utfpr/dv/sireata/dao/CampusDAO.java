package br.edu.utfpr.dv.sireata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.sireata.model.Campus;

public class CampusDAO {
	
	public Campus buscarPorId(int id) throws SQLException{
		String sql ="SELECT * FROM campus WHERE idCampus = ?";
		
		try (
				Connection conn = ConnectionDAO.getInstance().getConnection();
				Statement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery(sql);
			){
			
			if(rs.next()){
				return this.carregarObjeto(rs);
			}else{
				return null;
			}
		}
	}
	
	public Campus buscarPorDepartamento(int idDepartamento) throws SQLException{
		String sql ="SELECT idCampus FROM departamentos WHERE idDepartamento=?";
		
		try (
				Connection conn = ConnectionDAO.getInstance().getConnection();
				Statement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery(sql);
			) {
			
			if(rs.next()){
				return this.buscarPorId(rs.getInt("idCampus"));
			}else{
				return null;
			}
		}
	}
	
	public List<Campus> listarTodos(boolean apenasAtivos) throws SQLException{
		String sql = "SELECT * FROM campus \" + (apenasAtivos ? \" WHERE ativo=1\" : \"\") + \" ORDER BY nome";
		
		try (
				Connection conn = ConnectionDAO.getInstance().getConnection();
				Statement stmt = conn.createStatement();				
				ResultSet rs = stmt.executeQuery(sql);
		    ){
				List<Campus> list = new ArrayList<Campus>();
				
				while(rs.next()) {
					list.add(this.carregarObjeto(rs));
				}
				
				return list;
				
			}
	}

	
	public List<Campus> listarParaCriacaoAta(int idUsuario) throws SQLException{
		String sql = "SELECT DISTINCT campus.* FROM campus \" +\r\n" + 
				     "\"INNER JOIN departamentos ON departamentos.idCampus=campus.idCampus \" +\r\n" + 
				     "\"INNER JOIN orgaos ON orgaos.idDepartamento=departamentos.idDepartamento \" +\r\n" + 
				     "\"WHERE campus.ativo=1 AND (orgaos.idPresidente=\" + String.valueOf(idUsuario) + \" OR orgaos.idSecretario=\" + String.valueOf(idUsuario) + \r\n" + 
				     "\") ORDER BY campus.nome";
		
		try(
				Connection conn = ConnectionDAO.getInstance().getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			) {
			 
			List<Campus> list = new ArrayList<Campus>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
				return list;
			}
	}
		
	
	public List<Campus> listarParaConsultaAtas(int idUsuario) throws SQLException{
		String sql ="SELECT DISTINCT campus.* FROM campus \" +\r\n" + 
					"\"INNER JOIN departamentos ON departamentos.idCampus=campus.idCampus \" +\r\n" + 
					"\"INNER JOIN orgaos ON orgaos.idDepartamento=departamentos.idDepartamento \" +\r\n" + 
					"\"INNER JOIN atas ON atas.idOrgao=orgaos.idOrgao \" +\r\n" + 
					"\"INNER JOIN ataParticipantes ON ataParticipantes.idAta=atas.idAta \" +\r\n" + 
					"\"WHERE atas.publicada=0 AND ataParticipantes.presente=1 AND ataParticipantes.idUsuario=\" + String.valueOf(idUsuario) + \r\n" + 
					"\" ORDER BY campus.nome";
		
		try (
				Connection conn = ConnectionDAO.getInstance().getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
			) {
			
			List<Campus> list = new ArrayList<Campus>();
			
			while(rs.next()){
				list.add(this.carregarObjeto(rs));
			}
			
			return list;
		}
	}	
	
	public int salvar(Campus campus) throws SQLException{
		boolean insert = (campus.getIdCampus() == 0);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
		
			if(insert){
				stmt = conn.prepareStatement("INSERT INTO campus(nome, endereco, logo, ativo, site) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			}else{
				stmt = conn.prepareStatement("UPDATE campus SET nome=?, endereco=?, logo=?, ativo=?, site=? WHERE idCampus=?");
			}
			
			stmt.setString(1, campus.getNome());
			stmt.setString(2, campus.getEndereco());
			if(campus.getLogo() == null){
				stmt.setNull(3, Types.BINARY);
			}else{
				stmt.setBytes(3, campus.getLogo());	
			}
			stmt.setInt(4, campus.isAtivo() ? 1 : 0);
			stmt.setString(5, campus.getSite());
			
			if(!insert){
				stmt.setInt(6, campus.getIdCampus());
			}
			
			stmt.execute();
			
			if(insert){
				rs = stmt.getGeneratedKeys();
				
				if(rs.next()){
					campus.setIdCampus(rs.getInt(1));
				}
			}
			
			return campus.getIdCampus();
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	private Campus carregarObjeto(ResultSet rs) throws SQLException{
		Campus campus = new Campus();
		
		campus.setIdCampus(rs.getInt("idCampus"));
		campus.setNome(rs.getString("nome"));
		campus.setEndereco(rs.getString("endereco"));
		campus.setLogo(rs.getBytes("logo"));
		campus.setAtivo(rs.getInt("ativo") == 1);
		campus.setSite(rs.getString("site"));
		
		return campus;
	}
}
