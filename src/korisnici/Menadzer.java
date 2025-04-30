package korisnici;

import java.util.HashMap;

public class Menadzer extends Zaposleni{
	
	//konstruktori
	public Menadzer() {}
	
	public static int bilans = 0;
	public static int rashodi = 0;
	
	public static HashMap<String, Menadzer> mapa = new HashMap<>();
	
	public Menadzer(String ime, String prezime, String pol, String telefon, String adresa, 
			String korisnickoIme, String lozinka, int nivoStrucneSpreme, int staz, int bonus) {
		super(ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, nivoStrucneSpreme, staz, bonus, 100000);
		
		mapa.put(korisnickoIme, this);
	}
}