package korisnici;

import java.util.HashMap;

public class Recepcioner extends Zaposleni{
	
	public static HashMap<String, Recepcioner> mapa = new HashMap<>();
	
	//konstruktori
	public Recepcioner() {}
	
	public Recepcioner(String ime, String prezime, String pol, String telefon, String adresa, 
			String korisnickoIme, String lozinka, int nivoStrucneSpreme, int staz, int bonus) {
		
		super(ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, nivoStrucneSpreme, staz, bonus, 58000);
		
		mapa.put(korisnickoIme, this);
	}
}