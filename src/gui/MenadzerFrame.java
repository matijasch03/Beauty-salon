package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.markers.SeriesMarkers;

import korisnici.Klijent;
import korisnici.Kozmeticar;
import korisnici.Menadzer;
import menadzeri.MenadzerMenadzer;
import net.miginfocom.swing.MigLayout;
import tretmani.StatusTretmana;
import tretmani.TipTretmana;
import tretmani.Usluga;
import tretmani.ZakazaniTretman;

public class MenadzerFrame {
	private Menadzer men;
	
	public MenadzerFrame(Menadzer men) {
		this.men = men;
		MainFrame mf = new MainFrame(men);
		createMenadzerFrame(mf);
	}

	public Menadzer getMen() {
		return men;
	}

	public void setMen(Menadzer men) {
		this.men = men;
	}
	
	private void createMenadzerFrame(MainFrame mf) {
		JMenuBar menu = new JMenuBar();
		JMenu sviKozmeticari = new JMenu("Svi kozmetičari");
		JMenu sviKlijenti = new JMenu("Svi klijenti");
		JMenu zakazani = new JMenu("Statusi tretmana");
		JMenu usluge = new JMenu("Svi tretmani");
		JMenu grafikoni = new JMenu("Grafikoni");

		menu.add(sviKozmeticari);
		menu.add(sviKlijenti);
		menu.add(zakazani);
		menu.add(usluge);
		menu.add(grafikoni);
		
		JMenuItem prihodiPoTretmanima = new JMenuItem("Prihodi po tertmanima");
		JMenuItem angazmanKozmeticara = new JMenuItem("Angažman kozmetičara");
		JMenuItem statusiTretmana = new JMenuItem("Statusi tretmana");
		
		prihodiPoTretmanima.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	double[] dani = new double[5];
            	double prviDan = 2;
            	for (int i = 0; i < 5; i++) {
            		dani[i] = prviDan + i;
            	}
            	double[][] prihodi = new double[4][5];
            	
            	for (int i = 0; i < 4; i++) {
            		for (int j = 0; j < 5; j++) {
            			prihodi[i][j] = 0;
            		}
            	}
            	
            	for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
            		if (z.zakazanoVreme.isBefore(LocalDateTime.of(2023, 9, 7, 0, 0))) {
            			prihodi[z.getTip().ordinal()][z.zakazanoVreme.getDayOfMonth() - 2] += z.cena;
            			prihodi[3][z.zakazanoVreme.getDayOfMonth() - 2] += z.cena;
            		}
            	}

                    XYChart chart = new XYChartBuilder()
                            .width(800)
                            .height(400)
                            .title("Prihodi po danima septembra")
                            .xAxisTitle("Dani")
                            .yAxisTitle("Prihodi")
                            .theme(Styler.ChartTheme.GGPlot2)
                            .build();

                    for (int i = 0; i < 3; i++) {
                        chart.addSeries(TipTretmana.values()[i].toString(), dani, prihodi[i])
                                .setMarker(SeriesMarkers.CIRCLE)
                                .setMarkerColor(XChartSeriesColors.BLUE);
                    }
                    
                    chart.addSeries("UKUPNO", dani, prihodi[3])
                    .setMarker(SeriesMarkers.CIRCLE)
                    .setMarkerColor(XChartSeriesColors.BLUE);

                    JPanel chartPanel = new XChartPanel<>(chart);

                    JFrame chartFrame = new JFrame("Prihodi po tertmanima");
                    chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chartFrame.getContentPane().add(chartPanel, BorderLayout.CENTER);
                    chartFrame.pack();
                    chartFrame.setLocationRelativeTo(null);
                    chartFrame.setVisible(true);
            }
        });
		
		statusiTretmana.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PieChart chart = new PieChartBuilder().width(800).height(600).title("Statusi kozmetičkih tretmana u prošlom mesecu").build();
                chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
                
                HashMap<StatusTretmana, Integer> statusi = new HashMap<>();
	            for (StatusTretmana s : StatusTretmana.values()) {
	            	statusi.put(s, 0);
	            }
	            for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
	            	statusi.put(z.status, statusi.get(z.status) + 1);
	            	
	            }
	            
	            for (StatusTretmana s : statusi.keySet()) {
	                chart.addSeries(s.toString(), statusi.get(s));
	            }
                
                JPanel chartPanel = new XChartPanel<>(chart);
                
                JFrame chartFrame = new JFrame("Statusi tretmana");
                chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                chartFrame.getContentPane().add(chartPanel, BorderLayout.CENTER);
                chartFrame.pack();
                chartFrame.setLocationRelativeTo(null);
                chartFrame.setVisible(true);
            }
        });
		
		angazmanKozmeticara.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PieChart chart = new PieChartBuilder().width(800).height(600).title("Opterećenje kozmetičara u prošlom mesecu").build();
                chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
                
                HashMap<Kozmeticar, Integer> izvrseniTretmani = new HashMap<>();
	            for (Kozmeticar k : Kozmeticar.mapa.values()) {
	            	izvrseniTretmani.put(k, 0);
	            }
	            for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
	            	if (z.status == StatusTretmana.IZVRŠEN) {
	            		izvrseniTretmani.put(z.kozmeticar, izvrseniTretmani.get(z.kozmeticar) + 1);
	            	}
	            }
	            
	            for (Kozmeticar k : izvrseniTretmani.keySet()) {
	                chart.addSeries(k.getKorisnickoIme(), izvrseniTretmani.get(k));
	            }
                
                JPanel chartPanel = new XChartPanel<>(chart);
                
                JFrame chartFrame = new JFrame("Angažovanost kozmetičara");
                chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                chartFrame.getContentPane().add(chartPanel, BorderLayout.CENTER);
                chartFrame.pack();
                chartFrame.setLocationRelativeTo(null);
                chartFrame.setVisible(true);
            }
        });


		grafikoni.add(prihodiPoTretmanima);
		grafikoni.add(angazmanKozmeticara);
		grafikoni.add(statusiTretmana);
		
		
		RecepcionerFrame.sviKlijentKlik(sviKlijenti);
		sviKozmeticariKlik(sviKozmeticari);
		zakazaniKlik(zakazani);
		sviTretmaniKlik(usluge);
		
		mf.setJMenuBar(menu);
		
	}
	
	private void sviKozmeticariKlik(JMenu sviKoz) {
		sviKoz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Svi kozmetičari");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setLayout(new MigLayout("wrap 1", "15[][]", "10[]10"));
					
					JLabel natpis = new JLabel("Ovde imate uvid u sve kozmetičare i njihove podatke.");
					dialog.add(natpis);
					
					DefaultTableModel model = new DefaultTableModel();
		            JTable table = new JTable(model);

		            model.addColumn("#");
		            model.addColumn("korisničko ime");
		            model.addColumn("ime");
		            model.addColumn("prezime");
		            model.addColumn("doprinos");
		            model.addColumn("izvršeni tretmani");
		            
		            HashMap<Kozmeticar, Integer> izvrseniTretmani = new HashMap<>();
		            for (Kozmeticar k : Kozmeticar.mapa.values()) {
		            	izvrseniTretmani.put(k, 0);
		            }
		            for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
		            	if (z.status == StatusTretmana.IZVRŠEN) {
		            		izvrseniTretmani.put(z.kozmeticar, izvrseniTretmani.get(z.kozmeticar) + 1);
		            	}
		            }

		            int brojac = 1;
		            for (Kozmeticar k : Kozmeticar.mapa.values()) {
		            	
			            model.addRow(new Object[]{brojac, k.getKorisnickoIme(), k.getIme(), k.getPrezime(), k.getZaradio() + " RSD", izvrseniTretmani.get(k)});
			            brojac++;
		            }
		            table.getColumnModel().getColumn(0).setPreferredWidth(5);
		            table.setPreferredScrollableViewportSize(new Dimension(380, 150));

		            JScrollPane scrollPane = new JScrollPane(table);
		            dialog.add(scrollPane);
		            
		            dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);

				}
			}
		});
	}
	
	private void zakazaniKlik(JMenu zak) {
		zak.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Zakazani tretmani");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setLayout(new MigLayout("wrap 2", "15[]20[]", "10[]10"));
					dialog.setSize(500, 500);
					
					JLabel natpis = new JLabel("Ovde imate uvid u sve statuse tretmana");
					JLabel natpis2 = new JLabel("u izabranom vremenskom intervalu. (format: DD.MM.GGGG)");

					dialog.add(natpis, "span 2");
					dialog.add(natpis2, "span 2");

					dialog.add(new JLabel("Od"), "split 2");
					JTextField odTf = new JTextField(10);
					dialog.add(odTf);
					dialog.add(new JLabel("Do"), "split 2");
					JTextField doTf = new JTextField(10);
					dialog.add(doTf);
					JButton prikaz = new JButton("Prikaži");
					dialog.add(prikaz, "width 150px, height 30px, span 2");
					
					prikaz.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
							String odStr = odTf.getText().trim();
							String doStr = doTf.getText().trim();
					        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
					        
					        try {
					        	 LocalDate odDate = LocalDate.parse(odStr, formatter);
							     LocalDate doDate = LocalDate.parse(doStr, formatter);
							     HashMap<StatusTretmana, Integer> statusi = new HashMap<>();
						            for (StatusTretmana s : StatusTretmana.values()) {
						            	statusi.put(s, 0);
						            }
						            for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
						            	if (z.zakazanoVreme.toLocalDate().isAfter(odDate) && z.zakazanoVreme.toLocalDate().isBefore(doDate)) {
						            		statusi.put(z.status, statusi.get(z.status) + 1);
						            	}
						            }

						            int brojac = 1;
						            for (StatusTretmana s : StatusTretmana.values()) {
						            	dialog.add(new JLabel(brojac + ". " + s + ": " + statusi.get(s)));
							            brojac++;
						            }
														            
					        } catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "Datum je unesen u pogrešnom formatu.", "Greška", JOptionPane.ERROR_MESSAGE);
					        }
				        	dialog.revalidate();
					        dialog.repaint();
						}
					});
					
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);
				}
			}
		});
	}
	
	private void sviTretmaniKlik(JMenu btn) {
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Svi tretmani");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setLayout(new MigLayout("wrap 2", "15[]30[]", "10[]10"));
					dialog.setSize(600, 600);
					
					JLabel natpis = new JLabel("Ovde imate uvid u sve tretmane i njihove podatke.");
					JLabel natpis2 = new JLabel("u izabranom vremenskom intervalu. (format: DD.MM.GGGG)");
					dialog.add(natpis, "span 2");
					dialog.add(natpis2, "span 2");

					
					DefaultTableModel model = new DefaultTableModel();
		            JTable table = new JTable(model);

		            model.addColumn("#");
		            model.addColumn("naziv");
		            model.addColumn("tip");
		            model.addColumn("broj tretmana");
		            model.addColumn("prihodi");
		            
		            dialog.add(new JLabel("Od"), "split 2");
					JTextField odTf = new JTextField(10);
					dialog.add(odTf);
					dialog.add(new JLabel("Do"), "split 2");
					JTextField doTf = new JTextField(10);
					dialog.add(doTf);
					JButton prikaz = new JButton("Prikaži");
					dialog.add(prikaz, "width 150px, height 30px, span 2");
					
					prikaz.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
							String odStr = odTf.getText().trim();
							String doStr = doTf.getText().trim();
					        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
							boolean greska = false;

					        try {
					        	 LocalDate odDate = LocalDate.parse(odStr, formatter);
							     LocalDate doDate = LocalDate.parse(doStr, formatter);
							     
							     HashMap<String, Integer> usluge = new HashMap<>();
					             for (Usluga u : Usluga.mapa.values()) {
					             	 usluge.put(u.nazivUsluge, 0);
					             }
					             
					             HashMap<String, Integer> zarade = new HashMap<>();
					             for (Usluga u : Usluga.mapa.values()) {
					            	 zarade.put(u.nazivUsluge, 0);
					             }
					            
					             for (ZakazaniTretman z : ZakazaniTretman.mapa.values()) {
					            	 if (z.zakazanoVreme.toLocalDate().isAfter(odDate) && z.zakazanoVreme.toLocalDate().isBefore(doDate)) {
					            		 usluge.put(z.nazivUsluge, usluge.get(z.nazivUsluge) + 1);
							             zarade.put(z.nazivUsluge, zarade.get(z.nazivUsluge) + z.getCena());

					            	 }
					             }

					             int brojac = 1;
					             for (Usluga u : Usluga.mapa.values()) {
							            model.addRow(new Object[]{brojac, u.getNazivUsluge(), u.getTip().toString().toLowerCase(), usluge.get(u.nazivUsluge), zarade.get(u.nazivUsluge)});
							            brojac++;
						         }
														            
					        } catch (Exception e1) {
					        	greska = true;
					        	JOptionPane.showMessageDialog(null, "Datum je unesen u pogrešnom formatu.", "Greška", JOptionPane.ERROR_MESSAGE);
					        }
					        
					        if (!greska) {
						        table.getColumnModel().getColumn(0).setPreferredWidth(5);
					            table.setPreferredScrollableViewportSize(new Dimension(550, 150));
	
					            JScrollPane scrollPane = new JScrollPane(table);
					            dialog.add(scrollPane, "span 2");
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
}
