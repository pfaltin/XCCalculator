package model;

public class TockaOkretna {

	public TockaOkretna(String name, String code, String country, String lat,
			String lon, String elev, String style, String rwdir, String rwlen,
			String freq, String descr) {
		super();
		this.name = name;
		this.code = code;
		this.country = country;
		this.lat = lat;
		this.lon = lon;
		this.elev = elev;
		this.style = style;
		this.rwdir = rwdir;
		this.rwlen = rwlen;
		this.freq = freq;
		this.descr = descr;
	}
	public TockaOkretna() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -1463219999785713801L;
	 private String  name;
	 private String  code;
	 private String  country;
	 private String  lat;
	 private String  lon;
	 private String  elev;
	 private String  style;
	 private String  rwdir;
	 private String  rwlen;
	 private String  freq;
	 private String  descr;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getElev() {
		return elev;
	}
	public void setElev(String elev) {
		this.elev = elev;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getRwdir() {
		return rwdir;
	}
	public void setRwdir(String rwdir) {
		this.rwdir = rwdir;
	}
	public String getRwlen() {
		return rwlen;
	}
	public void setRwlen(String rwlen) {
		this.rwlen = rwlen;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	 
	 

}
