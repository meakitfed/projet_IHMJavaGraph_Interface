package tutoriel;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import controller.Controller;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class WindowedTest 
{
	
	// TODO : Add here a static variable which will make possible 
	// to store a link to your JMonkeyEngine application 
	// For example : private static MyApplication canvasApplication;
	private static EarthTest canvasApplication;
	
	private static Canvas canvas; // JAVA Swing Canvas
	
	private static JFrame frame;
	private static JPanel panel;
	private static Controller c;
	
	
	private static void createNewJFrame() 
	{

		frame = new JFrame("Java - Graphique - IHM");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e) 
			{
				// TODO : Uncomment this in order to stop the application
				// when the windows will be closed.
				canvasApplication.stop();
			}
		});
		
		panel = new JPanel(new BorderLayout());

		// Create the menus
		JPanel conteneur = new JPanel();
		conteneur.setLayout(new FlowLayout());
		conteneur.setPreferredSize(new Dimension(220,frame.getHeight()));
		
		JPanel tempsReel = new JPanel();
		tempsReel.setLayout(new BorderLayout());
		tempsReel.setPreferredSize(new Dimension(200,100));
		tempsReel.setBorder(BorderFactory.createTitledBorder("Temps"));
		
		JButton demarrer=new JButton("Démarrer");
		
		JPanel timePanel = new JPanel();
		JLabel time = new JLabel(" temps ");
		JSlider inputTime = new JSlider(JSlider.HORIZONTAL, 0,10,1);
		timePanel.add(time);
		timePanel.add(inputTime);
		
		tempsReel.add(demarrer,BorderLayout.NORTH);
		tempsReel.add(time,BorderLayout.CENTER);
		tempsReel.add(inputTime,BorderLayout.SOUTH);
		
		
		
		
		JPanel volSelection = new JPanel();
		volSelection.setPreferredSize(new Dimension(200,130));
		volSelection.setBorder(BorderFactory.createTitledBorder("Selection Vol"));

		DefaultListModel dlm = new DefaultListModel();
		JList selection = new JList(dlm);
		dlm.addElement("donne 1");
		dlm.addElement("donne 2");
		dlm.addElement("donne 3");
		dlm.addElement("donne 4");
		
		
		volSelection.add(selection);
		
		JPanel informationVolSelection = new JPanel();
		informationVolSelection.setPreferredSize(new Dimension(200,200));
		informationVolSelection.setBorder(BorderFactory.createTitledBorder("Infos vol sélectionné"));
		informationVolSelection.setLayout(new BoxLayout(informationVolSelection,BoxLayout.Y_AXIS));
		
		JLabel id = new JLabel("Identifiant : ");
		JLabel depart = new JLabel("Depart : ");
		JLabel arrive = new JLabel("Arrivée : ");
		JLabel vitesse = new JLabel("Vitesse : ");
		JLabel altitude = new JLabel("Altitude : ");
		JLabel typeAvion = new JLabel("Type avion : ");
		JButton vueAvionButton = new JButton("Vue Avion");
		
		informationVolSelection.add(id);
		informationVolSelection.add(typeAvion);
		informationVolSelection.add(vitesse);
		informationVolSelection.add(altitude);
		informationVolSelection.add(depart);
		informationVolSelection.add(arrive);
		informationVolSelection.add(vueAvionButton);
		
		
		JPanel affichage = new JPanel();
		affichage.setPreferredSize(new Dimension(200,200));
		affichage.setBorder(BorderFactory.createTitledBorder("Affichage"));
		affichage.setLayout(new FlowLayout());
		
		JCheckBox affichageAeroport = new JCheckBox("aéroport",true);
		JCheckBox affichageAvions = new JCheckBox("avions",true);
		JCheckBox affichageTrajectoire = new JCheckBox("trajectoire",true);
		
		JPanel filtresVol = new JPanel();
		filtresVol.setPreferredSize(new Dimension(180,100));
		filtresVol.setBorder(BorderFactory.createTitledBorder("Filtres vols"));
		filtresVol.setLayout(new BoxLayout(filtresVol,BoxLayout.Y_AXIS));
		
		JRadioButton radioButton1 = new JRadioButton("option 1 affichage");
		filtresVol.add(radioButton1);
		
		affichage.add(affichageAeroport);
		affichage.add(affichageAvions);
		affichage.add(affichageTrajectoire);
		affichage.add(filtresVol);
		
		
		conteneur.add(tempsReel);
		conteneur.add(volSelection);
		conteneur.add(informationVolSelection);
		conteneur.add(affichage);
		
		
		
		panel.add(conteneur, BorderLayout.WEST);
		
		// Add the canvas to the panel
		panel.add(canvas, BorderLayout.CENTER);
		
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	

	public static void main(String[] args)
	{
		c= new Controller();
		// create new JME appsettings
		AppSettings settings = new AppSettings(true);
		settings.setResolution(1280, 800);
		settings.setSamples(8);
		settings.setFrameRate(60);
		settings.setVSync(true);

		// TODO : create here a new JMonkeyEngine application
		canvasApplication = new EarthTest(c);
		
		// TODO : apply the settings and configure our application
		// in the same way than in the "public static void main()" method from SimpleApplication
		canvasApplication.setSettings(settings);
		canvasApplication.setShowSettings(false);
		canvasApplication.setDisplayStatView(false);
		canvasApplication.setDisplayFps(false);
		canvasApplication.setPauseOnLostFocus(false);
		// TODO : Uncomment this line to start the application
		// NB : this line is used instead of the app.start();
		canvasApplication.createCanvas(); // create canvas!
		
		
		// TODO : Uncomment the following lines to get the canvas from our application
		JmeCanvasContext ctx = (JmeCanvasContext) canvasApplication.getContext();
		canvas = ctx.getCanvas();
		Dimension dim = new Dimension(settings.getWidth(), settings.getHeight());
		canvas.setPreferredSize(dim);

		// Create the JFrame with the Canvas on the middle
		createNewJFrame();
	}

}
