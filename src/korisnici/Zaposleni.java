package korisnici;

import java.util.HashMap;

public abstract class Zaposleni extends Korisnik{
	private int nivoStrucneSpreme;
	private int staz;
	private int bonus;
	private int osnovaPlate;
	private int plata;
	public static HashMap<String, Zaposleni> mapa = new HashMap<>();
	
	public int getNivoStrucneSpreme() {
		return nivoStrucneSpreme;
	}
	public void setNivoStrucneSpreme(int nivoStrucneSpreme) {
		this.nivoStrucneSpreme = nivoStrucneSpreme;
	}
	
	public int getStaz() {
		return staz;
	}
	public void setStaz(int staz) {
		this.staz = staz;
	}
	
	public int getBonus() {
		return bonus;
	}
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	public int getOsnovaPlate() {
		return plata;
	}
	
	public void setOsnovaPlate(int osnovaPlate) {
		this.osnovaPlate = osnovaPlate;
	}
	
	public int getPlata() {
		return plata;
	}
	
	public void setPlata(int plata) {
		this.plata = plata;
	}
	
	//konstruktori
	public Zaposleni() {}
	
	public Zaposleni(String ime, String prezime, String pol, String telefon, String adresa, 
			String korisnickoIme, String lozinka, int nivoStrucneSpreme, int staz, int bonus,
			int osnovaPlate) {
		
		super(ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka);
		this.nivoStrucneSpreme = nivoStrucneSpreme;
		this.staz = staz;
		this.bonus = bonus;
		this.osnovaPlate = osnovaPlate;
		this.plata = (osnovaPlate / 5 * nivoStrucneSpreme) + (staz * 800) + bonus;
		Menadzer.rashodi += this.plata;
		
		mapa.put(korisnickoIme, this);
	}
}