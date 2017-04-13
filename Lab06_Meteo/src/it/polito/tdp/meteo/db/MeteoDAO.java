package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		final String sql = "SELECT localita, data, umidita FROM situazione WHERE localita = ? AND data>=? AND data<=?";
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setString(2, "2013-"+mese+"-01");
			st.setString(3, "2013-"+mese+"-31");

			ResultSet rs = st.executeQuery();
			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return rilevamenti;
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT AVG(umidita) AS media FROM situazione WHERE localita = ? AND data>=? AND data<=?";
		double media=0;
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setString(2, "2013-"+mese+"-01");
			st.setString(3, "2013-"+mese+"-31");

			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				media = rs.getDouble("media");
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

	

		return media;
	}
	
	public List<Citta> getCitta(){
		final String sql = "SELECT DISTINCT localita FROM situazione";
		List<Citta> citta = new ArrayList<Citta>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Citta c = new Citta(rs.getString("Localita"));
				citta.add(c);
				
			}
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return citta;

		
	}
	
	public int calcolaCosto(int giorno, int mese, String localita){
		int costo =0;
		final String sql = "SELECT umidita FROM situazione WHERE localita=? AND data=?";
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setString(2, "2013-"+mese+"-"+giorno);
			ResultSet rs = st.executeQuery();
			
			
			if (rs.next()) {
			costo = rs.getInt("umidita");
				
			}
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return costo;
	}

}
