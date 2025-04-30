package gui;

import korisnici.Kozmeticar;

public class KozmeticarFrame {
	private Kozmeticar koz;
	
	public KozmeticarFrame(Kozmeticar koz) {
		this.koz = koz;
		MainFrame mf = new MainFrame(koz);
		createKozmeticarFrame(mf);
	}

	public Kozmeticar getKoz() {
		return koz;
	}

	public void setKoz(Kozmeticar koz) {
		this.koz = koz;
	}
	
	private void createKozmeticarFrame(MainFrame mf) {
		
	}
}
