package tretmanMenadzeri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import tretmani.TipTretmana;
import tretmani.Tretman;
import tretmani.Usluga;

public class MenadzerUsluga {
	
public static void procitaj(String putanja) throws IOException {
		
		//provera postojanja tretmana (ako je neki tretman u medjuvremenu obrisan, brisu se i usluge)
		for (Tretman t : Tretman.mapa.values()) {
			if (t == null) {
				for (Usluga u : Usluga.mapa.values()) {
					if (u.tip == t.tip) {
						izbrisi(u);
					}
				}
			}
		}
		Scanner sc = new Scanner(new File(putanja));
		System.out.println("\n  Lista usluga: \n");
		int brojac = 1;
		while (sc.hasNextLine()) {
			String data = sc.nextLine();
			String[] items = data.split(", ");
			if ((Tretman.mapa.size() == 2 && brojac != 6) || Tretman.mapa.size() == 3) {
				System.out.println(brojac + ". " + items[1] + ", " + items[2] + " min, " + items[3] + " RSD");
				brojac++;
			}
		}
		
	}
	
	public static void azuriraj(Usluga u, TipTretmana tip, String nazivUsluge, int trajanje, int cena) throws IOException {
		
		u.tip = tip;
		u.nazivUsluge = nazivUsluge;
		u.trajanje = trajanje;
		u.cena = cena;
		
		//brisanje usluge sa starim vrednostima iz fajla
		FileWriter writer = new FileWriter("usluge.txt");
		writer.write("");
		writer.close();
		
		//dodavanje usluge sa azuriranim vrednostima
		for (Usluga usluga : Usluga.mapa.values()) {
			upisiUFajl(usluga);
		}
	}
	
	public static void izbrisi(Usluga us) throws IOException {
		
		FileWriter writer = new FileWriter("usluge.txt");
		writer.write("");
		writer.close();
		
		Usluga.mapa.remove(us.redniBroj);
		us = null;

		for (Usluga u : Usluga.mapa.values()) {
			upisiUFajl(u);
		}
	}
	
	public static void upisiUFajl(Usluga u) {
		try {
			FileWriter writer = new FileWriter("usluge.txt", true);
		    writer.write(u.tip + ", " + u.nazivUsluge + ", " + u.trajanje + ", " + u.cena + "\n");
		    writer.close();
		} catch(IOException e) {
			System.err.println("Problem pri upisu tretman-usluge u fajl.");
		}
	}
}
