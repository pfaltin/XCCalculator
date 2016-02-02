package model;

import java.util.ArrayList;
import java.util.Random;

public class TockaMeteo extends Tocka {

    public ArrayList<SlojZraka> getListaSlojZraka() {
		return listaSlojZraka;
	}
	public void setListaSlojZraka(ArrayList<SlojZraka> listaSlojZraka) {
		this.listaSlojZraka = listaSlojZraka;
	}
	public TockaMeteo(String naziv, float lat, float lon, int visina,
			String napomena) {
		super(naziv, lat, lon, visina, napomena);
		// TODO Auto-generated constructor stub
	}
	public TockaMeteo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2166783396407271184L;


	private ArrayList<SlojZraka> listaSlojZraka = new ArrayList<SlojZraka>();
    
	private Long id_tockameteo;
	private int vrijeme;
	private String datum;
	private int satGFS;
	private int godinaGFS;
	private int danGFS;
	private int mjesecGFS;
    
	public Long getId_tockameteo() {
		if ((datum != null) || ( danGFS != 0 && mjesecGFS != 0 && godinaGFS != 0 )){
		String latlon = String.valueOf(this.getLat())+String.valueOf(this.getLon());
		String result = latlon.replaceAll("[-+.^:,]","");
		String id = (String.valueOf(godinaGFS) + String.valueOf(mjesecGFS) + String.valueOf(danGFS)+String.valueOf(satGFS)+result );
		try {
			id_tockameteo = Long.decode( id.trim().substring(3));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else {
			Random rnd = new Random();
			Long neki = (long) rnd.nextInt();
			id_tockameteo = neki;
		}
		//System.out.println("id tocke meteo je "+id_tockameteo);
		return id_tockameteo;
	}
	public void setId_tockameteo(int id_tockameteo) {
		this.id_tockameteo = (long) id_tockameteo;
	}
	public int getSatGFS() {
		return satGFS;
	}
	public void setSatGFS(int satGFS) {
		this.satGFS = satGFS;
	}
	public int getGodinaGFS() {
		return godinaGFS;
	}
	public void setGodinaGFS(int godinaGFS) {
		this.godinaGFS = godinaGFS;
	}
	public int getDanGFS() {
		return danGFS;
	}
	public void setDanGFS(int danGFS) {
		this.danGFS = danGFS;
	}
	public int getMjesecGFS() {
		return mjesecGFS;
	}
	public void setMjesecGFS(int mjesecGFS) {
		this.mjesecGFS = mjesecGFS;
	}
	public int getVrijeme() {
		return vrijeme;
	}
	public void setVrijeme(int vrijeme) {
		this.vrijeme = vrijeme;
	}
	public String getDatum() {
		String datumNovi = (String.valueOf(godinaGFS) + "-"+String.valueOf(mjesecGFS)+"-" + String.valueOf(danGFS));
		if (datum==null){
			return datumNovi;
		}
		else{
		return datum;
		}
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	
	public String getNazivM() {
		Random rnd = new Random();
		Long neki = (long) rnd.nextInt();

		String latlon = String.valueOf(satGFS)+String.valueOf(this.getLat())+String.valueOf(this.getLon());
		String result = latlon.replaceAll("[-+.^:,]","");
		//System.out.println("TockaMeteo - getNaziv:"+String.valueOf(godinaGFS).substring(2) + String.valueOf(mjesecGFS) + String.valueOf(danGFS)+result+String.valueOf(neki).substring(3) );
		String id = (String.valueOf(godinaGFS).substring(3) + String.valueOf(mjesecGFS) + String.valueOf(danGFS)+result+String.valueOf(neki).substring(3) );
		return id;
	}

}