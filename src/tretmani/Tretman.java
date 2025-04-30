package tretmani;

import java.util.HashMap;

import tretmanMenadzeri.MenadzerTretman;

public class Tretman {
	public TipTretmana tip;
	public boolean obrisano;
	public int redniBroj = 1;
	public static HashMap<Integer, Tretman> mapa = new HashMap<>();
	
	public TipTretmana getTip() {
		return tip;
	}

	public void setSpisakTretmana(TipTretmana tip) {
		this.tip = tip;
	}
	
	public boolean isObrisano() {
		return obrisano;
	}

	public void setObrisano(boolean obrisano) {
		this.obrisano = obrisano;
	}
	
	public Tretman() {}
	
	public Tretman(TipTretmana tip) {
		this.tip = tip;
		setObrisano(false);
		mapa.put(redniBroj, this);
		redniBroj++;
		
		MenadzerTretman.upisiUFajl(this);
	}
}
