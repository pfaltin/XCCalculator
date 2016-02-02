package controller;

import model.RutaPreletaIzracun;

public class IzracunIspis {



    private RutaPreletaIzracun rutaPreletaIzracun = new RutaPreletaIzracun();
    private String ispisPreleta = "";


	public IzracunIspis(RutaPreletaIzracun rutaPreletaIzracun) {


			
		String zaglavlje = "Naziv_pocetak,"+"Naziv_kraj,"+ "Visina_m,"+ "Dizanje_m/s,"+ "Baza_m,"+ "Vjetar_m/s,"+"Vjetar_smjer,"+"Duljina_dionice,"+
				"V dionica_km/h,"+"Dolazak_na pocetak,"+"Vrijeme_dionica,"+"Dolazak_na kraj,"+"V start_km/h,"+"Vrijeme_ukupno,"+"Duljina_km,"+"Kindex,"+ "Stratus vj.," ;

		ispisPreleta = zaglavlje;
		double  duljinaPreleta = 0;
		for (  int j = 0; j<rutaPreletaIzracun.getTockaIzracunaPreleta().size()-1;j++ ){
			String prikaz = "";
			ispisPreleta = ispisPreleta +"\n"+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getNaziv()));
			ispisPreleta = ispisPreleta +","+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getNaziv()));
//			ispisPreleta = ispisPreleta +(1,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getLat()));
//			ispisPreleta = ispisPreleta +(2,String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getLon()));
			ispisPreleta = ispisPreleta +","+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVisina()));
			ispisPreleta = ispisPreleta +","+(String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getDizanjeBrzina()));
			ispisPreleta = ispisPreleta +","+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getDizanjeVisina()));
			ispisPreleta = ispisPreleta +","+(String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVjetarBrzina()));
			ispisPreleta = ispisPreleta +","+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVjetarSmjer()));
			ispisPreleta = ispisPreleta +","+(String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getDuljinaEtape()));
			ispisPreleta = ispisPreleta +","+(String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getBrzinaEtapa()));

			long trajanjeSec = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j).getVrijemeDolaska(); // 9 "Dolazak\nna pocetak"
			int sati = (int) Math.floor(trajanjeSec/3600); 
			int minute = (int) ((trajanjeSec/60)%60) ;
			if (minute<10) {
				prikaz= sati+":0"+minute;
			}else{
				prikaz= sati+":"+minute;
			}
			ispisPreleta = ispisPreleta +","+(prikaz);
			
			long trajanjeSecE = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getTrajanjeEtapa(); 
			int satiE = (int) Math.floor(trajanjeSecE/3600); 
			int minuteE = (int) ((trajanjeSec/60)%60);
			if (minuteE<10) {
				prikaz= satiE+":0"+minuteE;
			}else{
				prikaz= satiE+":"+minuteE;
			}
			ispisPreleta = ispisPreleta +","+(prikaz);
			
			long trajanjeSecST = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getVrijemeDolaska(); // 9 "Dolazak\nna pocetak"
			int satiST = (int) Math.floor(trajanjeSecST/3600); 
			int minuteST = (int) ((trajanjeSecST/60)%60) ;
			ispisPreleta = ispisPreleta +","+(satiST+":"+minuteST);

			ispisPreleta = ispisPreleta +","+(String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getBrzinaStart()));			
			long trajanjeSecSt = rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getTrajanjeStart(); 
			int satiSt = (int) Math.floor(trajanjeSecSt/3600); 
			int minuteSt = (int) ((trajanjeSec/60)%60) ;
			if (minute<10) {
				prikaz = satiSt+":0"+minuteSt;
			}else{
				prikaz = satiSt+":"+minuteSt;
			}
			ispisPreleta = ispisPreleta +","+(prikaz);

			duljinaPreleta = duljinaPreleta + rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getDuljinaEtape();
			ispisPreleta = ispisPreleta +","+(String.format("%.2f",duljinaPreleta));			
			ispisPreleta = ispisPreleta +","+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getVjerojatnostCb()));
			ispisPreleta = ispisPreleta +","+(String.valueOf(rutaPreletaIzracun.getTockaIzracunaPreleta().get(j+1).getVjerojatnostStratus()));

			}
		
		
		

		int zadnja = rutaPreletaIzracun.getTockaIzracunaPreleta().size()-1;
		String ispisSumaPreleta = "";
		String metoda = "";
		String prikazTrajanjeStart = "";
		long trajanjeSecStr = rutaPreletaIzracun.getTockaIzracunaPreleta().get(zadnja).getTrajanjeStart(); 
		int satiStr = (int) Math.floor(trajanjeSecStr/3600); 
		int minuteStr = (int) ((trajanjeSecStr/60)%60) ;
		prikazTrajanjeStart = satiStr+" sati i "+minuteStr+" minuta.";
		long polazak = rutaPreletaIzracun.getTockaIzracunaPreleta().get(0).getVrijemeDolaska(); 
		int satiP = (int) Math.floor(polazak/3600); 
		long dolazak = rutaPreletaIzracun.getTockaIzracunaPreleta().get(zadnja).getVrijemeDolaska(); 
		int satiD = (int) Math.floor(dolazak/3600); 
		int minuteD = (int) ((dolazak/60)%60) ;
		String prikazDolazak= satiD+" sati i "+minuteD+"minuta.";
		if (rutaPreletaIzracun.getMetodaMeteo() == 4) metoda = "dobiveni sa poslužitelja ESRL/GSD. kod:";
		if (rutaPreletaIzracun.getMetodaMeteo() == 3) metoda = "su uneseni rucno. kod:";
		ispisSumaPreleta = "Prelet po ruti: "+rutaPreletaIzracun.getNaziv()
				+"\nna zrakoplovu: "+rutaPreletaIzracun.getZrakoplov().getNaziv()
				+" ,na dan:"+rutaPreletaIzracun.getDatumPreleta()
				+"\nMeteo podaci "+metoda+" "+rutaPreletaIzracun.getMetodaMeteo()
				+"\nUkupno vrijeme preleta "+prikazTrajanjeStart
				+"\nDuljina rute preleta je "+String.format("%.2f",duljinaPreleta)+"km"
				+"\nOcekivana brzina preleta je "+String.format("%.2f",rutaPreletaIzracun.getTockaIzracunaPreleta().get(zadnja).getBrzinaStart())+"km/h"
				+"\nTrajanje preleta je "+prikazTrajanjeStart
				+"\nPocetak preleta je u "+satiP+" sati\nDolazak na cilj u "+prikazDolazak+"\n\n";
		
		
		ispisPreleta = ispisSumaPreleta + ispisPreleta ;
	
		

	}

	public RutaPreletaIzracun getRutaPreletaIzracun() {
		return rutaPreletaIzracun;
	}

	public void setRutaPreletaIzracun(RutaPreletaIzracun rutaPreletaIzracun) {
		this.rutaPreletaIzracun = rutaPreletaIzracun;
		
	}

	public String getIspisPreleta() {
		return ispisPreleta;
	}

	public void setIspisPreleta(String ispisPreleta) {
		this.ispisPreleta = ispisPreleta;
	}

}
