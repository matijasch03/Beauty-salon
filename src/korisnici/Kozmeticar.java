package korisnici;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import tretmani.TipTretmana;
import tretmani.ZakazaniTretman;

public class Kozmeticar extends Zaposleni{
	
	private TipTretmana[] spisakTretmana;
	private int zaradio;
	public ArrayList<Integer> sifreTretmana = new ArrayList<>();
	public static HashMap<String, Kozmeticar> mapa = new HashMap<>();
	
	//konstruktori
	public Kozmeticar() {}
	
	public Kozmeticar(String ime, String prezime, String pol, String telefon, String adresa, 
			String korisnickoIme, String lozinka, int nivoStrucneSpreme, int staz, int bonus, 
			TipTretmana[] spisakTretmana, int zaradio, String sifreTretmana) {
		
		super(ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, nivoStrucneSpreme, staz, bonus, 75000);
		this.setSpisakTretmana(spisakTretmana);
		this.zaradio = zaradio;
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

	public TipTretmana[] getSpisakTretmana() {
		return spisakTretmana;
	}

	public void setSpisakTretmana(TipTretmana[] spisakTretmana) {
		this.spisakTretmana = spisakTretmana;
	}

	public ArrayList<Integer> getSifreTretmana() {
		return sifreTretmana;
	}

	public void setSifreTretmana(ArrayList<Integer> sifreTretmana) {
		this.sifreTretmana = sifreTretmana;
	}

	public int getZaradio() {
		return zaradio;
	}

	public void setZaradio(int zaradio) {
		this.zaradio = zaradio;
	}
	
}