package classes;

import java.util.ArrayList;

import com.jme3.scene.Node;

import Data.FlightNode;

public class AirportNode extends Node
{
	public Node airport;
	public ArrayList<FlightNode> flightsNode = new ArrayList<FlightNode>();
	
	public AirportNode(String name)
	{
		super(name);
	}
	
	public FlightNode findFlightNodeNamed(String name)
	{
		for(FlightNode f : flightsNode)
		{
			if(f.getName().equals(name))
			{
				return f;
			}
		}
		return null;
	}
	public void suprOtherNodeFlight(String id)
	{
		for(FlightNode f : flightsNode)
		{
			if(!f.getName().equals(id))
			{
				f.removeFromParent();
			}
			
		}
	}
}
