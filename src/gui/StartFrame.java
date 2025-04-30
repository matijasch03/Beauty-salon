package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

import korisnici.Klijent;
import korisnici.Korisnik;
import korisnici.Kozmeticar;
import korisnici.Menadzer;
import korisnici.Recepcioner;
import korisnici.Zaposleni;
import menadzeri.MenadzerKlijent;
import menadzeri.MenadzerZaposleni;
import net.miginfocom.swing.MigLayout;
import tretmani.StatusTretmana;
import tretmani.TipTretmana;
import tretmani.Usluga;
import tretmani.ZakazaniTretman;

public class StartFrame {
	
	public StartFrame() throws FileNotFoundException {
		fillMap("klijenti.txt");
		fillMap("kozmeticari.txt");
		fillMap("recepcioneri.txt");
		fillMap("menadzeri.txt");
		createUsluge();
		createZakazane();

		startDialog();
	}
	
	private void fillMap(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path));
		while(sc.hasNextLine()) {
			String[] data = sc.nextLine().split(", ");
			switch (path) {
			case "klijenti.txt":
				String sifreZakazanih = null;
				if (data.length == 9) {
					sifreZakazanih = data[8];
				}
				Klijent kli = new Klijent(data[1], data[2], data[3], data[4], data[5], data[0], data[6], Integer.parseInt(data[7]), sifreZakazanih);
				break;
			
			case "kozmeticari.txt":
				String[] strTretmani = data[10].split(";");
				TipTretmana[] tretmani = new TipTretmana[3];
				int brojac = 0;
				for (String s : strTretmani) {
					tretmani[brojac] = TipTretmana.valueOf(s);
					brojac++;
				}
				String sifreTretmana = null;
				if (data.length == 13) {
					sifreTretmana = data[12];
				}
				Kozmeticar koz = new Kozmeticar(data[1], data[2], data[3], data[4], data[5], data[0], data[6], 
						Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9]), tretmani, Integer.parseInt(data[11]), sifreTretmana);
				break;
				
			case "recepcioneri.txt":
				Recepcioner rec = new Recepcioner(data[1], data[2], data[3], data[4], data[5], data[0], data[6], 
						Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9]));
				break;

			default:
				Menadzer men = new Menadzer(data[1], data[2], data[3], data[4], data[5], data[0], data[6], 
						Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9]));
			}
		}
		sc.close();
	}
	
	private void createUsluge() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("usluge.txt"));
		while(sc.hasNextLine()) {
			String[] data = sc.nextLine().split(", ");
			Usluga u = new Usluga(TipTretmana.valueOf(data[0]), data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
		}
		sc.close();
	}
	
	private void createZakazane() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("zakazani.txt"));
		while(sc.hasNextLine()) {
			String[] data = sc.nextLine().split(", ");
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH dd.MM.yyyy");
			ZakazaniTretman zt = new ZakazaniTretman(Usluga.mapa.get(data[2]), Kozmeticar.mapa.get(data[3]), 
			Klijent.mapa.get(data[1]), LocalDateTime.parse(data[4], format), StatusTretmana.valueOf(data[5]), Integer.parseInt(data[6]));
		}
		sc.close();
	}
	
    private void mergeTextFiles(String file1Path, String file2Path, String file3Path, String mergedFilePath) {
        try (
            BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
            BufferedReader reader2 = new BufferedReader(new FileReader(file2Path));
            BufferedReader reader3 = new BufferedReader(new FileReader(file3Path));
            BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFilePath))
        ) {
            String line;

            while ((line = reader1.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            while ((line = reader2.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            while ((line = reader3.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void startDialog() {
	
		JDialog dialog = new JDialog(); 
		dialog.setTitle("Moj salon");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	
	    JPanel panel = new JPanel(new MigLayout("wrap, insets 10, fill", "10[center]20[center]10", "10[]10[]20[]20[]"));
	
	    JLabel dobroDosli = new JLabel("Dobro došli u korisnički interfejs našeg kozmetičkog salona!");
	    JLabel radnoVreme = new JLabel("Radno vreme: 10-20 h");

	    JButton logInUser = new JButton("Uloguj se (klijent)");
	    JButton register = new JButton("Registruj se");
	    JButton logInEmployee = new JButton("Uloguj se (radnik)");
	    JButton cancel = new JButton("Izađi");
	
	    panel.add(dobroDosli, "span");
	    panel.add(radnoVreme, "span");
	    panel.add(logInUser);
	    panel.add(register, "width 155px");
	    panel.add(logInEmployee);
	    panel.add(cancel, "width 155px");
	
	    dialog.add(panel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
	    
	    //KREIRANJE AKCIJA NA KLIK DUGMICA
	    cancelButton(cancel, dialog);
	    registerButton(register, "klijenti.txt", dialog);
	    
    	logInButton(logInUser, "klijenti.txt", dialog);
    	mergeTextFiles("kozmeticari.txt", "recepcioneri.txt", "menadzeri.txt", "zaposleni.txt");
    	logInButton(logInEmployee, "zaposleni.txt", dialog);
	}
	
	private void logInButton(JButton btn, String path, JDialog parentDialog) {
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JDialog prijava = new JDialog();
				prijava.setTitle("Prijava");
				MigLayout mig = new MigLayout("wrap 2", "[][]", "[]20[]12[]");
				prijava.setLayout(mig);
				prijava.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				JLabel molbaLab = new JLabel("Molimo unesite Vaše podatke:");
				JLabel imeLab = new JLabel("Korisničko ime: ");
				JTextField imeTf = new JTextField(20);
				JLabel lozinkaLab = new JLabel("Lozinka: ");
				JPasswordField lozinkaPf = new JPasswordField(20);
				JButton okBtn = new JButton("OK");
				JButton izadjiBtn = new JButton("Izađi");
				
				prijava.getRootPane().setDefaultButton(okBtn);
				
				prijava.add(molbaLab, "span");
				prijava.add(imeLab);
				prijava.add(imeTf);
				prijava.add(lozinkaLab);
				prijava.add(lozinkaPf);
				prijava.add(new JLabel(""));
				prijava.add(okBtn, "split");
				prijava.add(izadjiBtn, "gapleft 20");
				
				prijava.pack();
			    prijava.setLocationRelativeTo(null);
				prijava.setVisible(true);
				
				cancelButton(izadjiBtn, prijava);
				
				okBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String korisnickoIme = imeTf.getText().trim();
						String lozinka = new String(lozinkaPf.getPassword()).trim();
						if (!korisnickoIme.equals("") && !lozinka.equals("")) {
							
							try {
								boolean korisnikPronadjen = false;
								Scanner scanner = new Scanner(new File(path));
								while (scanner.hasNextLine()) {
									String data = scanner.nextLine();
									String[] items = data.split(",");
									if (items[0].equals(korisnickoIme)) {
										if (lozinka.equals(items[6].trim())) {
											
											prijava.setVisible(false);
											prijava.dispose();
											parentDialog.setVisible(false);
											parentDialog.dispose();
											
											findUser(korisnickoIme, path);
										}
										else {
											JOptionPane.showMessageDialog(null, "Pogrešna lozinka", "Greška", JOptionPane.ERROR_MESSAGE);
										}
										korisnikPronadjen = true;
										break;
									}
								}
								scanner.close();
								
								if (!korisnikPronadjen) {
									JOptionPane.showMessageDialog(null, "Ne postoji korisnik sa ovakvim korisničkim imenom.", "Greška", JOptionPane.ERROR_MESSAGE);
								}
								
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}	
						}
						
						else {
							JOptionPane.showMessageDialog(null, "Niste uneli sve podatke.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
						}
					}
				});
			}
		});
	}
	
	static void cancelButton(JButton btn, JDialog dialog) {
		
		btn.setBackground(new Color(255, 0, 50));
	    btn.setForeground(new Color(220, 220, 220));
		btn.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
	}
	
	private void registerButton(JButton btn, String path, JDialog parentDialog) {
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog registracija = new JDialog();
				registracija.setTitle("Registracija");
				MigLayout mig = new MigLayout("wrap", "[][]", "[]20[]12[]");
				registracija.setLayout(mig);
				registracija.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				JLabel molbaLab = new JLabel("Molimo unesite Vaše podatke:");
				JLabel userNameLab = new JLabel("Korisničko ime: ");
				JTextField userNameTf = new JTextField(20);
				JLabel lozinkaLab = new JLabel("Lozinka: ");
				JTextField lozinkaTf = new JTextField(20);
				JLabel imeLab = new JLabel("Ime: ");
				JTextField imeTf = new JTextField(20);
				JLabel prezimeLab = new JLabel("Prezime: ");
				JTextField prezimeTf = new JTextField(20);
				
				JLabel polLab = new JLabel("Pol: ");
				JRadioButton muskoRb = new JRadioButton("muški");    
				JRadioButton zenskoRb = new JRadioButton("ženski");
				ButtonGroup bg = new ButtonGroup();
				bg.add(muskoRb);
				bg.add(zenskoRb);
				
				JLabel telefonLab = new JLabel("Telefon: ");
				JTextField telefonTf = new JTextField(20);
				JLabel adresaLab = new JLabel("Adresa: ");
				JTextField adresaTf = new JTextField(20);
				JButton okBtn = new JButton("OK");
				JButton izadjiBtn = new JButton("Izađi");
				
				registracija.getRootPane().setDefaultButton(okBtn);
				
				registracija.add(molbaLab, "span 2");
				registracija.add(userNameLab);
				registracija.add(userNameTf);
				registracija.add(lozinkaLab);
				registracija.add(lozinkaTf);
				registracija.add(imeLab);
				registracija.add(imeTf);
				registracija.add(prezimeLab);
				registracija.add(prezimeTf);
				registracija.add(polLab);
				registracija.add(muskoRb, "split 2");
				registracija.add(zenskoRb);
				registracija.add(telefonLab);
				registracija.add(telefonTf);
				registracija.add(adresaLab);
				registracija.add(adresaTf);
				
				registracija.add(new JLabel(""));
				registracija.add(okBtn, "split");
				registracija.add(izadjiBtn, "gapleft 20");
				
				registracija.pack();
				registracija.setLocationRelativeTo(null);
				registracija.setVisible(true);
				
				cancelButton(izadjiBtn, registracija);
				
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
									if (items[0].equals(korisnickoIme)) {
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
								Klijent klijent = new Klijent(ime, prezime, pol, telefon, adresa, korisnickoIme, lozinka, 0, null);
								MenadzerKlijent.upisiUFajl("klijenti.txt", klijent);
								
								registracija.setVisible(false);
								registracija.dispose();
								parentDialog.setVisible(false);
								parentDialog.dispose();
								
								KlijentFrame kf = new KlijentFrame(klijent);
							}
						}
					}
				});
			}
		});
	}

	
	private void findUser(String korisnickoIme, String path) {
		
		if (path.equals("klijenti.txt")){
			if (Klijent.mapa.containsKey(korisnickoIme)) {
					KlijentFrame kf = new KlijentFrame(Klijent.mapa.get(korisnickoIme));
				}
		}
		else { //"zaposleni.txt"
			Zaposleni z = Zaposleni.mapa.get(korisnickoIme);
			if (z instanceof Kozmeticar) {
				if (Kozmeticar.mapa.containsKey(korisnickoIme)) {
					KozmeticarFrame kf = new KozmeticarFrame(Kozmeticar.mapa.get(korisnickoIme));
				}
			}
				
			else if (z instanceof Recepcioner) {
				if (Recepcioner.mapa.containsKey(korisnickoIme)) {
					RecepcionerFrame rf = new RecepcionerFrame(Recepcioner.mapa.get(korisnickoIme));
				}
			}
	
			else if (z instanceof Menadzer) {
				if (Menadzer.mapa.containsKey(korisnickoIme)) {
					MenadzerFrame mf = new MenadzerFrame(Menadzer.mapa.get(korisnickoIme));
				}
			}
		}
	}
}
