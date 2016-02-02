package model;


public class PrognozaProstorVrijeme {



		public PrognozaProstorVrijeme() {
		super();
		// TODO Auto-generated constructor stub
	}
		private double dizanjeBrzina;
	    private int dizanjeVisina;
	    private int vjetarSmjer;
	    private double vjetarBrzina;
	    private int vjerojatnostCb;
	    private int vjerojatnostStratus;
	    private String datum;
	    private int sat;
	    private Tocka tocka;
	    private double propadanje;
	    
	    private TockaMeteo tockaMeteo;
   
    
	    
	    public double getDizanjeBrzina() {
			return dizanjeBrzina;
		}


		public void setDizanjeBrzina(double dizanjeBrzina) {
			this.dizanjeBrzina = dizanjeBrzina;
		}


		public int getDizanjeVisina() {
			return dizanjeVisina;
		}


		public void setDizanjeVisina(int dizanjeVisina) {
			this.dizanjeVisina = dizanjeVisina;
		}


		public int getVjetarSmjer() {
			return vjetarSmjer;
		}


		public void setVjetarSmjer(int vjetarSmjer) {
			this.vjetarSmjer = vjetarSmjer;
		}


		public double getVjetarBrzina() {
			return vjetarBrzina;
		}


		public void setVjetarBrzina(double vjetarBrzina) {
			this.vjetarBrzina = vjetarBrzina;
		}





		public int getVjerojatnostCb() {
			return vjerojatnostCb;
		}


		public void setVjerojatnostCb(int vjerojatnostCb) {
			this.vjerojatnostCb = vjerojatnostCb;
		}


		public int getVjerojatnostStratus() {
			return vjerojatnostStratus;
		}


		public void setVjerojatnostStratus(int vjerojatnostStratus) {
			this.vjerojatnostStratus = vjerojatnostStratus;
		}



		public Tocka getTocka() {
			return tocka;
		}


		public void setTocka(Tocka tocka) {
			this.tocka = tocka;
		}


		public TockaMeteo getTockaMeteo() {
			return tockaMeteo;
		}


		public void setTockaMeteo(TockaMeteo tockaMeteo) {
			this.tockaMeteo = tockaMeteo;
		}


		public String getDatum() {
			return datum;
		}


		public void setDatum(String datum) {
			this.datum = datum;
		}


		public int getSat() {
			return sat;
		}


		public void setSat(int sat) {
			this.sat = sat;
		}


		public double getPropadanje() {
			return propadanje;
		}


		public void setPropadanje(double propadanje) {
			this.propadanje = propadanje;
		}






	    

}
