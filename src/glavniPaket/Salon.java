package glavniPaket;

import java.time.LocalTime;

public class Salon {
	private String naziv;
	private LocalTime pocetakRadnogVremena;
	private LocalTime krajRadnogVremena;

	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public LocalTime getPocetakRadnogVremena() {
		return pocetakRadnogVremena;
	}
	public void setPocetakRadnogVremena(LocalTime pocetakRadnogVremena) {
		this.pocetakRadnogVremena = pocetakRadnogVremena;
	}
	
	public LocalTime getKrajRadnogVremena() {
		return krajRadnogVremena;
	}
	public void setKrajRadnogVremena(LocalTime krajRadnogVremena) {
		this.krajRadnogVremena = krajRadnogVremena;
	}
	
	public Salon() {}
	
	public Salon(String naziv, LocalTime pocetakRadnogVremena, LocalTime krajRadnogVremena) {
		this.naziv = naziv;
		this.pocetakRadnogVremena = pocetakRadnogVremena;
		this.krajRadnogVremena = krajRadnogVremena;
	}
}


