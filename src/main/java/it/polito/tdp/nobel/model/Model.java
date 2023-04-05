package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> allEsami;//raccoglie tutti gli esami
	private Set<Esame> migliore;//lo costruisco man mano nella ricorsione
	private double mediaMigliore;
	
	
	
	public Model() {
        EsameDAO dao =	new EsameDAO();
        this.allEsami = dao.getTuttiEsami();//così la Lista allEsami prende dalla classe Esamedao tutti gli esami
}


	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		migliore = new HashSet<>();
		mediaMigliore = 0.0;
		Set<Esame> parziale = new HashSet<>();
		
		cercaMeglio(parziale, 0, numeroCrediti);
		return migliore;	
	}

	
	private void cerca(Set<Esame> parziale, int L, int numeroCrediti) {
		
		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > numeroCrediti)//se la sommaCrediti supera il numeroCrediti esco
			return;
		
		if(sommaCrediti == numeroCrediti){//possibile avere soluzione in questo caso
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<>(parziale);
			}
			return;
		}
		
		if(L == allEsami.size())//se ho gia aggiunto tutti gli esami della lista esco
			return;
		
		//se arrivo qui, numeroCrediti > sommaCrediti: ho soluzoni!!!
		for (Esame e : allEsami) {//ciclo sui dati e chiamo metodo che fa ricorsione(cerca)
			if(!parziale.contains(e)) {//se l soluz parziale non contiene l'esame e lo aggingo
				parziale.add(e);
				cerca(parziale, L+1, numeroCrediti);//chiamo metodo cerca
				parziale.remove(e);//ricordare di rimuovere l'oggetto(serve per far si che alla prossima iterazione parziale sarà vuoto
			}
		}
		
	}
	
	private void cercaMeglio(Set<Esame> parziale, int L, int numeroCrediti) {//cerco di fare ricorsione meglio

		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > numeroCrediti)//se la sommaCrediti supera il numeroCrediti esco
			return;
		
		if(sommaCrediti == numeroCrediti){//possibile avere soluzione in questo caso
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<>(parziale);
			}
			return;
		}
		
		if(L == allEsami.size())//se ho gia aggiunto tutti gli esami della lista esco
			return;
		
		//provo ad aggiungere prossimo elemento
		parziale.add(allEsami.get(L));
		cercaMeglio(parziale, L+1, numeroCrediti);
		parziale.remove(allEsami.get(L));
		
		//provo a non aggiungere prossimo elemento
		cercaMeglio(parziale, L+1, numeroCrediti);

	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
