package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

import korisnici.Klijent;
import korisnici.Korisnik;
import korisnici.Kozmeticar;
import korisnici.Menadzer;
import korisnici.Recepcioner;
import korisnici.Zaposleni;
import menadzeri.MenadzerKlijent;
import menadzeri.MenadzerKozmeticar;
import menadzeri.MenadzerMenadzer;
import menadzeri.MenadzerRecepcioner;
import menadzeri.MenadzerZaposleni;
import net.miginfocom.swing.MigLayout;
import tretmanMenadzeri.MenadzerZakazani;
import tretmani.TipTretmana;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private Korisnik k;
	protected JPanel panel;

	public Korisnik getK() {
		return k;
	}

	public void setK(Korisnik k) {
		this.k = k;
	}
	
	public MainFrame(Korisnik k) {
		this.k = k;
		createFrame();
	}
	
	private void createFrame() {
		this.setTitle("Glavni meni");
		this.setSize(505, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
				
		MigLayout mig = new MigLayout("wrap 2, center", "20[]45[]", "30[]20[]12[]");
		panel = new JPanel(mig);
		
		JLabel natpis = new JLabel("VAŠI LIČNI PODACI");
		JLabel ime = new JLabel("Ime: " + k.getIme());
		JLabel prezime = new JLabel("Prezime: " + k.getPrezime());
		JLabel pol = new JLabel("Pol: " + k.getPol());
		JLabel telefon = new JLabel("Telefon: " + k.getTelefon());
		JLabel adresa = new JLabel("Adresa: " + k.getAdresa());
		JLabel korisnickoIme = new JLabel("Korisničko ime: " + k.getKorisnickoIme());
		JLabel lozinka = new JLabel("Lozinka: *****");
		
		panel.add(natpis, "span 2, gapleft 110px");
		panel.add(ime);
		panel.add(prezime);
		panel.add(pol);
		panel.add(telefon);
		panel.add(adresa);
		panel.add(korisnickoIme);
		panel.add(lozinka, "wrap");
		
		String path = "";
		
		if (k instanceof Klijent) {
			path = "klijenti.txt";
		}
		else {
			Zaposleni z = (Zaposleni) k;

			panel.add(new JLabel(""), "span 2");
			JLabel nivo = new JLabel("Nivo stručne spreme : " + z.getNivoStrucneSpreme());
			JLabel staz = new JLabel("Staž: " + z.getStaz());
			JLabel bonus = new JLabel("Bonus (RSD): " + z.getBonus());
			JLabel plata = new JLabel("Plata (RSD): " + z.getPlata());
			panel.add(nivo);
			panel.add(staz);
			panel.add(bonus);
			panel.add(plata, "wrap");
			
			if (k instanceof Recepcioner) {
				path = "recepcioneri.txt";
			}
			
			else if (k instanceof Menadzer) {
				path = "menadzeri.txt";
			}
			
			else {
				path = "kozmeticari.txt";
				Kozmeticar koz = (Kozmeticar) k;
				String tretmani = "Tretmani: ";
				for (TipTretmana tip : koz.getSpisakTretmana()) {
					if (tip != null)
					{
						tretmani += tip.toString().toLowerCase() + ", ";
					}				
				}
				tretmani = tretmani.substring(0, tretmani.length() - 2);
			}
		}
		
		JButton izmeni = new JButton("Izmeni");
		panel.add(izmeni, "span 2, center");
		izmeniKlik(izmeni, path);
		this.add(panel);
		this.setVisible(true);
	}
	
	private void izmeniKlik(JButton izmeni, String path) {
		izmeni.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog();
				dialog.setTitle("Izmena podataka");
				MigLayout mig = new MigLayout("wrap", "[][]", "[]20[]12[]");
				dialog.setLayout(mig);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				JLabel molbaLab = new JLabel("Molimo unesite Vaše nove podatke:");
				JLabel userNameLab = new JLabel("Korisničko ime: ");
				JTextField userNameTf = new JTextField(20);
	            userNameTf.setText(k.getKorisnickoIme());
				JLabel lozinkaLab = new JLabel("Lozinka: ");
				JTextField lozinkaTf = new JTextField(20);
				lozinkaTf.setText(k.getLozinka());
				JLabel imeLab = new JLabel("Ime: ");
				JTextField imeTf = new JTextField(20);
				imeTf.setText(k.getIme());
				JLabel prezimeLab = new JLabel("Prezime: ");
				JTextField prezimeTf = new JTextField(20);
				prezimeTf.setText(k.getPrezime());
				
				JLabel polLab = new JLabel("Pol: ");
				JRadioButton muskoRb = new JRadioButton("muški");    
				JRadioButton zenskoRb = new JRadioButton("ženski");
				ButtonGroup bg = new ButtonGroup();
				bg.add(muskoRb);
				bg.add(zenskoRb);
				
				if (k.getPol().equals("muški")) {
		            muskoRb.setSelected(true);
				}
				else {
		            zenskoRb.setSelected(true);
				}
				
				JLabel telefonLab = new JLabel("Telefon: ");
				JTextField telefonTf = new JTextField(20);
				telefonTf.setText(k.getTelefon());

				JLabel adresaLab = new JLabel("Adresa: ");
				JTextField adresaTf = new JTextField(20);
				adresaTf.setText(k.getAdresa());

				JButton okBtn = new JButton("Sačuvaj");
				JButton izadjiBtn = new JButton("Izađi");
				
				dialog.getRootPane().setDefaultButton(okBtn);
				
				dialog.add(molbaLab, "span 2");
				dialog.add(userNameLab);
				dialog.add(userNameTf);
				dialog.add(lozinkaLab);
				dialog.add(lozinkaTf);
				dialog.add(imeLab);
				dialog.add(imeTf);
				dialog.add(prezimeLab);
				dialog.add(prezimeTf);
				dialog.add(polLab);
				dialog.add(muskoRb, "split 2");
				dialog.add(zenskoRb);
				dialog.add(telefonLab);
				dialog.add(telefonTf);
				dialog.add(adresaLab);
				dialog.add(adresaTf);
				
				dialog.add(new JLabel(""));
				dialog.add(okBtn, "split");
				dialog.add(izadjiBtn, "gapleft 20");
				
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				
				StartFrame.cancelButton(izadjiBtn, dialog);
				
				okBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String korisnickoIme = userNameTf.getText().trim();
						String lozinka = lozinkaTf.getText().trim();
						String ime = imeTf.getText().trim();
						String prezime = prezimeTf.getText().trim();
						String telefon = telefonTf.getText().trim();
						String adresa = adresaTf.getText().trim();
						String pol = "";
						if (muskoRb.isSelected()) pol = "muški";
						else if (zenskoRb.isSelected()) pol = "ženski";
						
						String[] podaci = {korisnickoIme, lozinka, ime, prezime, telefon, adresa, pol};
						boolean sveJeUneto = true;
						for (String podatak : podaci) {
							if (podatak.equals("")) {
								sveJeUneto = false;
								JOptionPane.showMessageDialog(null, "Niste uneli sve podatke.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
								break;
							}
						}

						if (sveJeUneto) {
							boolean postojiDuplikat = false;
							try {
								Scanner scanner = new Scanner(new File(path));
								while (scanner.hasNextLine()) {
									String data = scanner.nextLine();
									String[] items = data.split(",");
									if (items[0].equals(korisnickoIme) && !items[0].equals(korisnickoIme)) {
										postojiDuplikat = true;
										JOptionPane.showMessageDialog(null, "Ovo korisničko ime je već u upotrebi. Molimo Vas da upotrebite neko drugo.", "Greška", JOptionPane.ERROR_MESSAGE);
										break;
									}
								}
								scanner.close();
								
							}catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
							if (!postojiDuplikat) {
								switch (path) {
								case "klijenti.txt":
									Klijent klijent = Klijent.mapa.get(k.getKorisnickoIme());
									try {
										MenadzerKlijent.azuriraj("klijenti.txt", klijent, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, klijent.getPotrosenaSuma(), klijent.sifreTretmana);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									KlijentFrame kf = new KlijentFrame(klijent);
									break;
								
								case "kozmeticari.txt":
									Kozmeticar koz = Kozmeticar.mapa.get(k.getKorisnickoIme());
									try {
										MenadzerKozmeticar.azuriraj("kozmeticari.txt", koz, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, koz.getNivoStrucneSpreme(), koz.getStaz(), koz.getBonus(), koz.getOsnovaPlate(), koz.getSpisakTretmana(), koz.getZaradio(), koz.sifreTretmana);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									KozmeticarFrame kof = new KozmeticarFrame(koz);
									break;
									
								case "recepcioneri.txt":
									Recepcioner rec = Recepcioner.mapa.get(k.getKorisnickoIme());
									try {
										MenadzerRecepcioner.azuriraj("kozmeticari.txt", rec, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, rec.getNivoStrucneSpreme(), rec.getStaz(), rec.getBonus(), rec.getOsnovaPlate());
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									RecepcionerFrame rf = new RecepcionerFrame(rec);
									break;

								default:
									Menadzer men = Menadzer.mapa.get(k.getKorisnickoIme());
									try {
										MenadzerMenadzer.azuriraj("menadzeri.txt", men, ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, men.getNivoStrucneSpreme(), men.getStaz(), men.getBonus(), men.getOsnovaPlate());
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									MenadzerFrame mf = new MenadzerFrame(men);
								}
								
								dialog.setVisible(false);
								dialog.dispose();
								dispose();
							}
						}
					}
				});
			}
		});
	}
}
