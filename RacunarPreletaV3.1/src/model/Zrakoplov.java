package model;

public class Zrakoplov {
	private int id_zrakoplovi;
	private String naziv;
    private String tezina;
    private int balast;
    private int postotakBalasta;
    private double V1;
    private double V2;
    private double V3;
    private double W1;
    private double W2;
    private double W3;
    private double minBrzina;
    private double opterecenje;
    private double parA;
    private double parB;
    private double parC;    
    
	public double getParA() {
		return parA;
	}
	public void setParA(double parA) {
		this.parA = parA;
	}
	public double getParB() {
		return parB;
	}
	public void setParB(double parB) {
		this.parB = parB;
	}
	public double getParC() {
		return parC;
	}
	public void setParC(double parC) {
		this.parC = parC;
	}
	public Zrakoplov(String naziv, String tezina, int balast,
			int postotakBalasta, double v1, double v2, double v3, double w1,
			double w2, double w3, double minBrzina) {
		super();
		this.naziv = naziv;
		this.tezina = tezina;
		this.balast = balast;
		this.postotakBalasta = postotakBalasta;
		V1 = v1;
		V2 = v2;
		V3 = v3;
		W1 = w1;
		W2 = w2;
		W3 = w3;
		this.minBrzina = minBrzina;
	}
	public Zrakoplov() {
		// TODO Auto-generated constructor stub
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getTezina() {
		return tezina;
	}
	public void setTezina(String tezina) {
		this.tezina = tezina;
	}
	public int getBalast() {
		return balast;
	}
	public void setBalast(int balast) {
		this.balast = balast;
	}
	public int getPostotakBalasta() {
		return postotakBalasta;
	}
	public void setPostotakBalasta(int postotakBalasta) {
		this.postotakBalasta = postotakBalasta;
	}
	public double getV1() {
		V1 = minBrzina*1.5;
		return V1;
	}
	public void setV1(double v1) {
		V1 = v1;
	}
	public double getV2() {
		V2 = minBrzina*1.7;
		return V2;
	}
	public void setV2(double v2) {
		V2 = v2;
	}
	public double getV3() {
		V3 = minBrzina*2.3;
		return V3;
	}
	public void setV3(double v3) {
		V3 = v3;
	}
	public double getW1() {
		double V=(getV1()/3600);
		W1=parA*V*V+parB*V+parC;
		return W1;
	}
	public void setW1(double w1) {
		W1 = w1;
	}
	public double getW2() {
		double V=(getV2()/3600);
		W2=parA*V*V+parB*V+parC;
		return W2;
	}
	public void setW2(double w2) {
		W2 = w2;
	}
	public double getW3() {
		double V=(getV3()/3600);
		W3=(0.1+parA)*V*V + parB*V + parC;
		return W3;
	}
	public void setW3(double w3) {
		W3 = w3;
	}
	public double getMinBrzina() {
		return minBrzina;
	}
	public void setMinBrzina(double minBrzina) {
		this.minBrzina = minBrzina;
	}
	public double getOpterecenje() {
		return opterecenje;
	}
	public void setOpterecenje(double opterecenje) {
		this.opterecenje = opterecenje;
	}
	public int getId_zrakoplovi() {
		return id_zrakoplovi;
	}
	public void setId_zrakoplovi(int id_zrakoplovi) {
		this.id_zrakoplovi = id_zrakoplovi;
	}
	
	public double getPropadanje() {
		if (W1 != 0)return W1;
		double V= ((minBrzina/100)*1.3);
		double W = parA*V*V+parB*V+parC;
		return W;
	}
    
    
    

}
