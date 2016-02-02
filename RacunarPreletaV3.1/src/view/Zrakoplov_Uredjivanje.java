package view;

import java.util.ArrayList;

import model.Zrakoplov;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Zrakoplov_Uredjivanje extends Dialog {

	protected Object result;
	protected Shell shlUredjivanjeZrakoplova;
	private Text textNaziv;
	private Text textSuhaTezina;
	private Text textBalast;
	private Text textV1;
	private Text textW1;
	private Text textV2;
	private Text textW2;
	private Text textV3;
	private Text textW3;
	private List listPolara; 
	private Label lblAbc;
	
	private ArrayList<Zrakoplov> listaPolara = new ArrayList<Zrakoplov>();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Zrakoplov_Uredjivanje(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlUredjivanjeZrakoplova.open();
		shlUredjivanjeZrakoplova.layout();
		Display display = getParent().getDisplay();
		while (!shlUredjivanjeZrakoplova.isDisposed()) {
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
		shlUredjivanjeZrakoplova = new Shell(getParent(), getStyle());
		shlUredjivanjeZrakoplova.setSize(499, 309);
		shlUredjivanjeZrakoplova.setText("Uredjivanje Zrakoplova");
		
		listPolara = new List(shlUredjivanjeZrakoplova, SWT.BORDER | SWT.V_SCROLL);
		listPolara.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				textNaziv.setText(listaPolara.get(listPolara.getSelectionIndex()).getNaziv());
				textSuhaTezina.setText(String.valueOf(listaPolara.get(listPolara.getSelectionIndex()).getTezina()));
				textBalast.setText(String.valueOf(listaPolara.get(listPolara.getSelectionIndex()).getBalast()));
				textV1.setText(String.format("%.4f",listaPolara.get(listPolara.getSelectionIndex()).getV1()));
				textV2.setText(String.format("%.4f",listaPolara.get(listPolara.getSelectionIndex()).getV2()));
				textV3.setText(String.format("%.4f",listaPolara.get(listPolara.getSelectionIndex()).getV3()));
				textW1.setText(String.format("%.4f",listaPolara.get(listPolara.getSelectionIndex()).getW1()));
				textW2.setText(String.format("%.4f",listaPolara.get(listPolara.getSelectionIndex()).getW2()));
				textW3.setText(String.format("%.4f",listaPolara.get(listPolara.getSelectionIndex()).getW3()));

					String abc = ("Parametri zrakoplova: \na: "
							+ (String.valueOf(listaPolara.get(
									listPolara.getSelectionIndex()).getParA()))
							+ "\nb: "
							+ (String.valueOf(listaPolara.get(
									listPolara.getSelectionIndex()).getParB()))
							+ "\nc: "
							+ (String.valueOf(listaPolara.get(
									listPolara.getSelectionIndex()).getParC())));
							abc=(abc + "\nMinimalna brzina: "
							+ (String.valueOf(listaPolara.get(
									listPolara.getSelectionIndex())
									.getMinBrzina()))
							+ "\nOpterećenje: "
							+ (String.valueOf(listaPolara.get(
									listPolara.getSelectionIndex())
									.getOpterecenje())));
					try {
						lblAbc.setText(abc);
				} catch (Exception e) {
					MessageBox messageBox = new MessageBox(shlUredjivanjeZrakoplova, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					messageBox.setMessage("Prvo odaberi zrakoplov!"); //ispisuje poruku iz kontrolera
					messageBox.open();
				}
				
			}
		});
		listPolara.setBounds(30, 10, 237, 129);
		for (int i=0; i < listaPolara.size(); i++ ){
			listPolara.add(listaPolara.get(i).getNaziv());			
		}
		
		textNaziv = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textNaziv.setBounds(286, 29, 185, 19);
		
		textSuhaTezina = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textSuhaTezina.setBounds(286, 74, 76, 19);
		
		textBalast = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textBalast.setBounds(395, 74, 76, 19);
		
		textV1 = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textV1.setBounds(286, 118, 76, 19);
		
		textW1 = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textW1.setBounds(395, 118, 76, 19);
		
		textV2 = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textV2.setBounds(286, 162, 76, 19);
		
		textW2 = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textW2.setBounds(395, 162, 76, 19);
		
		textV3 = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textV3.setBounds(286, 209, 76, 19);
		
		textW3 = new Text(shlUredjivanjeZrakoplova, SWT.BORDER);
		textW3.setBounds(395, 209, 76, 19);
		
		Label lblNaziv = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblNaziv.setBounds(286, 10, 49, 13);
		lblNaziv.setText("Naziv");
		
		Label lblMaxSuhaTezina = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblMaxSuhaTezina.setBounds(286, 55, 92, 13);
		lblMaxSuhaTezina.setText("Max Suha Tezina");
		
		Label lblBalast = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblBalast.setBounds(395, 55, 49, 13);
		lblBalast.setText("Balast");
		
		Label lblV = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblV.setBounds(286, 99, 49, 13);
		lblV.setText("V1 km/h");
		
		Label lblW = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblW.setBounds(395, 99, 49, 13);
		lblW.setText("W1 m/s");
		
		Label lblV_1 = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblV_1.setBounds(286, 143, 49, 13);
		lblV_1.setText("V2 km/h");
		
		Label lblW_1 = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblW_1.setBounds(395, 143, 49, 13);
		lblW_1.setText("W2 m/s");
		
		Label lblV_2 = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblV_2.setBounds(286, 190, 49, 13);
		lblV_2.setText("V3 km/h");
		
		Label lblW_2 = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblW_2.setBounds(395, 190, 49, 13);
		lblW_2.setText("W3 m/s");
		
		lblAbc = new Label(shlUredjivanjeZrakoplova, SWT.NONE);
		lblAbc.setBounds(30, 174, 237, 104);
		lblAbc.setText("abc");
		
		
		Button btnBrisanje = new Button(shlUredjivanjeZrakoplova, SWT.NONE);
		btnBrisanje.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				textNaziv.setText(listaPolara.get(listPolara.getSelectionIndex()).getNaziv());
				PotvrdiBrisanje potvrdiBrisanje = new PotvrdiBrisanje(shlUredjivanjeZrakoplova, SWT.DIALOG_TRIM);
				potvrdiBrisanje.setPoruka("Brisanje "+ listaPolara.get(listPolara.getSelectionIndex()).getNaziv());
				potvrdiBrisanje.open();
				if (potvrdiBrisanje.isPotvrda()== true){
					listaPolara.remove(listPolara.getSelectionIndex());
					listPolara.removeAll();
					for (int i=0; i < listaPolara.size(); i++ )
					{
					listPolara.add(listaPolara.get(i).getNaziv());
				}
				
				}
				potvrdiBrisanje.ugasiProzor();
				
			
				
			}
		});
		btnBrisanje.setBounds(286, 241, 76, 25);
		btnBrisanje.setText("brisanje");
		
		Button btnUnosNovog = new Button(shlUredjivanjeZrakoplova, SWT.NONE);
		btnUnosNovog.setGrayed(true);
		btnUnosNovog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
							
				Zrakoplov polara = new Zrakoplov();
				polara.setNaziv(textNaziv.getText());
				polara.setTezina(textSuhaTezina.getText());
				polara.setBalast(Integer.valueOf(textBalast.getText()));
				polara.setV1(Double.valueOf(  textV1.getText()));
				polara.setV2(Double.valueOf(  textV2.getText()));
				polara.setV3(Double.valueOf(  textV3.getText()));
				polara.setW1(Double.valueOf(  textW1.getText()));
				polara.setW2(Double.valueOf(  textW2.getText()));
				polara.setW3(Double.valueOf(  textW3.getText()));

				listaPolara.add(polara);
				listPolara.removeAll();
					for (int i=0; i < listaPolara.size(); i++ )
					{
					listPolara.add(listaPolara.get(i).getNaziv());
					}
			}
				catch(Exception e) {
					MessageBox messageBox = new MessageBox(shlUredjivanjeZrakoplova, SWT.DIALOG_TRIM);
					messageBox.setText("Greska"); // ispisuje se u naslovu
					//messageBox.setMessage(e.getMessage()); //ispisuje poruku iz kontrolera
					messageBox.setMessage("Provjeri isprvnost svih podataka");
					messageBox.open();
					}
			}
		});
		btnUnosNovog.setBounds(199, 145, 68, 23);
		btnUnosNovog.setText("unos novog");
		
		Button btnIzlaz = new Button(shlUredjivanjeZrakoplova, SWT.NONE);
		btnIzlaz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				zatvoriProzor();
			}
		});
		btnIzlaz.setBounds(395, 243, 76, 23);
		btnIzlaz.setText("Izlaz");
		


	}

	public ArrayList<Zrakoplov> getListaPolara() {
		return listaPolara;
	}

	public void setListaPolara(ArrayList<Zrakoplov> listaPolara) {
		this.listaPolara = listaPolara;
	}
	public void zatvoriProzor(){
		shlUredjivanjeZrakoplova.dispose();
		
	
	}
}
