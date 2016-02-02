package view;

import model.RutaPreletaIzracun;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class IzracunPrikaz extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table table;
    private RutaPreletaIzracun rutaPreletaIzracun = new RutaPreletaIzracun();
    private String ispisPreleta = "";

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public IzracunPrikaz(Shell parent, int style) {
		super(parent, style);
		setText("Prikaz izracuna preleta");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(925, 588);
		shell.setText(getText());
		shell.setLayout(null);

		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(42, 144, 840, 397);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		

		
		Button btnIzlaz = new Button(shell, SWT.NONE);
		btnIzlaz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				zatvoriProzor();
			}
		});
		btnIzlaz.setBounds(813, 24, 69, 24);
		btnIzlaz.setText("izlaz");
		

		
		
		String[] titles = {"Naziv\npocetak","Naziv\nkraj", "Visina\nm", "Dizanje\nm/s", "Baza\nm", "Vjetar\nm/s","Vjetar\nsmjer","Duljina\ndionice",
				"V dionica\nkm/h","Dolazak\nna pocetak","Vrijeme\ndionica","Dolazak\nna kraj","V start\nkm/h","Vrijeme\nukupno","Duljina\nkm","Kindex", "Stratus vj." };
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}	

		double  duljinaPreleta = 0;
		for (  int j = 0; j<rutaPreletaIzracun.getTockaIzracunaPreleta().size()-1;j++ ){
			String prikaz = "";
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0,String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getNaziv()));
			item.setText (1,String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getNaziv()));
//			item.setText (1,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getLat()));
//			item.setText (2,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getLon()));
			item.setText (2,String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVisina()));
			item.setText (3,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getDizanjeBrzina()));
			item.setText (4,String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getDizanjeVisina()));
			item.setText (5,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVjetarBrzina()));
			item.setText (6,String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVjetarSmjer()));
			item.setText (7,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getDuljinaEtape()));
			item.setText (8,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getBrzinaEtapa()));

			long trajanjeSec = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVrijemeDolaska(); // 9 "Dolazak\nna pocetak"
			int sati = (int) Math.floor(trajanjeSec/3600); 
			int minute = (int) ((trajanjeSec/60)%60) ;
			if (minute<10) {
				prikaz= sati+":0"+minute;
			}else{
				prikaz= sati+":"+minute;
			}
			item.setText (9,prikaz);
			
			long trajanjeSecE = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getTrajanjeEtapa(); 
			int satiE = (int) Math.floor(trajanjeSecE/3600); 
			int minuteE = (int) ((trajanjeSec/60)%60);
			if (minuteE<10) {
				prikaz= satiE+":0"+minuteE;
			}else{
				prikaz= satiE+":"+minuteE;
			}
			item.setText (10,prikaz);
			
			long trajanjeSecST = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getVrijemeDolaska(); // 9 "Dolazak\nna pocetak"
			int satiST = (int) Math.floor(trajanjeSecST/3600); 
			int minuteST = (int) ((trajanjeSecST/60)%60) ;
			item.setText (11,satiST+":"+minuteST);

			item.setText (12,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getBrzinaStart()));			
			long trajanjeSecSt = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getTrajanjeStart(); 
			int satiSt = (int) Math.floor(trajanjeSecSt/3600); 
			int minuteSt = (int) ((trajanjeSec/60)%60) ;
			if (minute<10) {
				prikaz = satiSt+":0"+minuteSt;
			}else{
				prikaz = satiSt+":"+minuteSt;
			}
			item.setText (13,prikaz);

			duljinaPreleta = duljinaPreleta + rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getDuljinaEtape();
			item.setText (14,String.format("%.2f",duljinaPreleta));			
			item.setText (15,String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getVjerojatnostCb()));
			item.setText (16,String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getVjerojatnostStratus()));

			}
		
		
		
		
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		int zadnja = rutaPreletaIzracun.getTockaIzracunaPreleta().size()-1;
		String ispisSumaPreleta = "";
		String metoda = "";
		String prikazTrajanjeStart = "";
		long trajanjeSecStr = rutaPreletaIzracun.getTockaIzracunaPreleta().get(zadnja).getTrajanjeStart(); 
		int satiStr = (int) Math.floor(trajanjeSecStr/3600); 
		int minuteStr = (int) ((trajanjeSecStr/60)%60) ;
		prikazTrajanjeStart = satiStr+" sati i "+minuteStr+" minuta.";
		long polazak = rutaPreletaIzracun.getTockaIzracunaPreleta().get(0).getVrijemeDolaska(); 
		int satiP = (int) Math.floor(polazak/3600); 
		long dolazak = rutaPreletaIzracun.getTockaIzracunaPreleta().get(zadnja).getVrijemeDolaska(); 
		int satiD = (int) Math.floor(dolazak/3600); 
		int minuteD = (int) ((dolazak/60)%60) ;
		String prikazDolazak= satiD+" sati i "+minuteD+"minuta.";
		if (rutaPreletaIzracun.getMetodaMeteo() == 4) metoda = "dobiveni sa poslužitelja ESRL/GSD. kod:";
		if (rutaPreletaIzracun.getMetodaMeteo() == 3) metoda = "su uneseni rucno. kod:";
		ispisSumaPreleta = "Prelet po ruti: "+rutaPreletaIzracun.getNaziv()
				+"\nna zrakoplovu: "+rutaPreletaIzracun.getZrakoplov().getNaziv()
				+" ,na dan:"+rutaPreletaIzracun.getDatumPreleta()
				+"\nMeteo podaci "+metoda+" "+rutaPreletaIzracun.getMetodaMeteo()
				+"\nUkupno vrijeme preleta "+prikazTrajanjeStart
				+"\nDuljina rute preleta je "+String.format("%.2f",duljinaPreleta)+"km"
				+"\nOcekivana brzina preleta je "+String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(zadnja).getBrzinaStart())+"km/h"
				+"\nTrajanje preleta je "+prikazTrajanjeStart
				+"\nPocetak preleta je u "+satiP+" sati a dolazak na cilj se očekuje u "+prikazDolazak;
		
		Label lblSumaPreleta = new Label(shell, SWT.NONE);
		lblSumaPreleta.setBounds(42, 28, 556, 192);
		lblSumaPreleta.setText("Suma Preleta");
		lblSumaPreleta.setText(ispisSumaPreleta);
		

	
		

	}

	public RutaPreletaIzracun getRutaPreletaIzracun() {
		return rutaPreletaIzracun;
	}

	public void setRutaPreletaIzracun(RutaPreletaIzracun rutaPreletaIzracun) {
		this.rutaPreletaIzracun = rutaPreletaIzracun;
		
	}

	public String getIspisPreleta() {
		return ispisPreleta;
	}

	public void setIspisPreleta(String ispisPreleta) {
		this.ispisPreleta = ispisPreleta;
	}
	public void zatvoriProzor(){
		shell.dispose();
		
	
	}
}
