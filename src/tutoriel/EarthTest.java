package tutoriel;

import java.sql.Date;
import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;

import classes.Airport;
import classes.Flight;
import controller.Controller;

public class EarthTest extends SimpleApplication 
{
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
	private static final float TEXTURE_LON_OFFSET = 2.8f;
	private Controller controller;
	
	public EarthTest(Controller controller)
	{
		this.controller=controller;
		// TODO Auto-generated constructor stub
	}

	public EarthTest(AppState... initialStates) 
	{
		super(initialStates);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void simpleUpdate(float tpf)
	{
		if(!controller.isPause())
		{
			System.out.println();
			controller.incrementCurrentTime(500000);
			controller.setD(new Date(controller.getRealCurrentTime()));
			WindowedTest.getTime().setText("    temps : "+WindowedTest.getShortDateFormat().format(controller.getD()));
			controller.updateRealTimeFlightsData(controller.getRealTimeFile(), controller.getLastUpdateTime(controller.getRealTimeFile()));
			paintPlanes(controller.getFlights());
		}
		else
		{
			paintPlanes(controller.getFlights());	
		}
	}

	
	@Override
	public void simpleInitApp() 
	{
		
		
		assetManager.registerLocator("earth.zip",ZipLocator.class);
		
		Spatial earth_geom =assetManager.loadModel("earth/Sphere.mesh.xml");
		Node earth_node = new Node("earth");
		earth_node.attachChild(earth_geom);
		earth_node.setLocalScale(5.0f);
		rootNode.attachChild(earth_node);
		// TODO Auto-generated method stub
		
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(-2,-10,1));
		directionalLight.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(directionalLight);
		
		viewPort.setBackgroundColor(new ColorRGBA(1f,0.2f,0.4f,1.0f));
		
		
		ChaseCamera chaseCam = new ChaseCamera(cam,earth_geom,inputManager);
		
		chaseCam.setDragToRotate(true);
		
		chaseCam.setInvertVerticalAxis(true);
		chaseCam.setRotationSpeed(10.0f);
		chaseCam.setMinVerticalRotation((float)-(Math.PI/2-0.0001f));
		chaseCam.setMaxVerticalRotation((float)Math.PI/2);
		chaseCam.setMinDistance(7.5f);
		chaseCam.setMaxDistance(30.0f);
		
		//création ligne
		Node LinesNode= new Node("LinesNode");
		Vector3f oldVect = new Vector3f(1,0,0);
		Vector3f newVect = new Vector3f(-1,1,0);
		
		Line line = new Line(oldVect,newVect);
		Geometry lineGeo = new Geometry("lineGeo",line);
		Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		Material mat2 = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		lineGeo.setMaterial(mat2);
		
		mat.getAdditionalRenderState().setLineWidth(4.0f);
		mat.setColor("Color",ColorRGBA.Green);
		LinesNode.setMaterial(mat);
		LinesNode.attachChild(lineGeo);
		paintAirport(controller.getAirports());
		rootNode.attachChild(LinesNode);
		
		
		//dessineHelice(new Vector3f(0.f,0.f,0.f));
	}

	public void dessineHelice(Vector3f vect)
	{
		Vector3f oldVect = vect;
		
		for(int i=0; i<10000;i++)
		{
			float t = i/5.f;
			
			Vector3f newVect = new Vector3f(FastMath.cos(t), t/5.0f, FastMath.sin(t));
			
			Node LinesNode= new Node("LinesNode");
			
			Line line = new Line(oldVect,newVect);
			Geometry lineGeo = new Geometry("lineGeo",line);
			Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
			Material mat2 = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
			lineGeo.setMaterial(mat2);						
			mat.getAdditionalRenderState().setLineWidth(4.0f);
			mat.setColor("Color",ColorRGBA.Green);
			LinesNode.setMaterial(mat);
			LinesNode.attachChild(lineGeo);
			
			rootNode.attachChild(LinesNode);
			
			
			oldVect = newVect;
		}
	}
	
	
	//A TERMINER EN SINSPIRER DE HELICE
	/*
	public void drawPath(ArrayList<Geolocation> path )
	{
		Vector3f oldVect = geoCoordTo3dCoord(path.get(0).getLatitude(), path.get(0).getLongitude());
		
		for(int i = 1 ; i< path.size()-1; i++)
		{
			
		}
	}
	*/
	
	
	private static Vector3f geoCoordTo3dCoord(float lat, float lon)
	{
		float lat_cor = lat+TEXTURE_LAT_OFFSET;
		float lon_cor = lon+TEXTURE_LON_OFFSET;
		return new Vector3f(-FastMath.sin(lon_cor*FastMath.DEG_TO_RAD)
							*FastMath.cos(lat_cor*FastMath.DEG_TO_RAD),
							 FastMath.sin(lat_cor*FastMath.DEG_TO_RAD),
							-FastMath.cos(lon_cor*FastMath.DEG_TO_RAD)
							*FastMath.cos(lat_cor*FastMath.DEG_TO_RAD)).mult(5);
	}
	
	
	public void displayAirport(float latitude,float longitude)
	{
		
		Vector3f v = geoCoordTo3dCoord(latitude, longitude);
		Sphere sphere = new Sphere(16,8,0.008f);
		Geometry aeroportGeom = new Geometry("Aeroport",sphere);
		Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		aeroportGeom.setMaterial(mat);
		rootNode.attachChild(aeroportGeom);
		aeroportGeom.setLocalTranslation(v);
	}
	
	
	
	public void displayPlane(Flight f)
	{
		Node n= (Node) rootNode.getChild(f.getId());
		
		if(!f.getPlane().isisArrived())
		{
			
			if(n==null)
			{
				//cree un node pour l'avion
				Node planeNode = new Node(f.getId());
				Spatial plane_geom =assetManager.loadModel("earth/plane.obj");
				Vector3f v = geoCoordTo3dCoord(f.getPlane().getGeolocation().getLatitude(), f.getPlane().getGeolocation().getLongitude());
				Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
				mat.setColor("Color", ColorRGBA.Green);
				plane_geom.setMaterial(mat);
				planeNode.attachChild(plane_geom);
				rootNode.attachChild(planeNode);
				//deplace l'avion
				planeNode.setLocalTranslation(v.mult(1+ (f.getPlane().getGeolocation().getHeight())/100000));
				planeNode.scale(0.1f);
				planeNode.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
				planeNode.rotate((float)Math.PI/2,0,0);
				planeNode.rotate(0,(float)(f.getPlane().getDirection()*(Math.PI/180)),0);
				
			}
			else
			{
				
				
				
				Vector3f v = geoCoordTo3dCoord(f.getPlane().getGeolocation().getLatitude(), f.getPlane().getGeolocation().getLongitude());
				n.setLocalTranslation(v.mult(1+ (f.getPlane().getGeolocation().getHeight())/100000));
				n.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
				n.rotate((float)Math.PI/2,0,0);
				n.rotate(0,(float)(f.getPlane().getDirection()*(Math.PI/180)),0);
				
				
				
			}
		}
	
		
		
		
	}
	
	
	public void paintPlanes(ArrayList<Flight> flights)
	{
		Node n;
		for(Flight f : flights)
		{
			if (!f.getPlane().isisArrived())
			{
				displayPlane(f);
				f.getPlane().setisArrived(f.landed());
			}
			else if ((n = (Node) rootNode.getChild(f.getId()))  !=  null)
			{
				System.out.println("Je LE REMOVE DU NODe aAAAAARGH");
				n.removeFromParent();
			}
		}
	}
	
	public void paintAirport(ArrayList<Airport> aeroport)
	{
		
		for(Airport a : aeroport)
		{
			displayAirport(a.getGeolocation().getLatitude(),a.getGeolocation().getLongitude());
		}
	}


}