package ctrl.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.junit.Test;

import model.Ruta;
import model.TockaIzracunaPreleta;
import model.Tocka;
import model.Zrakoplov;
import controller.Prelet;
import controller.PripremaGFSRuta;
import controller.UpravljanjePodacima;

public class PreletTest {






	@Test
	public void testGFSIzracunDionicePreleta() throws ParseException, IOException {
		Tocka T1 = new Tocka();
		Tocka T2 = new Tocka();
		T1.setLat( 46.20);
		T1.setLon( 15.30);
		T2.setLat( 45.30);
		T2.setLon( 16.30);
		T1.setVisina(300);
		T2.setVisina(200);
		Prelet prelet = new Prelet();
		
		
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.ciscenjeCijelaBaze();
		Ruta ruta = new Ruta();
		ruta.getTockeRute().add(T2);
		ruta.getTockeRute().add(T1);

		PripremaGFSRuta priprema = new PripremaGFSRuta();
		priprema.ciscenjeBaze();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		String datum= "2014-05-27";
		Date d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.pripremiGFS();
		
		TockaIzracunaPreleta zadnjaTocka = new TockaIzracunaPreleta();
		ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();

		String putanja = ("/home/k2/tmp/SeeYou.plr");
		upravPodaci.procitajZrakoplovDatoteka(putanja);
		upravPodaci.spremiZrakoploveBaza();
		upravPodaci.procitajZrakoploveBaza();

		listaZrakoplov.addAll(upravPodaci.getListaZrakoplov());
		prelet.setZrakoplov(listaZrakoplov.get(22));
		System.out.println("\nLetjelica "+prelet.getZrakoplov().getNaziv());
		
		prelet.setDatumPreleta("2014-05-27");
		prelet.setVrijemeStarta(12);
		
		zadnjaTocka = prelet.izracunDionicePreleta(T1, T2, 12);
		System.out.println("\nTEST dionica GFS");
		System.out.println(" getTrajanjeStart: "+zadnjaTocka.getTrajanjeStart());
		System.out.println(" getVrijemeDolaska: "+zadnjaTocka.getVrijemeDolaska()+"sec dana");
		System.out.println(" getBrzinaStart: "+zadnjaTocka.getBrzinaStart());
		System.out.println(" getBrzinaEtapa: "+zadnjaTocka.getBrzinaEtapa());
		
		assertEquals(48949,zadnjaTocka.getVrijemeDolaska());
		
		
	}
	@Test
	public void testRucniIzracunDionicePreleta() throws ParseException, IOException {
		Tocka T1 = new Tocka();
		Tocka T2 = new Tocka();
		T1.setLat( 47.20);
		T1.setLon( 15.30);
		T2.setLat( 44.30);
		T2.setLon( 13.30);
		T1.setVisina(300);
		T2.setVisina(200);
		Prelet prelet = new Prelet();
		
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.ciscenjeCijelaBaze();
		TockaIzracunaPreleta zadnjaTocka = new TockaIzracunaPreleta();
		ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();
		
		model.SlojZraka meteoRucniUnos = new model.SlojZraka();
		meteoRucniUnos.setTemperaturaZraka(25);
		meteoRucniUnos.setTemperaturaRose(20);
		meteoRucniUnos.setVjetarSmjer(180);
		meteoRucniUnos.setVjetarBrzina(10);
		prelet.setMeteoRucno(meteoRucniUnos);

		String putanja = ("/home/k2/tmp/SeeYou.plr");
		upravPodaci.procitajZrakoplovDatoteka(putanja);
		upravPodaci.spremiZrakoploveBaza();
		upravPodaci.procitajZrakoploveBaza();

		listaZrakoplov.addAll(upravPodaci.getListaZrakoplov());
		prelet.setZrakoplov(listaZrakoplov.get(22));
		System.out.println("\nLetjelica "+prelet.getZrakoplov().getNaziv());
		prelet.setDatumPreleta("2014-05-27");
		prelet.setVrijemeStarta(12);
		
		zadnjaTocka = prelet.izracunDionicePreleta(T1, T2, 12);
		System.out.println("\nTEST ");
		System.out.println(" getTrajanjeStart: "+zadnjaTocka.getTrajanjeStart());
		System.out.println(" getVrijemeDolaska: "+zadnjaTocka.getVrijemeDolaska());
		long trajanjeSec = zadnjaTocka.getVrijemeDolaska(); 
		int sati = (int) Math.floor(trajanjeSec/3600); 
		int minute = (int) ((trajanjeSec/60)%60) ;
		int sekunde = (int) ((trajanjeSec%60)) ;
		
		System.out.println(" format VrijemeDolaska: "+sati+" sati "+minute+" minuta "+sekunde+" sekundi");
		System.out.println(" getBrzinaStart: "+zadnjaTocka.getBrzinaStart());
		System.out.println(" getBrzinaEtapa: "+zadnjaTocka.getBrzinaEtapa());
		assertEquals(63519,zadnjaTocka.getVrijemeDolaska());
		
		
	}
	
	@Test
	public void testIzracunPreletGFS() throws IOException, ParseException {
		System.out.println("\n\n------------- testIzracunPreletGFS ------------------");
		Prelet prelet = new Prelet();
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.ciscenjeCijelaBaze();	//ciscenje baze zbog stalog unosa istih vrijednosti
		Ruta ruta = new Ruta();
		ArrayList<Tocka> tockeRute = new ArrayList<Tocka>();
		TockaIzracunaPreleta zadnjaTocka = new TockaIzracunaPreleta();
		ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();
		
		// priprema rute
		String putanjaTocke = ("/home/k2/tmp/IstraLika.cup");
		upravPodaci.procitajTockeDatoteka(putanjaTocke);
		upravPodaci.pretvoriOkretneTocke();

		upravPodaci.spremiTockeBaza();		
		upravPodaci.procitajTockeBaza();

		Random id = new Random( );
		ruta.setId_rute(Math.abs(id.nextInt()));
		ruta.setImeRute("Ruta testPrelet");

		tockeRute.add(upravPodaci.getListTocka().get(3));
		tockeRute.add(upravPodaci.getListTocka().get(22));
		tockeRute.add(upravPodaci.getListTocka().get(42));
		tockeRute.add(upravPodaci.getListTocka().get(116));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("1 tocka"+tockeRute.get(0).getNaziv());
		System.out.println("2 tocka"+tockeRute.get(1).getNaziv());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		System.out.println("4 tocka"+tockeRute.get(3).getNaziv());
		ruta.setTockeRute(tockeRute);
		prelet.setRuta(ruta);
		
		//priprema zrakoplov
		String putanjaZrak = ("/home/k2/tmp/SeeYou.plr");
		upravPodaci.procitajZrakoplovDatoteka(putanjaZrak);
		upravPodaci.spremiZrakoploveBaza();
		upravPodaci.procitajZrakoploveBaza();

		listaZrakoplov.addAll(upravPodaci.getListaZrakoplov());
		prelet.setZrakoplov(listaZrakoplov.get(22));
		System.out.println("\nLetjelica "+prelet.getZrakoplov().getNaziv());
		
		prelet.setDatumPreleta("2014-06-01");
		//prelet.setVrijemeStarta(12);
		upravPodaci.setRuta(ruta);
		upravPodaci.setDatum("2014-06-01");
		upravPodaci.spremiGFS();
		
		prelet.IzracunPrelet();
		
		// kontrola izracuna
		zadnjaTocka = prelet.getRutaPreletaIzracun().getTockaIzracunaPreleta().get(prelet.getRutaPreletaIzracun().getTockaIzracunaPreleta().size()-1);
		
		System.out.println("\nIzracun \n"+prelet.getIspisPreleta());
				
		assertEquals(40576,zadnjaTocka.getVrijemeDolaska());
	}

	@Test
	public void testIzracunPreletRucno() throws IOException{
		System.out.println("\n\n------------- testIzracunPreletRucno ------------------");
		Prelet prelet = new Prelet();
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.ciscenjeCijelaBaze();	//ciscenje baze zbog stalog unosa istih vrijednosti
		Ruta ruta = new Ruta();
		ArrayList<Tocka> tockeRute = new ArrayList<Tocka>();
		TockaIzracunaPreleta zadnjaTocka = new TockaIzracunaPreleta();
		ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();
		
		// priprema rute
		String putanjaTocke = ("/home/k2/tmp/IstraLika.cup");
		upravPodaci.procitajTockeDatoteka(putanjaTocke);
		upravPodaci.pretvoriOkretneTocke();

		upravPodaci.spremiTockeBaza();		
		upravPodaci.procitajTockeBaza();

		Random id = new Random( );
		ruta.setId_rute(Math.abs(id.nextInt()));
		ruta.setImeRute("Ruta testPrelet");

		tockeRute.add(upravPodaci.getListTocka().get(3));
		tockeRute.add(upravPodaci.getListTocka().get(22));
		tockeRute.add(upravPodaci.getListTocka().get(42));
		tockeRute.add(upravPodaci.getListTocka().get(116));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("1 tocka"+tockeRute.get(0).getNaziv());
		System.out.println("2 tocka"+tockeRute.get(1).getNaziv());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		System.out.println("4 tocka"+tockeRute.get(3).getNaziv());
		ruta.setTockeRute(tockeRute);
		prelet.setRuta(ruta);
		
		//priprema zrakoplov
		String putanjaZrak = ("/home/k2/tmp/SeeYou.plr");
		upravPodaci.procitajZrakoplovDatoteka(putanjaZrak);
		upravPodaci.spremiZrakoploveBaza();
		upravPodaci.procitajZrakoploveBaza();

		listaZrakoplov.addAll(upravPodaci.getListaZrakoplov());
		prelet.setZrakoplov(listaZrakoplov.get(22));
		System.out.println("\nLetjelica "+prelet.getZrakoplov().getNaziv());
		
		prelet.setDatumPreleta("2014-06-01");
		prelet.setVrijemeStarta(12);

		model.SlojZraka meteoRucniUnos = new model.SlojZraka();
		meteoRucniUnos.setTemperaturaZraka(25);
		meteoRucniUnos.setTemperaturaRose(20);
		meteoRucniUnos.setVjetarSmjer(180);
		meteoRucniUnos.setVjetarBrzina(10);
		prelet.setMeteoRucno(meteoRucniUnos);
		
		prelet.IzracunPrelet();
		
		// kontrola izracuna
		zadnjaTocka = prelet.getRutaPreletaIzracun().getTockaIzracunaPreleta().get(prelet.getRutaPreletaIzracun().getTockaIzracunaPreleta().size()-1);
		
		System.out.println("\nIzracun \n"+prelet.getIspisPreleta());
				
		assertEquals(52722,zadnjaTocka.getVrijemeDolaska());

	}
	@Test
	public void testPreletKontrola(){
		Prelet prelet = new Prelet();
		prelet.IzracunPrelet();
		
		String poruka = prelet.getIspisPreleta();
		assertEquals(poruka, ("Problemi sa podacima") );
	}


	
	

}
