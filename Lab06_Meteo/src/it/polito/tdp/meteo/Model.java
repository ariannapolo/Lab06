package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private List<Citta> citta;
	private MeteoDAO dao;
	private List<SimpleCity> best;

	public Model() {
		dao = new MeteoDAO();
		citta = new ArrayList<Citta>(dao.getCitta());
		best = new ArrayList<SimpleCity>();

	}

	public String getUmiditaMedia(int mese) {
		String result="";
		for(Citta c: citta){
			result += c.getNome()+" "+dao.getAvgRilevamentiLocalitaMese(mese, c.getNome())+"\n";
		}
		
		

		return result;
	}

	public String trovaSequenza(int mese) {
		List<SimpleCity> parziale = new ArrayList<SimpleCity>();
		int step=1;
		
		recursive(parziale,step, mese);

		return best.toString();
	}

	private void recursive(List<SimpleCity> parziale, int step, int mese) {
		//System.out.println(step+""+best+" "+parziale);
		
		if(step == NUMERO_GIORNI_TOTALI+1){
			double punteggiobest = punteggioSoluzione(best);
			/*for(Citta c: citta){
				if(!parziale.contains(new SimpleCity(c.getNome(),0)))
					return;
			}*/
			if(best.size()==0){
				punteggiobest = 20000000;
			}
			if(punteggioSoluzione(parziale)<=punteggiobest){
				best.clear();
				best.addAll(parziale);
			}
			//System.out.println(punteggiobest);
			return;
		}
		else{
		for(Citta c: citta){
			SimpleCity s = new SimpleCity(c.getNome(),calcolaCosto(step,mese,c.getNome()));		
			s.setId(step);
			//System.out.println(s.getNome()+" giorno: "+step+", mese: "+mese+", costo: "+s.getCosto());
			parziale.add(s);
			c.increaseCounter();
			//System.out.println(c.getNome()+" "+c.getCounter());
			if(c.getCounter()<=NUMERO_GIORNI_CITTA_MAX && this.controllaParziale(parziale)){
				recursive(parziale, step+1, mese);
			}
			
		    parziale.remove(s);
			c.setCounter(c.getCounter()-1);			
		}
		}
		
		
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;
		for(SimpleCity s: soluzioneCandidata) {
			score+= s.getCosto();
				//System.out.println(s.getNome()+" "+s.getCosto());
		}
		int cont =0;
		for(int i=0; i<soluzioneCandidata.size()-1; i++){
			if(soluzioneCandidata.get(i).getNome().compareTo(soluzioneCandidata.get(i+1).getNome())!=0)
				cont++;
		}
		score+= cont*COST;
		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		int cont=1;
		for(int i=0; i<parziale.size()-1; i++){
			if(parziale.get(i).getNome().compareTo(parziale.get(i+1).getNome())==0)
				cont++;
			else {
				if(cont<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					return false;
				cont=1;
			}
		}
			
		

		return true;
	}

	public int calcolaCosto(int step, int mese, String localita){
		return dao.calcolaCosto(step, mese,localita);
		//s.setCosto(costo);
		//System.out.println(s.getNome()+" giorno: "+step+", mese: "+mese+", costo: "+costo);
		
	}
	
	public List<SimpleCity> getBest(){
		return best;
	}
}
