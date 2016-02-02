package ctrl.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;

import model.Ruta;
import model.Tocka;
import model.Zrakoplov;

import org.junit.Test;

import controller.UpravljanjePodacima;

public class TestUpravljanjePodacima {

	
//	@Test
//	public void ciscenjeBaze(){
//		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
//		upravPodaci.ciscenjeBaze();
//		assertEquals(119, 119);
//		
//	}
	
	
	@Test
	public void	testUpisTXTDatoteka(){
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();

		upravPodaci.upisTXTDatoteka("proba23.txt","Neki tekst");
		String procitano = readTextFile("proba23.txt");
		String kontrola = "Neki tekst";
		assertEquals(procitano.length(),kontrola.length()+1 );
		
	}
	
	@Test
	public void testProcitajTockeDatoteka() throws IOException {
		
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		String putanja = ("/home/k2/tmp/IstraLika.cup");
		upravPodaci.procitajTockeDatoteka(putanja);
		int velicinaListe = upravPodaci.getListaOkretnihTocka().size();
		assertEquals(109, velicinaListe);
	}
	
	@Test
	public void testPretvoriOkretneTocke() throws IOException{
		String putanja = ("/home/k2/tmp/IstraLika.cup");
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
	
		upravPodaci.procitajTockeDatoteka(putanja);
		upravPodaci.pretvoriOkretneTocke();
		int velicinaListeOKT = upravPodaci.getListaOkretnihTocka().size();
		int velicinaListeT =1+ upravPodaci.getListTocka().size();
		assertEquals(velicinaListeT, velicinaListeOKT);
		
	}



	@Test
	public void testProcitajZrakoplovDatoteka() throws IOException {
			
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		String putanja = ("/home/k2/tmp/SeeYou.plr");
		upravPodaci.procitajZrakoplovDatoteka(putanja);
		int velicinaListe = upravPodaci.getListaZrakoplov().size();
		assertEquals(142, velicinaListe);
	}

	// ------------------ BAZA ------------------ 

	@Test
	public void testSpremiTockeBaza() throws IOException {
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		System.out.println("ciscenje tocaka baze!!!");
		upravPodaci.ciscenjeTockeBaza();
		String putanja = ("/home/k2/tmp/IstraLika.cup");
		upravPodaci.procitajTockeDatoteka(putanja);
		upravPodaci.pretvoriOkretneTocke();
		upravPodaci.spremiTockeBaza();
		assertEquals(108, upravPodaci.getListTocka().size());
	}
	@Test
	public void testSpremiRutuBaza() throws IOException {
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		String putanja = ("/home/k2/tmp/IstraLika.cup");
		upravPodaci.procitajTockeDatoteka(putanja);
		upravPodaci.pretvoriOkretneTocke();
		upravPodaci.ciscenjeCijelaBaze();
		upravPodaci.spremiTockeBaza();		
		upravPodaci.procitajTockeBaza();
		Ruta ruta = new Ruta();
		Random id = new Random( );
		
		ruta.setId_rute(Math.abs(id.nextInt()));
		ruta.setImeRute("Ruta Test1");
		ArrayList<Tocka> tockeRute = new ArrayList<Tocka>();
		tockeRute.add(upravPodaci.getListTocka().get(12));
		tockeRute.add(upravPodaci.getListTocka().get(13));
		tockeRute.add(upravPodaci.getListTocka().get(42));
		tockeRute.add(upravPodaci.getListTocka().get(22));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		ruta.setTockeRute(tockeRute);
		upravPodaci.setRuta(ruta);
		upravPodaci.spremiRutuBaza();
		
		testProcitajRuteBaza();
		assertEquals(tockeRute.get(2).getId_tocka(), upravPodaci.getRuta().getTockeRute().get(2).getId_tocka());
		
	}

	@Test
	public void testSpremiViseRutaBaza() throws IOException {
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		ArrayList<Ruta> listRuta = new ArrayList<Ruta>();
		String putanja = ("/home/k2/tmp/IstraLika.cup");
		upravPodaci.procitajTockeDatoteka(putanja);
		upravPodaci.pretvoriOkretneTocke();
		upravPodaci.ciscenjeTockeBaza();
		upravPodaci.spremiTockeBaza();		
		upravPodaci.procitajTockeBaza();
		Ruta ruta = new Ruta();
		
		
		ruta.setId_rute(1);
		ruta.setImeRute("Ruta Prva");
		ArrayList<Tocka> tockeRute = new ArrayList<Tocka>();
		tockeRute.add(upravPodaci.getListTocka().get(12));
		tockeRute.add(upravPodaci.getListTocka().get(13));
		tockeRute.add(upravPodaci.getListTocka().get(42));
		tockeRute.add(upravPodaci.getListTocka().get(22));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		ruta.setTockeRute(tockeRute);
		listRuta.add(ruta);
		
		ruta = new Ruta();
		ruta.setId_rute(2);
		ruta.setImeRute("Ruta Druga");
		tockeRute = new ArrayList<Tocka>();
		tockeRute.add(upravPodaci.getListTocka().get(1));
		tockeRute.add(upravPodaci.getListTocka().get(3));
		tockeRute.add(upravPodaci.getListTocka().get(4));
		tockeRute.add(upravPodaci.getListTocka().get(21));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		ruta.setTockeRute(tockeRute);
		listRuta.add(ruta);
		
		ruta = new Ruta();
		ruta.setId_rute(3);
		ruta.setImeRute("Ruta Treca");
		tockeRute = new ArrayList<Tocka>();
		tockeRute.add(upravPodaci.getListTocka().get(4));
		tockeRute.add(upravPodaci.getListTocka().get(22));
		tockeRute.add(upravPodaci.getListTocka().get(41));
		tockeRute.add(upravPodaci.getListTocka().get(11));
		System.out.println("broj tocka"+tockeRute.size());
		System.out.println("3 tocka"+tockeRute.get(2).getNaziv());
		ruta.setTockeRute(tockeRute);
		listRuta.add(ruta);
		
		System.out.println("upisano ruta: "+listRuta.size());
		upravPodaci.setListRuta(listRuta);
		upravPodaci.spremiCRuteBaza();
		
		upravPodaci.procitajRuteBaza();

		
		assertEquals(listRuta.size(), upravPodaci.getListRuta().size());
	}

	@Test
	public void testSpremiZrakoploveBaza() throws IOException {
		String putanja = ("/home/k2/tmp/SeeYou.plr");
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.procitajZrakoplovDatoteka(putanja);
		ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();
		listaZrakoplov.addAll(upravPodaci.getListaZrakoplov());
		upravPodaci.ciscenjeZrakoplovaBaza();
		upravPodaci.spremiZrakoploveBaza();
		int velicinaListe = upravPodaci.getListaZrakoplov().size();
		assertEquals(142, velicinaListe);
	}

	@Test
	public void testProcitajTockeBaza() throws IOException {
		
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
//		String putanja = ("/home/k2/tmp/IstraLika.cup");
//		upravPodaci.procitajTockeDatoteka(putanja);
//		upravPodaci.pretvoriOkretneTocke();
//		upravPodaci.spremiTockeBaza();
		upravPodaci.procitajTockeBaza();
		assertEquals(108, upravPodaci.getListTocka().size());	}

//	@Test
	public void testProcitajRuteBaza() {
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.procitajRuteBaza();
		//int idrute = upravPodaci.getListRuta().get(0).getId_rute();
		System.out.println("\ntestProcitajRuteBaza RUTA ima:"+upravPodaci.getListRuta().size());
		
	}

	@Test
	public void testProcitajZrakoploveBaza() throws IOException {
		ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> ();
		ArrayList<Zrakoplov> listaZrakoplovBaza = new ArrayList<Zrakoplov> ();
		String putanja = ("/home/k2/tmp/SeeYou.plr");
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.procitajZrakoplovDatoteka(putanja);
		
		listaZrakoplov.addAll(upravPodaci.getListaZrakoplov());

		upravPodaci.procitajZrakoploveBaza();
		listaZrakoplovBaza.addAll(upravPodaci.getListaZrakoplov());
		int velicinaListe = listaZrakoplovBaza.size();
		
		assertEquals(141, velicinaListe);
			}
	// -------------------- DATOTEKA 
	
	@Test
	public void testSpremiZrakoplovDatoteka() throws IOException {
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		String putanja = ("/home/k2/tmp/SeeYou.plr");
		upravPodaci.procitajZrakoplovDatoteka(putanja);
		upravPodaci.spremiZrakoploveBaza();
		upravPodaci.procitajZrakoploveBaza();
		
		File target = new File("/home/k2/tmp/Zrakoplovi.plr");
		target.delete( ); // brisanje "/home/k2/tmp/Zrakoplovi.plr"
		
		upravPodaci.spremiZrakoplovDatoteka("/home/k2/tmp/Zrakoplovi.plr");

		String sadrzaj2=readTextFile("/home/k2/tmp/Zrakoplovi.plr");
	
		assertEquals(10494, sadrzaj2.length());
				
	}
	

	@Test
	public void testSpremiTockeDatoteka() throws IOException {
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		String putanjaOrig = ("/home/k2/tmp/IstraLika.cup");
		String putanjaRad = ("/home/k2/tmp/tockeJava.cup");
		upravPodaci.procitajTockeDatoteka(putanjaOrig);
		upravPodaci.spremiTockeBaza();
		upravPodaci.procitajTockeBaza();
		upravPodaci.spremiTockeDatoteka(putanjaRad);
		String sadrzaj2=readTextFile(putanjaRad);
		assertEquals(6020, sadrzaj2.length());
	}
	
	@Test
	public void testPunjenjeBaze() throws IOException, ParseException{
		UpravljanjePodacima upravPodaci = new UpravljanjePodacima();
		upravPodaci.punjenjeBaze();
	}
	
	public String readTextFile(String fileName) {
		  
		  String returnValue = "";
		  FileReader file = null;
		  
		  try {
		    file = new FileReader(fileName);
		    BufferedReader reader = new BufferedReader(file);
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		      returnValue += line + "\n";
		    }
		    reader.close();
		  } catch (FileNotFoundException e) {
		      throw new RuntimeException("File not found");
		  } catch (IOException e) {
		      throw new RuntimeException("IO Error occured");
		  } finally {
		    if (file != null) {
		      try {
		        file.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
		  }
		  
		  return returnValue;
		} 
}
