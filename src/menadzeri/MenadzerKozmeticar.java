package menadzeri;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import korisnici.Korisnik;
import korisnici.Kozmeticar;
import korisnici.Recepcioner;
import korisnici.Zaposleni;
import tretmani.StatusTretmana;
import tretmani.TipTretmana;
import tretmani.ZakazaniTretman;

public class MenadzerKozmeticar{

	public static void upisiUFajl(String path, Kozmeticar k) {
		try {
			FileWriter writer = new FileWriter(path, true);
			String tretmani = "";
			for (TipTretmana tip : k.getSpisakTretmana()) {
				if (tip != null)
				{
					tretmani += tip.toString() + ";";
				}
			}
			tretmani = tretmani.substring(0, tretmani.length() - 1);
			
			String zakazani = "";
			for (int sifra : k.getSifreTretmana()) {
				zakazani += sifra + ";";
			}
			if (!zakazani.equals("")) {
				zakazani = zakazani.substring(0, zakazani.length() - 1);
			}
			
		    writer.write(k.getKorisnickoIme() + ", " + k.getIme() + ", " + k.getPrezime() + ", " + k.getPol() + ", " 
			+ k.getTelefon() + ", " + k.getAdresa() + ", " + k.getLozinka() + ", " + k.getNivoStrucneSpreme() + ", "
			+ k.getStaz() + ", " + k.getBonus() + ", " + tretmani + ", " + k.getZaradio() + ", "+ zakazani +  "\n");
		    writer.close();
		} catch(IOException e) {
			System.err.println("Problem pri upisu radnika u fajl.");
		}
	}
	
	public static void procitaj() throws FileNotFoundException {
		System.out.println("\nKozmeticari:");
		int brojac = 1;
		
		for (Kozmeticar k : Kozmeticar.mapa.values()) {
			System.out.println(brojac + ". " + k.getIme() + " " + k.getPrezime() + " (" + k.getKorisnickoIme() + ")");
			brojac++;
		}
	}
	
	public static void izbrisi(String path, Kozmeticar k) throws IOException {
		MenadzerZaposleni.izbrisi(k, path);
		Kozmeticar.mapa.remove(k.getKorisnickoIme());
	}
	
	public static void azuriraj(String path, Kozmeticar k, String novoIme, String novoPrezime, String noviPol, String noviTelefon, 
			String novaAdresa, String novoKorisnickoIme, String novaLozinka, int noviNivoStrucneSpreme, 
			int noviStaz, int noviBonus, int novaOsnovaPlate, TipTretmana[] noviSpisakTretmana, 
			int novoZaradio, ArrayList<Integer> noveSifre) throws IOException {
				
			k.setIme(novoIme);
			k.setPrezime(novoPrezime);
			k.setPol(noviPol);
			k.setTelefon(noviTelefon);
			k.setAdresa(novaAdresa);
			k.setKorisnickoIme(novoKorisnickoIme);
			k.setLozinka(novaLozinka);
			k.setNivoStrucneSpreme(noviNivoStrucneSpreme);
			k.setStaz(noviStaz);
			k.setBonus(noviBonus);
			k.setPlata((novaOsnovaPlate / 5 * noviNivoStrucneSpreme) + (noviStaz * 800) + noviBonus);
			k.setSpisakTretmana(noviSpisakTretmana);
			k.sifreTretmana = noveSifre;
			k.setZaradio(novoZaradio);
			
			//brisanje zaposlenih sa starim vrednostima iz fajla
			FileWriter writer = new FileWriter(path);
			writer.write("");
			writer.close();
			
			//dodavanje zaposlenih sa azuriranim vrednostima
			for (Kozmeticar koz : Kozmeticar.mapa.values()) {
				upisiUFajl(path, koz);
			}
	}
	
	public static Korisnik logovaniKozmeticar = new Kozmeticar();
	
	public static void logIn() {
		logovaniKozmeticar = MenadzerZaposleni.logIn();
	}
	
	public static void prikaziZakazane() {
		if (logovaniKozmeticar.getKorisnickoIme() != null) {
			System.out.println("\nLista zakazanih tretmana kozmeticara " + logovaniKozmeticar.getIme() + ":\n");
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy.");
			for (ZakazaniTretman t : ZakazaniTretman.mapa.values()) {
				if (t.status == StatusTretmana.ZAKAZAN && t.kozmeticar.equals(logovaniKozmeticar)){
					String formatiranoVreme = (t.zakazanoVreme).format(format);
					System.out.println("Klijent: " + t.klijent.getKorisnickoIme() + "; Vreme: " + formatiranoVreme);
				}
			}
		}
		
		else {
			System.out.println("Prvo je potrebno da se ulogujete.");
		}
	}
}
