package affichage;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;

import classes.Airport;
import classes.Flight;
import classes.Geolocation;
import controller.Controller;

public class EarthTest extends SimpleApplication 
{
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
	private static final float TEXTURE_LON_OFFSET = 2.8f;
	private Controller controller;
	private ChaseCamera chaseCam;

	private ArrayList<CountryNode> countryNodes = new ArrayList<CountryNode>();
	
	
	/**
	 * Constructeur qui lie un controller à l'application
	 * @param controller
	 */
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
	
	/**
	 * fonction appelé en boucle qui gère les affichages en fonciton des options choisies
	 */
	@Override
	public void simpleUpdate(float tpf)
	{
		if(controller.isPrintPlane())
		{
			if(!controller.isPause())
			{
				controller.incrementCurrentTime(10000*controller.getSpeedTime());
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
		else
		{
			suprOtherNodeFlight(null);
		}
		if(controller.getVolSelection()!=null && controller.isPrintPathPlane() )
		{
			if(rootNode.getChild("path")!=null)
			{
				rootNode.getChild("path").removeFromParent();
			}
			controller.setVolSelectionAsChanged(false);
			ArrayList<Geolocation> path = controller.getPathOf(controller.getVolSelection());
			drawPath(path);
		}
		if(controller.isPrintAirport() && !controller.isAlreadyPrintAirport())
		{
			suprAllAirportNode();
			paintAirport(controller.getAirports());
			controller.setAlreadyPrintAirport(true);
		}
		
		
	}

	/**
	 * trouve le Node associé à un pays et le renvoie
	 * @param name
	 * @return CountryNode : le node du pays recherché ou null s'il n'existe pas
	 */
	public CountryNode findCountryNodeNamed(String name)
	{
		for(CountryNode c : countryNodes)
		{
			if(c.getName().equals(name))
			{
				return c;
			}
		}
		return null;
	}
	
	/**
	 * fonction qui initialise les données de l'application (charge la terre...)
	 */
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
		
		for(Airport a : controller.getAirports())
		{
			CountryNode b;
			if(findCountryNodeNamed(a.getCountry())!=null)
			{
				b = findCountryNodeNamed(a.getCountry());
			}
			else
			{
				b = new CountryNode(a.getCountry());
				countryNodes.add(b);
				rootNode.attachChild(b);
			}
			AirportNode c = new AirportNode(a.getShortName());
			b.attachChild(c);
			b.airportNodes.add(c);
		}

		paintAirport(controller.getAirports());
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(-2,-10,1));
		directionalLight.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(directionalLight);
		
		viewPort.setBackgroundColor(ColorRGBA.Black);
		
		flyCam.setEnabled(false);
		
		chaseCam = new ChaseCamera(cam,earth_geom,inputManager);
		
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
		mat.setColor("Color",ColorRGBA.Black);
		LinesNode.setMaterial(mat);
		LinesNode.attachChild(lineGeo);
		rootNode.attachChild(LinesNode);
	}
	
	/**
	 * Affiche les trajectoires déjà effectuées à l'aide d'une arrayList de position rentrée
	 * @param path
	 */
	public void drawPath(ArrayList<Geolocation> path)
	{
		if(path.size()>0)
		{
			Vector3f oldVect = geoCoordTo3dCoord(path.get(0).getLatitude(), path.get(0).getLongitude()).mult(1+ path.get(0).getHeight()/100000);
			Node allPathNode = new Node("path");
			
			for(int i = 1 ; i< path.size()-1; i++)
			{
				Vector3f newVect = geoCoordTo3dCoord(path.get(i).getLatitude(), path.get(i).getLongitude()).mult(1+ path.get(i).getHeight()/100000);
				
				Node pathNode = new Node();
				
				Line line = new Line(oldVect, newVect);
				Geometry lineGeo = new Geometry("lineGeo",line);
				Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
				Material mat2 = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
				lineGeo.setMaterial(mat2);						
				mat.getAdditionalRenderState().setLineWidth(4.0f);

				mat2.setColor("Color",ColorRGBA.Magenta);
				pathNode.setMaterial(mat);
				pathNode.attachChild(lineGeo);
				allPathNode.attachChild(pathNode);
				oldVect = newVect;
			}
			rootNode.attachChild(allPathNode);
		}

		
	}

	/**
	 * traduit les données de géolocalisation en Vector3f pour positionner un objet dans l'espace
	 * @param lat
	 * @param lon
	 * @return Vector3F : les positions de l'objet en question 
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
	
	
	/**
	 * effectue l'affichage d'un aéroport avec les informations rentrées 
	 * @param latitude
	 * @param longitude
	 * @param shortName
	 * @param country
	 */
	public void displayAirport(float latitude,float longitude,String shortName,String country)
	{
		Vector3f v = geoCoordTo3dCoord(latitude, longitude);
		Sphere sphere = new Sphere(16,8,0.008f);
		Geometry aeroportGeom = new Geometry("Aeroport",sphere);
		Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		aeroportGeom.setMaterial(mat);
		

		findCountryNodeNamed(country).findAirportNodeNamed(shortName).attachChild(aeroportGeom);
		findCountryNodeNamed(country).attachChild(findCountryNodeNamed(country).findAirportNodeNamed(shortName));
		
		aeroportGeom.setLocalTranslation(v);
	}
	
	/**
	 * effectue l'affichage d'un avion avec les informations du vol
	 * @param f
	 */
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
				planeNode.scale(0.05f);
				planeNode.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
 				planeNode.rotate((float)Math.PI/2,0,0);
 				planeNode.rotate(0,(float)(f.getPlane().getDirection()*(Math.PI/180)),0);
 				
 			}
 			else
 			{
 				if(f.getPlane().isGrounded())
 				{
 					n.removeFromParent();
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
	}

	/**
	 * suprime les nodes des aéroports en parcourant les nodes des pays
	 * (les détaches du rootnNode, ne les supprime pas vraiment)
	 */
	public void suprAllAirportNode()
	{
		for(CountryNode a : countryNodes)
		{
			a.suprAllAirportNode();
		}
	}

	/**
	 * suprime les nodes des flight n'ayant pas pour Id celle rentrée en paramètre
	 * (les détaches du rootnNode, ne les supprime pas vraiment)
	 * @param id
	 */
	public void suprOtherNodeFlight(String id)
	{
		for(Flight f : controller.getFlights())
		{
			if(rootNode.getChild(f.getId())!=null && id !=f.getId())
			{
				rootNode.getChild(f.getId()).removeFromParent();
			}
		}
	}
	
	/**
	 * permet l'affichage de l'ensemble des avions concernés en fonction des optiosn choisies
	 * @param flights
	 */
	public void paintPlanes(ArrayList<Flight> flights)
	{
		if(controller.getVolSelection()==null)
		{
			for(Flight f : flights)
			{
				if (!f.getPlane().isisArrived())
				{
					if(controller.isPrintOnlyCountry())
					{
						
						if(controller.getCountrySelection()!= null  && 
								(f.getArrival().getCountry().equals(controller.getCountrySelection())|| controller.getCountrySelection().equals(f.getDeparture().getCountry())))
						{
							
							displayPlane(f);
							f.getPlane().setisArrived(f.landed());
						}
						else
						{
							if(rootNode.getChild(f.getId())!=null)
							{
								rootNode.getChild(f.getId()).removeFromParent();
							}
						}
					}
					else if(controller.isPrintOnlyAirport())
					{
						if(controller.getAirportSelection()!= null  && 
								(f.getArrival().getShortName().equals(controller.getAirportSelection())) || f.getDeparture().getShortName().equals(controller.getAirportSelection()))
						{
							displayPlane(f);
							f.getPlane().setisArrived(f.landed());
						}
						else
						{
							if(rootNode.getChild(f.getId())!=null)
							{
								rootNode.getChild(f.getId()).removeFromParent();
							}
						}
					}
					else
					{
						displayPlane(f);
						f.getPlane().setisArrived(f.landed());
					}
				}
				else if(rootNode.getChild(f.getId())!=null)
				{
					rootNode.getChild(f.getId()).removeFromParent();
				}
			}
		}
		else
		{
			if(controller.isPrintOnlyCountry() && controller.getCountrySelection()!= null  && 
					(controller.getVolSelection().getArrival().getCountry()==controller.getCountrySelection()|| controller.getCountrySelection()==controller.getVolSelection().getDeparture().getCountry()))
			{
				suprOtherNodeFlight(controller.getVolSelection().getId());
				displayPlane(controller.getVolSelection());
				Node text_node = new Node("texte");
				BitmapText etiquette = new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt"), false);
				text_node.attachChild(etiquette);
				rootNode.attachChild(text_node);
				etiquette.setLocalScale(100);
			}
			else 
			{
				suprOtherNodeFlight(controller.getVolSelection().getId());
				displayPlane(controller.getVolSelection());
				
				if(((Node)rootNode.getChild(controller.getVolSelection().getId()))!=null)
				{
					Node n = (Node)((Node)rootNode.getChild(controller.getVolSelection().getId())).getChild("texte");
					if(n == null)
					{
						BitmapFont gFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
						Node text_node = new Node("texte"+controller.getVolSelection().getId());
						
						BitmapText etiquette = new BitmapText(gFont, false);
						
						etiquette.setColor(ColorRGBA.White);
						etiquette.setText(controller.getVolSelection().bitMapInfoVol());
						etiquette.setSize(gFont.getCharSet().getRenderedSize());
						etiquette.scale(0.10f);
						etiquette.setLocalTranslation(0f, 5f, 0f);
						etiquette.setLocalTranslation(1f, -2f, 4f);
						text_node.attachChild(etiquette);
						BillboardControl control = new BillboardControl();
						text_node.addControl(control);
						((Node)rootNode.getChild(controller.getVolSelection().getId())).attachChild(text_node);
					}
				}
			}
			
		}
		
	}
	
	/**
	 * permet l'affichage des aéroports en fonction des options choisies
	 * @param aeroport
	 */
	public void paintAirport(ArrayList<Airport> aeroport)
	{
		for(Airport a : aeroport)
		{
			if(controller.isPrintOnlyCountry())
			{
				if(a.getCountry().equals(controller.getCountrySelection()))
				{
					displayAirport(a.getGeolocation().getLatitude(),a.getGeolocation().getLongitude(),a.getShortName(),a.getCountry());
				}
			}
			else
			{
				displayAirport(a.getGeolocation().getLatitude(),a.getGeolocation().getLongitude(),a.getShortName(),a.getCountry());
			}
			
		}
	}

	/**
	 * @return the chaseCam
	 */
	public ChaseCamera getChaseCam() {
		return chaseCam;
	}

	/**
	 * @param chaseCam the chaseCam to set
	 */
	public void setChaseCam(ChaseCamera chaseCam) {
		this.chaseCam = chaseCam;
	}
	
	
	


}