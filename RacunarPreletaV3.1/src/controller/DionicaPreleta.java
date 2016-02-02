package controller;

import model.Tocka;

public class DionicaPreleta {
	
	public DionicaPreleta(float parametarA, float parametarB, float parametarC,
			float brzinaDizanja, float vjetarBrzina, int vjetarSmjer,
			Tocka tocka1, Tocka tocka2, IzracuniGC izracuniGC,
			double prosjecnaBrzinaDIonice, double potrebnoVrijeme) {
		super();
		this.parametarA = parametarA;
		this.parametarB = parametarB;
		this.parametarC = parametarC;
		this.brzinaDizanja = brzinaDizanja;
		this.vjetarBrzina = vjetarBrzina;
		this.vjetarSmjer = vjetarSmjer;
		this.tocka1 = tocka1;
		this.tocka2 = tocka2;
		this.izracuniGC = izracuniGC;
		this.prosjecnaBrzinaDIonice = prosjecnaBrzinaDIonice;
		this.potrebnoVrijeme = potrebnoVrijeme;
	}


	public DionicaPreleta() {
		// TODO Auto-generated constructor stub
	}


	private double parametarA;
    private double parametarB;
    private double parametarC;
    private double brzinaDizanja;
    private double vjetarBrzina;
    private int vjetarSmjer;
    private Tocka tocka1;
    private Tocka tocka2;
    IzracuniGC izracuniGC = new IzracuniGC();
	public double getProsjecnaBrzinaDIonice() {
		return prosjecnaBrzinaDIonice;
	}


	public void setProsjecnaBrzinaDIonice(double prosjecnaBrzinaDIonice) {
		this.prosjecnaBrzinaDIonice = prosjecnaBrzinaDIonice;
	}


	public long getPotrebnoVrijeme() {
		
		return (long) potrebnoVrijeme;
	}


	public void setPotrebnoVrijeme(double potrebnoVrijeme) {
		this.potrebnoVrijeme = potrebnoVrijeme;
	}


	private double prosjecnaBrzinaDIonice;
	private double potrebnoVrijeme;
    
    public void setTocka1(Tocka tocka1) {
		this.tocka1 = tocka1;
	}
	
	public void setTocka2(Tocka tocka2) {
		this.tocka2 = tocka2;
	}

	public void setParametarA(double d) {
		this.parametarA = d+0.1;
	}

	public void setParametarB(double d) {
		this.parametarB = d;
	}

	public void setParametarC(double d) {
		this.parametarC = d;
	}

	public void setBrzinaDizanja(double brzinaDizanja) {
		this.brzinaDizanja = brzinaDizanja;
	}

	public void setVjetarBrzina(double vjetarBrzina) {
		this.vjetarBrzina = vjetarBrzina;
	}

	public void setVjetarSmjer(int vjetarSmjer) {
		this.vjetarSmjer = vjetarSmjer;
	}
// ------------- izracuni ------------------	

	public void izracunEtape() {
		// XC brzina
	    double brzinaVjetra = vjetarBrzina;
	    int smjerVjetra = vjetarSmjer;

	    double prosjekDizanja = Math.abs(brzinaDizanja*3.6);
	    double Vxco	;// putna brzina bez vjetra
	    double Vpol ;// TSI


	    //izracun parametara duljine i kursa dionice
	    
	    int kurs = izracuniGC.GCkurs(tocka1, tocka2);
	  
	    double duljinaDionice = izracuniGC.udaljenostT1T2(tocka1, tocka2);
	    
	    System.out.println("\n*** dionica - duljina:" + duljinaDionice);
	    System.out.println("*** dionica - meteo: uspono:" + prosjekDizanja);
	    		    
	    //   Racuna XC brzinu i vrijeme na ruti. Parametri: */
	    

	    	    
	    Vpol = Math.sqrt(Math.abs((parametarC - prosjekDizanja) / parametarA));
	    System.out.println("*** dionica - Vpolara: " + Vpol);
	    
	    Vxco = 100*(prosjekDizanja * Vpol)/((2 * prosjekDizanja )-( 2 * parametarC) -( parametarB * Vpol));
	    System.out.println("*** dionica - Vxc0:"+Vxco);
	    
	    int kutVjetra =  Math.abs(kurs - smjerVjetra)  ;
	    double kutVjetraRad = Math.toRadians(kutVjetra);
	    
	    System.out.println("*** dionica - meteo: kutVjRad:" + kutVjetraRad); 
	    System.out.println("*** dionica - meteo: kutVj:" + kutVjetra);
	    System.out.println("*** dionica - meteo: vjetar: "+brzinaVjetra);

	    System.out.println("*** dionica - abc= " + parametarA + " " + parametarB + " " + parametarC); 
	    		
	    prosjecnaBrzinaDIonice = Math.sqrt((Math.pow(Vxco,2))-(Math.pow(brzinaVjetra,2)*((Math.pow(Math.sin(kutVjetraRad),2))- brzinaVjetra * Math.cos(kutVjetraRad))));
	   
	    potrebnoVrijeme = ((duljinaDionice ) / prosjecnaBrzinaDIonice)*3600; //izračun je u sec
	    
	    System.out.println("*** dionica - Prosijecna brzina je" + prosjecnaBrzinaDIonice);
		System.out.println("*** dionica - Potrebno vrijeme je sec" + potrebnoVrijeme);
	}
	// vrijeme preleta dionice

    

	
	
}
