package model;

import java.util.ArrayList;

public class Ruta {
		private int id_rute;
		private String imeRute;
		private String opis;
		private ArrayList<Tocka> tockeRute = new ArrayList<Tocka>();


	public Ruta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId_rute() {
		return id_rute;
	}

	public void setId_rute(int id_rute) {
		this.id_rute = id_rute;
	}

	public String getImeRute() {
		return imeRute;
	}

	public void setImeRute(String imeRute) {
		this.imeRute = imeRute;
	}



	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public ArrayList<Tocka> getTockeRute() {
		return tockeRute;
	}

	public void setTockeRute(ArrayList<Tocka> tockeRute) {
		this.tockeRute = tockeRute;
	}

}
