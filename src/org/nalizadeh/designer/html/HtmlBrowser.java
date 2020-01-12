package org.nalizadeh.designer.html;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HtmlBrowser {

	private static HtmlViewerFrame mainFrame = null;

	public static void main(String[] args) {
		showHtml("http://localhost:8080", null, true, 700, 600);
	}

	public static void showHtml(final String url, final String html, boolean standalone, int w, int h) {

		if (mainFrame == null) {
			mainFrame = new HtmlViewerFrame();
			mainFrame.setTitle("Html preview");
		}

		mainFrame.start(url, html, w, h);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(standalone ? JFrame.EXIT_ON_CLOSE : JFrame.HIDE_ON_CLOSE);
		mainFrame.setSize(w, h);
	}
}

/**
 * Main window used to display some HTML content.
 */
class HtmlViewerFrame extends JFrame {

	private JFXPanel javafxPanel;
	private WebView browser;
	private JPanel mainPanel;

	public HtmlViewerFrame() {

		javafxPanel = new JFXPanel();

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(javafxPanel, BorderLayout.CENTER);

		add(mainPanel);
	}

	public void start(final String url, final String html, final int w, final int h) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				BorderPane borderPane = new BorderPane();
				browser = new WebView();

				if (url != null) {
					browser.getEngine().load(url);
				} else {
					browser.getEngine().loadContent(html);
				}
				borderPane.setCenter(browser);
				Scene scene = new Scene(borderPane, w, h);
				javafxPanel.setScene(scene);

				// zum debuggen (nicht funktioniert!)
				
				browser.getEngine().getLoadWorker().stateProperty()
						.addListener(new javafx.beans.value.ChangeListener<State>() {
							@Override
							public void changed(ObservableValue<? extends State> observable, 
									State oldValue,
									State newValue) {
								JSObject jsobj = (JSObject) browser.getEngine().executeScript("window");
								jsobj.setMember("java", new JSListener());
							}
						});
			}
		});
	}

	class JSListener {

		public void log(String text) {
			System.out.println(text);
		}
	}
}
