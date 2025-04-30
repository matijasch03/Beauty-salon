package menadzeri;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import korisnici.Korisnik;
import korisnici.Kozmeticar;
import korisnici.Menadzer;
import korisnici.Recepcioner;
import korisnici.Zaposleni;

public class MenadzerMenadzer{
	
	public static void procitaj() throws FileNotFoundException {
		//MenadzerZaposleni.procitaj("\nMenadzeri:", Menadzer.mapa);
	}
	
	public static void izbrisi(String path, Menadzer m) throws IOException {
		MenadzerZaposleni.izbrisi(m, path);
		Menadzer.mapa.remove(m.getKorisnickoIme());
	}
	
	public static void azuriraj(String path, Menadzer m, String novoIme, String novoPrezime, String noviPol, String noviTelefon, 
			String novaAdresa, String novoKorisnickoIme, String novaLozinka, 
			int noviNivoStrucneSpreme, int noviStaz, int noviBonus, int novaOsnovaPlate) throws IOException {
				
			
		m.setIme(novoIme);
		m.setPrezime(novoPrezime);
		m.setPol(noviPol);
		m.setTelefon(noviTelefon);
		m.setAdresa(novaAdresa);
		m.setKorisnickoIme(novoKorisnickoIme);
		m.setLozinka(novaLozinka);
		m.setNivoStrucneSpreme(noviNivoStrucneSpreme);
		m.setStaz(noviStaz);
		m.setBonus(noviBonus);
		m.setPlata((novaOsnovaPlate / 5 * noviNivoStrucneSpreme) + (noviStaz * 800) + noviBonus);
		
		//brisanje zaposlenih sa starim vrednostima iz fajla
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		
		//dodavanje zaposlenih sa azuriranim vrednostima
		for (Menadzer zaposleni : Menadzer.mapa.values()) {
			MenadzerZaposleni.upisiUFajl(zaposleni, path);
		}
	}
	
	public static Korisnik logovaniMenadzer = new Menadzer();
	
	public static void logIn() {
		logovaniMenadzer = MenadzerZaposleni.logIn();
	}
	
	private static int lojalnaSuma = 10000;
	
	public static int getLojalnaSuma() {
		return lojalnaSuma;
	}

	public static void setLojalnaSuma(int lojalnaSuma2) {
		lojalnaSuma = lojalnaSuma2;
		System.out.println("Suma za karticu lojalnosti je promenjena na " + lojalnaSuma);
	}
	
	public static void prikaziBonuse() {
		System.out.println("\nBonusi:");
		for (Zaposleni z : Zaposleni.mapa.values()) {
			System.out.println(z.getKorisnickoIme() + ": " + z.getBonus() + " RSD");
		}
	}
	
}
