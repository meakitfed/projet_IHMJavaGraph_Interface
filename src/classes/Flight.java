package classes;

public class Flight 
{
	private String modelAvion;
	private Plane plane;
	private Airport arrival;
	private Airport departure;
	private String company;
	private String id;
	private long realTime;

	
	public Flight(String id, Airport a1, Airport a2, String company, String model, Plane plane) 
	{
		this.id = id;
		this.departure = a1;
		this.arrival = a2;
		this.company = company;
		this.modelAvion=model;
		this.plane=plane;
	}

	public String bitMapInfoVol()
	{
		String str = "";
		
		str+="Identifiant : "+ id +
				"\nDepart : "+departure.getCityName()+
				"\nArriv�e : "+arrival.getCityName()+
				"\nVitesse : "+(plane.getSpeedX()+plane.getSpeedY())+
				"\nAltitude : "+plane.getGeolocation().getHeight()+
				"\nType Avion : "+modelAvion;
		
		return str;
	}
	@Override
	public String toString()
	{
		String str = "";
		
		str+= "vol numéro "+id+" au bord d'un " + modelAvion +" appartenant a la compagnie "+company+" qui va de : "+ departure.getShortName() +" à "+arrival.getShortName(); 
		return str;
	}
	
	/**
	 * returne un tableau de taille 2 contenant le nom du pays de depart et arrivee du vol
	 * @param f le vol
	 * @return tableau de string
	 */
	public String[] printCountriesOfFlight()
	{
		String[] toReturn = new String[2];
		
		toReturn[0] = getDeparture().getCountry();
		toReturn[1] = getArrival().getCountry();
		
		return toReturn;
	}
	
	/**
	 * returne un tableau de taille 2 contenant les noms de la ville de depart et arrivee du vol
	 * @param f le vol
	 * @return tableau de string
	 */
	public String[] printCitiesOfFlight()
	{
		String[] toReturn = new String[2];
		
		toReturn[0] = getDeparture().getCityName();
		toReturn[1] = getArrival().getCityName();
		
		return toReturn;
	}

	/**
	 * @return the arrival
	 */
	public Airport getArrival() 
	{
		return arrival;
	}

	/**
	 * @return the departure
	 */
	public Airport getDeparture() 
	{
		return departure;
	}

	/**
	 * @param plane the plane to set
	 */
	public void setPlane(Plane plane) 
	{
		this.plane = plane;
	}

	/**
	 * @return the id
	 */
	public String getId() 
	{
		return id;
	}

	/**
	 * @return the plane
	 */
	public Plane getPlane() 
	{
		return plane;
	}

	/**
	 * @param realTime the realTime to set
	 */
	public void setRealTime(long realTime) 
	{
		this.realTime = realTime;
	}
	
	
	public boolean landed()
	{
		if (this.getArrival().getGeolocation().isCloseTo(this.getPlane().getGeolocation())) return true;
		else return false;
	}

	/**
	 * @return the modelAvion
	 */
	public String getModelAvion() {
		return modelAvion;
	}
	
	
	
	
	

	

}
