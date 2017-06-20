package classes;

import java.util.ArrayList;

import com.jme3.scene.Node;

public class CountryNode extends Node
{
	public CountryNode(String name)
	{
		super(name);
	}
	public ArrayList<AirportNode> airportNodes = new ArrayList<AirportNode>();
	
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
	public void suprAllAirportNode()
	{
		for(AirportNode a : airportNodes)
		{
			a.removeFromParent();			
		}
	}

}
