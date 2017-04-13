package it.polito.tdp.meteo;

import it.polito.tdp.meteo.bean.SimpleCity;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		//System.out.println(m.getUmiditaMedia(12));
		
		/*System.out.println(m.trovaSequenza(5));
		for(SimpleCity s: m.getBest()){
			System.out.println(s.getNome()+" "+s.getCosto());
		}*/
	
		System.out.println(m.trovaSequenza(4));
	}

}
