package tutoriel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import classes.Airport;
import classes.Flight;
import controller.Controller;

public class WindowedTest 
{
	

	private static EarthTest canvasApplication;
	
	private static Canvas canvas; 
	
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
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e) 
			{
				canvasApplication.stop();
			}
		});
		/*canvasApplication.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("CREATE HEATMAP");
				
				canvasApplication.enqueue(new Callable<Object>(
						public Object call() throws Exception 
						{
							canvasApplication.addHeatMap();
							return null;
						});
			}
		});*/
		
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
		JButton vueAvionButton = new JButton(" Vue Avion ");
		
		vueAvionButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				JButton button = (JButton) event.getSource();
				if (button.getText().equals(" Vue Avion ")) 
				{
					c.setPlaneView(true);
					



					button.setText(" Vue Globale ");
				} 
				else 
				{
					ChaseCamera chaseCam = new ChaseCamera(canvasApplication.getCamera(),canvasApplication.getRootNode().getChild("earth"),canvasApplication.getInputManager());
					chaseCam.setDragToRotate(true);
					chaseCam.setInvertVerticalAxis(true);
					chaseCam.setRotationSpeed(10.0f);
					chaseCam.setMinVerticalRotation((float)-(Math.PI/2-0.0001f));
					chaseCam.setMaxVerticalRotation((float)Math.PI/2);
					chaseCam.setMinDistance(7.5f);
					chaseCam.setMaxDistance(30.0f);
					
					c.setPlaneView(false);
					button.setText(" Vue Avion ");
				}
			}
		});
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
                    	c.setVolSelectionAsChanged(true);
                    	id.setText(" Identifiant : "+f.getId());
                		depart.setText(" Depart : "+f.getDeparture().getCityName());
                		arrive.setText(" Arrivée : "+f.getArrival().getCityName());
                		vitesse.setText(" Vitesse : "+(f.getPlane().getSpeedX()+f.getPlane().getSpeedY())+" km/h");
                		altitude.setText(" Altitude : "+f.getPlane().getGeolocation().getHeight()+" m ");
                		typeAvion.setText(" Type avion : "+f.getModelAvion());
                		
                    }
                    else
                    {
                    	c.setVolSelectionAsChanged(false);
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
		affichage.setPreferredSize(new Dimension(200,240));
		affichage.setBorder(BorderFactory.createTitledBorder("Affichage"));
		affichage.setLayout(new FlowLayout());
		
		JCheckBox affichageAeroport = new JCheckBox("aéroport",true);
		JCheckBox affichageAvions = new JCheckBox("avions",true);
		JCheckBox affichageTrajectoire = new JCheckBox("trajectoire",false);
		
		affichageAvions.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AbstractButton event = (AbstractButton) e.getSource();
				if(!event.getModel().isSelected())
				{
					c.setPrintPlane(false);
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
				AbstractButton event = (AbstractButton) e.getSource();
				
				if(!event.getModel().isSelected())
				{
					canvasApplication.suprAllAirportNode();
					c.setPrintAirport(false);
					c.setAlreadyPrintAirport(false);
				}
				else
				{
					c.setPrintAirport(true);
					c.setAlreadyPrintAirport(false);
				}
				
			}
		});
		
		affichageTrajectoire.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AbstractButton event = (AbstractButton) e.getSource();
				if(!event.getModel().isSelected())
				{
					c.setPrintPathPlane(false);
				}
				else
				{
					c.setPrintPathPlane(true);
				}
				
			}
		});
		
		JPanel filtresVol = new JPanel();
		filtresVol.setPreferredSize(new Dimension(180,140));
		filtresVol.setBorder(BorderFactory.createTitledBorder("Filtres vols"));
		filtresVol.setLayout(new BoxLayout(filtresVol,BoxLayout.Y_AXIS));
		

		JComboBox<String> listeAeroport = new JComboBox<String>();
		
		List<String> listTemp = new ArrayList<String>() ;
		
		for(Airport a: c.getAirports())
		{
			listTemp.add(a.getShortName());	
		}
		
		listTemp=removeDuplicatedItems(listTemp);
        
		Collections.sort(listTemp);
		
		for(String s : listTemp)
		{
			if(!s.equals("\\N"))
			{
				listeAeroport.addItem(s);
			}
			
		}
		listeAeroport.addItemListener(new ItemListener() 
        {
        @Override
        public void itemStateChanged(ItemEvent e) 
        {
            if(e.getStateChange() == ItemEvent.SELECTED) 
            {
            	canvasApplication.suprAllAirportNode();
            	c.setAirportSelection(e.getItem().toString());
            	c.setAlreadyPrintAirport(false);
            }
        }
        });
		JRadioButton radioButton1 = new JRadioButton("a�roport");
		radioButton1.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AbstractButton event = (AbstractButton) e.getSource();
				if(!event.getModel().isSelected())
				{
					canvasApplication.suprAllAirportNode();
					c.setPrintOnlyAirport(false);
					c.setAlreadyPrintAirport(false);
				}
				else
				{
					canvasApplication.suprAllAirportNode();
					c.setPrintOnlyAirport(true);
					c.setAlreadyPrintAirport(false);
				}
				
			}
		});
		
		JComboBox<String> listePays = new JComboBox<String>();
		
		listTemp.removeAll(listTemp);
		
		for(Airport a: c.getAirports())
		{
			listTemp.add(a.getCountry());	
		}
		
		listTemp=removeDuplicatedItems(listTemp);
        
		Collections.sort(listTemp);
        for(String s : listTemp)
		{
        	listePays.addItem(s);	
		}
        listePays.addItemListener(new ItemListener() 
        {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED) 
            {
            	c.setCountrySelection(e.getItem().toString());
            	c.setAlreadyPrintAirport(false);
            }
        }
        });
		
		JRadioButton radioButton2 = new JRadioButton("pays");
		radioButton2.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AbstractButton event = (AbstractButton) e.getSource();
				if(!event.getModel().isSelected())
				{
					c.setPrintOnlyCountry(false);
					c.setAlreadyPrintAirport(false);
				}
				else
				{
					c.setAlreadyPrintAirport(false);
					c.setPrintOnlyCountry(true);
				}
				
			}
		});
		
		filtresVol.add(radioButton1);
		filtresVol.add(listeAeroport);
		filtresVol.add(radioButton2);
		filtresVol.add(listePays);
		
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
	public static List<String> removeDuplicatedItems(List<String> liste)
	{
		Set<String> set = new HashSet<String>() ;
        	set.addAll(liste) ;
        	return new ArrayList<String>(set);
	}


	public static void main(String[] args)
	{
		c= new Controller();
		
		
		AppSettings settings = new AppSettings(true);
		settings.setResolution(1280, 800);
		settings.setSamples(8);
		settings.setFrameRate(60);
		settings.setVSync(true);

		canvasApplication = new EarthTest(c);

		canvasApplication.setSettings(settings);
		canvasApplication.setShowSettings(false);
		canvasApplication.setDisplayStatView(false);
		canvasApplication.setDisplayFps(false);
		canvasApplication.setPauseOnLostFocus(false);

		canvasApplication.createCanvas(); 
		
		
		JmeCanvasContext ctx = (JmeCanvasContext) canvasApplication.getContext();
		canvas = ctx.getCanvas();
		Dimension dim = new Dimension(settings.getWidth(), settings.getHeight());
		canvas.setPreferredSize(dim);

		createNewJFrame();
	}

}
