package controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import model.Ruta;
import model.SlojZraka;
import model.Tocka;
import model.TockaMeteo;


public class PripremaGFSRuta {
	
	    public PripremaGFSRuta(Ruta ruta, Date datum, ArrayList<TockaMeteo> tockaGFS) {
		super();
		this.ruta = ruta;
		this.datum = datum;
		this.tockaGFS = tockaGFS;
	}


		public PripremaGFSRuta() {
			// TODO Auto-generated constructor stub
		}
		private Ruta ruta = new Ruta();
	    private Date datum;
	    private ArrayList<TockaMeteo> tockaGFS = new ArrayList<TockaMeteo> ();
		private ArrayList<Tocka> urlTocke = new ArrayList<Tocka>();
		private ArrayList<TockaMeteo>  listaModelGFS = new ArrayList<TockaMeteo>(); 
		
		private ArrayList<String> urlLista = new ArrayList<String>();

		public ArrayList<TockaMeteo> getListaModelGFS() {
			return listaModelGFS;
		}


		public void setListaModelGFS(ArrayList<TockaMeteo> listaModelGFS) {
			this.listaModelGFS = listaModelGFS;
		}





		public ArrayList<String> getUrlLista() {
			return urlLista;
		}


		public void setUrlLista(ArrayList<String> urlLista) {
			this.urlLista = urlLista;
		}


		public ArrayList<Tocka> getUrlTocke() {
			return urlTocke;
		}

		public void setUrlTocke(ArrayList<Tocka> urlTocke) {
			this.urlTocke = urlTocke;
		}


		public Ruta getRuta() {
			return ruta;
		}


		public void setRuta(Ruta ruta) {
			this.ruta = ruta;
		}


		public Date getDatum() {
			return datum;
		}


		public void setDatum(Date datum) {
			this.datum = datum;
		}


		public ArrayList<TockaMeteo> getTockaGFS() {
			return tockaGFS;
		}


		public void setTockaGFS(ArrayList<TockaMeteo> tockaGFS) {
			this.tockaGFS = tockaGFS;
		}


		public void izracunGFSTocaka() {
	    	double latNajVeci = 0 ;
	    	double latNajManji = 90 ;
	    	double lonNajVeci = 0 ;
	    	double lonNajManji = 180;
	    	//System.out.println("ruta od "+this.ruta.getTockeRute().size());
	    	int i=0;
	    	for (i=0; i< this.ruta.getTockeRute().size(); i++){
	    		Tocka tockaRute = new Tocka();
	    		tockaRute = this.ruta.getTockeRute().get(i);
	    		
	    		if (latNajVeci < tockaRute.getLat()) latNajVeci = tockaRute.getLat();
	    		if (latNajManji > tockaRute.getLat()) latNajManji = tockaRute.getLat();
	    		if (lonNajVeci < tockaRute.getLon()) lonNajVeci = tockaRute.getLon();
	    		if (lonNajManji > tockaRute.getLon()) lonNajManji = tockaRute.getLon();
	    		
//	    		System.out.println(i+"ekstremi veći:"+latNajVeci+" manji"+latNajManji+" veći "+lonNajVeci+" manji"+lonNajManji);
//	    		System.out.println(" lat:"+tockaRute.getLat()+" lon"+tockaRute.getLon());
	    	}
//	    		System.out.println("kraj :"+latNajVeci+" "+latNajManji+" "+lonNajVeci+" "+lonNajManji);
    			latNajVeci = Math.ceil(latNajVeci);   
    			latNajManji =  Math.floor(latNajManji);
    			lonNajVeci =  Math.ceil(lonNajVeci);
    			lonNajManji = Math.floor(lonNajManji);
//    			System.out.println("Zaokruzeni:"+latNajVeci+" manji "+latNajManji+" veći "+lonNajVeci+" manji "+lonNajManji);
    			i=0;
    			double j;
    			for ( j= latNajManji; j<=latNajVeci; j= j+0.5){
//        			System.out.println("j"+j +" i"+i);
    				
    				for (double k = lonNajManji; k<=lonNajVeci; k=k+0.5){
//        				System.out.println("k"+k + " i"+ i);
        				Tocka tocka = new Tocka();
        				tocka.setLat(j);
        				tocka.setLon(k);
        				urlTocke.add(tocka);
        				i++;
        			}
    				i++;
    			}
    			i=0;
    	
	    }


	    public void kompozijaURL() {
	    	int i;
	    	for (i=0; i < urlTocke.size(); i++){
	        	NumberFormat form = NumberFormat.getInstance( );
	        	form.setMinimumIntegerDigits(2);
	        	form.setMinimumFractionDigits(2);
	        	form.setMaximumFractionDigits(2);
	        	//http://192.168.56.18/ruc/ruc_44.00-13.00_2014-04-27.txt
	        	//http://192.168.56.18/ruc/ruc_44.00-15.00_2014-05-02.txt
	        	
	        	SimpleDateFormat datumForma = new SimpleDateFormat("yyyy-MM-dd") ;//("EEE, dd MMM yyyy HH:mm:ss");
	        	String datumFormatiran = datumForma.format(datum);
	        	
	        	//System.out.println("URL http://192.168.56.18/ruc/ruc_"+ form.format(urlTocke.get(i).getLat()) +"-"+form.format(urlTocke.get(i).getLon()) +"_"+datumFormatiran+".txt");
	    		// spajanje na testni server
	        	this.urlLista.add("http://192.168.56.18/ruc/ruc_"+ form.format(urlTocke.get(i).getLat()) +"-"+form.format(urlTocke.get(i).getLon()) +"_"+datumFormatiran+".txt");
	        	// spajanje na zivi GFS
	        	//this.url.add("http://rucsoundings.noaa.gov/get_soundings.cgi?data_source=GFS;latest=latest;n_hrs=72;fcst_len=shortest;airport="+ form.format(urlTocke.get(i).getLat()) +"%2C"+form.format(urlTocke.get(i).getLon()) +";text=Ascii%20text%20%28GSD%20format%29;hydrometeors=false&start=latest");
	    			
	    	}

	    }
	    
	    public void preuzmiSvePodatke(){
	    	for (int i=0; i < urlLista.size(); i++){
	    		String url ="";
	    		url = urlLista.get(i);
	    		preuzimanjePodataka(url);	    		
	    		
	    	}
	    	
	    }

	    public void preuzimanjePodataka( String urlMeteo) {
	    	System.out.println("preuzimanje :"+urlMeteo);
	    	 try
	         {
	       		 SlojZraka slojGFS = new SlojZraka();
	       		 TockaMeteo modelGFS = new TockaMeteo();
	       		 ArrayList<SlojZraka>  listaSlojGFS = new ArrayList<SlojZraka>();
	       		 URL url = new URL(urlMeteo);
	       		 URLConnection urlPoveznica = url.openConnection();
	       		 HttpURLConnection poveznica = null;
	   	    		 if(urlPoveznica instanceof HttpURLConnection)
	   	    		 {
	   	    			 poveznica = (HttpURLConnection) urlPoveznica;
	   		         }
	   		         else
	   		         {
	   		            System.out.println("Unesi HTTP URL.");
	   		            return;
	   		         }
	            BufferedReader ulaz = new BufferedReader(new InputStreamReader(poveznica.getInputStream()));
	            String kursor;

	            int l=0;
	        	 String[] slog = new String[19] ;
	            
	            while((kursor = ulaz.readLine()) != null) //provjera ima li još linija
	            {
	                Scanner citacLinije = new Scanner(kursor);
	          	     citacLinije.useDelimiter("\\s+");
	          	  int i = 0;
	          	  // ispis linje
	          	  while (citacLinije.hasNext()){
	      	       i++;
	      	       slog[i] = citacLinije.next();
	          	  }
//	          	System.out.println("+ ---- slog "+slog[1]+", "+slog[2]+" l="+l);

	   		  // -------------------------------------------------------------------------------------
	   		  // Upis procitanog u liste
	      		if (slog[1].equals("GFS") && (slog[2].equals("analysis") || slog[4].equals("forecast"))){
	   			  if (!listaModelGFS.isEmpty()){
	   			  listaModelGFS.get(listaModelGFS.size()-1).setListaSlojZraka(listaSlojGFS);
	   			  listaSlojGFS = new ArrayList<SlojZraka>();
	   			  }
	   			  l=0;
//	   			  System.out.println("---- NOVA PROGNOZA ----\n\n"); 
	   		  	}
	   		if (slog[1].equals("GFS") && l==1){
	   			  modelGFS.setSatGFS(Integer.parseInt(slog[2]));
	   			  modelGFS.setDanGFS(Integer.parseInt(slog[3]));
	   			  modelGFS.setMjesecGFS(mjesecBroj(slog[4]));
	   			  modelGFS.setGodinaGFS(Integer.parseInt(slog[5]));
	   			}

	   		if (slog[1].equals("1") && l==3) {	  
	   			  modelGFS.setLat(Float.parseFloat(slog[4]));
	   			  modelGFS.setLon(Float.parseFloat(slog[5].substring(1)));
//	   			System.out.println("lat-lon :"+modelGFS.getLat()+" "+modelGFS.getLon());
	   			  listaModelGFS.add(modelGFS);
	   			  modelGFS = new TockaMeteo();
	   			  }
	   		
	   		if (slog[1].equals("4") || slog[1].equals("9")) {
	   			 slojGFS.setTlak(Integer.parseInt(slog[2]));
	   			 slojGFS.setVisina(Integer.parseInt(slog[3]));
	   			 slojGFS.setTemperaturaZraka(Integer.parseInt(slog[4]));
	   			 slojGFS.setTemperaturaRose(Integer.parseInt(slog[5]));
	   			 slojGFS.setVjetarSmjer(Integer.parseInt(slog[6]));
	   			 slojGFS.setVjetarBrzina(Integer.parseInt(slog[7]));
	   			 listaSlojGFS.add(slojGFS);
//	   			 System.out.println("sloj "+slojGFS.getTlak()+" "+slojGFS.getVisina()+" "+slojGFS.getTemperaturaZraka()+" "+slojGFS.getTemperaturaRose()+" "+slojGFS.getVjetarSmjer()+" "+slojGFS.getVjetarBrzina());
	   			 slojGFS=new SlojZraka();
	   			 }

		   	if ((slog[1].equals("4") && slog[2].equals("100"))  ) {
		   		listaModelGFS.get(listaModelGFS.size()-1).setListaSlojZraka(listaSlojGFS);
		   			//System.out.println("sloj upisan ");
		   		
		   		}
	   			citacLinije.close();
	   		 	  slog[1]="";
	   		  	  slog[9]="";
	   		  	  l++;
	            }  // kraj citanja jedne linije

	            //ispis procitanog i spremnjenog u liste
//	            System.out.println("\n\n Upisano je prognoza :"+listaModelGFS.size());
//	            for (  int j = 0; j<listaModelGFS.size();j++ ){
//	                System.out.println("Prognoza za: "+listaModelGFS.get(j).getSatGFS()+" "+listaModelGFS.get(j).getDanGFS()+" "+listaModelGFS.get(j).getMjesecGFS()+" "+listaModelGFS.get(j).getGodinaGFS() );
//	                System.out.println("slojeva :"+listaModelGFS.get(j).getListaSlojZraka().size());
//	                for (  int e = 0;e<=listaModelGFS.get(j).getListaSlojZraka().size()-1;e++ ){
//	                System.out.println(listaModelGFS.get(j).getListaSlojZraka().get(e).getVisina());
//	                }
//	            }
	         }//kraj try
	         catch(IOException e)
	         {
	            e.printStackTrace();
	         }
	     }



	    public void spremanjePodatkaBaza() throws ParseException {
	    	BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
	    	veza.insertMeteo(listaModelGFS);
	    	
	    }
	    public void pripremiGFS() throws ParseException{
	    	if (ruta != null || datum !=null){
			izracunGFSTocaka();
			kompozijaURL();
			preuzmiSvePodatke();
			spremanjePodatkaBaza();
			System.out.println("Priprema GFS gotova");
	    	}
	    	System.out.println("Priprema GFS - nema rute ili datuma");
	    	
	    }
	    
	   
	    public void ciscenjeBaze(){
	    	BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
	    	veza.ciscenjeMeteoBaze();
	    	
	    }
		 
		  static int mjesecBroj(String mjesec)
		  {
				int mjesecBroj = 0;
				
				if(mjesec.equals("Jan")) mjesecBroj = 1;
				if(mjesec.equals("Feb")) mjesecBroj = 2;
				if(mjesec.equals("Mar")) mjesecBroj = 3;
				if(mjesec.equals("Apr")) mjesecBroj = 4;
				if(mjesec.equals("May")) mjesecBroj = 5;
				if(mjesec.equals("Jun")) mjesecBroj = 6;
				if(mjesec.equals("Jul")) mjesecBroj = 7;
				if(mjesec.equals("Aug")) mjesecBroj = 8;
				if(mjesec.equals("Sep")) mjesecBroj = 9;
				if(mjesec.equals("Oct")) mjesecBroj = 10;
				if(mjesec.equals("Nov")) mjesecBroj = 11;
				if(mjesec.equals("Dec")) mjesecBroj = 12;
					
				return mjesecBroj;
			 }



}
