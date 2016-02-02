package model;

import java.util.ArrayList;


public class RutaPreletaIzracun {
	
    public RutaPreletaIzracun(String naziv,
			ArrayList<TockaIzracunaPreleta> tockaIzracunaPreleta) {
		super();
		this.naziv = naziv;
		this.tockaIzracunaPreleta = tockaIzracunaPreleta;
		
	}

	public RutaPreletaIzracun() {
		// TODO Auto-generated constructor stub
	}



	private long id_izracuna;
	private String naziv;
    private  ArrayList<TockaIzracunaPreleta> tockaIzracunaPreleta = new ArrayList<TockaIzracunaPreleta> ();
    private String datumPreleta;
    private Zrakoplov zrakoplov;
    private int metodaMeteo = 0; // 1-rucno, 2-GFS
    
  
    
	public long getId_izracuna() {
		return id_izracuna;
	}

	public void setId_izracuna(long id_izracuna) {
		this.id_izracuna = id_izracuna;
	}
    
    public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getDatumPreleta() {
		return datumPreleta;
	}

	public void setDatumPreleta(String datumPreleta) {
		this.datumPreleta = datumPreleta;
	}
	
	public Zrakoplov getZrakoplov() {
		return zrakoplov;
	}

	public void setZrakoplov(Zrakoplov zrakoplov) {
		this.zrakoplov = zrakoplov;
	}

	public int getMetodaMeteo() {
		return metodaMeteo;
	}

	public void setMetodaMeteo(int metodaMeteo) {
		this.metodaMeteo = metodaMeteo;
	}

	public ArrayList<TockaIzracunaPreleta> getTockaIzracunaPreleta() {
		return tockaIzracunaPreleta;
	}

	public void setTockaIzracunaPreleta(
			ArrayList<TockaIzracunaPreleta> tockaIzracunaPreleta) {
		this.tockaIzracunaPreleta = tockaIzracunaPreleta;
	}

	public void dajTockeEtape() {
    }

    public void upisRezultataDionice() {
    }

}
