package biofeedback.Controller;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SystemTrayIcon {
	public static void setTrayIcon(Stage stage) {
		
		SystemTray sTray = null;
		sTray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage("images/icon4.png");
		
		PopupMenu popup = new PopupMenu();
		MenuItem showItem = new MenuItem("Open");
		MenuItem exitItem = new MenuItem("Exit");
		TrayIcon icon = new TrayIcon(image, "Bio Feed-Back+", popup);
		
		ActionListener listenerShow = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						stage.show();
					}
				});
			}
		};

		ActionListener listenerClose = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				stage.hide();
			}
		});
		
		
		showItem.addActionListener(listenerShow);
		exitItem.addActionListener(listenerClose);

		popup.add(showItem);
		popup.add(exitItem);
		try {
			sTray.add(icon);
		}
		catch (AWTException e) {
			System.err.println(e);
		}
	}

}