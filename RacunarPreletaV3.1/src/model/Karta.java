package model;

public class Karta {
	private String naziv;
    private String datotekaKarta;
    private String datotekaGeoReferenca;
    
    
	public Karta(String naziv, String datotekaKarta, String datotekaGeoReferenca) {
		super();
		this.naziv = naziv;
		this.datotekaKarta = datotekaKarta;
		this.datotekaGeoReferenca = datotekaGeoReferenca;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getDatotekaKarta() {
		return datotekaKarta;
	}
	public void setDatotekaKarta(String datotekaKarta) {
		this.datotekaKarta = datotekaKarta;
	}
	public String getDatotekaGeoReferenca() {
		return datotekaGeoReferenca;
	}
	public void setDatotekaGeoReferenca(String datotekaGeoReferenca) {
		this.datotekaGeoReferenca = datotekaGeoReferenca;
	}
    
    

}
