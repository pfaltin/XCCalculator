package ctrl.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Ruta;
import model.Tocka;

import org.junit.Test;

import controller.PripremaGFSRuta;

public class PripremaGFSTest {
	Tocka T1 = new Tocka();
	Tocka T2 = new Tocka();
	Tocka T3 = new Tocka();
	Tocka T4 = new Tocka();
	Ruta ruta = new Ruta();

	

	//@Test
	public void testPripremaGFSRuta() {
		//ruc test lat 44-47 lon 13-17
		this.T1.setLat( 44.30);
		this.T1.setLon( 13.30);
		this.T2.setLat( 47.30);
		this.T2.setLon( 16.30);
		this.T1.setVisina(300);
		this.T2.setVisina(200);
		this.ruta.getTockeRute().add(T1);
		this.ruta.getTockeRute().add(T2);
		assertEquals(1,3);
		
	}
	
	@Test
	public void testDatum() throws ParseException{
		PripremaGFSRuta priprema = new PripremaGFSRuta();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		String datum= "2014-05-27";
		Date d = formatter.parse(datum);
		priprema.setDatum(d);
		System.out.println("Datum :"+priprema.getDatum());
	}

	@Test
	public void testIzracunGFSTocaka() {
		this.T1.setLat( 44.30);
		this.T1.setLon( 13.30);
		this.T2.setLat( 46.59);
		this.T2.setLon( 16.39);
		this.T3.setLat( 45.30);
		this.T3.setLon( 13.10);
		this.ruta.getTockeRute().add(T2);
		this.ruta.getTockeRute().add(T1);
		this.ruta.getTockeRute().add(T3);
		PripremaGFSRuta priprema = new PripremaGFSRuta();
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		Double lat= priprema.getUrlTocke().get(14).getLat();
		
		assertEquals(44.50,lat,0.01);
	}

	@Test
	public void testKompozijaURL() throws ParseException {
		//ruc test lat 44-47 lon 13-17
		this.T1.setLat( 44.10);
		this.T1.setLon( 13.23);
		this.T2.setLat( 46.03);
		this.T2.setLon( 13.23);
		this.T3.setLat( 44.20);
		this.T3.setLon( 16.51);
		this.T4.setLat( 46.20);
		this.T4.setLon( 16.20);
		this.ruta.getTockeRute().add(T2);
		this.ruta.getTockeRute().add(T1);
		this.ruta.getTockeRute().add(T3);
		this.ruta.getTockeRute().add(T4);
		PripremaGFSRuta priprema = new PripremaGFSRuta();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		String datum= "2014-05-27";
		Date d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
	
		assertEquals(63, priprema.getUrlLista().size());
	}

	@Test
	public void testPreuzimanjePodataka() {
		PripremaGFSRuta priprema = new PripremaGFSRuta();
		priprema.preuzimanjePodataka("http://192.168.56.18/ruc/ruc_44.00-15.00_2014-05-27.txt");
		assertEquals(24, priprema.getListaModelGFS().size());
		

	}

	@Test
	public void testSpremanjePodatkaBaza() throws ParseException {
		//ruc test lat 44-47 lon 13-17 datum 2014-04-22 > 2014-05-19
		this.T1.setLat( 44.10);
		this.T1.setLon( 13.23);
		this.T2.setLat( 46.03);
		this.T2.setLon( 13.23);
		this.T3.setLat( 44.20);
		this.T3.setLon( 16.51);
		this.T4.setLat( 46.20);
		this.T4.setLon( 16.20);
		this.ruta.getTockeRute().add(T2);
		this.ruta.getTockeRute().add(T1);
		this.ruta.getTockeRute().add(T3);
		this.ruta.getTockeRute().add(T4);
		PripremaGFSRuta priprema = new PripremaGFSRuta();
		priprema.ciscenjeBaze();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		String datum= "2014-05-27";
		Date d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.izracunGFSTocaka();
		priprema.kompozijaURL();
		priprema.preuzmiSvePodatke();
		priprema.spremanjePodatkaBaza();
		
		
		assertEquals(1512, priprema.getListaModelGFS().size());
	}
	
	@Test
	public void testPripremiGFS() throws ParseException{
		//ruc test lat 44-47 lon 13-17 datum 2014-04-22 > 2014-05-19
		this.T1.setLat( 44.10);
		this.T1.setLon( 13.23);
		this.T2.setLat( 46.03);
		this.T2.setLon( 13.23);
		this.T3.setLat( 44.20);
		this.T3.setLon( 16.51);
		this.T4.setLat( 46.20);
		this.T4.setLon( 16.20);
		this.ruta.getTockeRute().add(T2);
		this.ruta.getTockeRute().add(T1);
		this.ruta.getTockeRute().add(T3);
		this.ruta.getTockeRute().add(T4);
		PripremaGFSRuta priprema = new PripremaGFSRuta();
		priprema.ciscenjeBaze();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
		String datum= "2014-05-27";
		Date d = formatter.parse(datum);
		priprema.setDatum(d);
		priprema.setRuta(ruta);
		priprema.pripremiGFS();
		assertEquals(1512, priprema.getListaModelGFS().size());
		
	}
	

}
