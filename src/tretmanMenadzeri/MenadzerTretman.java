package tretmanMenadzeri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import tretmani.TipTretmana;
import tretmani.Tretman;

public class MenadzerTretman {
	
	public static void procitaj(String putanja) throws FileNotFoundException {
		
		Scanner sc = new Scanner(new File(putanja));
		System.out.println("\n  Lista tretmana: \n");
		int brojac = 1;
		while (sc.hasNextLine()) {
			String data = sc.nextLine();
			System.out.println(brojac + ". " + data);
			brojac++;
		}
	}
	
	public static void azuriraj(Tretman t, TipTretmana tip) throws IOException {
		
		t.tip = tip;
		t.setObrisano(true);
		
		//brisanje tretmana sa starim vrednostima iz fajla
		FileWriter writer = new FileWriter("tretmani.txt");
		writer.write("");
		writer.close();
		
		//dodavanje tretmana sa azuriranim vrednostima
		for (Tretman tretman : Tretman.mapa.values()) {
			if (!tretman.obrisano) {
				upisiUFajl(tretman);
			}
		}
	}
	
	public static void izbrisi(Tretman tr) throws IOException {
		
		tr.setObrisano(true);
		
		FileWriter writer = new FileWriter("tretmani.txt");
		writer.write("");
		writer.close();
		
		for (Tretman t : Tretman.mapa.values()) {
			if (!t.obrisano) {
				upisiUFajl(t);
			}
		}
	}
	
	public static void upisiUFajl(Tretman t) {
		try {
			FileWriter writer = new FileWriter("tretmani.txt", true);
		    writer.write(t.tip + "\n");
		    writer.close();
		} catch(IOException e) {
			System.err.println("Problem pri upisu tretmana u fajl.");
		}
	}
}
