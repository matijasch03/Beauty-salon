package menadzeri;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import korisnici.Korisnik;
import korisnici.Zaposleni;

public class MenadzerZaposleni {
	
	public static void upisiUFajl(Zaposleni z, String path) {
		try {
			FileWriter writer = new FileWriter(path, true);
		    writer.write(z.getKorisnickoIme() + ", " + z.getIme() + ", " + z.getPrezime() + ", " + z.getPol() + ", " 
			+ z.getTelefon() + ", " + z.getAdresa() + ", " + z.getLozinka() + ", " + z.getNivoStrucneSpreme() + ", "
			+ z.getStaz() + ", " + z.getBonus() + "\n");
		    writer.close();
		} catch(IOException e) {
			System.err.println("Problem pri upisu radnika u fajl.");
		}
	}
	
	//metoda koja se poziva u klasama naslednicama zaposlenih za ispisivanje svake grupe radnika ponaosob
	public static void procitaj(String text, HashMap<String, Zaposleni> mapa) throws FileNotFoundException {
		System.out.println(text);
		int brojac = 1;
		
		for (Zaposleni z : mapa.values()) {
			System.out.println(brojac + ". " + z.getIme() + " " + z.getPrezime() + " (" + z.getKorisnickoIme() + ")");
			brojac++;
		}
	}
	
	//metoda za ispisivanje svih radnika odjedared
	public static void procitajSve() throws FileNotFoundException {
		MenadzerZaposleni.procitaj("\n  LISTA ZAPOSLENIH (nesortirano):", Zaposleni.mapa);
	}
	
	public static void azuriraj(Zaposleni z, String novoIme, String novoPrezime, String noviPol, String noviTelefon, 
			String novaAdresa, String novoKorisnickoIme, String novaLozinka, int noviNivoStrucneSpreme, 
			int noviStaz, int noviBonus, int novaOsnovaPlate, String path, HashMap<String, Zaposleni> mapa) throws IOException {
		
		z.setIme(novoIme);
		z.setPrezime(novoPrezime);
		z.setPol(noviPol);
		z.setTelefon(noviTelefon);
		z.setAdresa(novaAdresa);
		z.setKorisnickoIme(novoKorisnickoIme);
		z.setLozinka(novaLozinka);
		z.setNivoStrucneSpreme(noviNivoStrucneSpreme);
		z.setStaz(noviStaz);
		z.setBonus(noviBonus);
		z.setPlata((novaOsnovaPlate / 5 * noviNivoStrucneSpreme) + (noviStaz * 800) + noviBonus);
		
		//brisanje zaposlenih sa starim vrednostima iz fajla
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		
		//dodavanje zaposlenih sa azuriranim vrednostima
		for (Zaposleni zaposleni : mapa.values()) {
			upisiUFajl(zaposleni, path);
		}
	}
	
	public static void izbrisi(Zaposleni radnik, String path) throws IOException {
		
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		
		Zaposleni.mapa.remove(radnik.getKorisnickoIme());
		radnik = null;

		for (Zaposleni z : Zaposleni.mapa.values()) {
			upisiUFajl(z, path);
		}
	}
		
	public static Korisnik logovaniKorisnik;
	
	public static Korisnik logIn() {
		Scanner in = new Scanner(System.in);
		
		logovaniKorisnik = null;
		System.out.print("\nUnesite Vase korisnicko ime: ");
		String userName = in.nextLine();
		System.out.print("Unesite Vasu lozinku: ");
		String password = in.nextLine();
		
		boolean postojiKorisnik = false;
		
		HashMap<String, Zaposleni> mapa = Zaposleni.mapa;
		for (Korisnik k : mapa.values()) {
			if (userName.equals(k.getKorisnickoIme())) {
				
				postojiKorisnik = true;
				
				if (password.equals(k.getLozinka())) {
					System.out.println("Postovani " + k.getIme() + ", uspesno ste se ulogovali.");
					logovaniKorisnik = k;
				}
				else {
					System.out.println("Pogresno unesena sifra.");
				}
				
				break;
			}
		}
		
		if (!postojiKorisnik) {
			System.out.println("Ne postoji korisnik sa ovim korisnickim imenom.");
		}
		return logovaniKorisnik;
	}
}
