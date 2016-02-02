package model;

import java.awt.Point;

public class Tocka extends Point {
	 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private int id_tocka;
		private String naziv;
	    private double lat;
	    private double lon;
	    private int visina;
	    private String napomena;
	    
	    
		public Tocka(String naziv, double lat, double lon, int visina,
				String napomena) {
			super();
			this.naziv = naziv;
			this.lat = lat;
			this.lon = lon;
			this.visina = visina;
			this.napomena = napomena;
		}
		public Tocka() {
			// TODO Auto-generated constructor stub
		}
		
		public int getId_tocka() {
			return id_tocka;
		}
		public void setId_tocka(int id_tocka) {
			this.id_tocka = id_tocka;
		}
		public String getNaziv() {
			return naziv;
		}
		public void setNaziv(String naziv) {
			this.naziv = naziv;
		}
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public double getLon() {
			return lon;
		}
		public void setLon(double lon) {
			this.lon = lon;
		}
		public int getVisina() {
			return visina;
		}
		public void setVisina(int visina) {
			this.visina = visina;
		}
		public String getNapomena() {
			return napomena;
		}
		public void setNapomena(String napomena) {
			this.napomena = napomena;
		}

}
