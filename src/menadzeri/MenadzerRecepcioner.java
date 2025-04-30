package menadzeri;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import korisnici.Klijent;
import korisnici.Korisnik;
import korisnici.Kozmeticar;
import korisnici.Menadzer;
import korisnici.Recepcioner;
import korisnici.Zaposleni;
import tretmanMenadzeri.MenadzerZakazani;
import tretmani.StatusTretmana;
import tretmani.TipTretmana;
import tretmani.Usluga;
import tretmani.ZakazaniTretman;

public class MenadzerRecepcioner{

	public static void procitaj() throws FileNotFoundException {
		//MenadzerZaposleni.procitaj("\nRecepcioneri:", Recepcioner.mapa);
	}
	
	public static void izbrisi(String path, Recepcioner r) throws IOException {
		MenadzerZaposleni.izbrisi(r, path);
		Recepcioner.mapa.remove(r.getKorisnickoIme());
	}
	
	public static void azuriraj(String path, Recepcioner r, String novoIme, String novoPrezime, String noviPol, String noviTelefon, 
			String novaAdresa, String novoKorisnickoIme, String novaLozinka, 
			int noviNivoStrucneSpreme, int noviStaz, int noviBonus, int novaOsnovaPlate) throws IOException {
				
			
		r.setIme(novoIme);
		r.setPrezime(novoPrezime);
		r.setPol(noviPol);
		r.setTelefon(noviTelefon);
		r.setAdresa(novaAdresa);
		r.setKorisnickoIme(novoKorisnickoIme);
		r.setLozinka(novaLozinka);
		r.setNivoStrucneSpreme(noviNivoStrucneSpreme);
		r.setStaz(noviStaz);
		r.setBonus(noviBonus);
		r.setPlata((novaOsnovaPlate / 5 * noviNivoStrucneSpreme) + (noviStaz * 800) + noviBonus);
		
		//brisanje zaposlenih sa starim vrednostima iz fajla
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		
		//dodavanje zaposlenih sa azuriranim vrednostima
		for (Recepcioner zaposleni : Recepcioner.mapa.values()) {
			MenadzerZaposleni.upisiUFajl(zaposleni, path);
		}

	}
	
	public static Korisnik logovaniRecepcioner = new Recepcioner();
	
	public static void logIn() {
		logovaniRecepcioner = MenadzerZaposleni.logIn();
	}
	
	public static ZakazaniTretman zakaziTretman(Usluga u, Kozmeticar kozmeticar, Klijent klijent, LocalDateTime zakazanoVreme) {
		ZakazaniTretman zt = new ZakazaniTretman(u, kozmeticar, klijent, zakazanoVreme, StatusTretmana.ZAKAZAN, null);
		if (logovaniRecepcioner.getKorisnickoIme() != null) {
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
			//zt = new ZakazaniTretman(u, kozmeticar, klijent, zakazanoVreme);
		}
		else {
			System.out.println("Ne mozete zakazati tretman pre nego sto se ne ulogujete.");
		}
		return zt;
	}
	
	public static void setujStatusTretmana(Integer sifraTretmana, StatusTretmana status) {
		ZakazaniTretman tretman = ZakazaniTretman.mapa.get(sifraTretmana);
		String nazivUsluge = tretman.nazivUsluge;
		tretman.status = status;
		
		if (status == StatusTretmana.OTKAZAO_KLIJENT) {
			tretman.klijent.setPotrosenaSuma(tretman.klijent.getPotrosenaSuma() - (tretman.cena / 10 * 9));
			tretman.kozmeticar.setZaradio(tretman.kozmeticar.getZaradio() - (tretman.cena / 10 * 9));
			Menadzer.bilans -= (tretman.cena / 10 * 9);
			tretman.setCena(tretman.cena / 10);

		}
		
		else if (status == StatusTretmana.OTKAZAO_SALON) {
			tretman.klijent.setPotrosenaSuma(tretman.klijent.getPotrosenaSuma() - tretman.cena);
			tretman.kozmeticar.setZaradio(tretman.kozmeticar.getZaradio() - tretman.cena);
			Menadzer.bilans -= tretman.cena;
			tretman.setCena(0);

		}
				
		Klijent kl = Klijent.mapa.get(tretman.klijent.getKorisnickoIme());
    	Kozmeticar kz = Kozmeticar.mapa.get(tretman.kozmeticar.getKorisnickoIme());
    	kz.sifreTretmana.remove(sifraTretmana);
    	
    	try {
			MenadzerKozmeticar.azuriraj("kozmeticari.txt", kz, kz.getIme(), kz.getPrezime(), kz.getPol(), kz.getTelefon(), kz.getAdresa(), kz.getKorisnickoIme(), kz.getLozinka(), kz.getNivoStrucneSpreme(), kz.getStaz(), kz.getBonus(), kz.getOsnovaPlate(), kz.getSpisakTretmana(), kz.getZaradio(), kz.sifreTretmana);
    		MenadzerKlijent.azuriraj("klijenti.txt", kl, kl.getIme(), kl.getPrezime(), kl.getPol(), kl.getTelefon(), kl.getAdresa(), kl.getKorisnickoIme(), kl.getLozinka(), kl.getPotrosenaSuma(), kl.sifreTretmana);
    		MenadzerZakazani.azuriraj(tretman, Usluga.mapa.get(nazivUsluge), kz, kl, tretman.zakazanoVreme, tretman.status, tretman.cena);
    		
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
