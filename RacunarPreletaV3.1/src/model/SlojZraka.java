package model;

public class SlojZraka {
	 	private int visina;

	    private int tlak;

	    private int temperaturaZraka;

	    private int temperaturaRose;

	    private float vjetarBrzina;

	    private int vjetarSmjer;
	    
	    

		public SlojZraka(int visina, int tlak, int temperaturaZraka,
				int temperaturaRose, float vjetarBrzina, int vjetarSmjer) {
			super();
			this.visina = visina;
			this.tlak = tlak;
			this.temperaturaZraka = temperaturaZraka;
			this.temperaturaRose = temperaturaRose;
			this.vjetarBrzina = vjetarBrzina;
			this.vjetarSmjer = vjetarSmjer;
		}

		public SlojZraka() {
			// TODO Auto-generated constructor stub
		}

		public int getVisina() {
			return visina;
		}

		public void setVisina(int visina) {
			this.visina = visina;
		}

		public int getTlak() {
			return tlak;
		}

		public void setTlak(int tlak) {
			this.tlak = tlak;
		}

		public int getTemperaturaZraka() {
			return temperaturaZraka;
		}

		public void setTemperaturaZraka(int temperaturaZraka) {
			this.temperaturaZraka = temperaturaZraka;
		}

		public int getTemperaturaRose() {
			return temperaturaRose;
		}

		public void setTemperaturaRose(int temperaturaRose) {
			this.temperaturaRose = temperaturaRose;
		}

		public float getVjetarBrzina() {
			return vjetarBrzina;
		}

		public void setVjetarBrzina(float vjetarBrzina) {
			this.vjetarBrzina = vjetarBrzina;
		}

		public int getVjetarSmjer() {
			return vjetarSmjer;
		}

		public void setVjetarSmjer(int vjetarSmjer) {
			this.vjetarSmjer = vjetarSmjer;
		}

	    
	    
	    
}
