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

import korisnici.Kozmeticar;
import korisnici.Recepcioner;
import menadzeri.MenadzerKozmeticar;
import menadzeri.MenadzerRecepcioner;
import menadzeri.MenadzerZaposleni;

class MenadzerRecepcionerTest {

	public String path = "test.txt";
	public Recepcioner mica = new Recepcioner("Jovana", "Jovanović", "ženski", "022", "ok", "mica", "loz", 6, 5, 2000);
	public File file = new File(path);;
	
	@BeforeEach
	void setUp() throws Exception {
		FileWriter writer = new FileWriter(path);
		writer.write("");
		writer.close();
		MenadzerZaposleni.upisiUFajl(mica, "test.txt");
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
		assertEquals(written, "mica, Jovana, Jovanović, ženski, 022, ok, loz, 6, 5, 2000");
		sc.close();
	}
	
	@Test
	public void brisanjeBrojnostTest() throws IOException {
	    Recepcioner cica = new Recepcioner("Jovana", "Jovanović", "ženski", "022", "ok", "cica", "loz", 6, 5, 2000);
		
		assertEquals(2, Recepcioner.mapa.size());
		MenadzerRecepcioner.izbrisi(path, mica);
		assertEquals(1, Recepcioner.mapa.size());
		
	}
	
	@Test
	public void azurirajTest() throws IOException {
		
		MenadzerRecepcioner.azuriraj(path, mica, "Joca", "Jovanović", "ženski", "022", "ok", "cica", "loz", 6, 5, 2000, 20000);
		assertEquals("Joca", mica.getIme());
	}
}

