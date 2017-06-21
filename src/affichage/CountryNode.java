package affichage;

import java.util.ArrayList;

import com.jme3.scene.Node;

public class CountryNode extends Node
{
	public ArrayList<AirportNode> airportNodes = new ArrayList<AirportNode>();
	
	/**
	 * constructeur d'un node concernant un pays
	 * @param name
	 */
	public CountryNode(String name)
	{
		super(name);
	}
	
	/**
	 * trouve un aéroport avec son nom dans l'arrayList d'aéroport du pays
	 * @param name
	 * @return
	 */
	public AirportNode findAirportNodeNamed(String name)
	{
		for(AirportNode c : airportNodes)
		{
			if(c.getName().equals(name))
			{
				return c;
			}
		}
		return null;
	}
	/**
	 * supprime tous les aéroports liés au node d'un pays
	 */
	public void suprAllAirportNode()
	{
		for(AirportNode a : airportNodes)
		{
			a.removeFromParent();			
		}
	}

}
