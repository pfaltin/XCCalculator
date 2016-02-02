package ctrl.test;

import static org.junit.Assert.*;
import model.Tocka;

import org.junit.Test;

import controller.IzracuniGC;

public class IzracuniGCTest {
	Tocka T1 = new Tocka();
	Tocka T2 = new Tocka();
	int kurs = 0;
	


	@Test
	public void testIzracuniGC() {

		
		IzracuniGC izracun = new IzracuniGC(T1,T2,kurs);
		assertEquals(izracun.getTocka1(), T2);
	}



	@Test
	public void testSetTocka1() {
		this.T1.setLat( 45.30);
		this.T1.setLon( 13.30);
		this.T2.setLat( 46.30);
		this.T2.setLon( 14.30);
		this.T1.setVisina(300);
		this.T2.setVisina(200);
		this.kurs = 0;
		assertEquals(T1.getLon(), 13.30,0.02);
		
	}

	@Test
	public void testGetTocka2() {
		this.T1.setLat( 45.30);
		this.T1.setLon( 13.30);
		this.T2.setLat( 46.30);
		this.T2.setLon( 14.30);
		this.T1.setVisina(300);
		this.T2.setVisina(200);
		this.kurs = 0;
		assertEquals(T2.getLon(), 14.30,0.02);
	}



	@Test
	public void testGCkurs() {
		this.T1.setLat( 45.30);
		this.T1.setLon( 13.30);
		this.T2.setLat( 46.30);
		this.T2.setLon( 14.30);
		this.T1.setVisina(300);
		this.T2.setVisina(200);
		this.kurs = 0;
		IzracuniGC izracun = new IzracuniGC();
		int iZkurs= izracun.GCkurs(T1, T2);
		assertEquals(iZkurs, 34);
	}

	@Test
	public void testSetKurs() {
		this.kurs = 18;
		IzracuniGC izracun = new IzracuniGC();
		 izracun.setKurs(kurs);
		assertEquals(izracun.getKurs(), 18);
		
	}

	@Test
	public void testUdaljenostT1T2() {

		this.T1.setLat( 45.30);
		this.T1.setLon( 13.30);
		this.T2.setLat( 46.30);
		this.T2.setLon( 14.30);
		this.T1.setVisina(300);
		this.T2.setVisina(200);
		this.kurs = 0;
		IzracuniGC izracun = new IzracuniGC();
		izracun.setTocka1(T1);
		izracun.setTocka2(T2);
		double udaljenost = 0;
		udaljenost=izracun.udaljenostT1T2(T1, T2);
		assertEquals(udaljenost, 135.4,0.2);
	}



	@Test
	public void testTockaNaKursu() {
		this.T1.setLat( 45.30);
		this.T1.setLon( 13.30);
		this.T2.setLat( 46.30);
		this.T2.setLon( 14.30);
		this.T1.setVisina(300);
		this.T2.setVisina(200);
		this.kurs = 0;
		IzracuniGC izracun = new IzracuniGC();
		izracun.setTocka1(T1);
		int kurs =35;
		double dist = 135.5;
		Tocka naKursu = new Tocka();
		naKursu = izracun.tockaNaKursu(kurs, dist);
		assertEquals(T2.getLat(), naKursu.getLat(),0.2);
		assertEquals(46.30,naKursu.getLat(), 0.1);
	}

	@Test
	public void testStupMinDecimal() {
    	//br:117 ZBEVNI,ZBEVNI, ,4527.383N,01400.917E,855.0m,1, , , ,desc
		IzracuniGC izracun = new IzracuniGC();
		String sms = "4529.999N"; //45°30.00N
		double decimal = izracun.stupMinDecimal(sms);
		assertEquals(decimal, 45.50000,0.1);
		
	}

	@Test
	public void testDecimalMinSec() {
		IzracuniGC izracun = new IzracuniGC();
		double stup = 45.50;
		String cup = izracun.decimalMinSec(stup);
		assertEquals("4530.000", cup);
		
	}

	

}
