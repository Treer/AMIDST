package amidst.gui.version;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import MoF.FinderWindow;
import amidst.Options;
import amidst.minecraft.MinecraftUtil;
import amidst.minecraft.remote.RemoteMinecraft;

public class RemoteVersionComponent extends VersionComponent {
	private String remoteAddress;
	private String name;
	
	protected int oldWidth = 0;
	protected String drawName;
	
	
	public RemoteVersionComponent(String address) {
		remoteAddress = address;
		name = "remote:" + address;
	}
	public RemoteVersionComponent() {
		this("127.0.0.1");
	}
	
	@Override
	public void load() {
		isLoading = true;
		repaint();
		Options.instance.lastProfile.set(name);
		(new Thread(new Runnable() {
			@Override
			public void run() {
				MinecraftUtil.setBiomeInterface(new RemoteMinecraft(remoteAddress));
				new FinderWindow();
				VersionSelectWindow.get().dispose();
			}
		})).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		FontMetrics fontMetrics = null;
		
		if (isLoading)
			g2d.setColor(loadingBgColor);
		else if (isSelected())
			g2d.setColor(selectedBgColor);
		else
			g2d.setColor(Color.white);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		g2d.setColor(Color.black);
		g2d.setFont(remoteNameFont);
		if (oldWidth != getWidth()) {
			fontMetrics = g2d.getFontMetrics();
			int availableWidth = getWidth() - 40;
			String name = getVersionName();
			if (fontMetrics.stringWidth(name) > availableWidth - 25) {
				int widthSum = 0;
				for (int i = 0; i < name.length(); i++) {
					widthSum += fontMetrics.charWidth(name.charAt(i));
					if (widthSum > availableWidth - 25) {
						name = name.substring(0, i) + "...";
						break;
					}
				}
			}
			drawName = name;
			oldWidth = getWidth();
		}
		g2d.drawString(drawName, 5, 30);
		
		BufferedImage image = inactiveIcon;
		if (isLoading) {
			image = loadingIcon;
		} else if (isReadyToLoad()) {
			image = activeIcon;
		}
		g2d.drawImage(image, getWidth() - image.getWidth() - 5, 4, null);
	}
	
	@Override
	public boolean isReadyToLoad() {
		return true;
	}
	@Override
	public String getVersionName() {
		return name;
	}
}
