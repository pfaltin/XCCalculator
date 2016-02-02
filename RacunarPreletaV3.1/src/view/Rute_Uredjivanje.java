package view;

import java.util.ArrayList;
import java.util.Random;

import model.Ruta;
import model.Tocka;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import controller.UpravljanjePodacima;

public class Rute_Uredjivanje extends Dialog {

	protected Object result;
	protected Shell shlUredjivanjeRute;
	private Text textNazivRute;
	private ArrayList<Ruta> listaRutePodaci = new ArrayList<Ruta>();
	private ArrayList<Tocka> listaTockePodaci = new ArrayList<Tocka>(); 
	private ArrayList<Tocka> tockeZaUbacit = new ArrayList<Tocka>();
	private List ruteList;
	private List listTocke;
	private Ruta rutaAktivna = new Ruta();
	private Ruta rutaNova = new Ruta();
	private Tocka tockaAktivna;
	private Label lblRutaSadrzaj;
	private Text textNazivTocke;
	private Text textNMVisina;
	private Text textLat;
	private Text textLon;
	private UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Rute_Uredjivanje(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlUredjivanjeRute.open();
		shlUredjivanjeRute.layout();
		Display display = getParent().getDisplay();
		while (!shlUredjivanjeRute.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlUredjivanjeRute = new Shell(getParent(), getStyle());
		upravPodaci.procitajTockeBaza();
		upravPodaci.procitajRuteBaza();
		setListaRutePodaci(upravPodaci.getListRuta());
		setListaTockePodaci(upravPodaci.getListTocka());

		shlUredjivanjeRute.setSize(694, 421);
		shlUredjivanjeRute.setText("Uredjivanje rute");
		
		ruteList = new List(shlUredjivanjeRute, SWT.BORDER);
		ruteList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				rutaAktivna= listaRutePodaci.get(ruteList.getSelectionIndex());
				String zaIspis = "Naziv rute:\n"+rutaAktivna.getImeRute();
						for (int i=0; i < rutaAktivna.getTockeRute().size(); i++ ){
							zaIspis= zaIspis + "\n"+(i+1)+".tocka "+rutaAktivna.getTockeRute().get(i).getNaziv();			
						}
				lblRutaSadrzaj.setText(zaIspis);
			
						
				
			}
		});
		ruteList.setBounds(37, 50, 200, 132);
		for (int i=0; i < listaRutePodaci.size(); i++ ){
			ruteList.add(listaRutePodaci.get(i).getImeRute());			
		}
		
		Button btnBrisanjeRute = new Button(shlUredjivanjeRute, SWT.NONE);
		btnBrisanjeRute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				try {
					PotvrdiBrisanje potvrdiBrisanje = new PotvrdiBrisanje(
							shlUredjivanjeRute, SWT.DIALOG_TRIM);
					potvrdiBrisanje.setPoruka("Brisanje "
							+ listaRutePodaci.get(ruteList.getSelectionIndex())
									.getImeRute());
					potvrdiBrisanje.open();
					if (potvrdiBrisanje.isPotvrda() == true) {
						listaRutePodaci.remove(ruteList.getSelectionIndex());
						upravPodaci.setListRuta(listaRutePodaci);
						upravPodaci.spremiCRuteBaza();
						upravPodaci.procitajRuteBaza();
						setListaRutePodaci(upravPodaci.getListRuta());

						ruteList.removeAll();
						for (int i = 0; i < listaRutePodaci.size(); i++) {
							ruteList.add(listaRutePodaci.get(i).getImeRute());
						}

					}
					potvrdiBrisanje.ugasiProzor();
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlUredjivanjeRute, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Nema rute za brisanje"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}
				
				
			}
		});
		btnBrisanjeRute.setBounds(37, 188, 84, 23);
		btnBrisanjeRute.setText("BRISANJE");
		
		Button btnUnosRute = new Button(shlUredjivanjeRute, SWT.NONE);
		btnUnosRute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				rutaNova = new Ruta();
				tockeZaUbacit = new ArrayList<Tocka>();
				textNazivRute.setText("");
				
				//rutaNova.setImeRute(textNazivRute.getText());				
				
				lblRutaSadrzaj.setText("Upisi ime rute!\nOdaberi tocke!");
				
			}
		});
		btnUnosRute.setBounds(264, 329, 95, 34);
		btnUnosRute.setText("Unos rute");
		
		textNazivRute = new Text(shlUredjivanjeRute, SWT.BORDER);
		textNazivRute.setBounds(264, 50, 199, 24);
		
		listTocke = new List(shlUredjivanjeRute, SWT.BORDER | SWT.V_SCROLL);
		listTocke.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tockaAktivna= listaTockePodaci.get(listTocke.getSelectionIndex());
				textNazivTocke.setText(tockaAktivna.getNaziv());
				textNMVisina.setText(String.valueOf(tockaAktivna.getVisina()));
				textLat.setText(String.valueOf(tockaAktivna.getLat()));
				textLon.setText(String.valueOf(tockaAktivna.getLon()));
				
						
				
			}
		});
		listTocke.setBounds(264, 80, 199, 236);
		for (int i=0; i < listaTockePodaci.size(); i++ ){
			listTocke.add(listaTockePodaci.get(i).getNaziv());			
		}
		
		
		lblRutaSadrzaj = new Label(shlUredjivanjeRute, SWT.NONE);

		lblRutaSadrzaj.setBounds(37, 217, 200, 146);
		lblRutaSadrzaj.setText("Ruta:");
		
		Button btnUzmiTocku = new Button(shlUredjivanjeRute, SWT.NONE);
		btnUzmiTocku.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					tockeZaUbacit.add(tockaAktivna);
					String zaIspis = "Naziv rute: \n" + textNazivRute.getText();
					for (int i = 0; i < tockeZaUbacit.size(); i++) {
						zaIspis = zaIspis + "\n" + (i + 1) + ".tocka "
								+ tockeZaUbacit.get(i).getNaziv();
					}
					lblRutaSadrzaj.setText(zaIspis);
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlUredjivanjeRute, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Nedostaju podaci za unos tocke!"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}
			}
		});
		btnUzmiTocku.setBounds(469, 252, 95, 29);
		btnUzmiTocku.setText("Uzmi tocku");
		
		Button btnIzlaz = new Button(shlUredjivanjeRute, SWT.NONE);
		btnIzlaz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				zatvoriProzor();
			}
		});
		btnIzlaz.setBounds(10, 10, 68, 34);
		btnIzlaz.setText("Izlaz");
		
		textNazivTocke = new Text(shlUredjivanjeRute, SWT.BORDER);
		textNazivTocke.setBounds(504, 29, 140, 27);
		
		textNMVisina = new Text(shlUredjivanjeRute, SWT.BORDER);
		textNMVisina.setBounds(504, 88, 75, 27);
		
		textLat = new Text(shlUredjivanjeRute, SWT.BORDER);
		textLat.setBounds(504, 155, 140, 27);
		
		textLon = new Text(shlUredjivanjeRute, SWT.BORDER);
		textLon.setBounds(504, 214, 140, 27);
		
		Label lblNazivTocke = new Label(shlUredjivanjeRute, SWT.NONE);
		lblNazivTocke.setBounds(509, 10, 103, 17);
		lblNazivTocke.setText("naziv tocke");
		
		Label lblNewLabel = new Label(shlUredjivanjeRute, SWT.NONE);
		lblNewLabel.setBounds(504, 65, 120, 17);
		lblNewLabel.setText("n.m. visina");
		
		Label lblLatitude = new Label(shlUredjivanjeRute, SWT.NONE);
		lblLatitude.setBounds(504, 132, 70, 17);
		lblLatitude.setText("latitude");
		
		Label lblNewLabel_1 = new Label(shlUredjivanjeRute, SWT.NONE);
		lblNewLabel_1.setBounds(504, 188, 70, 17);
		lblNewLabel_1.setText("longitude");
		
		Button btnNovaTocka = new Button(shlUredjivanjeRute, SWT.NONE);
		btnNovaTocka.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try{
					tockaAktivna.setNaziv(textNazivTocke.getText());
					tockaAktivna.setLat(Double.valueOf( textLat.getText()));
					tockaAktivna.setLon(Double.valueOf( textLon.getText()));
					tockaAktivna.setVisina(Integer.valueOf( textNMVisina.getText()));
					listaTockePodaci.add(tockaAktivna);
					listTocke.removeAll();
					for (int i=0; i < listaTockePodaci.size(); i++ ){
					listTocke.add(listaTockePodaci.get(i).getNaziv());			
					}
				}
				catch(Exception e) {
					MessageBox messageBox = new MessageBox(shlUredjivanjeRute, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Nedostaju podaci za unos tocke!"); //ispisuje poruku iz kontrolera
					messageBox.open();

				}
				
			}
		});
		btnNovaTocka.setBounds(578, 252, 91, 29);
		btnNovaTocka.setText("Nova tocka");
		
/*		Button btnBrisiTocku = new Button(shlUredjivanjeRute, SWT.NONE);
		btnBrisiTocku.setGrayed(true);
		btnBrisiTocku.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					PotvrdiBrisanje potvrdiBrisanje = new PotvrdiBrisanje(
							shlUredjivanjeRute, SWT.DIALOG_TRIM);
					potvrdiBrisanje.setPoruka("Brisanje "
							+ listaTockePodaci.get(
									listTocke.getSelectionIndex()).getNaziv());
					potvrdiBrisanje.open();
					if (potvrdiBrisanje.isPotvrda() == true) {
						listaTockePodaci.remove(listTocke.getSelectionIndex());
						listTocke.removeAll();
						for (int i = 0; i < listaTockePodaci.size(); i++) {
							listTocke.add(listaTockePodaci.get(i).getNaziv());
						}

					}
					potvrdiBrisanje.ugasiProzor();
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlUredjivanjeRute, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Brisanje nije moguce!\nProvjerite podatke."); //ispisuje poruku iz kontrolera
					messageBox.open();
				}
			}
		});
		btnBrisiTocku.setBounds(578, 298, 91, 29);
		btnBrisiTocku.setText("Brisi tocku");*/
		
		Label lblNazivRute = new Label(shlUredjivanjeRute, SWT.NONE);
		lblNazivRute.setBounds(264, 27, 95, 17);
		lblNazivRute.setText("naziv rute");
		
		Button btnPotvrdaRute = new Button(shlUredjivanjeRute, SWT.NONE);
		btnPotvrdaRute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					rutaNova.setTockeRute(tockeZaUbacit);
					rutaNova.setImeRute(textNazivRute.getText());
					Random id = new Random();
					rutaNova.setId_rute(Math.abs(id.nextInt()));
					listaRutePodaci.add(rutaNova);
					spremiRutu();
					ruteList.removeAll();
					for (int i = 0; i < listaRutePodaci.size(); i++) {
						ruteList.add(listaRutePodaci.get(i).getImeRute());
					}
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlUredjivanjeRute, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Nedostaju podaci za unos !"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}
				
			}
		});
		btnPotvrdaRute.setBounds(365, 329, 98, 34);
		btnPotvrdaRute.setText("Potvrda rute");

	}

	public ArrayList<Ruta> getListaRutePodaci() {
		return listaRutePodaci;
	}

	public void setListaRutePodaci(ArrayList<Ruta> listaRuta) {
		this.listaRutePodaci = listaRuta;
		
	}
	public void zatvoriProzor(){
		shlUredjivanjeRute.dispose();
	
	}

	public ArrayList<Tocka> getListaTockePodaci() {
		return listaTockePodaci;
	}

	public void setListaTockePodaci(ArrayList<Tocka> listaTockePodaci) {
		this.listaTockePodaci = listaTockePodaci;
	}
	public void spremiRutu(){	
		upravPodaci.setRuta(rutaNova);
		upravPodaci.spremiRutuBaza();
		upravPodaci.procitajRuteBaza();
		setListaRutePodaci(upravPodaci.getListRuta());
		}
	
	public void spremiTocke(){
		upravPodaci.setListTocka(listaTockePodaci);
		upravPodaci.spremiTockeBaza();
		
	}
	
}
