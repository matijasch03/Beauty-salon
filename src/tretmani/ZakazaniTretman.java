package tretmani;

import korisnici.Klijent;
import korisnici.Kozmeticar;
import korisnici.Menadzer;
import korisnici.Zaposleni;
import menadzeri.MenadzerMenadzer;
import tretmanMenadzeri.MenadzerZakazani;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import java.time.LocalDateTime;


public class ZakazaniTretman extends Usluga {
	public Kozmeticar kozmeticar;
	public Klijent klijent;
	public LocalDateTime zakazanoVreme;
	public StatusTretmana status;
	public static int ukupanBroj = 1;
	public int redniBroj;
	public static HashMap<Integer, ZakazaniTretman> mapa = new HashMap<>();
	
	public Kozmeticar getKozmeticar() {
		return kozmeticar;
	}
	public void setKozmeticar(Kozmeticar kozmeticar) {
		this.kozmeticar = kozmeticar;
	}
	
	public Klijent getKlijent() {
		return klijent;
	}
	
	public void setKlijent(Klijent klijent) {
		this.klijent = klijent;
	}
	public LocalDateTime getZakazanoVreme() {
		return zakazanoVreme;
	}
	public void setZakazanoVreme(LocalDateTime zakazanoVreme) {
		this.zakazanoVreme = zakazanoVreme;
	}
	
	public StatusTretmana getStatus() {
		return status;
	}
	public void setStatus(StatusTretmana status) {
		this.status = status;
	}
		
	public ZakazaniTretman(Usluga usluga, Kozmeticar kozmeticar, Klijent klijent, LocalDateTime zakazanoVreme, StatusTretmana status, Integer novaCena){
		
		boolean mogucnostZakazivanja = true;
		
//		POTREBNO SAMO ZA KONZOLNO KREIRANJE ENTITETA
//		boolean postojanjeTretmana = false;
//		for (TipTretmana t : kozmeticar.spisakTretmana) { //da li je kozmeticar kvalifikovan za trazenu uslugu
//			if (usluga.tip.equals(t)) {
//				postojanjeTretmana = true;
//				break;
//			}
//		}
//		
//		if (!postojanjeTretmana) {
//			mogucnostZakazivanja = false;
//			System.out.println("Kozmeticar " + kozmeticar.ime + " nije obucen za izabrani tretman.");
//		}
//		
//		if (mogucnostZakazivanja) {
//			if (!zauzetostKozmeticara.containsKey(kozmeticar)) {
//				LocalDateTime[] niz = new LocalDateTime[10];
//				for (int i = 0; i < niz.length; i++) {
//					if (niz[i] == null) {
//						niz[i] = zakazanoVreme;
//					}
//				}
//				zauzetostKozmeticara.put(kozmeticar, niz);
//			}
//			
//			else {
//				LocalDateTime[] listaPopunjenosti = zauzetostKozmeticara.get(kozmeticar);
//				for (LocalDateTime i : listaPopunjenosti) {
//					if (i != null && i.equals(zakazanoVreme)) {
//						System.out.println("Ovaj termin kod kozmeticara " + kozmeticar.ime + " je vec popunjen.");
//						mogucnostZakazivanja = false;
//						break;
//					}
//				}
//			}
//		}
		
		if (mogucnostZakazivanja) {
			this.nazivUsluge = usluga.nazivUsluge;
			this.tip = usluga.tip;
			this.trajanje = usluga.trajanje;
			this.kozmeticar = kozmeticar;
			this.klijent = klijent;
			this.zakazanoVreme = zakazanoVreme;
			this.status = status;
			if (novaCena != null) {
				this.cena = novaCena;
			}
			else {
				if (klijent.isKarticaLojalnosti()) {
					this.cena = usluga.cena * 9 / 10;
				}
				else {
					this.cena = usluga.cena;
				}
			}
			
			Menadzer.bilans += this.cena;
			redniBroj = ukupanBroj;
			mapa.put(redniBroj, this);
			ukupanBroj++;
		}
		
	}
	
	public void platiTretman(Usluga usluga, Klijent klijent, Kozmeticar kozmeticar) {
		klijent.setPotrosenaSuma(klijent.getPotrosenaSuma() + this.cena);
		kozmeticar.setZaradio(kozmeticar.getZaradio() + this.cena);
	}
	
}