package amidst.gui.version;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;

import MoF.FinderWindow;
import amidst.Options;
import amidst.Util;
import amidst.logging.Log;
import amidst.minecraft.Minecraft;
import amidst.minecraft.MinecraftUtil;
import amidst.version.IProfileUpdateListener;
import amidst.version.MinecraftProfile;
import amidst.version.MinecraftVersion;
import amidst.version.ProfileUpdateEvent;
import amidst.version.MinecraftProfile.Status;

public class LocalVersionComponent extends VersionComponent {
	protected MinecraftVersion version;
	protected int oldWidth = 0;
	protected String drawName;
	private String name;
	
	
	public LocalVersionComponent(MinecraftVersion version) {
		this.version = version;
		drawName = version.getName();
		name = "local:" + drawName;
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
		g2d.setFont(versionFont);
		fontMetrics = g2d.getFontMetrics();
		int versionNameX = getWidth() - 40 - fontMetrics.stringWidth(version.getName());
		g2d.drawString(version.getName(), versionNameX, 20);
		
		g2d.setColor(Color.black);
		g2d.setFont(nameFont);
		if (oldWidth != getWidth()) {
			fontMetrics = g2d.getFontMetrics();
			String name = version.getName();
			if (fontMetrics.stringWidth(name) > versionNameX - 25) {
				int widthSum = 0;
				for (int i = 0; i < name.length(); i++) {
					widthSum += fontMetrics.charWidth(name.charAt(i));
					if (widthSum > versionNameX - 25) {
						name = name.substring(0, i) + "...";
						break;
					}
				}
			}
			drawName = name;
			oldWidth = getWidth();
		}
		g2d.drawString(drawName, 5, 30);
		
		g2d.setColor(Color.gray);
		g2d.setFont(statusFont);
		fontMetrics = g2d.getFontMetrics();
		String statusString = "found";
		g2d.drawString(statusString, getWidth() - 40 - fontMetrics.stringWidth(statusString), 32);
		
		BufferedImage image = activeIcon;
		g2d.drawImage(image, getWidth() - image.getWidth() - 5, 4, null);
	}
	
	
	@Override
	public boolean isReadyToLoad() {
		return true;
	}
	
	@Override
	public void load() {
		isLoading = true;
		repaint();
		Options.instance.lastProfile.set(name);
		(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Util.setProfileDirectory(null);
					MinecraftUtil.setBiomeInterface(new Minecraft(version.getJarFile()).createInterface());
					new FinderWindow();
					VersionSelectWindow.get().dispose();
				} catch (MalformedURLException e) {
					Log.crash(e, "MalformedURLException on Minecraft load.");
				}
			}
		})).start();
	}

	@Override
	public String getVersionName() {
		return name;
	}
}
