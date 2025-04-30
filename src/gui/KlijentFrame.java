package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import korisnici.Klijent;
import korisnici.Kozmeticar;
import menadzeri.MenadzerKlijent;
import menadzeri.MenadzerKozmeticar;
import menadzeri.MenadzerMenadzer;
import menadzeri.MenadzerRecepcioner;
import net.miginfocom.swing.MigLayout;
import tretmanMenadzeri.MenadzerZakazani;
import tretmani.StatusTretmana;
import tretmani.TipTretmana;
import tretmani.Usluga;
import tretmani.ZakazaniTretman;

public class KlijentFrame {
	
	private Klijent klijent;
	
	public KlijentFrame(Klijent k) {
		this.setK(k);
		MainFrame mf = new MainFrame(k);
		createKlijentFrame(mf);
	}

	public Klijent getK() {
		return klijent;
	}

	public void setK(Klijent k) {
		this.klijent = k;
	}
	
	private void createKlijentFrame(MainFrame mf) {
		JMenuBar menu = new JMenuBar();
		JMenu zakazivanje = new JMenu("Zakazivanje tretmana");
		JMenu istorija = new JMenu("Istorija tretmana");
		JMenu kartica = new JMenu("Kartica lojalnosti");
		
		menu.add(zakazivanje);
		menu.add(istorija);
		menu.add(kartica);
		
		zakazivanjeKlik(zakazivanje);
		istorijaKlik(istorija);
		karticaKlik(kartica);
		
		mf.setJMenuBar(menu);
	}
	
	private void zakazivanjeKlik(JMenu zakazivanje) {
		zakazivanje.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Zakazivanje tretmana");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setSize(550, 400);
					dialog.setLayout(new MigLayout("wrap 3", "15[][][][][]", "10[]20[]"));
					
					JLabel natpis = new JLabel("Odaberite željeni tretman na osnovu sledećih kriterijuma.");
					JLabel nula = new JLabel("0 označava da Vam taj kriterijum nije važan.");
					dialog.add(natpis, "span 3");
					dialog.add(nula, "span 3");
					
			        Set<String> tipSkup = new HashSet<>();
			        Set<Integer> trajanjeSkup = new HashSet<>();
			        Set<Integer> cenaSkup = new HashSet<>();
					
					for (Usluga u : Usluga.mapa.values()) {
						tipSkup.add(u.getTip().toString().toLowerCase());
				        trajanjeSkup.add(u.getTrajanje());
				        cenaSkup.add(u.getCena());
					}
					
					String[] tipNiz = new String[3];
					Integer[] trajanjeNiz = new Integer[5];
					Integer[] cenaNiz = new Integer[4];
					
					int brojac = 0;
					for (String obj : tipSkup) {
						tipNiz[brojac] = obj;
						brojac ++;
					}
					
					brojac = 0;
					for (Integer obj : trajanjeSkup) {
						trajanjeNiz[brojac] = obj;
						brojac ++;
					}
					
					brojac = 0;
					for (int obj : cenaSkup) {
						cenaNiz[brojac] = obj;
						brojac ++;
					}
					
					JComboBox<String> tipCombo = new JComboBox<String>(tipNiz);
					JComboBox<Integer> trajanjeCombo = new JComboBox<Integer>(trajanjeNiz);
					JComboBox<Integer> cenaCombo = new JComboBox<Integer>(cenaNiz);
					
					tipCombo.addItem("0");
					trajanjeCombo.addItem(0);
					cenaCombo.addItem(0);
					dialog.add(new JLabel("Tip: "), "split 2");
					dialog.add(tipCombo);
					dialog.add(new JLabel("Trajanje (min): "), "split 2");
					dialog.add(trajanjeCombo);
					dialog.add(new JLabel("Cena (RSD): "), "split 2");
					dialog.add(cenaCombo, "wrap");
					
					JButton pronadji = new JButton("Pronađi tretman");
					dialog.add(pronadji, "span 3, width 155px, height 30px");
					
					pronadji.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							pronadji.setEnabled(false);
							
							String izabraniTip = (String) tipCombo.getSelectedItem();
							Integer izabranoTrajanje = (Integer) trajanjeCombo.getSelectedItem();
							Integer izabranaCena = (Integer) cenaCombo.getSelectedItem();
					        boolean imaRezultata = false;
					        JLabel nema = new JLabel("Nema rezultata pretrage.");

							for (Usluga u : Usluga.mapa.values()) {
					        	if((izabraniTip.equals(u.tip.toString().toLowerCase()) || izabraniTip.equals("0")) && (izabranoTrajanje == u.trajanje || izabranoTrajanje == 0) && (izabranaCena == u.cena || izabranaCena == 0)) {
						        	JButton btn = new JButton(u.nazivUsluge);
						        	dialog.add(btn);
						        	imaRezultata = true;
						        	
						        	dialog.add(btn, "width 155px, height 30px, gapright 15px");
						        	dialog.revalidate();
					                dialog.repaint();
									btn.addActionListener(new ActionListener() {
										
										@Override
										public void actionPerformed(ActionEvent e) {
											JDialog dialogKozmeticar = new JDialog();
											dialogKozmeticar.setTitle("Izbor kozmetičara");
											dialogKozmeticar.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
											dialogKozmeticar.setSize(600, 500);
											dialogKozmeticar.setLayout(new MigLayout("wrap", "15[][]", "10[]20[]"));
											
											JLabel uputstvo = new JLabel("Odabrali ste tretman " + u.nazivUsluge + ". Sada Vas molimo da izaberete kozmetičara.");
											dialogKozmeticar.add(uputstvo, "span 2");
											
											HashMap<String, JButton> mapaDugmadiKoz = new HashMap<>();
											for (Kozmeticar k : Kozmeticar.mapa.values()) {
												for (TipTretmana t : k.getSpisakTretmana()) {
													if (t == u.tip) {
														JButton kozBtn = new JButton(k.getIme() + " " + k.getPrezime());
														dialogKozmeticar.add(kozBtn, "width 155px, height 30px, gapright 15px");
														mapaDugmadiKoz.put(k.getKorisnickoIme(), kozBtn);
														
														kozBtn.addActionListener(new ActionListener() {
															
															@Override
															public void actionPerformed(ActionEvent e) {
																dialogKozmeticar.add(new JLabel("Izabran je kozmetičar " + k.getIme() + " " + k.getPrezime() + ". "
																		+ "Sada izaberite željeni datum i vreme."), "span 2");
																
																LocalDateTime[] slobodniTermini = new LocalDateTime[50];
																String[] parsiraniTermini = new String[50];
														        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy.");
														        int umanjenje = 0; //sluzi za izbegavanje rupa u JListu na kojima bi bili null elementi
																for (int i = 0; i < 5; i++) {
																	LocalDateTime prviDnevniTermin = LocalDateTime.now().plusDays(i + 1).withHour(10).withMinute(0).withSecond(0).withNano(0); 
																	for (int j = 0; j < 10; j++) {
																		int trenutniIndeks = i * 10 + j - umanjenje;
																		slobodniTermini[trenutniIndeks] = prviDnevniTermin.plusHours(j);
																		parsiraniTermini[trenutniIndeks] = slobodniTermini[trenutniIndeks].format(formatter);
																		for (int sifra : k.sifreTretmana) {
																			if (slobodniTermini[trenutniIndeks].equals(ZakazaniTretman.mapa.get(sifra).zakazanoVreme)) {
																				parsiraniTermini[trenutniIndeks] = null;
																				umanjenje++;
																			}
																		}
																		
																	}
																}
																String[] redukovanNiz = new String[50 - umanjenje];
														        System.arraycopy(parsiraniTermini, 0, redukovanNiz, 0, 50 - umanjenje);
																JList<String> listBox = new JList<>(redukovanNiz);
																
														        JScrollPane scrollPane = new JScrollPane(listBox);
														        listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
														        
														        JButton potvrdi = new JButton("Zakazujem tretman");
																potvrdi.addActionListener(new ActionListener() {
																	
																	@Override
																	public void actionPerformed(ActionEvent e) {
																		String selektovaniDatum = listBox.getSelectedValue();
																		if (selektovaniDatum != null) {
																	        LocalDateTime zakazaniDatum = LocalDateTime.parse(selektovaniDatum, formatter);
																	        
																			ZakazaniTretman zt= new ZakazaniTretman(u, k, klijent, zakazaniDatum, StatusTretmana.ZAKAZAN, null);
																			zt.platiTretman(u, klijent, k);
																			JOptionPane.showMessageDialog(null, "Uspešno zakazan tretman " + u.nazivUsluge + " za klijenta " + klijent.getKorisnickoIme() + "."
																					+ " \nDetaljnije podatke o tretmanu i njegovoj izmeni \nmožete videti u rubrici 'Istorija tretmana'.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
																			MenadzerZakazani.upisiUFajl(zt);
																			try {
																				klijent.sifreTretmana.add(zt.redniBroj);
																				k.sifreTretmana.add(zt.redniBroj);

																				MenadzerKlijent.azuriraj("klijenti.txt", klijent, klijent.getIme(), klijent.getPrezime(), klijent.getPol(), klijent.getTelefon(), klijent.getAdresa(), klijent.getKorisnickoIme(), klijent.getLozinka(), klijent.getPotrosenaSuma(), klijent.sifreTretmana);
																				MenadzerKozmeticar.azuriraj("kozmeticari.txt", k, k.getIme(), k.getPrezime(), k.getPol(), k.getTelefon(), k.getAdresa(), k.getKorisnickoIme(), k.getLozinka(), k.getNivoStrucneSpreme(), k.getStaz(), k.getBonus(), k.getOsnovaPlate(), k.getSpisakTretmana(), k.getZaradio(), k.sifreTretmana);
																			} catch (IOException e1) {
																				e1.printStackTrace();
																			}
																			
																			dialogKozmeticar.dispose();
																			dialog.dispose();
																		}
																		else {
																			JOptionPane.showMessageDialog(null, "Niste izabrali datum i vreme.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
																		}
																	}
																});
														        
														        scrollPane.setPreferredSize(new Dimension(150, 300));

																dialogKozmeticar.add(scrollPane);
																dialogKozmeticar.add(potvrdi, "width 200px");
																dialogKozmeticar.setVisible(true);
																
															}
														});
														
														break;
													}
												}
											}
											JButton nijeVazno = new JButton("Nije važno");
											dialogKozmeticar.add(nijeVazno, "width 155px, height 30px, wrap");
											
											nijeVazno.addActionListener(new ActionListener() {
												
												@Override
												public void actionPerformed(ActionEvent e) {
													for (JButton btn : mapaDugmadiKoz.values()) {
														btn.doClick();
														break;
													}
													
												}
											});
																				
											for (JButton btn : mapaDugmadiKoz.values()) { //onemogucavanje kliktanja na ostale dugmice nakon 1.
												btn.addActionListener(new ActionListener() {
													
													@Override
													public void actionPerformed(ActionEvent e) {
														for (JButton btn : mapaDugmadiKoz.values()) {
															btn.setEnabled(false);
														}	
														nijeVazno.setEnabled(false);
													}
												});
											}
											
											dialogKozmeticar.setLocationRelativeTo(null);
											dialogKozmeticar.setVisible(true);
										}
									});
								}
					        }
							if (!imaRezultata) {
				        		dialog.add(nema);
				        		dialog.revalidate();
				                dialog.repaint();
				        	}
						}
					});
										
				    dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);
				}
			}
		});
	}
	
	private void istorijaKlik(JMenu istorija) {
		istorija.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Istorija tretmana");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setLayout(new MigLayout("wrap 3", "15[][]", "10[]20[]15[]"));
					
					JLabel natpis = new JLabel("Ovde imate uvid u sve Vaše tretmane uz mogućnost izmene.");
					dialog.add(natpis, "span 3");
					
					boolean imaSifara = false;
					
					for (int sifra : klijent.sifreTretmana) {
						imaSifara = true;
						ZakazaniTretman zt = ZakazaniTretman.mapa.get(sifra);
						String nazivUsluge = zt.nazivUsluge;
						JLabel tretman = new JLabel("(" + sifra + ") " + nazivUsluge);
						dialog.add(tretman);
						JButton otkazi = new JButton("Otkaži");
						dialog.add(otkazi);
						JButton vidi = new JButton("Detaljnije");
						dialog.add(vidi);
						
						if (zt.status != StatusTretmana.ZAKAZAN) {
							otkazi.setEnabled(false);
						}
						
						vidi.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								JDialog dialog = new JDialog();
								dialog.setTitle("Detaljnije o tretmanu");
								dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
								dialog.setLayout(new MigLayout("wrap", "[]", "12[]12"));
								
						        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy.");
						        JLabel naziv = new JLabel("Naziv: " + nazivUsluge);
								JLabel vreme = new JLabel("Vreme i datum: " + zt.zakazanoVreme.format(formatter));
								JLabel koz = new JLabel("Kozmetičar: " + zt.kozmeticar.getIme() + " " + zt.kozmeticar.getPrezime());
								JLabel cena = new JLabel("Cena: " + zt.getCena());
								JLabel status = new JLabel("Status: " + zt.getStatus().toString());
								
								dialog.add(naziv);
								dialog.add(vreme);
								dialog.add(koz);
								dialog.add(cena);
								dialog.add(status);
								
								dialog.pack();
								dialog.setLocationRelativeTo(null);
								dialog.setVisible(true);
								
							}
						});
						
						otkazi.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								UIManager.put("OptionPane.yesButtonText", "Da");
						        UIManager.put("OptionPane.noButtonText", "Ne");
						        int odgovor = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da otkažete tretman " + nazivUsluge + "?",
						        		"Potvrda", JOptionPane.YES_NO_OPTION);
						        if (odgovor == JOptionPane.YES_OPTION) {
						        	MenadzerRecepcioner.setujStatusTretmana(sifra, StatusTretmana.OTKAZAO_KLIJENT);
						        	otkazi.setEnabled(false);
						        	dialog.revalidate();
						            dialog.repaint();
						        }
							}
						});
					}
					
					if (!imaSifara) {
						dialog.add(new JLabel("(jošuvek nema zakazanih tretmana)"), "span3, center");
					}
					
					dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);
				}
			}
		});
	}
	
	private void karticaKlik(JMenu kartica) {
		kartica.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					MenadzerKlijent.traziKarticuLojalnosti(klijent);
					JDialog dialog = new JDialog();
					dialog.setTitle("Kartica lojalnosti");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setLayout(new MigLayout("wrap", "15[center]", "[]"));
					
					JLabel obavest = new JLabel("Minimalan potrošen iznos da biste ostvarili pravo na karticu lojalnosti je " + MenadzerMenadzer.getLojalnaSuma() + " dinara.");
					JLabel potrosenaSuma = new JLabel("Suma koju ste do sada potrošili u našem salonu je " + klijent.getPotrosenaSuma() + " dinara.");
					String ne = "i";
					if (!klijent.isKarticaLojalnosti()) {
						ne = "ne";
					}
					JLabel pravo = new JLabel("Stoga " + ne + "mate pravo na karticu lojalnosti.");
					
					dialog.add(obavest);
					dialog.add(potrosenaSuma);
					dialog.add(pravo);

					dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);
				 }
			}
		});
	}
}
