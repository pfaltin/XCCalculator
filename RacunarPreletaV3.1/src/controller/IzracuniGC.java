package controller;

import java.text.NumberFormat;

import model.Tocka;

public class IzracuniGC {
    public IzracuniGC(Tocka tocka1, Tocka tocka2, int kurs) {
		super();
		this.tocka1 = tocka1;
		this.tocka2 = tocka2;
		this.kurs = kurs;
	}


	public IzracuniGC() {
		
	}


	private Tocka tocka1;
    
    private Tocka tocka2;
    
    private int kurs;
    
    public Tocka getTocka1() {
		return tocka1;
	}


	public void setTocka1(Tocka tocka1) {
		this.tocka1 = tocka1;
	}


	public Tocka getTocka2() {
		return tocka2;
	}


	public void setTocka2(Tocka tocka2) {
		this.tocka2 = tocka2;
	}


	public int getKurs() {
		return kurs;
	}


	public void setKurs(int kurs) {
		this.kurs = kurs;
	}


	public double udaljenostT1T2(Tocka tocka1, Tocka tocka2) {
		// funkcija za izracun udaljenosti dvije geografske koordinate
		
	
			double lat1 = tocka1.getLat();
			double lon1 = tocka1.getLon();
			double lat2 = tocka2.getLat();
			double lon2 = tocka2.getLon();
			
			double dlat, dlon;
		    double aGC,cGC;
		    int polumjer = 6371;
		    lat1 =Math.toRadians (lat1);
		    lon1 =Math.toRadians (lon1);
		    lat2 =Math.toRadians (lat2);
		    lon2 =Math.toRadians (lon2);
		    dlat = (lat2-lat1);
		    dlon = (lon2-lon1);
		    aGC = Math.sin(dlat/2)*Math.sin(dlat/2) + ( Math.sin(dlon/2)*Math.sin(dlon/2) )*Math.cos(lat1)*Math.cos(lat2);

		    cGC = 2 * Math.atan2(Math.sqrt(aGC),Math.sqrt(1-aGC));
		    double duljinaDionice = polumjer * cGC;
		    return duljinaDionice;
		
		}
		
		
	// funkcija za izracun kursa između dvije tocke
	public int GCkurs(Tocka tocka1, Tocka tocka2) {
		double lat1 = tocka1.getLat();
		double lon1 = tocka1.getLon();
		double lat2 = tocka2.getLat();
		double lon2 = tocka2.getLon();
			
			double dlon ;
			lat1 =Math.toRadians (lat1);
		    lon1 =Math.toRadians (lon1);
		    lat2 =Math.toRadians (lat2);
		    lon2 =Math.toRadians (lon2);
		    dlon = (lon2-lon1);
			double yBear, xBear, bear;
			
		    yBear = Math.sin(dlon) * Math.cos(lat2);
		    xBear = Math.cos(lat1)*Math.sin(lat2) -
		            Math.sin(lat1)*Math.cos(lat2)*Math.cos(dlon);
		   bear = Math.atan2(yBear, xBear);
		   this.kurs = (int) Math.toDegrees(bear);
		    return kurs;
		     
		}

	// funkcija za izracun projektirane tocke prema kursu i udaljenosti od referntne tocke
    public Tocka tockaNaKursu(int brng,double dist) {
    	
    	  double udaljenost =0;
    	  udaljenost = dist/6371;  // konverzija udaljenost u kutnu udaljenost in radians
    	  double kursR = Math.toRadians(brng); 
    	  double lat1 = Math.toRadians(tocka1.getLat()), 
    			  lon1 = Math.toRadians(tocka1.getLon());
//    	  System.out.println("Ulaz:"+tocka1.getNaziv()+" lat:"+tocka1.getLat()+" lon:"+tocka1.getLon()+" kurs:"+brng+" daljina:"+dist);
//    	  System.out.println("Radijani:"+" lat:"+lat1+" lon:"+lon1+" kursR:"+kursR+" kutna udalj"+udaljenost);
    	  
    	  double lat2 = Math.asin( Math.sin(lat1)*Math.cos(udaljenost) + Math.cos(lat1)*Math.sin(udaljenost)*Math.cos(kursR));
//    	  System.out.println("lat2:"+lat2);
    	  double lon2 = lon1 + Math.atan2(Math.sin(kursR)*Math.sin(udaljenost)*Math.cos(lat1), 
    	                               Math.cos(udaljenost)-Math.sin(lat1)*Math.sin(lat2));
//    	  System.out.println("lon2:"+lon2);
    	  lon2 = (lon2+3*Math.PI)%(2*Math.PI) - Math.PI;  // normalizacija -180...+180
    	  Tocka tockaNaKursu = new Tocka();
    	  tockaNaKursu.setNaziv("kurs"+brng);
    	  tockaNaKursu.setLat(Math.toDegrees(lat2));
    	  tockaNaKursu.setLon(Math.toDegrees(lon2));
    	  tockaNaKursu.setNapomena("od tocke "+lat1+lat2);
//    	  System.out.println("Naziv:"+tockaNaKursu.getNaziv()+" lat:"+tockaNaKursu.getLat()+" lon:"+tockaNaKursu.getLon()+" napom:"+tockaNaKursu.getNapomena());
    	  return tockaNaKursu;
    }

 // funkcija za parsiranje i konverziju tocaka iz datoteke formata cup  
    public double stupMinDecimal(String stupMinSec) {
    	//br:117 ZBEVNI,ZBEVNI, ,4527.383N,01400.917E,855.0m,1, , , ,desc
    	double stup = 0, min = 0;// sec = 0; 
    	//System.out.println("Ulaz SMS :"+ stupMinSec + " duljine "+stupMinSec.length());
    		if (stupMinSec.length()==9){
				stup = Double.parseDouble(stupMinSec.substring(0,2));
				min = Double.parseDouble(stupMinSec.substring(2,4));
				//sec = Double.parseDouble(stupMinSec.substring(5,8));
				String nSEW=stupMinSec.substring(8);	
				if (nSEW.equals("S") || nSEW.equals("W")) stup = 0 - stup;
			}
			if (stupMinSec.length()==10){
				stup = Double.parseDouble(stupMinSec.substring(0,3));
				min = Double.parseDouble(stupMinSec.substring(3,9));
				//sec = Double.parseDouble(stupMinSec.substring(6,9));
				String nSEW=stupMinSec.substring(9);	
				if (nSEW.equals("S") || nSEW.equals("W")) stup = 0 - stup;
			}
		
    	   double stupDec = stup + min/60; // + sec/3600;
    	   //System.out.println("izlaz: "+stupDec + " od stup:"+stup +" min:"+min);// +" sec:"+sec);
    	   return stupDec;
    }

    
    public String decimalMinSec(double decimal) {
    	String stupMin = null;
    	int stup = (int) decimal;
    	double minute = (decimal - (double) stup)*60;
    	//System.out.println("ulaz "+decimal+" stupnjeva:"+stup+" minuta: "+minute);
    	NumberFormat form = NumberFormat.getInstance( );
    	form.setMinimumIntegerDigits(2);
    	form.setMinimumFractionDigits(3);
    	form.setMaximumFractionDigits(3);
    	
    	stupMin = stup + form.format(minute);
    	//System.out.println("izlaz:"+stupMin);
    	
		return stupMin;
    }

    
    
}
