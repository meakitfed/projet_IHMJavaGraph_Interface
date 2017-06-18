package controller;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Date;
import java.util.ArrayList;

import classes.Airport;
import classes.Flight;
import classes.Geolocation;
import classes.Plane;

public class Controller 
{	
	private ArrayList<Flight> flights=new ArrayList<Flight>();
	private ArrayList<Airport> airports=new ArrayList<Airport>();
	private long t0;
	private long currentTime = 0;
	private String realTimeFile = "src/Data/realtime_flights.dat";
	private boolean fin = false;
	private int indiceLigne = 0;
	private boolean pause=true;
	private int speedTime;
	private Flight volSelection;
	private boolean printPlane;
	private boolean printAirport;
	private boolean alreadyPrintAirport;
	private boolean printPathPlane;
	private boolean volSelectionAsChanged;
	private boolean planeView;
	private String airportSelection;
	private String countrySelection;
	private boolean printOnlyAirport;
	private boolean printOnlyCountry;
	Date d;
	
	
	/**
	 * 
	 * constructeur du du controller
	 * 
	 * initialise les arraylist d'aeroports et vols
	 */
	public Controller() 
	{
		getAirportData("src/Data/airports.dat");
		getFlightData("src/Data/flights.dat"); 
		getT0("src/Data/realtime_flights.dat");
		updateRealTimeFlightsData("src/Data/realtime_flights.dat", t0);
		d = new Date(getRealCurrentTime());
		speedTime=5;
		printPlane=true;
		printAirport=true;
		alreadyPrintAirport=false;
		printPathPlane=false;
		volSelectionAsChanged=false;
		planeView=false;
		printOnlyAirport=false;
		printOnlyCountry=false;
	}
	
	
	
	public void getAirportData(String path) 
	{
		try 
		{
			FileReader file=new FileReader(path);
			BufferedReader bufRead = new BufferedReader(file);

			String line= bufRead.readLine();
			
			
			while(line != null)
			{
				String[] array = line.split("///");
				float lon= Float.parseFloat(array[4]);
				float lat = Float.parseFloat(array[3]);
				airports.add(new Airport(new Geolocation(lon,lat,0),array[0],array[1],array[2]));				
				line = bufRead.readLine();
			}
			
			bufRead.close();
			file.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void getT0(String path)
	{
		try 
		{
			FileReader file=new FileReader(path);
			BufferedReader bufRead = new BufferedReader(file);
				
			String line= bufRead.readLine();
			String[] firstLine = line.split("///");
			
			this.t0 = Long.parseLong(firstLine[0]);
			
			bufRead.close();
			file.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public long getLastUpdateTime(String path)
	{
		try 
		{
			FileReader file=new FileReader(path);
			LineNumberReader bufRead = new LineNumberReader(file);
			long lastTime = t0;

			String line= bufRead.readLine();
			
			while(bufRead.getLineNumber()<indiceLigne) bufRead.readLine();
			
			while(line != null)
			{
				String[] array = line.split("///");
							
				if( (Long.parseLong(array[0]) - t0) == currentTime)
				{
					bufRead.close();
					return currentTime;
				}
				else if((Long.parseLong(array[0]) - t0)  > currentTime)
				{

					bufRead.close();
					return lastTime;
				}
				
				lastTime = Long.parseLong(array[0]);	
					
				line = bufRead.readLine();
			}
			
			bufRead.close();
			file.close();
			
			return lastTime;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return currentTime;
	}
	
	/**
	 * 
	 * update les données des vols avec les données 
	 * 
	 * @param path vers le fichier
	 */
	public void updateRealTimeFlightsData(String path, long time) 
	{
		if (currentTime + t0  > new Long((long) 1496195547396f))
		{
			//System.out.println("dernier temps atteint");
			fin =true;
		}
		try 			
		{
			FileReader file=new FileReader(path);
			LineNumberReader bufRead = new LineNumberReader(file);
				
			String line= bufRead.readLine();
			
			while(bufRead.getLineNumber()<indiceLigne) bufRead.readLine();
				
			while(line != null)
			{
				String[] array = line.split("///");
				try
				{
					if(time == (Long.parseLong(array[0]))) 
					{
						indiceLigne = bufRead.getLineNumber();
						float lat = Float.parseFloat(array[2]);
						float lon= Float.parseFloat(array[3]);
						float height = Float.parseFloat(array[4]);
						float speedX = Float.parseFloat(array[5]);
						float direction = Float.parseFloat(array[6]);
						float horodatageLocation = Float.parseFloat(array[7]);
						float horodatageSpeed = Float.parseFloat(array[8]);
						float speedY = Float.parseFloat(array[9]);
						boolean grounded = Boolean.parseBoolean(array[10]);
						for(Flight f: flights)
						{
							if(f.getId().equals(array[1].trim()))
							{
								f.setRealTime(currentTime - t0);
								f.getPlane().getGeolocation().setHeight(height);
								f.getPlane().getGeolocation().setLatitude(lat);
								f.getPlane().getGeolocation().setLongitude(lon);
								f.getPlane().setSpeedX(speedX);
								f.getPlane().setSpeedY(speedY);
								f.getPlane().setHorodatageGeolocalisation(horodatageLocation);
								f.getPlane().setHorodatageSpeed(horodatageSpeed);
								f.getPlane().setDirection(direction);
								f.getPlane().setGrounded(grounded);
							}
						}
					}
					else if(time < (Long.parseLong(array[0])))
					{
						return;
					}
				}
				catch(NumberFormatException e)
				{
					//System.err.println("Données erronées");
					
				}
					
					line = bufRead.readLine();
			}
			
			
			
			bufRead.close();
			file.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * @return the fin
	 */
	public boolean isFin() {
		return fin;
	}


	/**
	 * retourne un arraylist de geoloc qui contient tos les positions par ou passe le vol
	 * @param f  vol
	 * @return  path  associe au vol
	 */
	public ArrayList<Geolocation> getPathOf(Flight f)
	{
		ArrayList<Geolocation> toReturn = new ArrayList<Geolocation>();
		
		try 			
		{
			FileReader file=new FileReader(realTimeFile);
			LineNumberReader bufRead = new LineNumberReader(file);
				
			String line= bufRead.readLine();
					
			
			while(line != null)
			{
				String[] array = line.split("///");
				try
				{
					if(f.getId().equals(array[1].trim())) 
					{
						float lat = Float.parseFloat(array[2]);
						float lon= Float.parseFloat(array[3]);
						float height = Float.parseFloat(array[4]);
						toReturn.add(new Geolocation(lon, lat, height));
					}
		
				}
				catch(NumberFormatException e)
				{
					System.err.println("Données non conventionnelles");
				}
					
				line = bufRead.readLine();
			}
			
			
			
			bufRead.close();
			file.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(toReturn);
		return toReturn;
	}
	

	/**
	 * Cherche le vol d'id entré en parametre dans l'arrayist flights
	 * 
	 * @param id id du vol recherché
	 * @return le vol recherche ou null si ce vol n'est pas dans la liste
	 */
	public Flight getFlightByID(String id)
	{
		Flight toReturn = null;
		
		for (Flight f : flights)
		{
			if (f.getId().equals(id))
			{
				toReturn = f;
			}
		}
		
		return toReturn;
	}
	
	
	/**
	 * 
	 * @param country destination
	 * @return un arraylist contenant les vols allant a country
	 */
	public ArrayList<Flight> getFlightsGoingTo(String country)
	{
		ArrayList<Flight> toReturn = new ArrayList<Flight>();
		
		for (Flight f : flights)
		{
			if (f.getArrival().getCountry().equals(country))
			{
				toReturn.add(f);
			}
			
		}
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param a destination aeroport
	 * @return un arraylist contenant les vols allant a a
	 */
	public ArrayList<Flight> getFlightsGoingTo(Airport a)
	{
		ArrayList<Flight> toReturn = new ArrayList<Flight>();
		
		for (Flight f : flights)
		{
			if (f.getArrival() == a) //attention ?
			{
				toReturn.add(f);
			}
			
		}
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param country depart
	 * @return un arraylist contenant les vols venant de country
	 */
	public ArrayList<Flight> getFlightsFrom(String country)
	{
		ArrayList<Flight> toReturn = new ArrayList<Flight>();
		
		for (Flight f : flights)
		{
			if (f.getDeparture().getCountry().equals(country))
			{
				toReturn.add(f);
			}
			
		}
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param a depart
	 * @return un arraylist contenant les vols venant de a
	 */
	public ArrayList<Flight> getFlightGoingTo(Airport a)
	{
		ArrayList<Flight> toReturn = new ArrayList<Flight>();
		
		for (Flight f : flights)
		{
			if (f.getDeparture() == a)
			{
				toReturn.add(f);
			}
			
		}
		
		return toReturn;
	}
	
	public void incrementCurrentTime(long nb)
	{
		this.currentTime += nb;
	}
	
	/**
	 * 
	 * @param path
	 */
	public void getFlightData(String path) 
	{
		try 
		{
			FileReader file=new FileReader(path);
			BufferedReader bufRead = new BufferedReader(file);
				
			String line= bufRead.readLine();
			
			while(line != null)
			{
				String[] array = line.split("///");
				Airport temp1=null;
				Airport temp2=null;
				
				for(Airport a : airports)
				{
					if(a.getShortName().equals(array[1])) temp1 = a;
					if(a.getShortName().equals(array[2]) ) temp2 = a;
				}
				flights.add(new Flight(array[0], temp1, temp2, array[3], array[4], new Plane()));	
				line = bufRead.readLine();
			}
			
			bufRead.close();
			file.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}



	/**
	 * @return the airports
	 */
	public ArrayList<Airport> getAirports() 
	{
		return airports;
	}



	/**
	 * @return the flights
	 */
	public ArrayList<Flight> getFlights() {
		return flights;
	}
	



	/**
	 * @return the alreadyPrintAirport
	 */
	public boolean isAlreadyPrintAirport() {
		return alreadyPrintAirport;
	}



	/**
	 * @param alreadyPrintAirport the alreadyPrintAirport to set
	 */
	public void setAlreadyPrintAirport(boolean alreadyPrintAirport) {
		this.alreadyPrintAirport = alreadyPrintAirport;
	}



	/**
	 * @return the currentTime
	 */
	public long getCurrentTime() {
		return currentTime;
	}



	/**
	 * @return the realTimeFile
	 */
	public String getRealTimeFile() {
		return realTimeFile;
	}



	/**
	 * @return the pause
	 */
	public boolean isPause() 
	{
		return pause;
	}
	



	/**
	 * @return the speedTime
	 */
	public int getSpeedTime() {
		return speedTime;
	}
	



	/**
	 * @return the airportSelection
	 */
	public String getAirportSelection() {
		return airportSelection;
	}



	/**
	 * @param airportSelection the airportSelection to set
	 */
	public void setAirportSelection(String airportSelection) {
		this.airportSelection = airportSelection;
	}



	/**
	 * @return the countrySelection
	 */
	public String getCountrySelection() {
		return countrySelection;
	}



	/**
	 * @param countrySelection the countrySelection to set
	 */
	public void setCountrySelection(String countrySelection) {
		this.countrySelection = countrySelection;
	}



	/**
	 * @param speedTime the speedTime to set
	 */
	public void setSpeedTime(int speedTime) {
		this.speedTime = speedTime;
	}



	/**
	 * @param pause the pause to set
	 */
	public void setPause(boolean pause) 
	{
		this.pause = pause;
	}
	
	



	/**
	 * @return the printAirport
	 */
	public boolean isPrintAirport() {
		return printAirport;
	}



	/**
	 * @param printAirport the printAirport to set
	 */
	public void setPrintAirport(boolean printAirport) {
		this.printAirport = printAirport;
	}



	/**
	 * @param printPlane the printPlane to set
	 */
	public void setPrintPlane(boolean printPlane) {
		this.printPlane = printPlane;
	}
	
	
	



	/**
	 * @return the printPathPlane
	 */
	public boolean isPrintPathPlane() {
		return printPathPlane;
	}
	
	



	/**
	 * @return the planeView
	 */
	public boolean isPlaneView() {
		return planeView;
	}



	/**
	 * @param planeView the planeView to set
	 */
	public void setPlaneView(boolean planeView) {
		this.planeView = planeView;
	}



	/**
	 * @return the volSelectionAsChanged
	 */
	public boolean isVolSelectionAsChanged() {
		return volSelectionAsChanged;
	}



	/**
	 * @param volSelectionAsChanged the volSelectionAsChanged to set
	 */
	public void setVolSelectionAsChanged(boolean volSelectionAsChanged) {
		this.volSelectionAsChanged = volSelectionAsChanged;
	}



	/**
	 * @param printPathPlane the printPathPlane to set
	 */
	public void setPrintPathPlane(boolean printPathPlane) {
		this.printPathPlane = printPathPlane;
	}



	/**
	 * @return the printPlane
	 */
	public boolean isPrintPlane() {
		return printPlane;
	}




	/**
	 * @return the d
	 */
	public Date getD() {
		return d;
	}



	/**
	 * @param d the d to set
	 */
	public void setD(Date d) 
	{
		this.d = d;
	}
	public long getRealCurrentTime()
	{
		return currentTime+t0;
	}



	/**
	 * @return the volSelection
	 */
	public Flight getVolSelection() 
	{
		return volSelection;
	}
	



	/**
	 * @return the printOnlyAirport
	 */
	public boolean isPrintOnlyAirport() {
		return printOnlyAirport;
	}



	/**
	 * @param printOnlyAirport the printOnlyAirport to set
	 */
	public void setPrintOnlyAirport(boolean printOnlyAirport) {
		this.printOnlyAirport = printOnlyAirport;
	}



	/**
	 * @return the printOnlyCountry
	 */
	public boolean isPrintOnlyCountry() {
		return printOnlyCountry;
	}



	/**
	 * @param printOnlyCountry the printOnlyCountry to set
	 */
	public void setPrintOnlyCountry(boolean printOnlyCountry) {
		this.printOnlyCountry = printOnlyCountry;
	}



	/**
	 * @param volSelection the volSelection to set
	 */
	public void setVolSelection(Flight volSelection) 
	{
		this.volSelection = volSelection;
	}
	public Flight findFlightId(String id)
	{
		for(Flight f : flights)
		{
			if(f.getId().equals(id))
			{
				return f;
			}
		}
		return null;
	}
	
	
	
	
	
	
}
