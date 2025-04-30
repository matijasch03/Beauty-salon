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
import menadzeri.MenadzerKlijent;

class MenadzerKlijentTest {
	
	public String path = "test.txt";

	@BeforeEach
	public void setUp() throws IOException {
		File file = new File(path);
		FileWriter writer = new FileWriter(file);
		writer.write("kima, maki, seki\nivic, iva, kis");
		writer.close();

	}
	
	@AfterEach
	public void tearDown(){
		File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
	}
	
	@Test
	public void test() throws FileNotFoundException {
		String exit = MenadzerKlijent.procitaj(path);
        String[] lines = exit.split("\n");

        assertEquals(lines[0].trim(), "1. maki seki (kima)");
        assertEquals(lines[1].trim(), "2. iva kis (ivic)");
	}
	
	@Test
	public void azurirajTest() throws IOException {
		Klijent mica = new Klijent("Milica", "Milic", "m", "022", "ok", "mica", "loz", 0, "16");
		ArrayList<Integer> list = new ArrayList<>();

		MenadzerKlijent.azuriraj(path, mica, "Micka", "Milic", "m", "022", "ok", "kli1", "loz", 0, list);
		assertEquals("Micka", mica.getIme());
	}

	@Test
	public void brisanjeBrojnostTest() throws IOException {
		Klijent mica = new Klijent("Milica", "Milic", "m", "022", "ok", "mica", "loz", 0, "16");
		Klijent cica = new Klijent("Milica", "Milic", "m", "022", "ok", "mica2", "loz", 0, "16");
		
		assertEquals(2, Klijent.mapa.size());
		MenadzerKlijent.izbrisi(path, mica);
		assertEquals(1, Klijent.mapa.size());
	}
	
	@Test
	public void brisanjeFajlTest() throws IOException {
		Klijent mica = new Klijent("Milica", "Milic", "m", "022", "ok", "mica", "loz", 0, "16");
		Klijent cica = new Klijent("Milica", "Milic", "m", "022", "ok", "mica2", "loz", 0, "16");
		
		MenadzerKlijent.izbrisi(path, mica);
		
		Scanner sc = new Scanner(new File(path));
		String[] data = sc.nextLine().split(", ");
		assertEquals(data[0], "mica2");
		sc.close();
	}
	
	@Test
	public void upisiUFajlTest() throws FileNotFoundException {
		Klijent mica = new Klijent("Milica", "Milic", "m", "022", "ok", "mica", "loz", 0, "16");
		File file = new File("proba.txt");
		MenadzerKlijent.upisiUFajl("proba.txt", mica);
		Scanner sc = new Scanner(file);
		String written = sc.nextLine();
		assertEquals(written, "mica, Milica, Milic, m, 022, ok, loz, 0, 16");
		sc.close();
	}
	
	@Test
	public void karticaLojalnostiTest() {
		Klijent mica = new Klijent("Milica", "Milic", "m", "022", "ok", "mica", "loz", 9000, "16");
		assertEquals(mica.isKarticaLojalnosti(), false);
		
		mica.setPotrosenaSuma(11000);
		MenadzerKlijent.traziKarticuLojalnosti(mica);
		assertEquals(mica.isKarticaLojalnosti(), true);
	}
	
}
