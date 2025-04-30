package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import korisnici.Klijent;
import korisnici.Kozmeticar;
import korisnici.Recepcioner;
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


public class RecepcionerFrame extends MainFrame{
	private static final long serialVersionUID = 1L;
	private Recepcioner rec;
	
	public RecepcionerFrame(Recepcioner rec) {
		super(rec);
		this.rec = rec;
		createRecepcionerFrame();
	}

	public Recepcioner getRec() {
		return rec;
	}

	public void setRec(Recepcioner rec) {
		this.rec = rec;
	}
	
	private void createRecepcionerFrame() {
		JMenuBar menu = new JMenuBar();
		JMenu sviTretmani = new JMenu("Svi tretmani");
		JMenu sviKlijenti = new JMenu("Svi klijenti");
		
		menu.add(sviTretmani);
		menu.add(sviKlijenti);
		
		JLabel pretraga = new JLabel("Pretraga klijenata: ");
		JComboBox<String> combo = new JComboBox<>();
		
		for (String sifra : Klijent.mapa.keySet()) {
			combo.addItem(sifra);
		}
		JButton zakazi = new JButton("Zakaži");

		zakazi.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String korisnickoIme = (String) combo.getSelectedItem();
				Klijent klijent = Klijent.mapa.get(korisnickoIme);
				zakazivanjeKlik(zakazi, klijent);
				}
			});
		
		panel.add(pretraga, "wrap");
		panel.add(combo, "split 2");
		panel.add(zakazi);
		
		sviTretmaniKlik(sviTretmani);
		sviKlijentKlik(sviKlijenti);
		
		this.setJMenuBar(menu);
	}
	
	private void zakazivanjeKlik(JButton zakazivanje, Klijent klijent) {
		zakazivanje.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog();
				dialog.setTitle("Zakazivanje tretmana");
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLayout(new MigLayout("", "15[]", "10[]20[]"));
				
				JLabel natpis = new JLabel("Odaberite željeni tretman.");
				dialog.add(natpis, "wrap");
				
				ArrayList<ArrayList<Usluga>> matricaUsluga = new ArrayList<>(); //u istoj vrsti nalaze se usluge koje pripadaju istom tretmanu
				for (int i = 0; i < TipTretmana.values().length; i++) {
					ArrayList<Usluga> row = new ArrayList<>();
					matricaUsluga.add(row);
				}
				
				for (Usluga u : Usluga.mapa.values()) {
			        matricaUsluga.get(u.tip.ordinal()).add(u);
				}

				for (int i = 0; i < TipTretmana.values().length; i++) {
					dialog.add(new JLabel(matricaUsluga.get(i).get(0).tip.toString() + ":"), "wrap, gapleft 20");
					for (Usluga u : matricaUsluga.get(i)) {
						JButton btn = new JButton(u.nazivUsluge);
						dialog.add(btn, "width 155px, height 30px, gapright 15px");
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
																		+ " \nDetaljnije podatke o tretmanu i njegovoj izmeni \nmožete videti u rubrici 'Svi tretmani'.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
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
					dialog.add(new JLabel(""), "wrap");
				}	
				
				dialog.pack();
			    dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});
	}
	
	private void sviTretmaniKlik(JMenu sviTretmani) {
		sviTretmani.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Svi tretmani");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setSize(700, 500);
					dialog.setLayout(new MigLayout("wrap 4", "15[][][][][]", "10[]10"));
					
					JLabel natpis = new JLabel("Ovde imate uvid u sve tretmane i imate mogućnost izmene statusa.");
					dialog.add(natpis, "span 4");
					JLabel uput = new JLabel("Izaberite kriterijume na osnovu kojih ćete vršiti pretragu.");
					dialog.add(uput, "span 4");
					JLabel nula = new JLabel("0 označava da Vam taj kriterijum nije važan.");
					dialog.add(nula, "span 4");
					
					JComboBox<String> tretmanCombo = new JComboBox<String>();
					JComboBox<String> uslugaCombo = new JComboBox<String>();
					
					for (TipTretmana t : TipTretmana.values()) {
						tretmanCombo.addItem(t.toString().toLowerCase());
					}
					
					for (Usluga u : Usluga.mapa.values()) {
						uslugaCombo.addItem(u.getNazivUsluge());
					}
					
					tretmanCombo.addItem("0");
					uslugaCombo.addItem("0");
			
					dialog.add(new JLabel("Tretman: "), "split 4");
					dialog.add(tretmanCombo);
					dialog.add(new JLabel("Usluga: "), "gapleft 20px");
					dialog.add(uslugaCombo, "wrap");
					
					JTextField donjaTf = new JTextField(5);
					donjaTf.setText("0");
					JTextField gornjaTf = new JTextField(5);
					gornjaTf.setText("0");
					dialog.add(new JLabel("Donja cena: "), "split 4");
					dialog.add(donjaTf);
					dialog.add(new JLabel("Gornja cena: "), "gapleft 20px");
					dialog.add(gornjaTf, "wrap");
					
					JButton trazi = new JButton("Traži");
					dialog.add(trazi, "width 250px");
					JButton izmeni = new JButton("Izmeni");
					dialog.add(trazi, "width 250px, split 2");
					dialog.add(izmeni, "width 250px, wrap");
					
					trazi.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							String tretman = (String) tretmanCombo.getSelectedItem();
							String usluga = (String) uslugaCombo.getSelectedItem();
							int donja = Integer.parseInt(donjaTf.getText().trim());
							int gornja = Integer.parseInt(gornjaTf.getText().trim());
							
							DefaultTableModel model = new DefaultTableModel();
				            JTable table = new JTable(model);
				    		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				            model.addColumn("#");
				            model.addColumn("klijent");
				            model.addColumn("kozmetičar");
				            model.addColumn("naziv");
				            model.addColumn("vreme i datum");
				            model.addColumn("status");
							
							for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
								if ((tretman.equals("0") || z.getTip().toString().toLowerCase().equals(tretman))
										&& (usluga.equals("0") || z.getNazivUsluge().equals(usluga))
										&& (z.getCena() >= donja) && (z.getCena() <= gornja)) {
									
							        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
									String vreme = z.getZakazanoVreme().format(formatter);
						            model.addRow(new Object[]{z.redniBroj, z.klijent.getKorisnickoIme(), z.kozmeticar.getKorisnickoIme(), z.getNazivUsluge(), vreme, z.getStatus().toString()});
								}
							}
				            table.getColumnModel().getColumn(0).setPreferredWidth(5);
				            table.getColumnModel().getColumn(4).setPreferredWidth(100);
				            table.getColumnModel().getColumn(5).setPreferredWidth(100);

				            table.setPreferredScrollableViewportSize(new Dimension(600, 400));
							JScrollPane scrollPane = new JScrollPane(table);
				            dialog.add(scrollPane);
							dialog.revalidate();
			                dialog.repaint();
			                trazi.setEnabled(false);
			                
			                izmeni.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									int red = table.getSelectedRow();
									if(red == -1) {
										JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
									}else {
										int sifra = Integer.parseInt(table.getValueAt(red, 0).toString());
										JDialog dialog2 = new JDialog();
										dialog2.setTitle("Izmena statusa");
										dialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
										dialog2.setLayout(new MigLayout());
										
										JComboBox<StatusTretmana> statusCombo = new JComboBox<StatusTretmana>();
										statusCombo.addItem(StatusTretmana.IZVRŠEN);
										statusCombo.addItem(StatusTretmana.NIJE_SE_POJAVIO);
										statusCombo.addItem(StatusTretmana.OTKAZAO_SALON);
										
										dialog2.add(statusCombo);
										
										JButton potvrdi = new JButton("Sačuvaj izmenu");
										dialog2.add(potvrdi);
										potvrdi.addActionListener(new ActionListener() {
											
											@Override
											public void actionPerformed(ActionEvent e) {
												StatusTretmana status = (StatusTretmana) statusCombo.getSelectedItem();
												MenadzerRecepcioner.setujStatusTretmana(sifra, status);
												dialog2.dispose();
												DefaultTableModel model = (DefaultTableModel) table.getModel();
												model.setValueAt(status, red, 5);
												
												dialog.revalidate();
								                dialog.repaint();
											}
										});
										dialog2.pack();
										dialog2.setLocationRelativeTo(null);
										dialog2.setVisible(true);
									}
								}
							});
						}
					});
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);
				}
			}
		});
	}
	
	public static void sviKlijentKlik(JMenu sviKlijenti) {
		sviKlijenti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Svi klijenti");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setLayout(new MigLayout("wrap 1", "15[][]", "10[]10"));
					
					JLabel natpis = new JLabel("Ovde imate uvid u sve klijente i podatke o njihovim karticama lojalnosti.");
					dialog.add(natpis);
					JLabel obavest = new JLabel("Minimalan potrošen iznos da bi klijent ostvario pravo na karticu lojalnosti je " + MenadzerMenadzer.getLojalnaSuma() + " dinara.");
					dialog.add(obavest);
					
					DefaultTableModel model = new DefaultTableModel();
		            JTable table = new JTable(model);

		            model.addColumn("#");
		            model.addColumn("korisničko ime");
		            model.addColumn("ime");
		            model.addColumn("prezime");
		            model.addColumn("potrošeno");
		            model.addColumn("kartica lojalnosti");

		            int brojac = 1;
		            for (Klijent k : Klijent.mapa.values()) {
		            	String lojalnost = "NE";
		            	if (k.isKarticaLojalnosti()) {
		            		lojalnost = "DA";
		            	}
			            model.addRow(new Object[]{brojac, k.getKorisnickoIme(), k.getIme(), k.getPrezime(), k.getPotrosenaSuma() + " RSD", lojalnost});
			            brojac++;
		            }
		            table.getColumnModel().getColumn(0).setPreferredWidth(5);
		            table.setPreferredScrollableViewportSize(new Dimension(520, 150));

		            JScrollPane scrollPane = new JScrollPane(table);
		            dialog.add(scrollPane);
		            
		            dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);

				}
			}
		});
	}
}
