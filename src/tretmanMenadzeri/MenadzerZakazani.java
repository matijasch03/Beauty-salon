package tretmanMenadzeri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import korisnici.Klijent;
import korisnici.Kozmeticar;
import tretmani.StatusTretmana;
import tretmani.Usluga;
import tretmani.ZakazaniTretman;

public class MenadzerZakazani {

	public static void procitaj(String putanja) throws IOException {
		
		//provera postojanja usluga (ako je neka usluga u medjuvremenu obrisana, brisu se i 
		//odgovarajuci zakazani tretmani)
//		for (Usluga u : Usluga.listaUsluga) {
//			if (u.biloPromene) {
//				for (ZakazaniTretman z : ZakazaniTretman.listaZakazanih) {
//					if (z.nazivUsluge == u.nazivUsluge) {
//						azuriraj(z, z.nazivUsluge, z.kozmeticar, z.klijent, z.zakazanoVreme);
//					}
//				}
//			}
//		} NE TREBA, ALI MOZE POSLUZITI U NEKOM DRUGOM SLUCAJU
		
		Scanner sc = new Scanner(new File(putanja));
		System.out.println("\n  Lista zakazanih tretmana: \n");
		int brojac = 1;
		while (sc.hasNextLine()) {
			String data = sc.nextLine();
			System.out.println(brojac + ". " + data);
			brojac++;
		}
		
	}
	
	public static void azuriraj(ZakazaniTretman z, Usluga u, Kozmeticar kozmeticar, 
			Klijent klijent, LocalDateTime zakazanoVreme, StatusTretmana status, Integer cena) throws IOException {
		
		z.nazivUsluge = u.nazivUsluge;
		z.tip = u.tip;
		z.trajanje = u.trajanje;
		z.cena = u.cena;
		
		z.kozmeticar = kozmeticar;
		z.klijent = klijent;
		z.zakazanoVreme = zakazanoVreme;
		z.status = status;
		z.cena = cena;
		
		//brisanje zakazanog tretmana sa starim vrednostima iz fajla
		FileWriter writer = new FileWriter("zakazani.txt");
		writer.write("");
		writer.close();
		
		//dodavanje zakazanog tretmana sa azuriranim vrednostima
		for (ZakazaniTretman zak : ZakazaniTretman.mapa.values()) {
			upisiUFajl(zak);
		}
	}
	
	public static void izbrisiRadiLi(ZakazaniTretman zak) throws IOException {
		
		FileWriter writer = new FileWriter("zakazani.txt");
		writer.write("");
		writer.close();
		
		ZakazaniTretman.mapa.remove(zak.redniBroj);
		zak = null;

		for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
			upisiUFajl(z);
		}
	}
	
	public static void upisiUFajl(ZakazaniTretman z) {
		try {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH dd.MM.yyyy");
			
		    String formatiranoVreme = (z.zakazanoVreme).format(format);
			FileWriter writer = new FileWriter("zakazani.txt", true);
		    writer.write(z.redniBroj + ", " + z.klijent.getKorisnickoIme() + ", " + z.nazivUsluge + ", "
			+ z.kozmeticar.getKorisnickoIme() + ", " + formatiranoVreme + ", " + z.status +  ", " + z.cena + "\n");
		    writer.close();
		} catch(IOException e) {
			System.err.println("Problem pri upisu zakazanog tretmana u fajl.");
		}
	}
}
