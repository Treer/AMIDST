package amidst.map.widget;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import amidst.map.MapObject;
import amidst.resources.ResourceLoader;
import MoF.MapViewer;

public class PleaseWaitWidget extends PanelWidget {
	private String message_generating = "Generating long distance map...";
	private String message_exporting = "Exporting...";
	private BufferedImage icon = ResourceLoader.getImage("export_image.png");
	private int spacing_pxiels = 10;
	
	public PleaseWaitWidget(MapViewer mapViewer) {
		super(mapViewer);
		setDimensions(icon.getWidth(), icon.getHeight());
		forceVisibility(false);
	}
	
	@Override
	public void draw(Graphics2D g2d, float time) {
		String message = mapViewer.exporter.getMapGenerationInProgress() ? message_generating : message_exporting;
		int messageWidth = mapViewer.getFontMetrics().stringWidth(message);
		int messageHeight = mapViewer.getFontMetrics().getAscent();

		setWidth(icon.getWidth() + messageWidth + 2 * spacing_pxiels);
		super.draw(g2d, time);

		g2d.setColor(textColor);		
		g2d.drawImage(icon, x + messageWidth + 2 * spacing_pxiels, y, null);
		g2d.drawString(message, x + spacing_pxiels, y + messageHeight + ((icon.getHeight() - messageHeight) / 2));
	}
	
	@Override
	protected boolean onVisibilityCheck() {
		return mapViewer.exporter.getMapGenerationInProgress() || mapViewer.exporter.getExportRequestInProgress();
	}
}
