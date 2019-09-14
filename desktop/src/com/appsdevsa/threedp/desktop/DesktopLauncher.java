package com.appsdevsa.threedp.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.appsdevsa.threedp.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		String value = "";
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		new LwjglApplication(new MyGame(value), config);
	}
}
