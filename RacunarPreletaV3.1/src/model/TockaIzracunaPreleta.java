package model;

public class TockaIzracunaPreleta extends Tocka {
   public TockaIzracunaPreleta(String naziv, double lat, double lon, int visina,
			String napomena) {
		super(naziv, lat, lon, visina, napomena);
		// TODO Auto-generated constructor stub
	}

public TockaIzracunaPreleta() {
	// TODO Auto-generated constructor stub
}

/**
	 * 
	 */
	private static final long serialVersionUID = 6439737004951629514L;
	 private int id_tockaIzracuna;

private double dizanjeBrzina;

   private int dizanjeVisina;

   private double vjetarBrzina;

   private int vjetarSmjer;

   private double duljinaEtape; //udaljnost od predhodne tocke


   private double brzinaEtapa; //brzina od predhodne tocke

   private double brzinaStart; //brzina od pocetne tocke rute

   private long vrijemeDolaska; //vrijeme od 00:00

   private long trajanjeEtapa; //trajanje leta od predhodne tocke

   private long trajanjeStart;  //trajanje leta pocetne tocke rute

   private int vjerojatnostCb;

   private int vjerojatnostStratus;


public int getId_tockaIzracuna() {
	return id_tockaIzracuna;
}

public void setId_tockaIzracuna(int id_tockaIzracuna) {
	this.id_tockaIzracuna = id_tockaIzracuna;
}
public double getDizanjeBrzina() {
	return dizanjeBrzina;
}

public void setDizanjeBrzina(double dizanjeBrzina) {
	this.dizanjeBrzina = dizanjeBrzina;
}

public int getDizanjeVisina() {
	return dizanjeVisina;
}

public void setDizanjeVisina(int dizanjeVisina) {
	this.dizanjeVisina = dizanjeVisina;
}

public double getVjetarBrzina() {
	return vjetarBrzina;
}

public void setVjetarBrzina(double vjetarBrzina) {
	this.vjetarBrzina = vjetarBrzina;
}

public int getVjetarSmjer() {
	return vjetarSmjer;
}

public void setVjetarSmjer(int vetarSmjer) {
	this.vjetarSmjer = vetarSmjer;
}

public double getBrzinaEtapa() {
	return brzinaEtapa;
}

public void setBrzinaEtapa(double brzinaEtapa) {
	this.brzinaEtapa = brzinaEtapa;
}

public double getBrzinaStart() {
	return brzinaStart;
}

public void setBrzinaStart(double brzinaStart) {
	this.brzinaStart = brzinaStart;
}

public long getVrijemeDolaska() {
	return vrijemeDolaska;
}

public void setVrijemeDolaska(long vrijemeDolaska) {
	this.vrijemeDolaska = vrijemeDolaska;
}

public long getTrajanjeEtapa() {
	return trajanjeEtapa;
}

public void setTrajanjeEtapa(long trajanjeEtapa) {
	this.trajanjeEtapa = trajanjeEtapa;
}

public long getTrajanjeStart() {
	return trajanjeStart;
}

public void setTrajanjeStart(long trajanjeStart) {
	this.trajanjeStart = trajanjeStart;
}

public int getVjerojatnostCb() {
	return vjerojatnostCb;
}

public void setVjerojatnostCb(int vjerojatnostCb) {
	this.vjerojatnostCb = vjerojatnostCb;
}

public int getVjerojatnostStratus() {
	return vjerojatnostStratus;
}

public void setVjerojatnostStratus(int vjerojatnostStratus) {
	this.vjerojatnostStratus = vjerojatnostStratus;
}

public double getDuljinaEtape() {
	return duljinaEtape;
}

public void setDuljinaEtape(double duljinaEtape) {
	this.duljinaEtape = duljinaEtape;
}
   
   

}