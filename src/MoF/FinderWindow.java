package MoF;


import amidst.Amidst;
import amidst.Options;
import amidst.gui.menu.AmidstMenu;
import amidst.map.layers.EndCityLayer;
import amidst.map.layers.OceanMonumentLayer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;

import javax.swing.JFrame;

public class FinderWindow extends JFrame {
	private static final long serialVersionUID = 196896954675968191L;
	public static FinderWindow instance;
	private Container pane;
	public Project curProject;  //TODO
	public static boolean dataCollect;
	private final AmidstMenu menuBar;
	public FinderWindow() {
		//Initialize window
		super("Map Exporter v" + Amidst.exporterVersion() + ", a mod of AMIDST v" + Amidst.version());
		
		setSize(1000,800);
		//setLookAndFeel();
		pane = getContentPane();
		//UI Manager:
		OceanMonumentLayer.InitializeUIOptions(Options.instance.showOceanMonuments);
		EndCityLayer.InitializeUIOptions(Options.instance.showEndCities);
		pane.setLayout(new BorderLayout());
		new UpdateManager(this, true).start();
		setJMenuBar(menuBar = new AmidstMenu(this));
		setVisible(true);
		setIconImages(Amidst.icons);
		instance = this;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}
	
	public void clearProject() {
		// FIXME Release resources.
		if (curProject != null) {
			removeKeyListener(curProject.getKeyListener());
			curProject.dispose();
			pane.remove(curProject);
			Options.instance.seedText = null;			
			System.gc();
		}
	}
	public void setProject(Project ep) {
		menuBar.MapMenusEnabled(true);
		curProject = ep;

		addKeyListener(ep.getKeyListener());
		pane.add(curProject, BorderLayout.CENTER);
		
		this.validate();
	}
}
