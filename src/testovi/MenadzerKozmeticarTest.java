package testovi;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import korisnici.Klijent;
import korisnici.Kozmeticar;
import menadzeri.MenadzerKlijent;
import menadzeri.MenadzerKozmeticar;
import tretmani.TipTretmana;

class MenadzerKozmeticarTest {

	public String path = "test.txt";
	public TipTretmana[] t = {TipTretmana.MANIKIR};
	public Kozmeticar mica = new Kozmeticar("Jovana", "Jovanović", "ženski", "022", "ok", "mica", "loz", 6, 5, 2000, t, 7100, "27;29");
	public File file = new File(path);;
	
	@BeforeEach
	void setUp() throws Exception {
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		MenadzerKozmeticar.upisiUFajl("test.txt", mica);
	}

	public void tearDown(){
        if (file.exists()) {
            file.delete();
        }
	}

	@Test
	void upisiUFajlTest() throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		String written = sc.nextLine();
		assertEquals(written, "mica, Jovana, Jovanović, ženski, 022, ok, loz, 6, 5, 2000, MANIKIR, 7100, 27;29");
		sc.close();
	}
	
	@Test
	void procitajTest() throws FileNotFoundException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(byteArrayOutputStream));
        
        MenadzerKozmeticar.procitaj();
        
        String written = byteArrayOutputStream.toString();
        System.setOut(originalOut);
        String[] data = written.split("\n");
        assertEquals("1. Jovana Jovanović (mica)", data[2]);
	}
	
	@Test
	public void brisanjeBrojnostTest() throws IOException {
	    Kozmeticar cica = new Kozmeticar("Jovana", "Jovanović", "ženski", "022", "ok", "cica", "loz", 6, 5, 2000, t, 7100, "27;29");
		
		assertEquals(2, Kozmeticar.mapa.size());
		MenadzerKozmeticar.izbrisi(path, mica);
		assertEquals(1, Kozmeticar.mapa.size());
		
	}
	
	@Test
	public void azurirajTest() throws IOException {
		ArrayList<Integer> list = new ArrayList<>();

		MenadzerKozmeticar.azuriraj(path, mica, "Joca", "Jovanović", "ženski", "022", "ok", "cica", "loz", 6, 5, 2000, 20000, t, 7100, list);
		assertEquals("Joca", mica.getIme());
	}
}
