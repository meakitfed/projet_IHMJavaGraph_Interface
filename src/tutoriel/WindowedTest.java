package tutoriel;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import classes.Flight;
import controller.Controller;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private static DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
	private static JLabel time = new JLabel();
	private static JList selection;
	private static JPanel informationVolSelection;
	
	
	private static void createNewJFrame() 
	{

		frame = new JFrame("Java - Graphique - IHM");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e) 
			{
				canvasApplication.stop();
			}
		});
		
		
		panel = new JPanel(new BorderLayout());
		/*panel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("CREATE HEATMAP");
				
				canvasApplication.enqueue(new Callable<Object>(
						public Object call() throws Exception 
						{
							canvasApplication.addHeatMap();
							return null;
						}}
			);
			}
		});*/

		// Create the menus
		JPanel conteneur = new JPanel();
		conteneur.setLayout(new FlowLayout());
		conteneur.setPreferredSize(new Dimension(220,frame.getHeight()));
		
		JPanel tempsReel = new JPanel();
		tempsReel.setLayout(new BorderLayout());
		tempsReel.setPreferredSize(new Dimension(200,100));
		tempsReel.setBorder(BorderFactory.createTitledBorder("Temps"));
		
		JButton demarrer=new JButton("Démarrer");
		demarrer.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				JButton button = (JButton) event.getSource();
				if (button.getText().equals("Démarrer")) 
				{
					c.setPause(false);
					button.setText("Pause");
				} else 
				{
					c.setPause(true);
					button.setText("Démarrer");
				}
			}
		});
		
		JPanel timePanel = new JPanel();
		time = new JLabel("    temps : "+shortDateFormat.format(c.getD()));
		JSlider inputTime = new JSlider(JSlider.HORIZONTAL, 0,100,c.getSpeedTime());
		inputTime.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) 
			    {
			        c.setSpeedTime(source.getValue()) ;
			    }	
			}
		});
		timePanel.add(time);
		timePanel.add(inputTime);
		
		tempsReel.add(demarrer,BorderLayout.NORTH);
		tempsReel.add(time,BorderLayout.CENTER);
		tempsReel.add(inputTime,BorderLayout.SOUTH);
		
		
		JPanel volSelection = new JPanel();
		volSelection.setPreferredSize(new Dimension(200,130));
		volSelection.setBorder(BorderFactory.createTitledBorder("Selection Vol"));

		
		DefaultListModel dlm = new DefaultListModel();
		selection = new JList(dlm);
		dlm.addElement("Aucune sélection");
		for(Flight f : c.getFlights())
		{
			dlm.addElement(f.getId());
		}
		
		JLabel id = new JLabel(" Identifiant : ");
		JLabel depart = new JLabel(" Depart : ");
		JLabel arrive = new JLabel(" Arrivée : ");
		JLabel vitesse = new JLabel(" Vitesse : ");
		JLabel altitude = new JLabel(" Altitude : ");
		JLabel typeAvion = new JLabel(" Type avion : ");
		JButton vueAvionButton = new JButton(" Vue Avion");
		
		selection.addListSelectionListener(new ListSelectionListener() 
		{
            @Override
            public void valueChanged(ListSelectionEvent e) 
            {
                if (!e.getValueIsAdjusting()) 
                {
                    c.setVolSelection(c.getFlightByID((String)selection.getSelectedValue()));
                    Flight f= c.getVolSelection();
                    if(f!=null)
                    {
                    	id.setText(" Identifiant : "+f.getId());
                		depart.setText(" Depart : "+f.getDeparture().getCityName());
                		arrive.setText(" Arrivée : "+f.getArrival().getCityName());
                		vitesse.setText(" Vitesse : "+(f.getPlane().getSpeedX()+f.getPlane().getSpeedY())+" km/h");
                		altitude.setText(" Altitude : "+f.getPlane().getGeolocation().getHeight()+" m ");
                		typeAvion.setText(" Type avion : "+f.getModelAvion());
                    }
                    else
                    {
                    	id.setText(" Identifiant : ");
                		depart.setText(" Depart : ");
                		arrive.setText(" Arrivée : ");
                		vitesse.setText(" Vitesse : ");
                		altitude.setText(" Altitude : ");
                		typeAvion.setText(" Type avion : ");
                    }
                }
            }
        });
	
		JScrollPane scrollPane = new JScrollPane(selection);
		scrollPane.setViewportView(selection);
		scrollPane.setPreferredSize(new Dimension(180,100));
		
		
		volSelection.add(scrollPane);
		
		informationVolSelection = new JPanel();
		informationVolSelection.setPreferredSize(new Dimension(200,200));
		informationVolSelection.setBorder(BorderFactory.createTitledBorder("Infos vol sélectionné"));
		informationVolSelection.setLayout(new GridLayout(7,1,5,0));
		
		
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
		
		affichageAvions.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(c.isPrintPlane()==true)
				{
					c.setPrintPlane(false);
					c.setAlreadyPrintAirport(false);
				}
				else
				{
					c.setPrintPlane(true);
				}
				
			}
		});
		affichageAeroport.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(c.isPrintPlane()==true)
				{
					c.setPrintAirport(false);
				}
				else
				{
					c.setPrintAirport(true);
				}
				
			}
		});
		
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
	
	

	/**
	 * @return the time
	 */
	public static JLabel getTime() {
		return time;
	}
	



	/**
	 * @return the shortDateFormat
	 */
	public static DateFormat getShortDateFormat() 
	{
		return shortDateFormat;
	}
	



	/**
	 * @return the informationVolSelection
	 */
	public static JPanel getInformationVolSelection() {
		return informationVolSelection;
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
