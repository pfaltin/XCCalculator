package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import model.PopisPrognoza;
import model.PrognozaProstorVrijeme;
import model.Ruta;
import model.RutaPreletaIzracun;
import model.Tocka;
import model.TockaIzracunaPreleta;
import model.TockaMeteo;
import model.SlojZraka;
import model.TockaOkretna;
import model.Zrakoplov;


public class BazaVezaPostgreSQL {
	
  	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://postgis-c2:5432/RacunarPreleta";
	static final String USER = "postgres";
	static final String PASS = "Risnjak15";
	
	
	
	
	public BazaVezaPostgreSQL() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void insertMeteo(ArrayList<TockaMeteo> listaModelGFS) throws ParseException{
        for (  int j = 0; j<listaModelGFS.size();j++ ){
 //       System.out.println("za bazu Prognoza je: "+listaModelGFS.get(j).getSatGFS()+" "+listaModelGFS.get(j).getDanGFS()+" "+listaModelGFS.get(j).getMjesecGFS()+" "+listaModelGFS.get(j).getGodinaGFS() );
 //       System.out.println("za bazu slojeva :"+listaModelGFS.get(j).getListaSlojZraka().size());
        for (  int e = 0;e<=listaModelGFS.get(j).getListaSlojZraka().size()-1;e++ ){
 //       System.out.println(listaModelGFS.get(j).getListaSlojZraka().get(e).getVisina());
        }
    }
		
	//INSERT INTO
		try {
			Connection conn = null;
			Statement stmt = null;
			// Registracija JDBC drivera
					Class.forName("org.postgresql.Driver");

				// \nSpajanje na bazu
					System.out.println("\ninsertMeteo Spajanje na bazu...");
					conn = DriverManager.getConnection(DB_URL,USER,PASS);
					System.out.println("\nIzvršavanje INSERT-a");
					stmt = conn.createStatement();

					String sqlINSERT;
					//INSERT INTO meteoTocka					
					for (  int j = 0; j<listaModelGFS.size();j++ ){
//						SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
//						Date dan= (Date) formatter.parse(listaModelGFS.get(j).getDatum());
						String datum = listaModelGFS.get(j).getDatum(); //formatter.format(dan);
						
						sqlINSERT = "INSERT INTO \"tockameteo\" ( id_tockameteo,naziv, lat, lon, elev, vrijeme)VALUES ('"+
								String.valueOf(listaModelGFS.get(j).getId_tockameteo())+"','"+
								listaModelGFS.get(j).getNazivM()+"','"+
								listaModelGFS.get(j).getLat()+"','"+
								listaModelGFS.get(j).getLon()+"','"+
								listaModelGFS.get(j).getVisina()+"','"+
								datum+" "+listaModelGFS.get(j).getSatGFS()+":00') ;";
//						System.out.println("\n\"tockameteo\" Upit je : "+sqlINSERT);
					stmt.executeUpdate(sqlINSERT);
					//INSERT INTO SlojZraka						
				      		for ( int x=0; x < listaModelGFS.get(j).getListaSlojZraka().size();x++){
				      			
								sqlINSERT ="INSERT INTO slojzraka( id_tockameteo, tlak, visina, temperaturazraka, temperaturarose,vjetarsmjer, vjetarbrzina)  VALUES ("+
										listaModelGFS.get(j).getId_tockameteo()+","+
										listaModelGFS.get(j).getListaSlojZraka().get(x).getTlak()+","+
										listaModelGFS.get(j).getListaSlojZraka().get(x).getVisina()+","+
										listaModelGFS.get(j).getListaSlojZraka().get(x).getTemperaturaZraka()+","+
										listaModelGFS.get(j).getListaSlojZraka().get(x).getTemperaturaRose()+","+
										listaModelGFS.get(j).getListaSlojZraka().get(x).getVjetarSmjer()+","+
										listaModelGFS.get(j).getListaSlojZraka().get(x).getVjetarBrzina()+");";
//								System.out.println("\n slojzraka Upit je : "+sqlINSERT);
							stmt.executeUpdate(sqlINSERT);
				      		}// kraj unosa sloja u bazu
					}
				
				//Gotovo sa upitima - čišćenje okruženja 
					System.out.println("meteo INSERT gotov");
					stmt.close();
					conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}		

	public ArrayList<TockaMeteo> selectMeteo(Tocka tocka, Date datum){
		Connection conn = null;
		Statement stmt = null;
		ArrayList<TockaMeteo>  listaModelGFS = new ArrayList<TockaMeteo>();  
	try {
		// Registracija JDBC drivera
				Class.forName("org.postgresql.Driver");

			// \nSpajanje na bazu
				System.out.println("\nselectMeteo Spajanje na bazu...");
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
				stmt = conn.createStatement();
		
				// SELECT * FROM 
		      		String sqlSELECT;
		      		// SELECT slojzraka.id_tockameteo , naziv, lat, lon, elev, vrijeme,  id_sloj, slojzraka.id_tockameteo, tlak, visina,  temperaturazraka, temperaturarose, vjetarsmjer, vjetarbrzina FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo;
		      		sqlSELECT = "SELECT naziv, lat, lon, elev, vrijeme, id_sloj, slojzraka.id_tockameteo, "
		      				+ "tlak, visina,  temperaturazraka, temperaturarose, vjetarsmjer, vjetarbrzina "
		      				+ "FROM tockameteo LEFT JOIN slojzraka ON slojzraka.id_tockameteo = tockameteo.id_tockameteo"
		      				+ "WHERE (floor(lat)= floor("+tocka.getLat()+")OR ceil(lat)=ceil("+tocka.getLat()+"))AND (floor(lon)=floor("+tocka.getLon()+")OR ceil(lon)= ceil("+tocka.getLon()+")) "
		      						+ "AND (vrijeme,interval '2 hours') OVERLAPS (DATE'"+datum+"',interval '24 hours');;";
		      		
		      		System.out.println("\n sqlSELECT slojzraka Upit je : "+sqlSELECT);
		      		ResultSet rs = stmt.executeQuery(sqlSELECT);
		      		rs = stmt.executeQuery(sqlSELECT);
		      
		    	//Uzimanje rezultata upita
		      				int idTockeMet = 0;
		      				int idSLoj = 0;
		      				TockaMeteo meteoTocka = new TockaMeteo();
		      				SlojZraka sloj = new SlojZraka();
		      				ArrayList<SlojZraka> slojLista = new ArrayList<SlojZraka>();
		      				
		    				while(rs.next()){
		    					//po imenu kolone
		    				String kolona1 = rs.getString("id_tockameteo");
		    				String kolona2 = rs.getString("tlak");
		    				String kolona3 = rs.getString("visina");
		    				String kolona4 = rs.getString("temperaturazraka");
		    				String kolona5 = rs.getString("temperaturarose");
		    				String kolona6 = rs.getString("vjetarsmjer");
		    				String kolona7 = rs.getString("vjetarbrzina");
		    				//String kolona8 = rs.getString("id_sloj");
		    				//prikaz rezultata
		    				if (idTockeMet!=Integer.valueOf(kolona1)){
		    					listaModelGFS.add(meteoTocka);
		    					meteoTocka = new TockaMeteo();
		    				}
		    				if (idSLoj!=Integer.valueOf(kolona1))
		    					slojLista.add(sloj);
		    					sloj = new model.SlojZraka();
		    				
		    				meteoTocka.setId_tockameteo(Integer.valueOf(kolona1));
		    				
		    				sloj.setTlak(Integer.valueOf(kolona2));
		    				sloj.setVisina(Integer.valueOf(kolona3));
		    				sloj.setTemperaturaZraka(Integer.valueOf(kolona4));
		    				sloj.setTemperaturaRose(Integer.valueOf(kolona5));
		    				sloj.setVjetarSmjer(Integer.valueOf(kolona6));
		    				sloj.setVjetarBrzina(Integer.valueOf(kolona7));
		    				
//		    				System.out.print("\ntocka meteo ID: " + kolona1 +" kol2 " + kolona2+" kol3 " + kolona3 +" kol4 " + kolona4+" kol5 " + kolona5+" kol6 " + kolona6+" kol7 " + kolona7 );
		    				}
		      		
		      		
				//Gotovo sa upitima - čišćenje okruženja 
					stmt.close();
					conn.close();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return listaModelGFS;

	}
	
	public PopisPrognoza selectPocetak(Tocka tocka, String datum, double propadanje){
		Connection conn = null;
		Statement stmt = null;
		PopisPrognoza prognozaSat = new PopisPrognoza();
		try {
		// Registracija JDBC drivera
				Class.forName("org.postgresql.Driver");

			// \nSpajanje na bazu
				System.out.println("\nselectMeteo Spajanje na bazu...");
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
				stmt = conn.createStatement();
				
		
				// SELECT * FROM 
		      		String sqlSELECT;
		      		//  		SELECT * FROM pocetak(46.85,15.76,'2014-05-27',0.2);
		      		
		      		sqlSELECT = "SELECT * FROM pocetak("+tocka.getLat()+","+tocka.getLon()+",DATE '"+datum+"',"+propadanje+");";
		      		System.out.println("\n sqlSELECT slojzraka Upit je : "+sqlSELECT);
		      		ResultSet rs = stmt.executeQuery(sqlSELECT);
		      		rs = stmt.executeQuery(sqlSELECT);
		      
		    	//Uzimanje rezultata upita
		      				prognozaSat = new PopisPrognoza();
		      				
		    				while(rs.next()){
		      				
		    				String kolona1 = rs.getString("sat");
		    				String kolona2 = rs.getString("visinabaze");
		    				String kolona3 = rs.getString("dizanje");

		    				//prikaz rezultata
	    				
		    				prognozaSat.setSat(Integer.valueOf(kolona1));
		    				prognozaSat.setVisinaDizanja(Integer.valueOf(kolona2));
		    				prognozaSat.setDizanje(Double.valueOf(kolona3));
		    				//listaDan.add(prognozaSat);
		    					    				
		    				System.out.print("\n pocetak : kol1: " + kolona1 +" kol2 " + kolona2+" kol3 " + kolona3 );
		    				}
		      		
		      		
				//Gotovo sa upitima - čišćenje okruženja 
					stmt.close();
					conn.close();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return prognozaSat;

	}
	
	public PrognozaProstorVrijeme selectPrognoza(Tocka tocka, String datum, int sat){
		Connection conn = null;
		Statement stmt = null;
		 PrognozaProstorVrijeme prognoza = new PrognozaProstorVrijeme();
	try {
		// Registracija JDBC drivera
				Class.forName("org.postgresql.Driver");

			// \nSpajanje na bazu
				System.out.println("\nselectMeteo Spajanje na bazu...");
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
				stmt = conn.createStatement();
		
				// SELECT * FROM 
		      		String sqlSELECT;
		      		//SELECT * FROM prognoza(46.85,15.76,'2014-05-27','12:00');
		      		sqlSELECT = "SELECT * FROM prognoza("+tocka.getLat()+","+tocka.getLon()+",DATE \'"+datum+"\',INTERVAL '"+sat+":00');";
		      								
		      		
		      		System.out.println("\n sqlSELECT Prognoza upit je : "+sqlSELECT);
		      		ResultSet rs = stmt.executeQuery(sqlSELECT);
		      		rs = stmt.executeQuery(sqlSELECT);
		      		//Uzimanje rezultata upita
//		      		sirina double precision, duljina double precision, 
//		      		dizanjebr numeric,
//		      		dizanjevis integer,
//		      		vjetarbr integer,
//		      		vjetarsm integer,
//		      		vjertcb integer,
//		      		vjerstratus integer 
     				
		    				while(rs.next()){
		    					//po imenu kolone
			    				String kolona1 = rs.getString("dizanjebr");
			    				String kolona2 = rs.getString("dizanjevis");
			    				String kolona3 = rs.getString("vjetarbr");
			    				String kolona4 = rs.getString("vjetarsm");
			    				String kolona5 = rs.getString("vjertcb");
			    				String kolona6 = rs.getString("vjerstratus");
			    				
			    				prognoza.setDizanjeBrzina(Float.valueOf(kolona1));
			    				prognoza.setDizanjeVisina(Integer.valueOf(kolona2));
			    				prognoza.setVjetarBrzina(Float.valueOf(kolona3));
			    				prognoza.setVjetarSmjer(Integer.valueOf(kolona4)); 
			    				prognoza.setVjerojatnostCb(Integer.valueOf(kolona5)); //Kindex 
			    				int vjerojatnost = 0;
								int razlika = (Integer.valueOf(kolona6));
								if(razlika <10)vjerojatnost=90;
			    				if(razlika<15)vjerojatnost=70;
			    				if(razlika<20)vjerojatnost=50;
			    				if(razlika<25)vjerojatnost=20;
			    				if(razlika<30)vjerojatnost=10;
			    				prognoza.setVjerojatnostStratus(vjerojatnost);
			    				
		    					
//		    				System.out.print("\nPrognoza kol1: " + kolona1 +" kol2 " + kolona2);
			    			System.out.print("\n sqlSELECT Prognoza rezultat- kol1: " + kolona1 +" kol2 " + kolona2+" kol3 " + kolona3 +" kol4 " + kolona4+" kol5 " + kolona5+" kol6 " + kolona6);
		    				}


				//Gotovo sa upitima - čišćenje okruženja 
					stmt.close();
					conn.close();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return prognoza;

	}
	
	
    //  ------------------- Zrakoplovi ----------------------------
	
	public void insertZrakoplov(ArrayList<Zrakoplov> zrakoplov){
		
		//INSERT INTO
			try {
				Connection conn = null;
				Statement stmt = null;
				// Registracija JDBC drivera
						Class.forName("org.postgresql.Driver");

					// \nSpajanje na bazu
						System.out.println("\ninsertZrakoplov Spajanje na bazu...");
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						stmt = conn.createStatement();
					
						
						
					//INSERT INTO
						String sqlINSERT;

						for (  int j = 1; j<zrakoplov.size();j++ ){
						sqlINSERT = "INSERT INTO \"zrakoplovi\" ( zrakoplov_naziv, opterecenje, min_brzina, polara_a, polara_b, polara_c) VALUES ('"+
								zrakoplov.get(j).getNaziv()+"',"+
								zrakoplov.get(j).getOpterecenje()+","+
								zrakoplov.get(j).getMinBrzina()+","+
								zrakoplov.get(j).getParA()+","+
								zrakoplov.get(j).getParB()+","+
								zrakoplov.get(j).getParC()+");";
						//System.out.println("Upit je : "+sqlINSERT);
						stmt.executeUpdate(sqlINSERT);
						}
					//Gotovo sa upitima - čišćenje okruženja 
						stmt.close();
						conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
					
		}		

public ArrayList<Zrakoplov> selectZrakoplov(){
			Connection conn = null;
			Statement stmt = null;
			ArrayList<Zrakoplov> listaZrakoplov = new ArrayList<Zrakoplov> (); 
			
		try {
			// Registracija JDBC drivera
					Class.forName("org.postgresql.Driver");

				// \nSpajanje na bazu
					System.out.println("\nselectZrakoplov Spajanje na bazu...");
					conn = DriverManager.getConnection(DB_URL,USER,PASS);
					stmt = conn.createStatement();
				
			
					// SELECT * FROM 
			      		String sqlSELECT;
			      		sqlSELECT = "SELECT id_zrakoplovi, zrakoplov_naziv, opterecenje, min_brzina, polara_a, polara_b, polara_c FROM \"zrakoplovi\" ";
			      		ResultSet rs = stmt.executeQuery(sqlSELECT);
			      		rs = stmt.executeQuery(sqlSELECT);
			      		
			    		
			      		
			    	//Uzimanje rezultata upita
			    				while(rs.next()){
			    					//po imenu kolone
			    				String kolona1 = rs.getString("id_zrakoplovi");
			    				String kolona2 = rs.getString("zrakoplov_naziv");
			    				String kolona3 = rs.getString("opterecenje");
			    				String kolona4 = rs.getString("min_brzina");
			    				String kolona5 = rs.getString("polara_a");
			    				String kolona6 = rs.getString("polara_b");
			    				String kolona7 = rs.getString("polara_c");
			    				
			    				//prikaz rezultata
			    				//System.out.print("\nID: " + kolona1 +" kol2 " + kolona2+" kol3 " + kolona3 +" kol4 " + kolona4+" kol5 " + kolona5+" kol6 " + kolona6+" kol7 " + kolona7 );
			    				Zrakoplov zrakoplov = new Zrakoplov();
			    				zrakoplov.setId_zrakoplovi(Integer.valueOf(kolona1));
						    	  zrakoplov.setNaziv(kolona2);
						    	  zrakoplov.setOpterecenje(Float.valueOf(kolona3));
						    	  zrakoplov.setMinBrzina(Float.valueOf(kolona4));
						    	  zrakoplov.setParA(Float.valueOf(kolona5));
						    	  zrakoplov.setParB(Float.valueOf(kolona6));
						    	  zrakoplov.setParC(Float.valueOf(kolona7));
						    	  listaZrakoplov.add(zrakoplov);
			    				}
			      		
			      		
					//Gotovo sa upitima - čišćenje okruženja 
						stmt.close();
						conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaZrakoplov;

		}
	    //  ------------------- RUTE ----------------------------
public void insertRuta(Ruta ruta){
			
			//INSERT INTO
				try {
					Connection conn = null;
					Statement stmt = null;
					// Registracija JDBC drivera
							Class.forName("org.postgresql.Driver");

						// \nSpajanje na bazu
							System.out.println("\n insertRuta Spajanje na bazu...");
							conn = DriverManager.getConnection(DB_URL,USER,PASS);
							stmt = conn.createStatement();
						
							
							
						//INSERT INTO Ruta
							String sqlINSERT;
							if (ruta.getId_rute() == 0 ) return;
							sqlINSERT = "INSERT INTO \"ruta\" ( id_ruta, naziv_rute, opis) VALUES ("+
									ruta.getId_rute()+",'"+
									ruta.getImeRute()+"','"+
									ruta.getOpis()+"');";
							System.out.println("Ruta Upit je : "+sqlINSERT);	
							stmt.executeUpdate(sqlINSERT);
						
							//INSERT INTO INSERT INTO rutatocke
							
							for (  int j = 0; j<ruta.getTockeRute().size();j++ ){
								
							sqlINSERT = "INSERT INTO \"rutatocke\" (id_tocka, id_ruta) VALUES ("+
									ruta.getTockeRute().get(j).getId_tocka()+","+
									ruta.getId_rute()+");";
							System.out.println(" rutatocke Upit je : "+sqlINSERT);	
							stmt.executeUpdate(sqlINSERT);
							}
							
							
						//Gotovo sa upitima - čišćenje okruženja 
							stmt.close();
							conn.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
						
			}		

public ArrayList<Ruta> selectRuta(){
				Connection conn = null;
				Statement stmt = null;
				ArrayList<Ruta> listRuta = new ArrayList<Ruta> ();
			try {
				// Registracija JDBC drivera
						Class.forName("org.postgresql.Driver");

					// \nSpajanje na bazu
						System.out.println("\nselectRuta Spajanje na bazu...");
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						stmt = conn.createStatement();
					
				
						// SELECT * FROM Ruta
				      		String sqlSELECT;

				      		sqlSELECT = "SELECT id_ruta, naziv_rute, opis FROM \"ruta\"";
				      		ResultSet rs = stmt.executeQuery(sqlSELECT);
				      		rs = stmt.executeQuery(sqlSELECT);
				      		
				    	//spremanje rezultata upita u listu
				    				while(rs.next()){
				    					//po imenu kolone
				    				String kolona1 = rs.getString("id_ruta");
				    				String kolona2 = rs.getString("naziv_rute");
				    				String kolona3 = rs.getString("opis");
				    				Ruta ruta = new Ruta();
				    				//prikaz rezultata
				    				System.out.print("\nSELECT ruta ID: " + kolona1 +" kol2 " + kolona2 );
				    				ruta.setId_rute(Integer.valueOf(kolona1));
				    				ruta.setImeRute(kolona2);
				    				ruta.setOpis(kolona3);
				    				listRuta.add(ruta);
				    				}
				    				// SELECT JOIN rutatocka		
				    				int idRute;
				    				for (int i=0;i<listRuta.size(); i++){
				    					idRute= listRuta.get(i).getId_rute();
				    					sqlSELECT = "SELECT tocka.id_tocka,id, naziv, tocka.napomena, lat, lon, elev FROM tocka RIGHT JOIN rutatocke ON rutatocke.id_tocka = tocka.id_tocka WHERE id_ruta="+idRute+" ORDER BY id ;";
						      			rs = stmt.executeQuery(sqlSELECT);
								    	//spremanje tocaka u listu rute
						      			ArrayList<Tocka> tockeRute = new ArrayList<Tocka>();
					    				while(rs.next()){
					    					//po imenu kolone
					    				String kolona1 = rs.getString("id_tocka");
					    				String kolona2 = rs.getString("naziv");
					    				String kolona3 = rs.getString("napomena");
					    				String kolona4 = rs.getString("lat");
					    				String kolona5 = rs.getString("lon");
					    				String kolona6 = rs.getString("elev");
					    								    				
					    				Tocka tocka = new Tocka();
					    				//prikaz rezultata
					    				System.out.print("\nSELECT rutatocka ID: " + kolona1 +" kol2 " + kolona2 );
					    				tocka.setId_tocka(Integer.valueOf(kolona1));
					    				tocka.setNaziv(kolona2);
					    				tocka.setNapomena(kolona3);
					    				tocka.setLat(Double.valueOf(kolona4));
					    				tocka.setLon(Double.valueOf(kolona5));
					    				tocka.setVisina(Integer.valueOf(kolona6));
					    				tockeRute.add(tocka);
					    				}
					    				listRuta.get(i).setTockeRute(tockeRute);
						      			
				    				}
				    				
				      		
						//Gotovo sa upitima - čišćenje okruženja 
							stmt.close();
							conn.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return listRuta;

			}

		    //  ------------------- Tocka ----------------------------
public void insertTocka(ArrayList<Tocka> listaTocka){
				
				//INSERT INTO
					try {
						Connection conn = null;
						Statement stmt = null;
						// Registracija JDBC drivera
								Class.forName("org.postgresql.Driver");

							// \nSpajanje na bazu
								System.out.println("\ninsertTocka Spajanje na bazu...");
								conn = DriverManager.getConnection(DB_URL,USER,PASS);
								stmt = conn.createStatement();
							
								
								
							//INSERT INTO
								String sqlINSERT;

								for (  int j = 0; j<listaTocka.size();j++ ){
								sqlINSERT = "INSERT INTO \"tocka\" ( naziv, napomena, lat, lon, elev) VALUES ('"+
										listaTocka.get(j).getNaziv()+"','"+
										listaTocka.get(j).getNapomena()+"',"+
										listaTocka.get(j).getLat()+","+
										listaTocka.get(j).getLon()+","+
										listaTocka.get(j).getVisina()
										+");";
								//System.out.println("Upit je : "+sqlINSERT);	
								stmt.executeUpdate(sqlINSERT);
								}
							//Gotovo sa upitima - čišćenje okruženja 
								stmt.close();
								conn.close();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
							
							
				}		

				public ArrayList<Tocka> selectTocka(){
					Connection conn = null;
					Statement stmt = null;
					ArrayList<Tocka> listTocka = new ArrayList<Tocka> (); 
				try {
					// Registracija JDBC drivera
							Class.forName("org.postgresql.Driver");

						// \nSpajanje na bazu
							System.out.println("\nselectTocka Spajanje na bazu...");
							conn = DriverManager.getConnection(DB_URL,USER,PASS);
							stmt = conn.createStatement();
						
					
							// SELECT * FROM 
					      		String sqlSELECT;
					      		sqlSELECT = "SELECT id_tocka, naziv, napomena, lat, lon, elev FROM \"tocka\"";
					      		

					      		ResultSet rs = stmt.executeQuery(sqlSELECT);
					      		rs = stmt.executeQuery(sqlSELECT);
			      		
					    		

					    	//Uzimanje rezultata upita
					    				while(rs.next()){
					    					//po imenu kolone
						    				String kolona1 = rs.getString("id_tocka");
						    				String kolona2 = rs.getString("naziv");
						    				String kolona3 = rs.getString("napomena");
						    				String kolona4 = rs.getString("lat");
						    				String kolona5 = rs.getString("lon");
						    				String kolona6 = rs.getString("elev");

						    				//prikaz rezultata
						    				//System.out.print("\nID: " + kolona1 +" kol2 " + kolona2+" kol3 " + kolona3 +" kol4 " + kolona4+" kol5 " + kolona5+" kol6 " + kolona6);
						    				Tocka tocka = new Tocka();
						    				tocka.setId_tocka(Integer.valueOf(kolona1));
						    				tocka.setNaziv(kolona2);
						    				tocka.setNapomena(kolona3);
						    				tocka.setLat(Double.valueOf(kolona4));
						    				tocka.setLon(Double.valueOf(kolona5));
						    				tocka.setVisina(Integer.valueOf(kolona6));
						    				
									    	  listTocka.add(tocka);
					    				}
					      		
							//Gotovo sa upitima - čišćenje okruženja 
								stmt.close();
								conn.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return listTocka;

				}			
			    //  ------------------- RutaPreletaIzracun ----------------------------
public void insertRutaPreletaIzracun(RutaPreletaIzracun izracun){
					
					//INSERT INTO
						try {
							Connection conn = null;
							Statement stmt = null;
							// Registracija JDBC drivera
									Class.forName("org.postgresql.Driver");

								// \nSpajanje na bazu
									System.out.println("\ninsertRutaPreletaIzracun Spajanje na bazu...");
									conn = DriverManager.getConnection(DB_URL,USER,PASS);
									stmt = conn.createStatement();
								
									
									
								//INSERT INTO
									String sqlINSERT;
									sqlINSERT = "INSERT INTO \"rutapreletaizracun\" ( id_rutapreletaizracun, naziv_rute, opis) VALUES ("+
											izracun.getId_izracuna()+",'"+
											izracun.getNaziv()+"','"
													+ "Zrakoplov "+izracun.getZrakoplov().getNaziv()+" metoda:"+izracun.getMetodaMeteo()+ "');";
									System.out.println(" Izracun ruta Upit je : "+sqlINSERT);	
									stmt.executeUpdate(sqlINSERT);
								
									//INSERT INTO INSERT INTO rutatocke
								
									for (  int j = 0; j<izracun.getTockaIzracunaPreleta().size();j++ ){
										
									sqlINSERT = "INSERT INTO tockaizracunapreleta( id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vrijemedolaska, trajanjeetapa, trajanjeodstart, vjerojatnostcb, vjerojatnoststratus) VALUES ("+
											
											izracun.getId_izracuna()+",'"+
											izracun.getTockaIzracunaPreleta().get(j).getNaziv()+"',"+
											izracun.getTockaIzracunaPreleta().get(j).getLat()+","+
											izracun.getTockaIzracunaPreleta().get(j).getLon()+","+
											izracun.getTockaIzracunaPreleta().get(j).getVisina()+","+
											izracun.getTockaIzracunaPreleta().get(j).getDizanjeBrzina()+","+
											izracun.getTockaIzracunaPreleta().get(j).getDizanjeVisina()+","+
											izracun.getTockaIzracunaPreleta().get(j).getVjetarBrzina()+","+
											izracun.getTockaIzracunaPreleta().get(j).getVjetarSmjer()+","+
											izracun.getTockaIzracunaPreleta().get(j).getBrzinaEtapa()+","+
											izracun.getTockaIzracunaPreleta().get(j).getBrzinaStart()+","+
											izracun.getTockaIzracunaPreleta().get(j).getVrijemeDolaska()+","+
											izracun.getTockaIzracunaPreleta().get(j).getTrajanjeEtapa()+","+
											izracun.getTockaIzracunaPreleta().get(j).getTrajanjeStart()+","+
											izracun.getTockaIzracunaPreleta().get(j).getVjerojatnostCb()+","+
											izracun.getTockaIzracunaPreleta().get(j).getVjerojatnostStratus()+");";
									System.out.println(" izracun tocka Upit je : "+sqlINSERT);	
									stmt.executeUpdate(sqlINSERT);
									}
								
								//Gotovo sa upitima - čišćenje okruženja 
									stmt.close();
									conn.close();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
								
								
					}		

	public ArrayList<RutaPreletaIzracun> selectRutaPreletaIzracun(){
						Connection conn = null;
						Statement stmt = null;
						ArrayList<RutaPreletaIzracun> listaIzracuna = new ArrayList<RutaPreletaIzracun>(); 
					try {
						// Registracija JDBC drivera
								Class.forName("org.postgresql.Driver");

							// \nSpajanje na bazu
								System.out.println("\n selectRutaPreletaIzracun Spajanje na bazu...");
								conn = DriverManager.getConnection(DB_URL,USER,PASS);
								stmt = conn.createStatement();
							
						
								// SELECT * FROM rutapreletaizracun
						      		String sqlSELECT;
						      		sqlSELECT = "SELECT  id_rutapreletaizracun, naziv_rute, opis FROM \"rutapreletaizracun\"";
						      		ResultSet rs = stmt.executeQuery(sqlSELECT);
						      		rs = stmt.executeQuery(sqlSELECT);
						    	//Uzimanje rezultata upita
						    				while(rs.next()){
					    					//po imenu kolone
						    				String kolona1 = rs.getString("id_rutapreletaizracun");
						    				String kolona2 = rs.getString("naziv_rute");
						    				String kolona3 = rs.getString("opis");
						    				//prikaz rezultata
						    				System.out.print("ID: " + kolona1 +" kol2 " + kolona2 +" kol3 " + kolona3);
						    				RutaPreletaIzracun izracun = new RutaPreletaIzracun();
						    				izracun.setId_izracuna(Integer.valueOf(kolona1));
						    				izracun.setNaziv(kolona2);
						    				listaIzracuna.add(izracun);
						    				}
						    	// SELECT * FROM rutapreletaizracun
						    				for (  int j = 1; j<listaIzracuna.size();j++ ){
						    					
						    				sqlSELECT = "SELECT id_tockaizp, id_rutapreletaizracun, naziv, lat, lon, elev, dizanjebrzina, dizanjevisina, vjetarbrzina, vetarsmjer, brzinaetapa, brzinastart, vrijemedolaska, trajanjeetapa, trajanjeodstart, vjerojatnostcb, vjerojatnoststratus FROM \"tockaizracunapreleta\" WHERE id_rutapreletaizracun="+listaIzracuna.get(j).getId_izracuna()+";";
								      		rs = stmt.executeQuery(sqlSELECT);
								      		ArrayList<TockaIzracunaPreleta> tockeIzracuna = new ArrayList<TockaIzracunaPreleta>();
								      		//Uzimanje rezultata upita
								      		int i=0;
								    				while(rs.next()){
							    					//po imenu kolone
								    				String kolona1 = rs.getString("id_tockaizp");
								    				String kolona2 = rs.getString("id_rutapreletaizracun");
								    				String kolona3 = rs.getString("naziv");
								    				String kolona4 = rs.getString("lat");
								    				String kolona5 = rs.getString("lon");
								    				String kolona6 = rs.getString("elev");
								    				String kolona7 = rs.getString("dizanjebrzina");
								    				String kolona8 = rs.getString("dizanjevisina");
								    				String kolona9 = rs.getString("vjetarbrzina");
								    				String kolona10 = rs.getString("vetarsmjer");
								    				String kolona11 = rs.getString("brzinaetapa");
								    				String kolona12 = rs.getString("brzinastart");
								    				String kolona13 = rs.getString("vrijemedolaska");
								    				String kolona14 = rs.getString("trajanjeetapa");
								    				String kolona15 = rs.getString("trajanjeodstart");
								    				String kolona16 = rs.getString("vjerojatnostcb");
								    				String kolona17 = rs.getString("vjerojatnoststratus");

								    				//prikaz rezultata
								    				System.out.print("ID: " + kolona1 +" kol2 " + kolona2+" kol3 " + kolona3 +" kol4 " + kolona4+" kol5 " + kolona5 +" kol6 " + kolona6
								    						+" kol7 " + kolona7 +" kol8 " + kolona8 +" kol9 " + kolona9 +" kol10 " + kolona10 +" kol11 " + kolona11 +" kol12 " + kolona12
								    						+" kol13 " + kolona13 +" kol14 " + kolona14 +" kol15 " + kolona15+" kol16 " + kolona16+" kol17 " + kolona17);
								    				TockaIzracunaPreleta izracunTocka = new TockaIzracunaPreleta();
								    				izracunTocka.setId_tocka(Integer.valueOf(kolona1));
								    				izracunTocka.setNaziv(kolona3);
								    				izracunTocka.setLat(Double.valueOf(kolona4));
								    				izracunTocka.setVisina(Integer.valueOf(kolona6));
								    				izracunTocka.setDizanjeBrzina(Float.valueOf(kolona7));
								    				izracunTocka.setDizanjeVisina(Integer.valueOf(kolona8));
								    				izracunTocka.setVjetarBrzina(Float.valueOf(kolona9));
								    				izracunTocka.setVjetarSmjer(Integer.valueOf(kolona10));
								    				izracunTocka.setBrzinaEtapa(Float.valueOf(kolona11));
								    				izracunTocka.setBrzinaStart(Float.valueOf(kolona12));
								    				izracunTocka.setVrijemeDolaska(Long.valueOf(kolona13));
								    				izracunTocka.setTrajanjeEtapa(Long.valueOf(kolona14));
								    				izracunTocka.setTrajanjeStart(Long.valueOf(kolona15));
								    				izracunTocka.setVjerojatnostCb(Integer.valueOf(kolona16));
								    				izracunTocka.setVjerojatnostStratus(Integer.valueOf(kolona17));

								    				tockeIzracuna.add(izracunTocka); // dodaj u listu tocaka
								    				i++;
								    				}
								    				listaIzracuna.get(i).setTockaIzracunaPreleta(tockeIzracuna); //dodaj listu tocaka u listu izracuna

						    				}
						      		
								//Gotovo sa upitima - čišćenje okruženja 
									stmt.close();
									conn.close();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return listaIzracuna;

					}
					
				    //  ------------------- OkretnaTocka ----------------------------
		public void insertOkretnaTocka(ArrayList<TockaOkretna> listaOkretnihTocka){
						
						//INSERT INTO
							try {
								Connection conn = null;
								Statement stmt = null;
								// Registracija JDBC drivera
										Class.forName("org.postgresql.Driver");

									// \nSpajanje na bazu
										System.out.println("\ninsertOkretnaTocka Spajanje na bazu...");
										conn = DriverManager.getConnection(DB_URL,USER,PASS);
										stmt = conn.createStatement();
									
										
										
										String sqlIN;
										for (  int j = 1; j<listaOkretnihTocka.size();j++ ){
										sqlIN = "INSERT INTO tockaokretna (wpname,code,country, lat, lon, elev, style, rwdir,rwlen, freq, descr) VALUES ('"+ 
										listaOkretnihTocka.get(j).getName()+"','"+
						    			listaOkretnihTocka.get(j).getCode()+"','"+
						    			listaOkretnihTocka.get(j).getCountry()+"','"+
						    			listaOkretnihTocka.get(j).getLat()+"','"+
						    			listaOkretnihTocka.get(j).getLon()+"','"+
						    			listaOkretnihTocka.get(j).getElev()+"','"+
						    			listaOkretnihTocka.get(j).getStyle()+"','"+
						    			listaOkretnihTocka.get(j).getRwdir()+"','"+
						    			listaOkretnihTocka.get(j).getRwlen()+"','"+
						    			listaOkretnihTocka.get(j).getFreq()+"','"+
						    			listaOkretnihTocka.get(j).getDescr()+"') ;";
										System.out.println("Upit je : "+sqlIN);
										stmt.executeUpdate(sqlIN);
														
										}
										
									
									//Gotovo sa upitima - čišćenje okruženja 
										stmt.close();
										conn.close();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
									
									
						}		

	public ArrayList<TockaOkretna> selectOkretnaTocka(){
							Connection conn = null;
							Statement stmt = null;
							ArrayList<TockaOkretna> listaOkTocka = new ArrayList<TockaOkretna> (); 
						try {
							// Registracija JDBC drivera
									Class.forName("org.postgresql.Driver");

								// \nSpajanje na bazu
									System.out.println("\n selectOkretnaTocka Spajanje na bazu...");
									conn = DriverManager.getConnection(DB_URL,USER,PASS);
									stmt = conn.createStatement();

							
									// SELECT * FROM 
							      		String sqlSELECT;
							      		sqlSELECT = "SELECT id_oktocka, wpname, code, country, lat, lon, elev, style, rwdir, rwlen, freq, descr FROM tockaokretna";
							      		ResultSet rs = stmt.executeQuery(sqlSELECT);
							      		rs = stmt.executeQuery(sqlSELECT);
							      		
							    	//Uzimanje rezultata upita
							    				while(rs.next()){
							    				
							    					//po imenu kolone
								    				String kolona1 = rs.getString("id_oktocka");
								    				String kolona2 = rs.getString("wpname");
								    				String kolona3 = rs.getString("code");
								    				String kolona4 = rs.getString("country");
								    				String kolona5 = rs.getString("lat");
								    				String kolona6 = rs.getString("lon");
								    				String kolona7 = rs.getString("elev");
								    				String kolona8 = rs.getString("style");
								    				String kolona9 = rs.getString("rwdir");
								    				String kolona10 = rs.getString("rwlen");
								    				String kolona11 = rs.getString("freq");
								    				String kolona12 = rs.getString("descr");
								    				

								    				//prikaz rezultata
								    				System.out.print("ID: " + kolona1 +" kol2 " + kolona2+" kol3 " + kolona3 +" kol4 " + kolona4+" kol5 " + kolona5 +" kol6 " + kolona6
								    						+" kol7 " + kolona7 +" kol8 " + kolona8 +" kol9 " + kolona9 +" kol10 " + kolona10 +" kol11 " + kolona11 +" kol12 " + kolona12);
								    				TockaOkretna tocka = new TockaOkretna();
								    				tocka.setName(kolona1);
								    				tocka.setCode(kolona3);
								    				tocka.setCountry(kolona4);
								    				tocka.setLat(kolona5);
								    				tocka.setLon(kolona6);
								    				tocka.setElev(kolona7);
								    				tocka.setStyle(kolona8);
								    				tocka.setRwdir(kolona9);
								    				tocka.setRwlen(kolona10);
								    				tocka.setFreq(kolona11);
								    				tocka.setDescr(kolona12);
								    				listaOkTocka.add(tocka);
							    				}
							      		
									//Gotovo sa upitima - čišćenje okruženja 
										stmt.close();
										conn.close();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return listaOkTocka;

						}	
	public void ciscenjeBaze(){
		
		//DELETE
			try {
				Connection conn = null;
				Statement stmt = null;
				// Registracija JDBC drivera
						Class.forName("org.postgresql.Driver");

					// \nSpajanje na bazu
						System.out.println("\n BRISANJE Spajanje na bazu...");
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						stmt = conn.createStatement();

					//DELETE
						stmt.executeUpdate("DELETE FROM tockaizracunapreleta;");
						stmt.executeUpdate("DELETE FROM rutapreletaizracun;");
						stmt.executeUpdate("DELETE FROM rutatocke;");
						stmt.executeUpdate("DELETE FROM tocka;");
						stmt.executeUpdate("DELETE FROM ruta;");
						stmt.executeUpdate("DELETE FROM zrakoplovi;");
						stmt.executeUpdate("DELETE FROM tockaokretna;");
						stmt.executeUpdate("DELETE FROM slojzraka;");
						stmt.executeUpdate("DELETE FROM tockameteo;");
						//Gotovo sa upitima - čišćenje okruženja 
						stmt.close();
						conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public void ciscenjeRutaBaza(){
		
		//DELETE
			try {
				Connection conn = null;
				Statement stmt = null;
				// Registracija JDBC drivera
						Class.forName("org.postgresql.Driver");
					// \nSpajanje na bazu
						System.out.println("\n BRISANJE Spajanje na bazu...");
						conn = DriverManager.getConnection(DB_URL,USER,PASS);
						stmt = conn.createStatement();
					//DELETE
						stmt.executeUpdate("DELETE FROM rutatocke;");
						stmt.executeUpdate("DELETE FROM ruta;");
						//Gotovo sa upitima - čišćenje okruženja 
						stmt.close();
						conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public void ciscenjeZrakoplovaBaza(){
	
	//DELETE
		try {
			Connection conn = null;
			Statement stmt = null;
			// Registracija JDBC drivera
					Class.forName("org.postgresql.Driver");
				// \nSpajanje na bazu
					System.out.println("\n BRISANJE Spajanje na bazu...");
					conn = DriverManager.getConnection(DB_URL,USER,PASS);
					stmt = conn.createStatement();
				//DELETE
					stmt.executeUpdate("DELETE FROM zrakoplovi;");
					//Gotovo sa upitima - čišćenje okruženja 
					stmt.close();
					conn.close();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void ciscenjeTockeBaza(){
	
	//DELETE
		try {
			Connection conn = null;
			Statement stmt = null;
			// Registracija JDBC drivera
					Class.forName("org.postgresql.Driver");
				// \nSpajanje na bazu
					System.out.println("\n BRISANJE Ruta i Tocke Spajanje na bazu...");
					conn = DriverManager.getConnection(DB_URL,USER,PASS);
					stmt = conn.createStatement();
				//DELETE
					stmt.executeUpdate("DELETE FROM rutatocke;");
					stmt.executeUpdate("DELETE FROM tocka;");
					stmt.executeUpdate("DELETE FROM ruta;");
					stmt.executeUpdate("DELETE FROM tockaokretna;");
					//Gotovo sa upitima - čišćenje okruženja 
					stmt.close();
					conn.close();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void ciscenjeMeteoBaze(){
	
	//DELETE
		try {
			Connection conn = null;
			Statement stmt = null;
			// Registracija JDBC drivera
					Class.forName("org.postgresql.Driver");

				// \nSpajanje na bazu
					System.out.println("\n BRISANJE Spajanje na bazu...");
					conn = DriverManager.getConnection(DB_URL,USER,PASS);
					stmt = conn.createStatement();
				//DELETE
					stmt.executeUpdate("DELETE FROM slojzraka;");
					stmt.executeUpdate("DELETE FROM tockameteo;");
					//Gotovo sa upitima - čišćenje okruženja 
					stmt.close();
					conn.close();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	
	
	
	
    }//kraj



