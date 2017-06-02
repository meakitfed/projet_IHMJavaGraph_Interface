package tutoriel;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class CubesTest extends SimpleApplication 
{
	Box b = new Box(1,1,1);
	Geometry geom = new Geometry("Box",b);
	
	Box v = new Box(1,1,1);
	Geometry geom2 = new Geometry("Box",v);
	
	Box r = new Box(1,1,1);
	Geometry geom3 = new Geometry("Box",r);
	public CubesTest() 
	{
		// TODO Auto-generated constructor stub
	}

	public CubesTest(AppState... initialStates) 
	{
		super(initialStates);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void simpleUpdate(float tpf)
	{
		geom3.rotate(0.001f,0.001f,0.001f);
	}

	@Override
	public void simpleInitApp() 
	{
		
		
		Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color",ColorRGBA.Blue);
		geom.setMaterial(mat);
		
		
		
		Material mat2 = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color",ColorRGBA.Green);
		geom2.setMaterial(mat2);
		geom2.setLocalTranslation(0f,3f,0f);
		
		
		
		Material mat3 = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat3.setColor("Color",ColorRGBA.Red);
		geom3.setMaterial(mat3);
		geom3.setLocalTranslation(3f,0f,-3f);
		
		
		
		rootNode.attachChild(geom);
		rootNode.attachChild(geom2);
		rootNode.attachChild(geom3);
		// TODO Auto-generated method stub
		
		flyCam.setEnabled(false);
		
		ChaseCamera chaseCam = new ChaseCamera(cam,geom,inputManager);
		
		chaseCam.setDragToRotate(true);
		
		chaseCam.setInvertVerticalAxis(true);
		chaseCam.setRotationSpeed(10.0f);
		chaseCam.setMinVerticalRotation((float)-(Math.PI/2-0.0001f));
		chaseCam.setMaxVerticalRotation((float)Math.PI/2);
		chaseCam.setMinDistance(7.5f);
		chaseCam.setMaxDistance(30.0f);

	}

	public static void main(String[] args) 
	{
		AppSettings settings = new AppSettings(true);
		settings.setResolution(1280,800);
		settings.setSamples(8);
		settings.setFrameRate(60);
		settings.setVSync(true);
		
		CubesTest app = new CubesTest();
		app.setSettings(settings);
		app.setShowSettings(false);
		app.setDisplayStatView(false);
		app.setDisplayFps(false);
		//app.createCanvas();
		app.start();
		// TODO Auto-generated method stub

	}

}
