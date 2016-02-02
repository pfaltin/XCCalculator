package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import model.Karta;
import model.PopisPrognoza;
import model.Ruta;
import model.Tocka;
import model.TockaOkretna;
import model.Zrakoplov;

public class UpravljanjePodacima {
    

	private ArrayList<Tocka> listTocka = new ArrayList<Tocka> ();

    private Ruta ruta = new Ruta();
    private String datum; 
    
    private ArrayList<Ruta> listRuta = new ArrayList<Ruta>();
   
    private ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();
   
    private ArrayList<Karta> listaKarta = new ArrayList<Karta> ();
    
    private ArrayList<TockaOkretna> listaOkretnihTocka = new ArrayList<TockaOkretna>();


	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private Path fFilePath ;
    
    // Getters and Setters 

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		System.out.println("UpPod - Postavlja se datum:"+datum);
		
		this.datum = datum;
	}

	public ArrayList<Tocka> getListTocka() {
		return listTocka;
	}

	public void setListTocka(ArrayList<Tocka> listTocka) {
		this.listTocka = listTocka;
	}

	public ArrayList<Ruta> getListRuta() {
		return listRuta;
	}

	public void setListRuta(ArrayList<Ruta> listaRuta) {
		System.out.println("UP upisano ruta: "+listaRuta.size());
		this.listRuta = listaRuta;
		
	}

	public ArrayList<Karta> getListaKarta() {
		return listaKarta;
	}

	public void setListaKarta(ArrayList<Karta> listaKarta) {
		this.listaKarta = listaKarta;
	}

	public ArrayList<Tocka> getTocka() {
		return listTocka;
	}

	public void setTocka(ArrayList<Tocka> tocka) {
		this.listTocka = tocka;
	}

	public Ruta getRuta() {
		return ruta;
	}

	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
		System.out.println("ruta je postavljena "+ruta.getImeRute());
	}


	public ArrayList<Karta> getKarta() {
		return listaKarta;
	}

	public void setKarta(ArrayList<Karta> karta) {
		this.listaKarta = karta;
	}
    
	public ArrayList<TockaOkretna> getListaOkretnihTocka() {
		return listaOkretnihTocka;
	}

	public void setListaOkretnihTocka(ArrayList<TockaOkretna> listaOkretnihTocka) {
		this.listaOkretnihTocka = listaOkretnihTocka;
	}
	public ArrayList<Zrakoplov> getListaZrakoplov() {
		return listaZrakoplov;
	}

	public void setListaZrakoplov(ArrayList<Zrakoplov> listaZrakoplov) {
		this.listaZrakoplov = listaZrakoplov;
	}
	
	//Upravljanje podacima
	public void pretvoriOkretneTocke(){
		IzracuniGC izracun = new IzracuniGC();
		
		for (  int j = 1; j<this.listaOkretnihTocka.size();j++ ){
			Tocka tocka = new Tocka();
			tocka.setNaziv(listaOkretnihTocka.get(j).getName());
			tocka.setLat(izracun.stupMinDecimal(listaOkretnihTocka.get(j).getLat()));
			tocka.setLon(izracun.stupMinDecimal(listaOkretnihTocka.get(j).getLon()));
			
	    	  String broj=null;
	    	  StringTokenizer st = new StringTokenizer(listaOkretnihTocka.get(j).getElev(),".");
	    	  while (st.hasMoreTokens( )) {
	    		  String s = st.nextToken( );
	    		  if ( broj == null) broj = s;}
			tocka.setVisina(Integer.valueOf(broj));
			tocka.setNapomena(listaOkretnihTocka.get(j).getCode()+" "+listaOkretnihTocka.get(j).getDescr());
			listTocka.add(tocka);
		}
	}
	public String spremiGFS() throws ParseException{
		ciscenjeMeteoBaza();
		PripremaGFSRuta pripremaGFS = new PripremaGFSRuta();
		pripremaGFS.setRuta(ruta);
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		if (datum == null) return "\nNedostaje datum!";
		Date d = formatter.parse(datum);
		pripremaGFS.setDatum(d);
		String poruka= ("\nGotovo preuzimanje sa GFS poslužitelja\nMože se pokrenuti izračun preleta.");
		pripremaGFS.setDatum(d);
		try {
			pripremaGFS.pripremiGFS();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		PopisPrognoza prognozaSat = new PopisPrognoza();
		
		prognozaSat = veza.selectPocetak(ruta.getTockeRute().get(0), datum, 0.1);
		if( prognozaSat.getVisinaDizanja() > 5){
			return poruka;
		}
		else {
			return "\nPreuzimanje podataka nije uspjelo\nPokušajte ponovo ili unesite ručno.";
			
		}
	}
		
	
	
    // Datoteka
	


	public void procitajTockeDatoteka(String putanja)throws IOException {	
	
	 fFilePath = Paths.get(putanja);
			    try (Scanner citacDatoteke =  new Scanner(fFilePath, ENCODING.name()))
			     {
			    	String[] slog = new String[19] ;
			      while (citacDatoteke.hasNextLine())
			      {
			    	  //System.out.println(" - Obrada linije: "+l);
			    	  Scanner citacLinije = new Scanner(citacDatoteke.nextLine());
			    	  citacLinije.useDelimiter(",");
			    	  int i = 0;
			    	  while (citacLinije.hasNext()){
			    	       i++;
			    	       slog[i] = citacLinije.next();
			    	       if (slog[i].equals("")) slog[i]=" ";

			    	    }
			    	  
			    	  //uzmiPodatke(slog); name,code,country,lat,lon,elev,style,rwdir,rwlen,freq,desc
			    	  //System.out.println(l+" name"+slog[1]+", code "+slog[2]+" country ,"+slog[3]+" lat:"+slog[4]+" lon:"+slog[5]+" elev"+slog[6]+" style,"+slog[7]+"rwdir,"+slog[8]+"rwlen,"+slog[9]+"freq,"+slog[10]+"descl"+slog[11]);
			    	  TockaOkretna tocka = new TockaOkretna();
			    	  // trimanje navodnika iz naziva
			    	  String beznavodnika=null;
			    	  StringTokenizer st = new StringTokenizer(slog[1],"\"");
			    	  while (st.hasMoreTokens( )) {
			    		  String s = st.nextToken( );
			    		  if ( beznavodnika == null) beznavodnika = s;
			    	  }
			    		  //System.out.println("\n Bez navodnika: "+beznavodnika);  
			    		  
//			    	// lat to float
//			        	  String lat=null;
//			        	  st = new StringTokenizer(slog[4],"\"");
//			        	  String stup = st.nextToken( )+st.nextToken( );
//			        		  lat = stup;
//			        	  
//			        		  System.out.println("\n lat: "+lat);      		  
			    	
			    	  tocka.setName(beznavodnika);
			    	  tocka.setCode(slog[2]);
			    	  tocka.setCountry(slog[3]);
			    	  tocka.setLat(slog[4]);
			    	  tocka.setLon(slog[5]);
			    	  tocka.setElev(slog[6]);
			    	  tocka.setStyle(slog[7]);
			    	  tocka.setRwdir(slog[8]);
			    	  tocka.setRwlen(slog[9]);
			    	  tocka.setFreq(slog[10]);
			    	  if(!slog[11].equals("desc")){tocka.setDescr(slog[11]);} else {tocka.setDescr("");}
			    	  ;
			    	  
			    	  this.listaOkretnihTocka.add(tocka);
			/*    	  INSERT INTO "tockaOkretna"( id_oktocka, name, code, country, lat, lon, elev, style, rwdir,rwlen, freq, descr)
			    	    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);*/

			    	  //System.out.println("Kraj linije...");
			    	  
			    	    citacLinije.close();
			      } 
			     }
			
		
		
	}
	public void spremiTockeDatoteka(String putanja) throws IOException{
		PrintWriter upisivac = new PrintWriter
		(new FileWriter(putanja));
		String in = "name,code,country,lat,lon,elev,style,rwdir,rwlen,freq,descr";
		upisivac.println(in);
		for (  int j = 1; j<listTocka.size();j++ ){
			in = "\""+
					listaOkretnihTocka.get(j).getName()+"\","+
	    			listaOkretnihTocka.get(j).getCode()+","+
	    			listaOkretnihTocka.get(j).getCountry()+","+
	    			listaOkretnihTocka.get(j).getLat()+","+
	    			listaOkretnihTocka.get(j).getLon()+","+
	    			listaOkretnihTocka.get(j).getElev()+","+
	    			listaOkretnihTocka.get(j).getStyle()+","+
	    			listaOkretnihTocka.get(j).getRwdir()+","+
	    			listaOkretnihTocka.get(j).getRwlen()+","+
	    			listaOkretnihTocka.get(j).getFreq()+","+
	    			listaOkretnihTocka.get(j).getDescr();

					upisivac.println(in);
			}
		upisivac.close();
		
	}	
	
	public void procitajZrakoplovDatoteka(String putanja) throws IOException{
		
		 fFilePath = Paths.get(putanja);
				    try (Scanner citacDatoteke =  new Scanner(fFilePath, ENCODING.name()))
				     {
				    	String[] slog = new String[19] ;
				      while (citacDatoteke.hasNextLine())
				      {
				    	  //System.out.println(" - Obrada linije: "+l);
				    	  Scanner citacLinije = new Scanner(citacDatoteke.nextLine());
				    	  citacLinije.useDelimiter(",");
				    	  int i = 0;
				    	  while (citacLinije.hasNext()){
				    	       i++;
				    	       slog[i] = citacLinije.next();
				    	       if (slog[i].equals("")) slog[i]=" ";

				    	    }
//				    	  System.out.println(l+" 1: "+slog[1]+" 2:"+slog[2]+" 3:"+slog[3]+" 4:"+slog[4]+" 5:"+slog[5]+" 6:"+slog[6]+" 7:"+slog[7]+" 8:"+slog[8]+" 9:"+slog[9]+" 10:"+slog[10]+" 11:"+slog[11]);
				    	  Zrakoplov zrakoplov = new Zrakoplov();
				    	  zrakoplov.setNaziv(slog[1]);
				    	  zrakoplov.setOpterecenje(Double.valueOf(slog[2].substring(0,2)));
				    	  zrakoplov.setMinBrzina(Double.valueOf(slog[3].substring(0,2)));
				    	  zrakoplov.setParA(Double.valueOf(slog[4]));
				    	  zrakoplov.setParB(Double.valueOf(slog[5]));
				    	  zrakoplov.setParC(Double.valueOf(slog[6]));
				    	  this.listaZrakoplov.add(zrakoplov);
			    	    citacLinije.close();
				      } 
				     }
				
			
		
		
	}
	public void spremiZrakoplovDatoteka(String putanja) throws IOException{

		String upis= "";
		System.out.println(" listaZrakoplov.size()= "+listaZrakoplov.size());
		for (  int j = 0; j<listaZrakoplov.size();j++ ){
			upis = upis +
							listaZrakoplov.get(j).getNaziv()+","+
							listaZrakoplov.get(j).getOpterecenje()+","+
							listaZrakoplov.get(j).getMinBrzina()+","+
							listaZrakoplov.get(j).getParA()+","+
							listaZrakoplov.get(j).getParB()+","+
							listaZrakoplov.get(j).getParC()+"\n";

			
			}
		//System.out.println("upis:\n"+upis);
		upisTXTDatoteka(putanja, upis);
		
	}	
	
	public void upisTXTDatoteka(String imeDatoteke, String sadrzaj) {
		//System.out.println("upis datoteka:\n"+sadrzaj);

	    FileWriter output = null;
	    try {
	      output = new FileWriter(imeDatoteke);
	      BufferedWriter writer = new BufferedWriter(output);
	      
	      writer.write(sadrzaj);
	      writer.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	      if (output != null) {
	        try {
	          output.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }

	  } 
	
	// Baza
	public void spremiTockeBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		veza.insertTocka(listTocka);
		}
	
	public void spremiRutuBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		veza.insertRuta(ruta);
	}
	public void spremiCRuteBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		veza.ciscenjeRutaBaza();
		if (ruta==null)listRuta.add(ruta);
		for (int r=0; r<listRuta.size();r++){
			veza.insertRuta(listRuta.get(r));
		}
		//veza.insertRuta(ruta);
	}
	
	public void spremiZrakoploveBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		veza.ciscenjeZrakoplovaBaza();
    	veza.insertZrakoplov(listaZrakoplov);
    	
	}
	
	public void procitajTockeBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
    	this.setTocka(veza.selectTocka());
	}
	public void procitajRuteBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
    	this.setListRuta(veza.selectRuta());
	}
	public void procitajZrakoploveBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		this.listaZrakoplov = new ArrayList<Zrakoplov> ();
    	this.setListaZrakoplov(veza.selectZrakoplov());
    	
	}
	
	public void ciscenjeCijelaBaze(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		System.out.println("Brisanje Cijele Baze!!!");
		veza.ciscenjeBaze();
		}
	public void ciscenjeTockeBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		System.out.println("Brisanje Ruta i Tocki Baze!!!");
		veza.ciscenjeTockeBaza();
		}
	public void ciscenjeRuteBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		System.out.println("Brisanje Ruta Baze!!!");
		veza.ciscenjeRutaBaza();
		}
	public void ciscenjeZrakoplovaBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		System.out.println("Brisanje Zrakoplova Baze!!!");
		veza.ciscenjeZrakoplovaBaza();
		}
	public void ciscenjeMeteoBaza(){
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		System.out.println("Brisanje Zrakoplova Baze!!!");
		veza.ciscenjeMeteoBaze();
		}
	
	public static Date stringDatum(String datum) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		Date d = formatter.parse(datum);
		return d;
	}
	public void punjenjeBaze() throws IOException, ParseException{

		ciscenjeCijelaBaze();
		String putanja = ("/home/k2/tmp/IstraLika.cup");
		procitajTockeDatoteka(putanja);
		pretvoriOkretneTocke();
		spremiTockeBaza();		
		procitajTockeBaza();
		Ruta ruta = new Ruta();
		Random id = new Random( );
		
		ruta.setId_rute(Math.abs(id.nextInt()));
		ruta.setImeRute("Ruta Prva");
		ArrayList<Tocka> tockeRute = new ArrayList<Tocka>();
		tockeRute.add(getListTocka().get(12));
		tockeRute.add(getListTocka().get(13));
		tockeRute.add(getListTocka().get(42));
		tockeRute.add(getListTocka().get(22));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		ruta.setTockeRute(tockeRute);
		setRuta(ruta);
		spremiRutuBaza();
		
		ruta.setId_rute(Math.abs(id.nextInt()));
		ruta.setImeRute("Ruta Druga");
		tockeRute = new ArrayList<Tocka>();
		tockeRute.add(getListTocka().get(1));
		tockeRute.add(getListTocka().get(23));
		tockeRute.add(getListTocka().get(44));
		tockeRute.add(getListTocka().get(21));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		ruta.setTockeRute(tockeRute);
		setRuta(ruta);
		spremiRutuBaza();
		
		ruta.setId_rute(Math.abs(id.nextInt()));
		ruta.setImeRute("Ruta Treca");
		tockeRute = new ArrayList<Tocka>();
		tockeRute.add(getListTocka().get(4));
		tockeRute.add(getListTocka().get(22));
		tockeRute.add(getListTocka().get(41));
		tockeRute.add(getListTocka().get(11));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		ruta.setTockeRute(tockeRute);
		setRuta(ruta);
		spremiRutuBaza();
		
		
		putanja = ("/home/k2/tmp/SeeYou.plr");
		procitajZrakoplovDatoteka(putanja);
		ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();
		listaZrakoplov.addAll(getListaZrakoplov());
		spremiZrakoploveBaza();
		
		procitajTockeBaza();
		
		//ubaciMeteo();
		
//		Tocka T1 = new Tocka();
//		Tocka T2 = new Tocka();
//		Tocka T3 = new Tocka();
//		Tocka T4 = new Tocka();
//		T1.setLat( 44.10);
//		T1.setLon( 13.23);
//		T2.setLat( 46.03);
//		T2.setLon( 13.23);
//		T3.setLat( 44.20);
//		T3.setLon( 16.51);
//		T4.setLat( 46.20);
//		T4.setLon( 16.20);
//		this.ruta.getTockeRute().add(T2);
//		this.ruta.getTockeRute().add(T1);
//		this.ruta.getTockeRute().add(T3);
//		this.ruta.getTockeRute().add(T4);
//		PripremaGFSRuta priprema = new PripremaGFSRuta();
//		priprema.ciscenjeBaze();
//		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//		String datum= "2014-05-27";
//		Date d = formatter.parse(datum);
//		priprema.setDatum(d);
//		priprema.setRuta(ruta);
//		priprema.izracunGFSTocaka();
//		priprema.kompozijaURL();
//		priprema.preuzmiSvePodatke();
//		priprema.spremanjePodatkaBaza();

		
		
	}
	public void ubaciMeteo() throws ParseException{
		//meteo podaci
		Tocka T1 = new Tocka();
		Tocka T2 = new Tocka();
		Tocka T3 = new Tocka();
		Tocka T4 = new Tocka();
		Ruta ruta = new Ruta();
	    T1.setLat( 44.10);
	    T1.setLon( 13.23);
	    T2.setLat( 46.03);
	    T2.setLon( 13.23);
	    T3.setLat( 44.20);
	    T3.setLon( 16.51);
	    T4.setLat( 46.20);
	    T4.setLon( 16.20);
	    ruta.getTockeRute().add(T2);
	    ruta.getTockeRute().add(T1);
	    ruta.getTockeRute().add(T3);
	    ruta.getTockeRute().add(T4);
		PripremaGFSRuta priprema = new PripremaGFSRuta();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		String datum= "2014-05-27";
		Date d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();
		priprema.spremanjePodatkaBaza();
		priprema = new PripremaGFSRuta();
		datum= "2014-05-28";
		d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();
		priprema.spremanjePodatkaBaza();
		priprema = new PripremaGFSRuta();
		datum= "2014-05-29";
		d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();
		priprema.spremanjePodatkaBaza();
		priprema = new PripremaGFSRuta();
		datum= "2014-05-30";
		d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();
		priprema.spremanjePodatkaBaza();
		priprema = new PripremaGFSRuta();
		datum= "2014-05-31";
		d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();
		priprema.spremanjePodatkaBaza();
		priprema = new PripremaGFSRuta();
		datum= "2014-06-01";
		d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();
		priprema.spremanjePodatkaBaza();
		priprema = new PripremaGFSRuta();
		datum= "2014-06-02";
		d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();		
		priprema.spremanjePodatkaBaza();
	}

}
