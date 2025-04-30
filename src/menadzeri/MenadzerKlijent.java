package menadzeri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

import korisnici.Klijent;
import korisnici.Kozmeticar;
import tretmani.StatusTretmana;
import tretmani.TipTretmana;
import tretmani.Usluga;
import tretmani.ZakazaniTretman;

public class MenadzerKlijent {

	public static String procitaj(String putanja) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(putanja));
		System.out.println("\n  Lista klijenata: \n");
		int brojac = 1;
		String izlaz = "";
		while (sc.hasNextLine()) {
			String data = sc.nextLine();
			String[] items = data.split(", ");
			izlaz += brojac + ". " + items[1] + " " + items[2] + " (" + items[0] + ")\n";
			brojac++;
		}
		System.out.print(izlaz);
		return izlaz;
	}
	
	
	
	public static void azuriraj(String path, Klijent k, String novoIme, String novoPrezime, String noviPol, String noviTelefon, 
			String novaAdresa, String novoKorisnickoIme, String novaLozinka, int novaSuma, ArrayList<Integer> noviTretmani) throws IOException {
		
		k.setIme(novoIme);
		k.setPrezime(novoPrezime);
		k.setPol(noviPol);
		k.setTelefon(noviTelefon);
		k.setAdresa(novaAdresa);
		k.setKorisnickoIme(novoKorisnickoIme);
		k.setLozinka(novaLozinka);
		k.setPotrosenaSuma(novaSuma);
		k.sifreTretmana = noviTretmani;
		
		//brisanje klijenta sa starim vrednostima iz fajla
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		
		//dodavanje klijenata sa azuriranim vrednostima
		for (Klijent klijent : Klijent.mapa.values()) {
			upisiUFajl(path, klijent);
		}
	}
	
	public static void izbrisi(String path, Klijent klijent) throws IOException {
		
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		
		Klijent.mapa.remove(klijent.getKorisnickoIme());

		for (Klijent k : Klijent.mapa.values()) {
			upisiUFajl(path, k);
		}
	}
	
	public static void upisiUFajl(String path, Klijent k) {
		try {
			String zakazani = "";
			for (int sifra : k.getSifreTretmana()) {
				zakazani += sifra + ";";
			}
			if (!zakazani.equals("")) {
				zakazani = zakazani.substring(0, zakazani.length() - 1);
			}
			FileWriter writer = new FileWriter(path, true);
		    writer.write(k.getKorisnickoIme() + ", " + k.getIme() + ", " + k.getPrezime() + ", " + k.getPol() + ", " 
			+ k.getTelefon() + ", " + k.getAdresa() + ", " + k.getLozinka() + ", " + k.getPotrosenaSuma() + ", " + zakazani + "\n");
		    writer.close();
		} catch(IOException e) {
			System.err.println("Problem pri upisu klijenta u fajl.");
		}
	}
	
	public static Klijent logovaniKorisnik = new Klijent();
	
	public static void logIn() {
		Scanner in = new Scanner(System.in);
		
		System.out.print("\nUnesite Vase korisnicko ime: ");
		String userName = in.nextLine();
		System.out.print("Unesite Vasu lozinku: ");
		String password = in.nextLine();
		
		boolean postojiKorisnik = false;
		
		HashMap<String, Klijent> mapa = Klijent.mapa;
		for (Klijent k : mapa.values()) {
			if (k.getKorisnickoIme().equals(userName)) {
				
				postojiKorisnik = true;
				
				if (k.getLozinka().equals(password)) {
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
	}
	
	public static ZakazaniTretman zakaziTretman(Usluga u, Kozmeticar kozmeticar, LocalDateTime zakazanoVreme) {
		ZakazaniTretman zt = new ZakazaniTretman(u, kozmeticar, logovaniKorisnik, zakazanoVreme, StatusTretmana.ZAKAZAN, null);
		if (logovaniKorisnik.getKorisnickoIme() != null) {
			if (kozmeticar == null) {
				outerLoop:
				for (Kozmeticar k : Kozmeticar.mapa.values()) {
					for (TipTretmana t : k.getSpisakTretmana()) {
						if (t.equals(u.tip)) {
							kozmeticar = k;
							break outerLoop;
						}
					}
				}
			}
			//zt = new ZakazaniTretman(u, kozmeticar, logovaniKorisnik, zakazanoVreme, StatusTretmana.ZAKAZAN);
		}
		else {
			System.out.println("Ne mozete zakazati tretman pre nego sto se ne ulogujete.");
		}
		return zt;
	}
	
	public static void traziKarticuLojalnosti(Klijent k) {
		int lojalnaSuma = MenadzerMenadzer.getLojalnaSuma();
		if (k.getPotrosenaSuma() >= lojalnaSuma) {
			k.setKarticaLojalnosti(true);
			//System.out.println("Korisnik " + k.getIme() + " ima pravo na karticu lojalnosti.");
		}
		else {
			k.setKarticaLojalnosti(false);
			//System.out.println("Korisnik " + k.getIme() + " nema pravo na karticu lojalnosti.");
		}
	}
	
	public static void prikaziPotrosenuSumu() {
		System.out.println("Suma koju je potrosio korisnik " + logovaniKorisnik.getIme() + " je " + logovaniKorisnik.getPotrosenaSuma() + ".");
	}
	
	public static void prikaziDetalje(Klijent k) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy.");
		System.out.println("\nIstorija tretmana klijenta " + k.getKorisnickoIme() + ":\n");

		for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
			if (z.klijent.equals(k)) {
				String formatiranoVreme = (z.zakazanoVreme).format(format);
				
				System.out.println("Tretman " + z.nazivUsluge + " zakazan u " + formatiranoVreme + " kod " 
				+ z.kozmeticar.getKorisnickoIme() + "; Cena: " + z.cena + "; Status: " + z.status.toString());
			}
		}
		
	    
	}
}
