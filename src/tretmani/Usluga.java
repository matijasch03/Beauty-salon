package tretmani;

import java.util.HashMap;

import tretmanMenadzeri.MenadzerUsluga;

public class Usluga extends Tretman {
	public String nazivUsluge;
	public int trajanje; //izrazeno u minutama
	public int cena;
	public static HashMap<String, Usluga> mapa = new HashMap<>();

	
	public String getNazivUsluge() {
		return nazivUsluge;
	}

	public void setNazivUsluge(String nazivUsluge) {
		this.nazivUsluge = nazivUsluge;
	}

	public int getTrajanje() {
		return trajanje;
	}

	public void setTrajanje(int trajanje) {
		this.trajanje = trajanje;
	}

	public int getCena() {
		return cena;
	}

	public void setCena(int cena) {
		this.cena = cena;
	}

	public Usluga() {}
	
	public Usluga(TipTretmana tip, String nazivUsluge, int trajanje, int cena) {
		this.tip = tip;
		this.nazivUsluge = nazivUsluge;
		this.trajanje = trajanje;
		this.cena = cena;
		
		mapa.put(nazivUsluge, this);
	}
	
}