package view;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;

import model.Ruta;
import model.SlojZraka;
import controller.IzracunIspis;
import controller.Prelet;
import controller.UpravljanjePodacima;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;



public class GlavniProzor {
	private static Prelet prelet = new Prelet();
	private static UpravljanjePodacima podaci = new UpravljanjePodacima();
	private static SlojZraka meteoRucno = new SlojZraka();
	private static Ruta ruta = new Ruta();
	private static String datumPreleta = "2014-06-01";

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		podaci.procitajRuteBaza();
		podaci.procitajTockeBaza();
		podaci.procitajZrakoploveBaza();
		
		
			
		
		final Display display = Display.getDefault();
		final Shell shlRacunarPreleta = new Shell();
		shlRacunarPreleta.setMinimumSize(new Point(940, 670));
		shlRacunarPreleta.setSize(940, 672);
		shlRacunarPreleta.setText("Racunar Preleta 3.1");
		shlRacunarPreleta.setLayout(null);
		final List listZrakoplovi = new List(shlRacunarPreleta, SWT.BORDER);
		final List listRute = new List(shlRacunarPreleta, SWT.BORDER);
		
		
		Menu menu = new Menu(shlRacunarPreleta, SWT.BAR);
		shlRacunarPreleta.setMenuBar(menu);
		
		MenuItem mntmTocke = new MenuItem(menu, SWT.CASCADE);
		mntmTocke.setText("Tocke");
		
		Menu menu_1 = new Menu(mntmTocke);
		mntmTocke.setMenu(menu_1);
		
		MenuItem mntmUvozTocke = new MenuItem(menu_1, SWT.NONE);
		mntmUvozTocke.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//odabir datoteke
				FileDialog dialog = new FileDialog (shlRacunarPreleta, SWT.OPEN);
				String [] filterNames = new String [] {"Tocke datoteka", "All Files (*)"};
				String [] filterExtensions = new String [] {"*.cup;*.dat"};
				String filterPath = "/home";
				String platform = SWT.getPlatform();
				if (platform.equals("win32") || platform.equals("wpf")) {
					filterNames = new String [] {"Tocke datoteka", "All Files (*.*)"};
					filterExtensions = new String [] {"*.cup;*.dat"};
					filterPath = "c:\\";
				}
				dialog.setFilterNames (filterNames);
				dialog.setFilterExtensions (filterExtensions);
				dialog.setFilterPath (filterPath);
				dialog.setFileName ("datoteka");
				
				String putanja = ( dialog.open());
				System.out.println ("Otvori: " + putanja);
				try {
					podaci.procitajTockeDatoteka(putanja);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				podaci.pretvoriOkretneTocke();
				podaci.spremiTockeBaza();
				
//				while (!shlRacunarPreleta.isDisposed ()) {
//					if (!display.readAndDispatch ()) display.sleep ();
//				}
				
			}
		});
		mntmUvozTocke.setText("uvoz");
		
		MenuItem mntmIzvozTocke = new MenuItem(menu_1, SWT.NONE);
		mntmIzvozTocke.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog (shlRacunarPreleta, SWT.SAVE);
				String [] filterNames = new String [] {"Tocke datoteka", "All Files (*)"};
				String [] filterExtensions = new String [] {"*.cup;*.dat"};
				String filterPath = "/home";
				String platform = SWT.getPlatform();
				if (platform.equals("win32") || platform.equals("wpf")) {
					filterNames = new String [] {"Tocke datoteka", "All Files (*.*)"};
					filterExtensions = new String [] {"*.cup;*.dat"};
					filterPath = "c:\\";
				}
				dialog.setFilterNames (filterNames);
				dialog.setFilterExtensions (filterExtensions);
				dialog.setFilterPath (filterPath);
				dialog.setFileName ("TockeRP");
				podaci.procitajTockeBaza();
				String putanja =  (dialog.open());
				try {
					podaci.spremiTockeDatoteka(putanja);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println ("Snimi tamo: " + putanja);
//				while (!shlRacunarPreleta.isDisposed ()) {
//					if (!display.readAndDispatch ()) display.sleep ();
//				}
			}
		});
		mntmIzvozTocke.setText("izvoz");
		
		
		MenuItem mntmRute = new MenuItem(menu, SWT.CASCADE);
		mntmRute.setText("Rute");
		
		Menu menu_2 = new Menu(mntmRute);
		mntmRute.setMenu(menu_2);
		
		MenuItem mntmUredjivanjeRute = new MenuItem(menu_2, SWT.NONE);
		mntmUredjivanjeRute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Rute_Uredjivanje ruteUredjivanje = new Rute_Uredjivanje(shlRacunarPreleta,SWT.DIALOG_TRIM);
				ruteUredjivanje.open();
				

					ruteUredjivanje.zatvoriProzor();
					podaci.procitajRuteBaza();
					
					listRute.removeAll();
					for (int i = 0; i < podaci.getListRuta().size(); i++) {
						listRute.add(podaci.getListRuta().get(i).getImeRute());
					}

				
				
				
				
				
			}
		});

		mntmUredjivanjeRute.setText("uredjivanje");
		
		
		MenuItem mntmZrakoplovi = new MenuItem(menu, SWT.CASCADE);
		mntmZrakoplovi.setText("Zrakoplovi");
		
		Menu menu_3 = new Menu(mntmZrakoplovi);
		mntmZrakoplovi.setMenu(menu_3);
		
		MenuItem mntmUvozZrakoplovi = new MenuItem(menu_3, SWT.NONE);
		mntmUvozZrakoplovi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog (shlRacunarPreleta, SWT.OPEN);
				String [] filterNames = new String [] {"Polare", "All Files (*)"};
				String [] filterExtensions = new String [] {"*.plr;*.dat"};
				String filterPath = "/home";
				String platform = SWT.getPlatform();
				if (platform.equals("win32") || platform.equals("wpf")) {
					filterNames = new String [] {"Polare", "All Files (*.*)"};
					filterExtensions = new String [] {"*.plr;*.dat"};
					filterPath = "c:\\";
				}
				dialog.setFilterNames (filterNames);
				dialog.setFilterExtensions (filterExtensions);
				dialog.setFilterPath (filterPath);
				dialog.setFileName ("datoteka");
				
				String putanja = ( dialog.open());
				System.out.println ("Otvori PolareRP: " + putanja);

				try {
					podaci.procitajZrakoplovDatoteka(putanja);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				podaci.spremiZrakoploveBaza();
				podaci.procitajZrakoploveBaza();
				listZrakoplovi.removeAll();
				for (int i=0; i < podaci.getListaZrakoplov().size(); i++ ){
					
					listZrakoplovi.add(podaci.getListaZrakoplov().get(i).getNaziv());			
				}
				
			}
		});
		mntmUvozZrakoplovi.setText("uvoz");
		
		MenuItem mntmIzvozZrakoplovi = new MenuItem(menu_3, SWT.NONE);
		mntmIzvozZrakoplovi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog (shlRacunarPreleta, SWT.SAVE);
				String [] filterNames = new String [] {"PolareRP datoteka", "All Files (*)"};
				String [] filterExtensions = new String [] {"*.plr;*.dat"};
				String filterPath = "/home";
				String platform = SWT.getPlatform();
				if (platform.equals("win32") || platform.equals("wpf")) {
					filterNames = new String [] {"PolareRP datoteka", "All Files (*.*)"};
					filterExtensions = new String [] {"*.plr;*.dat"};
					filterPath = "c:\\";
				}
				dialog.setFilterNames (filterNames);
				dialog.setFilterExtensions (filterExtensions);
				dialog.setFilterPath (filterPath);
				dialog.setFileName ("PolareRP");
				podaci.procitajZrakoploveBaza();
				String putanja =  (dialog.open());
				try {
					podaci.spremiZrakoplovDatoteka(putanja);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println ("Snimi PolareRP: " + putanja);
				
				
			}
		});
		mntmIzvozZrakoplovi.setText("izvoz");
		
		MenuItem mntmUredjivanjeZrakoplovi = new MenuItem(menu_3, SWT.NONE);
		mntmUredjivanjeZrakoplovi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				Zrakoplov_Uredjivanje zrakoplovaUredjivanje = new Zrakoplov_Uredjivanje(shlRacunarPreleta, SWT.DIALOG_TRIM);
				zrakoplovaUredjivanje.setListaPolara(podaci.getListaZrakoplov());
				zrakoplovaUredjivanje.open();
				podaci.setListaZrakoplov(zrakoplovaUredjivanje.getListaPolara());
				zrakoplovaUredjivanje.zatvoriProzor();
				listZrakoplovi.removeAll();
				// ubacivanje prikaz
			    for (int i = 0; i< podaci.getListaZrakoplov().size(); i++) {
			    	listZrakoplovi.add(podaci.getListaZrakoplov().get(i).getNaziv());
			    }
				
				
				

			}
		});
		// TODO 
		mntmUredjivanjeZrakoplovi.setText("uredjivanje");
		
		MenuItem mntmOdrzavanje = new MenuItem(menu, SWT.CASCADE);
		mntmOdrzavanje.setText("Odrzavanje");
		
		Menu menu_4 = new Menu(mntmOdrzavanje);
		mntmOdrzavanje.setMenu(menu_4);
		
		MenuItem mntmBrisanjePodatakaIz = new MenuItem(menu_4, SWT.NONE);
		mntmBrisanjePodatakaIz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				listRute.removeAll();
				listZrakoplovi.removeAll();
				podaci.ciscenjeCijelaBaze();
				}
		});
		mntmBrisanjePodatakaIz.setText("Brisanje podataka iz baze");
		
		MenuItem mntmUcitavanjeZadanihPodataka = new MenuItem(menu_4, SWT.NONE);
		mntmUcitavanjeZadanihPodataka.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					podaci.punjenjeBaze();
					
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// TODO 
		mntmUcitavanjeZadanihPodataka.setText("Ucitavanje zadanih podataka");
		
		// lblGlavniProzorIspis - Ispis poruka u glavnom prozoru
		
		final Label lblGlavniProzorIspis = new Label(shlRacunarPreleta, SWT.NONE);

		lblGlavniProzorIspis.setText("Racunar Preleta v3.1");
		lblGlavniProzorIspis.setBounds(503, 10, 396, 362);
		

		
		
		
		// GUIlista ruta  

		listRute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i =0;
				Ruta zaispis = new Ruta();
				String ispisRute ="\nRuta :";
				zaispis= podaci.getListRuta().get(listRute.getSelectionIndex());
				ispisRute= ispisRute+zaispis.getImeRute();
				for (i =0;i<zaispis.getTockeRute().size();i++){
					ispisRute = ispisRute +"\n"+(i+1)+".tocka "+zaispis.getTockeRute().get(i).getNaziv();					
				}
				
				lblGlavniProzorIspis.setText(ispisGlavniProzor()+ispisRute);
				
			}
		});
		listRute.setBounds(25, 10, 219, 362);
		listRute.setToolTipText("Rute");
		// ODO slekcija i ispis tocaka u prpozoru
		for (int i=0; i < podaci.getListRuta().size(); i++ ){
			listRute.add(podaci.getListRuta().get(i).getImeRute());			
		}
		
		// GUIlista zrakoplova

		// ODO slekcija i ispis  u prpozoru
		listZrakoplovi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lblGlavniProzorIspis.setText(ispisGlavniProzor()+"\n\nZrakoplov: "+(podaci.getListaZrakoplov().get(listZrakoplovi.getSelectionIndex()).getNaziv()));
			}
		});
		listZrakoplovi.setBounds(250, 10, 219, 362);
		listZrakoplovi.setToolTipText("Zrakoplovi");
		for (int i=0; i < podaci.getListaZrakoplov().size(); i++ ){
			listZrakoplovi.add(podaci.getListaZrakoplov().get(i).getNaziv());			
		}
		
		
		final DateTime dateVrijeme = new DateTime(shlRacunarPreleta, SWT.BORDER | SWT.TIME | SWT.SHORT);
		dateVrijeme.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//int sekundeDana= dateVrijeme.getHours()*3600 + dateVrijeme.getMinutes()*60;
				prelet.setVrijemeStarta(dateVrijeme.getHours());
				lblGlavniProzorIspis.setText(ispisGlavniProzor() +"\nt:"+ (prelet.getVrijemeStarta()/3600));
			}
		});
		
		
		// ODO prelet set time
		dateVrijeme.setToolTipText("vrijeme pocetka");
		dateVrijeme.setBounds(507, 395, 87, 29);
		
		final DateTime dateDatumPreleta = new DateTime(shlRacunarPreleta, SWT.BORDER | SWT.DROP_DOWN);
		// TODO prelet set date
		dateDatumPreleta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				datumPreleta= dateDatumPreleta.getYear()+"-"+(dateDatumPreleta.getMonth()+1)+"-"+dateDatumPreleta.getDay();
				prelet.setDatumPreleta(datumPreleta);
				lblGlavniProzorIspis.setText(ispisGlavniProzor());
				
			}
		});
		dateDatumPreleta.setToolTipText("datum preleta");
		dateDatumPreleta.setBounds(600, 395, 87, 29);
		
		Button btnPrikazIzracuna = new Button(shlRacunarPreleta, SWT.NONE);
		// TODO ispitivanje podataka
		// TODO pokretanje izracuna
		// TODO otvaranje novog prozora
		btnPrikazIzracuna.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					IzracunPrikaz izracunPrikaz = new IzracunPrikaz(
							shlRacunarPreleta, SWT.DIALOG_TRIM);
					izracunPrikaz.setIspisPreleta(prelet.getIspisPreleta());
					izracunPrikaz.setRutaPreletaIzracun(prelet
							.getRutaPreletaIzracun());
					izracunPrikaz.open();
					izracunPrikaz.zatvoriProzor();
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlRacunarPreleta, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Prvo pokreni izracun!"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}


			}
		});
		btnPrikazIzracuna.setBounds(709, 395, 190, 29);
		btnPrikazIzracuna.setText("prikaz izracuna");
		
		Button btnPotvrdaRute = new Button(shlRacunarPreleta, SWT.NONE);
		btnPotvrdaRute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					int kursor = listRute.getSelectionIndex();
					prelet.setRuta(podaci.getListRuta().get(kursor));
					ruta = prelet.getRuta();
					lblGlavniProzorIspis.setText(ispisGlavniProzor());
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlRacunarPreleta, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Prvo odaberi rutu!"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}				
			}
		});
		// ODO upis odabranog u prelet
		btnPotvrdaRute.setBounds(25, 395, 219, 24);
		btnPotvrdaRute.setText("potvrda rute");
		
		Button btnPotZrakoplov = new Button(shlRacunarPreleta, SWT.NONE);
		btnPotZrakoplov.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					int kursor = listZrakoplovi.getSelectionIndex();
					prelet.setZrakoplov(podaci.getListaZrakoplov().get(kursor));
					lblGlavniProzorIspis.setText(ispisGlavniProzor());
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlRacunarPreleta, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Prvo odaberi zrakoplov!"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}
			}
		});
		// ODO upis odabranog u prelet
		btnPotZrakoplov.setBounds(250, 395, 219, 24);
		btnPotZrakoplov.setText("potvrda zrakoplova");
		
		final Scale scaleTempZraka = new Scale(shlRacunarPreleta, SWT.NONE);
		scaleTempZraka.setPageIncrement(1);

		scaleTempZraka.setMaximum(100);
		scaleTempZraka.setMinimum(0);
		scaleTempZraka.setSelection(65);
		scaleTempZraka.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int teZr = (scaleTempZraka.getSelection()-40);
				meteoRucno.setTemperaturaZraka(teZr);
				lblGlavniProzorIspis.setText(ispisGlavniProzor());
				
			}
		});
		scaleTempZraka.setToolTipText("temperatura zraka");
		scaleTempZraka.setBounds(25, 451, 184, 27);
		
		final Scale scaleTempRose = new Scale(shlRacunarPreleta, SWT.NONE);
		scaleTempRose.setPageIncrement(1);
		scaleTempRose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int teRo = (scaleTempRose.getSelection()-40);
				meteoRucno.setTemperaturaRose(teRo);
				lblGlavniProzorIspis.setText(ispisGlavniProzor());
				
			}
		});

		scaleTempRose.setMaximum(100);
		scaleTempRose.setMinimum(0);
		scaleTempRose.setSelection(50);
		scaleTempRose.setToolTipText("tepmperatura rose");
		scaleTempRose.setBounds(25, 530, 184, 27);
		
		final Scale scaleVjetarBrzina = new Scale(shlRacunarPreleta, SWT.NONE);
		scaleVjetarBrzina.setPageIncrement(2);
		scaleVjetarBrzina.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int vjeBr = scaleVjetarBrzina.getSelection();
				meteoRucno.setVjetarBrzina(vjeBr);
				lblGlavniProzorIspis.setText(ispisGlavniProzor());
			}
		});

		scaleVjetarBrzina.setMaximum(55);
		scaleVjetarBrzina.setToolTipText("brzina vjetra");
		scaleVjetarBrzina.setBounds(250, 451, 184, 27);
		
		final Scale scaleVjetarSmjer = new Scale(shlRacunarPreleta, SWT.BORDER);
		scaleVjetarSmjer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int vjeSmj = (scaleVjetarSmjer.getSelection());
				meteoRucno.setVjetarSmjer(vjeSmj);
				lblGlavniProzorIspis.setText(ispisGlavniProzor());
			}
		});

		scaleVjetarSmjer.setMaximum(359);
		scaleVjetarSmjer.setSelection(180);
		scaleVjetarSmjer.setToolTipText("smjer vjetra");
		scaleVjetarSmjer.setBounds(250, 530, 184, 27);
		
		Label lblNewLabel = new Label(shlRacunarPreleta, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(25, 433, 184, 12);
		lblNewLabel.setText("-35°C     -   temperatura zraka °C  -    55°C");
		
		Label lblTemperaturaRose = new Label(shlRacunarPreleta, SWT.NONE);
		lblTemperaturaRose.setAlignment(SWT.CENTER);
		lblTemperaturaRose.setBounds(25, 512, 184, 12);
		lblTemperaturaRose.setText("-35°C     -   temperatura rose °C  -    55°C");
		
		Label lblBrzinaVjetra = new Label(shlRacunarPreleta, SWT.NONE);
		lblBrzinaVjetra.setBounds(250, 433, 190, 12);
		lblBrzinaVjetra.setText("0km/h     -  brzina vjetra km/h  -    55km/h");
		
		Label lblSmjerVjetra = new Label(shlRacunarPreleta, SWT.NONE);
		lblSmjerVjetra.setAlignment(SWT.CENTER);
		lblSmjerVjetra.setBounds(250, 512, 190, 12);
		lblSmjerVjetra.setText("0°       -      smjer vjetra ° (0-359)      -      359°");
		
		Button btnRunuUnosMeteorolokih = new Button(shlRacunarPreleta, SWT.NONE);
		btnRunuUnosMeteorolokih.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				prelet.setMeteoRucno(meteoRucno);
				long vrijemeStarta = (dateVrijeme.getHours());
				prelet.setVrijemeStarta(vrijemeStarta);
				lblGlavniProzorIspis.setText(ispisGlavniProzor()+"\nOdabran je rucuni unos meteo parametrara!!!");
						
				
			}
		});
		btnRunuUnosMeteorolokih.setBounds(503, 530, 184, 24);
		btnRunuUnosMeteorolokih.setText("ručni unos ");
		
		Label lblVrijemIDatum = new Label(shlRacunarPreleta, SWT.NONE);
		lblVrijemIDatum.setAlignment(SWT.CENTER);
		lblVrijemIDatum.setBounds(503, 377, 174, 12);
		lblVrijemIDatum.setText("vrijeme i datum polijetanja");
		
		Button btnPreuzimanjeGfs = new Button(shlRacunarPreleta, SWT.NONE);
		btnPreuzimanjeGfs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//poziva se funkcija i povratna poruka se ispisuje
				lblGlavniProzorIspis.setText("\n\nPreuzimanje GFS podataka je pokrenuto!\nMolimo pričekajte...\n\n");
				
				podaci.setRuta(ruta);
				podaci.setDatum(datumPreleta);
				prelet.setMeteoRucno(null);
	
				
				if (podaci.getRuta().getTockeRute().size()!=0 && podaci.getDatum()!=null){
								
				try {
					
					String porukaGFS = podaci.spremiGFS();
					lblGlavniProzorIspis.setText("\n\n"+porukaGFS);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else{
					lblGlavniProzorIspis.setText("Preuzimanje GFS nije moguće bez odabrane rute i datuma preleta!");
				}
				
			}
		});
		btnPreuzimanjeGfs.setGrayed(true);
		btnPreuzimanjeGfs.setBounds(503, 451, 184, 55);
		btnPreuzimanjeGfs.setText("preuzimanje GFS");
		
		
		Label lblRute = new Label(shlRacunarPreleta, SWT.NONE);
		lblRute.setBounds(25, 377, 49, 12);
		lblRute.setText("Rute");
		
		Label lblZrakoplovi = new Label(shlRacunarPreleta, SWT.NONE);
		lblZrakoplovi.setBounds(250, 378, 49, 12);
		lblZrakoplovi.setText("Zrakoplovi");
		
		Label lblZaRuniUnos = new Label(shlRacunarPreleta, SWT.NONE);
		lblZaRuniUnos.setBounds(503, 560, 184, 38);
		lblZaRuniUnos.setText("za ručni unos:\npodesi parametre i potvrdi tipkom");
		
		Label lblDoTriDana = new Label(shlRacunarPreleta, SWT.NONE);
		lblDoTriDana.setAlignment(SWT.CENTER);
		lblDoTriDana.setBounds(503, 430, 184, 12);
		lblDoTriDana.setText("do tri dana od unaprijed");
		
		Button btnOsvjeiPodatkeIz = new Button(shlRacunarPreleta, SWT.NONE);
		btnOsvjeiPodatkeIz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
					prelet = new Prelet();
					podaci = new UpravljanjePodacima();
					meteoRucno = new SlojZraka();
					ruta = new Ruta();
					listRute.removeAll();
					podaci.procitajRuteBaza();
					for (int i=0; i < podaci.getListRuta().size(); i++ ){
					
						listRute.add(podaci.getListRuta().get(i).getImeRute());			
					}
					listZrakoplovi.removeAll();
					podaci.procitajZrakoploveBaza();
					for (int i=0; i < podaci.getListaZrakoplov().size(); i++ ){
						
						listZrakoplovi.add(podaci.getListaZrakoplov().get(i).getNaziv());			
					}
					lblGlavniProzorIspis.setText(ispisGlavniProzor());
								
			}
		});
		btnOsvjeiPodatkeIz.setBounds(709, 530, 190, 24);
		btnOsvjeiPodatkeIz.setText("novi izracun");
		
		Button btnPokreniIzracun = new Button(shlRacunarPreleta, SWT.NONE);
		btnPokreniIzracun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (prelet.isSemafor()==true){ 
					prelet.setDatumPreleta(datumPreleta);
					prelet.setRuta(ruta);
					prelet.IzracunPrelet();
					IzracunIspis izracunIspis = new IzracunIspis(prelet.getRutaPreletaIzracun());
					podaci.upisTXTDatoteka("prelet.txt", izracunIspis.getIspisPreleta());
					lblGlavniProzorIspis.setText(prelet.getIspisPreleta());
					}
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlRacunarPreleta, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Izracun nije izvediv,\nprovjeri podatke!"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}
			}
		});
		btnPokreniIzracun.setBounds(709, 451, 190, 55);
		btnPokreniIzracun.setText("pokretanje izracuna ");
		
		Button btnIzlaz = new Button(shlRacunarPreleta, SWT.NONE);
		btnIzlaz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shlRacunarPreleta.dispose();
			}
		});
		btnIzlaz.setBounds(830, 574, 69, 24);
		btnIzlaz.setText("izlaz");

		shlRacunarPreleta.open();
		shlRacunarPreleta.layout();
		while (!shlRacunarPreleta.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
	}
	public static String ispisGlavniProzor(){
		
		String tekstPocetni = ("Racunar preleta v3.1\nProgram za izračun mogućnosti preleta u jedriličarskom letenju \nprema zadanim meteorološkim uvjetima."
				+ "\nKoriste se meteorološki parametri GFS modela ili ručno uneseni."
				+ "\nGFS podaci se preuzimaju sa poslužitelja ESRL/GSD:"
				+ " \n http://rucsoundings.noaa.gov/\n");
		
		if ( ruta.getImeRute()==null){
			tekstPocetni=tekstPocetni
					+ "\nPo odabiru rute se može pokrenuti preuzimanje podataka."
					+ "\nRucni unos meteoroloskih parametara je pomoću klizača."
					+ "\nOdaberi rutu i potvrdi tipkom.\n";		
		}else{ 
			String rutaOdabrana = "\nOdabrana ruta je "+ruta.getImeRute();
			tekstPocetni=tekstPocetni + rutaOdabrana;
		}
				
		if (prelet.getZrakoplov()==null){
			tekstPocetni=tekstPocetni
					+ "\nOdaberi zrakoplov i potvrdi tipkom.";
			
		}else{
			String zrakoplovOdabrani = "\nOdabrani zrakoplov je "+prelet.getZrakoplov().getNaziv();
			tekstPocetni=tekstPocetni+zrakoplovOdabrani;
		}
		
		if(meteoRucno.getTemperaturaRose()!=0 || meteoRucno.getTemperaturaZraka()!=0 || meteoRucno.getVjetarBrzina()!=0 || meteoRucno.getVjetarSmjer()!=0){
			String meteoRucni = ("\nUnijeti meteorološki parametri preleta:"
					+ "\nTemperatura zraka:"+meteoRucno.getTemperaturaZraka()
					+"°C\nTemperatura rose"+meteoRucno.getTemperaturaRose()
					+"°C\nBrzina vjetra:"+meteoRucno.getVjetarBrzina()
					+"km/h\nSmjer vjetra:"+meteoRucno.getVjetarSmjer());
			tekstPocetni=tekstPocetni+meteoRucni;
		}
		
		if(prelet.getDatumPreleta()==null){
			tekstPocetni=tekstPocetni+"\nUnesi datum planiranog preleta."; 
		
		}else{
			String datumOdabrani= "\n\nDatum je "+prelet.getDatumPreleta();
			tekstPocetni=tekstPocetni+datumOdabrani;
		}
		if(prelet.getVrijemeStarta()<1){

			tekstPocetni=tekstPocetni+"\nPotrebno je i vrijeme starta.";
		}else{
			int sati = (int) Math.floor(prelet.getVrijemeStarta()/3600); 
			String vrijemeStart = ("\n\nVrijeme polijetanja: "+sati+" sati. ");
			tekstPocetni=tekstPocetni+vrijemeStart;
		}
		
		
		return tekstPocetni;
		
		
		
	}
	public static Date stringDatum(String datum) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		Date d = formatter.parse(datum);
		return d;
	}
	
}
