package view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PotvrdiBrisanje extends Dialog {

	protected Object result;
	protected Shell shell;
	private String poruka;
	
	private boolean potvrda;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PotvrdiBrisanje(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
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
		shell.setSize(238, 110);
		shell.setText(getText());
		
		Label lblPoruka = new Label(shell, SWT.NONE);
		lblPoruka.setAlignment(SWT.CENTER);
//		lblPoruka.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.NORMAL));
		lblPoruka.setBounds(29, 10, 165, 25);
		lblPoruka.setText(poruka);
		
		Button btnDa = new Button(shell, SWT.NONE);
		btnDa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setPotvrda(true);
				zatvoriProzor();
			}
		});
		btnDa.setBounds(29, 41, 68, 23);
		btnDa.setText("Brisanje");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setPotvrda(false);
				ugasiProzor();
			}
		});
		btnNewButton_1.setBounds(126, 41, 68, 23);
		btnNewButton_1.setText("Odustajem");

	}

	
	public boolean isPotvrda() {
		return potvrda;
	}

	public void setPotvrda(boolean potvrda) {
		this.potvrda = potvrda;
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}

	public void zatvoriProzor(){
		shell.close();
	}
	public void ugasiProzor(){
		shell.dispose();
		
	
	}
}
