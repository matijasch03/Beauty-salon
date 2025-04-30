package korisnici;

import java.util.ArrayList;
import java.util.HashMap;

import menadzeri.MenadzerMenadzer;

public class Klijent extends Korisnik{
	
	private int potrosenaSuma;
	private boolean karticaLojalnosti;
	public ArrayList<Integer> sifreTretmana = new ArrayList<>();
	public static HashMap<String, Klijent> mapa = new HashMap<>();
	
	//konstruktori
	public Klijent() {}
	
	public Klijent(String ime, String prezime, String pol, String telefon, String adresa, 
			String korisnickoIme, String lozinka, int potrosenaSuma, String sifreTretmana) {
		
		super(ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka);
		this.potrosenaSuma = potrosenaSuma;
		if (potrosenaSuma >= MenadzerMenadzer.getLojalnaSuma()) {
			karticaLojalnosti = true;
		}
		else {
			karticaLojalnosti = false;
		}
		
		if (sifreTretmana != null) {
			String[] sifre = sifreTretmana.split(";");
	
			for (String sifra : sifre) {
				this.sifreTretmana.add(Integer.parseInt(sifra));
		    }
		}
		else {
			this.sifreTretmana = new ArrayList<>();
		}
		
		mapa.put(korisnickoIme, this);
	}

	public int getPotrosenaSuma() {
		return potrosenaSuma;
	}

	public void setPotrosenaSuma(int potrosenaSuma) {
		this.potrosenaSuma = potrosenaSuma;
	}

	public boolean isKarticaLojalnosti() {
		return karticaLojalnosti;
	}

	public void setKarticaLojalnosti(boolean karticaLojalnosti) {
		this.karticaLojalnosti = karticaLojalnosti;
	}
	
	public int getListaKlijenata() {
		return potrosenaSuma;
	}
	
	public ArrayList<Integer> getSifreTretmana() {
		return sifreTretmana;
	}

	public void setSifreTretmana(ArrayList<Integer> sifreTretmana) {
		this.sifreTretmana = sifreTretmana;
	}

}