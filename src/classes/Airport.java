package classes;

public class Airport 
{
	private String shortName;
	private String cityName;
	private String country;
	private Geolocation geolocation;
	
	public Airport(Geolocation geolocation,String cityName,String country,String shortName) 
	{
		this.geolocation=geolocation;
		this.shortName=shortName;
		this.cityName=cityName;
		this.country=country;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString()
	{
		String str = "";
		
		str+= "Aeroport de " + cityName +" (" + shortName + "), "+ country +", position [" 
				+ geolocation.getLongitude() + ", " + geolocation.getLatitude()+"]";
		return str;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() 
	{
		return shortName;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() 
	{
		return cityName;
	}

	/**
	 * @return the country
	 */
	public String getCountry() 
	{
		return country;
	}
	
	
	

}
