package controller;

import java.util.ArrayList;
import java.util.Random;

import model.PopisPrognoza;
import model.PrognozaProstorVrijeme;
import model.Ruta;
import model.RutaPreletaIzracun;
import model.SlojZraka;
import model.Tocka;
import model.TockaIzracunaPreleta;
import model.Zrakoplov;

public class Prelet {
	
    private String datumPreleta;
    private long vrijemeStarta ; //vrijeme u sec od 00:00
    private Zrakoplov zrakoplov;
    private Ruta ruta;
    private PrognozaProstorVrijeme prognozaProstorVrijemeGFS = new PrognozaProstorVrijeme();
    private RutaPreletaIzracun rutaPreletaIzracun = new RutaPreletaIzracun();
    String ispisPreleta = "";
	private SlojZraka meteoRucno;
    private IzracuniGC izracuniGC = new IzracuniGC() ;
    private boolean semafor = true; 

	public boolean isSemafor() {
		return semafor;
	}


	public void setSemafor(boolean semafor) {
		this.semafor = semafor;
	}


	public model.RutaPreletaIzracun getRutaPreletaIzracun() {
		return rutaPreletaIzracun;
	}


	public void setDatumPreleta(String datump) {
		datumPreleta = datump;
		}


	public void setVrijemeStarta(long vrijemeStarta) {
		this.vrijemeStarta = vrijemeStarta*3600; //spremanje uz konverziju sati u sekunde dana
	}


	public void setZrakoplov(Zrakoplov zrakoplov) {
		this.zrakoplov = zrakoplov;
	}


	public Zrakoplov getZrakoplov() {
		return zrakoplov;
	}


	public String getDatumPreleta() {
		return datumPreleta;
	}


	public long getVrijemeStarta() {
		return vrijemeStarta;
	}


	public Ruta getRuta() {
		return ruta;
	}


	public PrognozaProstorVrijeme getPrognozaProstorVrijemeGFS() {
		return prognozaProstorVrijemeGFS;
	}


	public SlojZraka getMeteoRucno() {
		return meteoRucno;
	}


	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}
	
	public String getIspisPreleta() {
		return ispisPreleta;
	}

	public void setDionicaPreleta(ArrayList<DionicaPreleta> dionicaPreleta) {
	}


	public void setPrognozaProstorVrijemeGFS(
			PrognozaProstorVrijeme prognozaProstorVrijemeGFS) {
		this.prognozaProstorVrijemeGFS = prognozaProstorVrijemeGFS;
	}


	public void setMeteoRucno(SlojZraka slojZraka) {
		this.meteoRucno = slojZraka;
	}


	public void setIzracuniGC(IzracuniGC izracuniGC) {
		this.izracuniGC = izracuniGC;
	}
	



	
   
	public void IzracunPrelet() {
		setSemafor(false);
		String status = provjeriPodatke();
		if (!status.equals("ok")) {
			ispisPreleta="Problemi sa podacima";
			return;
		}
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		int brojTocakaRute =ruta.getTockeRute().size();
	
		// unesi točke u rutu-izračun
		int i=0;
		for (i =0;i<ruta.getTockeRute().size();i++){
			TockaIzracunaPreleta tockaIzracuna = new TockaIzracunaPreleta();
			tockaIzracuna.setNaziv(ruta.getTockeRute().get(i).getNaziv());
			tockaIzracuna.setLat(ruta.getTockeRute().get(i).getLat());
			tockaIzracuna.setLon(ruta.getTockeRute().get(i).getLon());
			tockaIzracuna.setVisina(ruta.getTockeRute().get(i).getVisina());
			rutaPreletaIzracun.getTockaIzracunaPreleta().add(tockaIzracuna);
		}
		String naziv = (rutaPreletaIzracun.getTockaIzracunaPreleta().get(0).getNaziv()+"-"+
				rutaPreletaIzracun.getTockaIzracunaPreleta().get(brojTocakaRute-1).getNaziv()+"_"+datumPreleta);
		//provjeri sve podatke
		rutaPreletaIzracun.setNaziv(naziv );
		rutaPreletaIzracun.setZrakoplov(zrakoplov);
		Random id = new Random( );

		long id_izracuna = Math.abs(id.nextInt());
		
		rutaPreletaIzracun.setId_izracuna(id_izracuna );
		System.out.println("Ruta preleta se zove "+rutaPreletaIzracun.getNaziv()+" i ima tocaka "+rutaPreletaIzracun.getTockaIzracunaPreleta().size());
		// unesi zrakolpov u rutu
		rutaPreletaIzracun.setZrakoplov(zrakoplov);
		
		//uzmi prvu tocku rute i izracunaj pocetak
		if (meteoRucno==null){
			PopisPrognoza pocetakSat = new PopisPrognoza();
			pocetakSat=((veza.selectPocetak(rutaPreletaIzracun.getTockaIzracunaPreleta().get(0), datumPreleta, zrakoplov.getPropadanje())));
			vrijemeStarta= (pocetakSat.getSat()*3600);
			if (pocetakSat.getSat()<10)vrijemeStarta=10*3600;			
			System.out.println("vrijeme start: "+vrijemeStarta/3600);
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(0).setVrijemeDolaska( vrijemeStarta);// upis pocetka preleta sekunde dana
			rutaPreletaIzracun.setDatumPreleta(datumPreleta); // upis datuma preleta u rutu izracuna
			}
		else {
			
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(0).setVrijemeDolaska( vrijemeStarta);// upis pocetka preleta sekunde dana
			rutaPreletaIzracun.setDatumPreleta(datumPreleta); // upis datuma preleta u rutu izracuna
			
		}
		
		//izracun dionica iz rute
		double brzina =0;
		for (int j = 0 ; j<rutaPreletaIzracun.getTockaIzracunaPreleta().size()-1; j++){
			TockaIzracunaPreleta dionicaIzracun = new TockaIzracunaPreleta();
			
			dionicaIzracun = izracunDionicePreleta(
					rutaPreletaIzracun.getTockaIzracunaPreleta().get(j),
					rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1),
					Integer.valueOf((int) (rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVrijemeDolaska()/3600)));
			//upis rezultata u krajnju tocku dionice
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).setBrzinaEtapa(dionicaIzracun.getBrzinaStart());
			brzina = brzina+dionicaIzracun.getBrzinaStart();
			if (j!=0) {
				rutaPreletaIzracun.getTockaIzracunaPreleta().get(j + 1).setBrzinaStart(brzina / (j+1));
						}
			else {
				rutaPreletaIzracun.getTockaIzracunaPreleta().get(j + 1).setBrzinaStart(brzina);
				}
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).setTrajanjeEtapa(dionicaIzracun.getTrajanjeStart());
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).setTrajanjeStart
				(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getTrajanjeStart()+dionicaIzracun.getTrajanjeStart());
			
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).setVrijemeDolaska
				(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVrijemeDolaska()+dionicaIzracun.getTrajanjeStart());
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).setDuljinaEtape( izracuniGC.udaljenostT1T2(
					rutaPreletaIzracun.getTockaIzracunaPreleta().get(j),
					rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1)));
			
			//upis meteo rezultata u pocetnu tocku dionice
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).setDizanjeBrzina(prognozaProstorVrijemeGFS.getDizanjeBrzina());
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).setDizanjeVisina(prognozaProstorVrijemeGFS.getDizanjeVisina());
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).setVjerojatnostCb(prognozaProstorVrijemeGFS.getVjerojatnostCb());
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).setVjerojatnostStratus(prognozaProstorVrijemeGFS.getVjerojatnostStratus());
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).setVjetarBrzina(prognozaProstorVrijemeGFS.getVjetarBrzina());
			rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).setVjetarSmjer(prognozaProstorVrijemeGFS.getVjetarSmjer());
			System.out.println("\n\n ---- \nizracun "+j+". dionice preleta sa trajanjem "+dionicaIzracun.getTrajanjeStart()+"\n-----\n");
		}
		//spremanje preleta u bazu
		veza.insertRutaPreletaIzracun(rutaPreletaIzracun);
		
		//ispis preleta
		for (int k = 1 ; k<rutaPreletaIzracun.getTockaIzracunaPreleta().size(); k++){
			if (k==1){
				ispisPreleta= ispisPreleta + (" Prelet je ruta " + rutaPreletaIzracun.getNaziv()
						+" i počinje u "+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getVrijemeDolaska()/3600)) +" sati"); 
				
			} 
			ispisPreleta= ispisPreleta+"\n\nL di: "+ String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getDuljinaEtape())+"km";
			ispisPreleta= ispisPreleta+"  V "+k+". dion: "+ String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getBrzinaEtapa())+"km/h";
			ispisPreleta= ispisPreleta+" Vpr: "+ String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getBrzinaStart())+"km/h";
			long trajanjeSec = rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getVrijemeDolaska(); 
			int sati = (int) Math.floor(trajanjeSec/3600); 
			int minute = (int) ((trajanjeSec/60)%60) ;
			int sekunde = (int) ((trajanjeSec%60)) ;
			
			ispisPreleta= ispisPreleta+("\nT dol : "+sati+" h "+minute+" m "+sekunde+" s");
			//ispisPreleta= ispisPreleta+" VrijemeDolaska : "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getVrijemeDolaska());
			ispisPreleta= ispisPreleta+" T dio: "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getTrajanjeEtapa()+"s");
			ispisPreleta= ispisPreleta+" T leta: "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getTrajanjeStart()+"s");
			
			ispisPreleta= ispisPreleta+"\nW: "+ String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(k-1).getDizanjeBrzina())+"m/s";
			ispisPreleta= ispisPreleta+" Hb: "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k-1).getDizanjeVisina());
			ispisPreleta= ispisPreleta+"m Vv: "+ String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(k-1).getVjetarBrzina());
			ispisPreleta= ispisPreleta+" iz : "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k-1).getVjetarSmjer());

			ispisPreleta= ispisPreleta+" Kind: "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k-1).getVjerojatnostCb());
			ispisPreleta= ispisPreleta+"  St: "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k-1).getVjerojatnostStratus());

			//ispisPreleta= ispisPreleta+"\nNapomene: "+String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(k).getNapomena());
		}
		//setSemafor(true);
		
    }

   

    
    public void izracunMeteoTocka( Tocka tocka, String datum, int sat) {
    	if (meteoRucno==null){
	    	BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
	    	sat = sat - sat%3; 
	    	System.out.println("izracun dionice po GFS, slanje meteo: "+datumPreleta+" "+sat);
			prognozaProstorVrijemeGFS=veza.selectPrognoza(tocka,  datum, sat );
			rutaPreletaIzracun.setMetodaMeteo(4);
	    	}
    	else {
    		System.out.println("---***------ izracun dionice prema rucnom unosu ---***------ ");
    		double visinaOblaka = (meteoRucno.getTemperaturaZraka() - meteoRucno.getTemperaturaRose())*122;
    		double dizanje=(-10+(0.078* visinaOblaka*3.28))*0.00508;
    		prognozaProstorVrijemeGFS.setDizanjeBrzina(dizanje);
    		prognozaProstorVrijemeGFS.setDizanjeVisina((int) visinaOblaka);
    		prognozaProstorVrijemeGFS.setVjetarBrzina(meteoRucno.getVjetarBrzina());
    		prognozaProstorVrijemeGFS.setVjetarSmjer(meteoRucno.getVjetarSmjer());
    		rutaPreletaIzracun.setMetodaMeteo(3);
    		
    	}
    	
    }
    
	public TockaIzracunaPreleta izracunDionicePreleta(Tocka tD1,Tocka tD2, long vrijemePocetka) {
		ArrayList<Tocka> tockeEtapa = new ArrayList<Tocka>();
		//formiraj etape
		tockeEtapa.addAll(formirajEtape(tD1, tD2));
		System.out.println("Tocaka RucIz etapa formirano je:"+tockeEtapa.size()); 
		model.RutaPreletaIzracun dionicaIzracuniEtapa = new model.RutaPreletaIzracun();
		
		int i=0;
		for (i =0;i<tockeEtapa.size();i++){
			TockaIzracunaPreleta tockaIzracuna = new TockaIzracunaPreleta();
			tockaIzracuna.setNaziv(tockeEtapa.get(i).getNaziv());
			tockaIzracuna.setLat(tockeEtapa.get(i).getLat());
			tockaIzracuna.setLon(tockeEtapa.get(i).getLon());
			tockaIzracuna.setVisina(tockeEtapa.get(i).getVisina());
			dionicaIzracuniEtapa.getTockaIzracunaPreleta().add(tockaIzracuna);
		}
		System.out.println("Dionica je na podijeljena na tocaka:"+dionicaIzracuniEtapa.getTockaIzracunaPreleta().size()); 
		
		//upisi vrijeme pocetka u prvu tocku
		dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(0).setVrijemeDolaska(vrijemePocetka*3600);
		int sat = 0;
		for ( i = 0 ; i<dionicaIzracuniEtapa.getTockaIzracunaPreleta().size()-1; i++){
			DionicaPreleta dionicaPreleta = new DionicaPreleta();
			//postavi paramatere izracuna T1, T2, vrijeme pocetka, meteo, abc parametre
			dionicaPreleta.setTocka1(tockeEtapa.get(i));
			dionicaPreleta.setTocka2(tockeEtapa.get(i+1));
			
			//uzmi pocetnu tocku etape i racunaj meteo za nju
			sat = (int) dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i).getVrijemeDolaska()/3600;
			izracunMeteoTocka(tockeEtapa.get(i),datumPreleta,sat);

			// upis meteo za izracun
			dionicaPreleta.setBrzinaDizanja(prognozaProstorVrijemeGFS.getDizanjeBrzina());
			dionicaPreleta.setVjetarBrzina(prognozaProstorVrijemeGFS.getVjetarBrzina());
			dionicaPreleta.setVjetarSmjer(prognozaProstorVrijemeGFS.getVjetarSmjer());
			
			dionicaPreleta.setParametarA(zrakoplov.getParA());
			dionicaPreleta.setParametarB(zrakoplov.getParB());
			dionicaPreleta.setParametarC(zrakoplov.getParC());
			
		    if (prognozaProstorVrijemeGFS.getDizanjeBrzina()<0.1){ // ako je dizanje blizu nule prekini
		    	
				TockaIzracunaPreleta zadnjaTocka = new TockaIzracunaPreleta();
				zadnjaTocka.setBrzinaEtapa(0);
				zadnjaTocka.setDizanjeBrzina(0);
				zadnjaTocka.setTrajanjeEtapa(24*3600);
				zadnjaTocka.setBrzinaStart(0);
				
		    	return zadnjaTocka;
		    	}
			//izracunaj etapu
			dionicaPreleta.izracunEtape();

			// upis rezultata u T2 etape
			double brzinaEtapa = 0; //brzina od predhodne tocke - izracunato funkcijom
			brzinaEtapa = dionicaPreleta.getProsjecnaBrzinaDIonice();
			dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i+1).setBrzinaEtapa(brzinaEtapa);
			
			long trajanjeEtapa = 0; //trajanje leta od predhodne tocke - izracunato funkcijom
			trajanjeEtapa=dionicaPreleta.getPotrebnoVrijeme();
			dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i+1).setTrajanjeEtapa(trajanjeEtapa);
			
			long vrijemeDolaska = 0;//na tocku, vrijeme od 00:00
			vrijemeDolaska = dionicaPreleta.getPotrebnoVrijeme() + dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i).getVrijemeDolaska();
				dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i+1).setVrijemeDolaska(vrijemeDolaska);
			
			long trajanjeStart = 0;  //trajanje leta pocetne tocke rute - suma svih od pocetka ili vrijednost sa predhodne + trajanjeEtapa
			trajanjeStart = dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i).getTrajanjeStart()+trajanjeEtapa;
			dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i+1).setTrajanjeStart(trajanjeStart);
			
			double brzinaStart = 0; //brzina od pocetne tocke rute - prosjek (suma V /broj etapa (tocka-1))
			brzinaStart = 3600*izracuniGC.udaljenostT1T2(dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(0), dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i+1))/trajanjeStart;
			dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(i+1).setBrzinaStart(brzinaStart);
			
			System.out.println("i ="+i+" Upis izracuna T2:  brzinaEtapa "+brzinaEtapa+" trajanjeEtapa "+trajanjeEtapa+" vrijemeDolaska "+vrijemeDolaska+" trajanjeStart "+trajanjeStart+" brzinaStart "+brzinaStart);
			
		}
		
		TockaIzracunaPreleta zadnjaTocka = new TockaIzracunaPreleta();
		int index = dionicaIzracuniEtapa.getTockaIzracunaPreleta().size()-1;
		zadnjaTocka = dionicaIzracuniEtapa.getTockaIzracunaPreleta().get(index);
		
		return zadnjaTocka;

		
		
		
		
		
	}
	public ArrayList<Tocka> formirajEtape(Tocka tD1,Tocka tD2) {
		Tocka tockaEtape = new Tocka();
		ArrayList<Tocka> tockeEtapa = new ArrayList<Tocka>();
		tockeEtapa.add(tD1);
		izracuniGC.setTocka1(tD1);	
		
		double udaljenost = izracuniGC.udaljenostT1T2(tD1, tD2);
		double faktorRazdaljine = (udaljenost/(Math.ceil(udaljenost/zrakoplov.getMinBrzina())));//određivanje faktora dijeljenja dionice
		System.out.println("udaljenost "+udaljenost+" faktorRazdaljine: "+faktorRazdaljine);
		int kursDionice = izracuniGC.GCkurs(tD1, tD2);
		//formiraj etape
//		ako l T1-T2 > V1/2 km
		
		if (udaljenost > faktorRazdaljine){
			
			while (izracuniGC.udaljenostT1T2(tockeEtapa.get((tockeEtapa.size())-1), tD2) > faktorRazdaljine){
//				lT-Te1 = T1 + V1/3 km @ kurs
				System.out.println("kursDionice: "+kursDionice+" faktor"+faktorRazdaljine);
			
			tockaEtape=izracuniGC.tockaNaKursu(kursDionice, faktorRazdaljine);
			System.out.println("\n- * - * - Tocka na kursu je  "+tockaEtape.getNaziv()+" lat"+tockaEtape.getLat()+"lon"+tockaEtape.getLon());
			tockeEtapa.add(tockaEtape);
			izracuniGC.setTocka1(tockaEtape);	
			tockaEtape = new Tocka();
			}
		}
		else {
//			ako l Te1-T2 < V1/2 km -> zapiši rutu
			tockeEtapa.add(tD1);tockeEtapa.add(tD2);
			return tockeEtapa;
		}
		tockeEtapa.add(tD2);//zavrsi dionicu zadnjom tockom
		System.out.println("Tocaka etapa formirano je:"+tockeEtapa.size()); 
		return tockeEtapa;
   }

	public String provjeriPodatke(){
		if (datumPreleta==null)return "Nedostaje datum, odaberi datum";
		if (ruta==null) return "Nedostaje ruta preleta!\nOdaberi rutu.";
		if (zrakoplov==null) return "Nedostaje zrakoplov!\nOdaberi zrakoplov.";
		BazaVezaPostgreSQL veza = new BazaVezaPostgreSQL();
		PopisPrognoza prognozaSat = new PopisPrognoza();
		prognozaSat = veza.selectPocetak(ruta.getTockeRute().get(0), datumPreleta, 0.1);
		if( prognozaSat == null && meteoRucno ==null) return "Nema meteo podataka!\nPokusajte preuzimanje podataka ili ih unesite rucno.";

		return "ok";			
		}








}
