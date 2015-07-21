package amidst.map.widget;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import amidst.map.MapObject;
import MoF.MapViewer;

public class SelectedObjectWidget extends PanelWidget {
	private String message = "";
	private BufferedImage icon;
	
	public SelectedObjectWidget(MapViewer mapViewer) {
		super(mapViewer);
		yPadding += 40;
		setDimensions(20, 35);
		forceVisibility(false);
	}
	
	@Override
	public void draw(Graphics2D g2d, float time) {
		if (targetVisibility) {
			MapObject selectedObject = mapViewer.getSelectedObject();
			
			message = selectedObject.getName();
			if (selectedObject.isLocatedInNether()) {
				
				message += "\n      Nether [" + selectedObject.getNetherCoordinates().x + ", " + selectedObject.getNetherCoordinates().y + "]";
				message += "\n      Overworld [" + selectedObject.getOverworldCoordinates().x + ", " + selectedObject.getOverworldCoordinates().y + "]";							
			} else {
				message += " [" + selectedObject.rx + ", " + selectedObject.ry + "]";
			}
			icon = selectedObject.getImage();
		}

		int maxStringWidth = 0;
		int stringHeight = 0;		
		for (String line : message.split("\n")) {
			stringHeight += g2d.getFontMetrics().getHeight();
			maxStringWidth = Math.max(maxStringWidth, mapViewer.getFontMetrics().stringWidth(line));
	    }				
		
		setDimensions(
			45 + maxStringWidth,
			Math.max(10/*icon-margins*/ + 25/*icon-size*/, 17/*font-margins*/ + stringHeight)
		);		
		super.draw(g2d, time);

		g2d.setColor(textColor);
		double imgWidth = icon.getWidth();
		double imgHeight = icon.getHeight();
		double ratio = imgWidth/imgHeight;
		
		g2d.drawImage(icon, x + 5, y + 5, (int)(25.*ratio), 25, null);
		drawMultilineString(g2d, message, x + 35, y + 23);
	}
	
	void drawMultilineString(Graphics2D g2d, String text, int x, int y) {
		
		int lineHeight = g2d.getFontMetrics().getHeight();
	    for (String line : text.split("\n")) {
	    	g2d.drawString(line, x, y);
	    	y += lineHeight;
	    }
	}	
	
	@Override
	protected boolean onVisibilityCheck() {
		return (mapViewer.getSelectedObject() != null);
	}
}
