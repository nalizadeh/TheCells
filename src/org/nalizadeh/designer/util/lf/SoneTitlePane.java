package org.nalizadeh.designer.util.lf;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import java.util.Locale;


class SoneTitlePane extends JComponent {

    private static final int IMAGE_HEIGHT = 11;
    private static final int IMAGE_WIDTH = 11;

    private static final int TITLEBAR_ICON_W = 30;//17;

    /**
     * PropertyChangeListener added to the JRootPane.
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * JMenuBar, typically renders the system menu items.
     */
    private JMenuBar menuBar;
    /**
     * Action used to close the Window.
     */
    private Action closeAction;

    /**
     * Action used to iconify the Frame.
     */
    private Action iconifyAction;

    /**
     * Action to restore the Frame size.
     */
    private Action restoreAction;

    /**
     * Action to restore the Frame size.
     */
    private Action maximizeAction;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton toggleButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton iconifyButton;

    /**
     * Button used to maximize or restore the Frame.
     */
    private JButton closeButton;

    /**
     * Icon used for toggleButton when window is normal size.
     */
    private Icon maximizeIcon;
    private Icon maximizeRolloverIcon;

    /**
     * Icon used for toggleButton when window is maximized.
     */
    private Icon minimizeIcon;
    private Icon minimizeRolloverIcon;

    /**
     * Icon used for menubar.
     */
    private Icon menuIcon;
    private Icon menuNormalIcon;
    private Icon menuRolloverIcon;

    private Icon titlebarIcon;
    private Icon titlebarIcon2;

    /**
     * Listens for changes in the state of the Window listener to update
     * the state of the widgets.
     */
    private WindowListener windowListener;

    /**
     * Window we're currently in.
     */
    private Window window;

    /**
     * JRootPane rendering for.
     */
    private JRootPane rootPane;

    /**
     * Buffered Frame.state property. As state isn't bound, this is kept
     * to determine when to avoid updating widgets.
     */
    private int state;

    /**
     * SoneRootPaneUI that created us.
     */
    private SoneRootPaneUI rootPaneUI;


    // Colors
    private Color inactiveBackground = null;
    private Color inactiveForeground = null;
    private Color activeBackground = null;
    private Color activeForeground = null;
    private Font font = null;

    public SoneTitlePane(JRootPane root, SoneRootPaneUI ui) {
        this.rootPane = root;

        rootPaneUI = ui;

        state = -1;

        determineDefaults();
        installSubcomponents();
        setLayout(createLayout());
    }

    /**
     * Uninstalls the necessary state.
     */
    private void uninstall() {
        uninstallListeners();
        window = null;
        removeAll();
    }

    /**
     * Installs the necessary listeners.
     */
    private void installListeners() {
        if (window != null) {
            windowListener = new WindowHandler();
            propertyChangeListener = new PropertyChangeHandler();
            window.addWindowListener(windowListener);
            window.addPropertyChangeListener(propertyChangeListener);
        }
    }

    /**
     * Uninstalls the necessary listeners.
     */
    private void uninstallListeners() {
        if (window != null) {
            window.removeWindowListener(windowListener);
            window.removePropertyChangeListener(propertyChangeListener);
        }
    }

    /**
     * Returns the <code>JRootPane</code> this was created for.
     */
    public JRootPane getRootPane() {
        return rootPane;
    }

    public void addNotify() {
        super.addNotify();

        uninstallListeners();

        window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            if (window instanceof Frame) {
                setState(((Frame)window).getExtendedState());
            }
            else {
                setState(0);
            }
            setActive(window.isActive());
            installListeners();
        }
    }

    public void removeNotify() {
        super.removeNotify();

        uninstallListeners();
        window = null;
    }

    /**
     * Adds any sub-Components contained in the <code>SoneTitlePane</code>.
     */
    private void installSubcomponents() {
        if (rootPane.getWindowDecorationStyle() == JRootPane.FRAME) {
            createActions();
            menuBar = createMenuBar();
            add(menuBar);
            createButtons();
            add(iconifyButton);
            add(toggleButton);
            add(closeButton);
        }
    }

    /**
     * Determines the Colors to draw with.
     */
    private void determineDefaults() {
        switch (rootPane.getWindowDecorationStyle()) {
        case JRootPane.FRAME:
            activeBackground = UIManager.getColor("RootPane.titlePaneActiveBackground");
            activeForeground = UIManager.getColor("RootPane.titlePaneActiveForeground");
            inactiveBackground = UIManager.getColor("inactiveCaption");
            inactiveForeground = UIManager.getColor("inactiveCaptionText");
            font = UIManager.getFont("RootPane.titleFont", getLocale());
            break;

        case JRootPane.ERROR_DIALOG: break;
        case JRootPane.QUESTION_DIALOG:
        case JRootPane.COLOR_CHOOSER_DIALOG:
        case JRootPane.FILE_CHOOSER_DIALOG: break;
        case JRootPane.WARNING_DIALOG: break;
        case JRootPane.PLAIN_DIALOG:
        case JRootPane.INFORMATION_DIALOG:
        default: break;
        }
    }

    /**
     * Returns the <code>JMenuBar</code> displaying the appropriate
     * system menu items.
     */
    protected JMenuBar createMenuBar() {

	titlebarIcon = UIManager.getIcon("RootPane.activeTitlebarIcon");
	titlebarIcon2 = UIManager.getIcon("RootPane.inactiveTitlebarIcon");
	menuNormalIcon = UIManager.getIcon("RootPane.menuIcon");
	menuRolloverIcon = UIManager.getIcon("RootPane.menuRolloverIcon");
        menuIcon = menuNormalIcon;

        menuBar = new SystemMenuBar();
        menuBar.setFocusable(false);
        menuBar.setBorderPainted(false);
        menuBar.add(createMenu());

	menuBar.addMouseListener(new MouseListener() {
          public void mouseClicked(MouseEvent e) {}
          public void mouseReleased(MouseEvent e) {}
          public void mousePressed(MouseEvent e) {}
          public void mouseEntered(MouseEvent e) {
		menuIcon = menuRolloverIcon;
		menuBar.updateUI();
          }
          public void mouseExited(MouseEvent e) {
		menuIcon = menuNormalIcon;
		menuBar.updateUI();
          }
        });

        return menuBar;
    }

    /**
     * Closes the Window.
     */
    private void close() {
        Window window = getWindow();

        if (window != null) {
            window.dispatchEvent(new WindowEvent(
                                 window, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Iconifies the Frame.
     */
    private void iconify() {
        Frame frame = getFrame();
        if (frame != null) {
            frame.setExtendedState(state | Frame.ICONIFIED);
        }
    }

    /**
     * Maximizes the Frame.
     */
    private void maximize() {
        Frame frame = getFrame();
        if (frame != null) {
            frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Restores the Frame size.
     */
    private void restore() {
        Frame frame = getFrame();

        if (frame == null) {
            return;
        }

        if ((state & Frame.ICONIFIED) != 0) {
            frame.setExtendedState(state & ~Frame.ICONIFIED);
        } else {
            frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
        }
    }

    /**
     * Create the <code>Action</code>s that get associated with the
     * buttons and menu items.
     */
    private void createActions() {
        closeAction = new CloseAction();
        iconifyAction = new IconifyAction();
        restoreAction = new RestoreAction();
        maximizeAction = new MaximizeAction();
    }

    /**
     * Returns the <code>JMenu</code> displaying the appropriate menu items
     * for manipulating the Frame.
     */
    private JMenu createMenu() {
        JMenu menu = new JMenu("");
        if (rootPane.getWindowDecorationStyle() == JRootPane.FRAME) {
            addMenuItems(menu);
        }
        return menu;
    }

    /**
     * Adds the necessary <code>JMenuItem</code>s to the passed in menu.
     */
    private void addMenuItems(JMenu menu) {

	menu.add(restoreAction);
	menu.add(iconifyAction);

        if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
		menu.add(maximizeAction);
        }

        menu.add(new JSeparator());
        menu.add(closeAction);
    }

    /**
     * Returns a <code>JButton</code> appropriate for placement on the
     * TitlePane.
     */
    private JButton createTitleButton() {
        JButton button = new JButton() {
          public void paint(Graphics g) {
            Icon c = getModel().isRollover() ? getRolloverIcon() : getIcon();
            c.paintIcon(this, g, 0,0);
          }
        };
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setOpaque(false);
	button.setBorder(null);
	button.setBorderPainted(false);
	button.setRolloverEnabled(true);
        return button;
    }

    /**
     * Creates the Buttons that will be placed on the TitlePane.
     */
    private void createButtons() {
        maximizeIcon = UIManager.getIcon("RootPane.maximizeIcon");
        minimizeIcon = UIManager.getIcon("RootPane.minimizeIcon");
        maximizeRolloverIcon = UIManager.getIcon("RootPane.maximizeRolloverIcon");
        minimizeRolloverIcon = UIManager.getIcon("RootPane.minimizeRolloverIcon");

        closeButton = createTitleButton();
        closeButton.setAction(closeAction);
        closeButton.setText(null);
        closeButton.putClientProperty("paintActive", Boolean.TRUE);
        closeButton.getAccessibleContext().setAccessibleName("Close");
        closeButton.setIcon(UIManager.getIcon("RootPane.closeIcon"));
        closeButton.setRolloverIcon(UIManager.getIcon("RootPane.closeRolloverIcon"));

        iconifyButton = createTitleButton();
        iconifyButton.setAction(iconifyAction);
        iconifyButton.setText(null);
//        iconifyButton.setBackground(UIManager.getColor("RootPane.titlePaneActiveBackground"));
        iconifyButton.putClientProperty("paintActive", Boolean.TRUE);
        iconifyButton.getAccessibleContext().setAccessibleName("Iconify");
        iconifyButton.setIcon(UIManager.getIcon("RootPane.iconifyIcon"));
        iconifyButton.setRolloverIcon(UIManager.getIcon("RootPane.iconifyRolloverIcon"));

        toggleButton = createTitleButton();
        toggleButton.putClientProperty("paintActive", Boolean.TRUE);
        toggleButton.getAccessibleContext().setAccessibleName("Maximize");
	 updateToggleButton(maximizeAction, maximizeIcon, maximizeRolloverIcon);
    }

    /**
     * Returns the <code>LayoutManager</code> that should be installed on
     * the <code>SoneTitlePane</code>.
     */
    private LayoutManager createLayout() {
        return new TitlePaneLayout();
    }

    /**
     * Updates state dependant upon the Window's active state.
     */
    private void setActive(boolean isActive) {
        if (rootPane.getWindowDecorationStyle() == JRootPane.FRAME) {
            Boolean activeB = isActive ? Boolean.TRUE : Boolean.FALSE;

            iconifyButton.putClientProperty("paintActive", activeB);
            closeButton.putClientProperty("paintActive", activeB);
            toggleButton.putClientProperty("paintActive", activeB);

            iconifyButton.setVisible(activeB.booleanValue());
            closeButton.setVisible(activeB.booleanValue());
            toggleButton.setVisible(activeB.booleanValue());
        }

        // Repaint the whole thing as the Borders that are used have
        // different colors for active vs inactive
        getRootPane().repaint();
    }

    /**
     * Sets the state of the Window.
     */
    private void setState(int state) {
        setState(state, false);
    }

    /**
     * Sets the state of the window. If <code>updateRegardless</code> is
     * true and the state has not changed, this will update anyway.
     */
    private void setState(int state, boolean updateRegardless) {
        Window w = getWindow();

        if (w != null && rootPane.getWindowDecorationStyle() == JRootPane.FRAME) {
            if (this.state == state && !updateRegardless) {
                return;
            }
            Frame frame = getFrame();

            if (frame != null) {
                JRootPane rootPane = getRootPane();

                if (((state & Frame.MAXIMIZED_BOTH) != 0) &&
                        (rootPane.getBorder() == null ||
                        (rootPane.getBorder() instanceof UIResource)) &&
                            frame.isShowing()) {
                    rootPane.setBorder(null);
                }
                else if ((state & Frame.MAXIMIZED_BOTH) == 0) {
                    // This is a croak, if state becomes bound, this can
                    // be nuked.
                    rootPaneUI.installBorder(rootPane);
                }
                if (frame.isResizable()) {
                    if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                        updateToggleButton(restoreAction, minimizeIcon, minimizeRolloverIcon);
                        maximizeAction.setEnabled(false);
                        restoreAction.setEnabled(true);
                    }
                    else {
                        updateToggleButton(maximizeAction, maximizeIcon, maximizeRolloverIcon);
                        maximizeAction.setEnabled(true);
                        restoreAction.setEnabled(false);
                    }
                    if (toggleButton.getParent() == null ||
                        iconifyButton.getParent() == null) {
                        add(toggleButton);
                        add(iconifyButton);
                        revalidate();
                        repaint();
                    }
                    toggleButton.setText(null);
                }
                else {
                    maximizeAction.setEnabled(false);
                    restoreAction.setEnabled(false);
                    if (toggleButton.getParent() != null) {
                        remove(toggleButton);
                        revalidate();
                        repaint();
                    }
                }
            }
            else {
                // Not contained in a Frame
                maximizeAction.setEnabled(false);
                restoreAction.setEnabled(false);
                iconifyAction.setEnabled(false);
                remove(toggleButton);
                remove(iconifyButton);
                revalidate();
                repaint();
            }
            closeAction.setEnabled(true);
            this.state = state;
        }
    }

    /**
     * Updates the toggle button to contain the Icon <code>icon</code>, and
     * Action <code>action</code>.
     */
    private void updateToggleButton(Action action, Icon icon, Icon rolloverIcon) {
        toggleButton.setAction(action);
        toggleButton.setIcon(icon);
        toggleButton.setRolloverIcon(rolloverIcon);
        toggleButton.setText(null);
    }

    /**
     * Returns the Frame rendering in. This will return null if the
     * <code>JRootPane</code> is not contained in a <code>Frame</code>.
     */
    private Frame getFrame() {
        Window window = getWindow();

        if (window instanceof Frame) {
            return (Frame)window;
        }
        return null;
    }

    /**
     * Returns the <code>Window</code> the <code>JRootPane</code> is
     * contained in. This will return null if there is no parent ancestor
     * of the <code>JRootPane</code>.
     */
    private Window getWindow() {
        return window;
    }

    /**
     * Returns the String to display as the title.
     */
    private String getTitle() {
        Window w = getWindow();

        if (w instanceof Frame) {
            return ((Frame)w).getTitle();
        }
        else if (w instanceof Dialog) {
            return ((Dialog)w).getTitle();
        }
        return null;
    }

    /**
     * Renders the TitlePane.
     */
    public void paintComponent(Graphics g)  {

        if (getFrame() != null) {
            setState(getFrame().getExtendedState());
        }

        Window window = getWindow();

        boolean isSelected = (window == null) ? true : window.isActive();
        int width = getWidth();
        int height = getHeight();

        Color background;
        Color foreground;

        if (isSelected) {
            background = activeBackground;
            foreground = activeForeground;
        } else {
            background = inactiveBackground;
            foreground = inactiveForeground;
        }

        g.setColor(UIManager.getColor("RootPane.background"));
        g.fillRect(0, 0, width, height);

        g.setColor(background);
        g.fillRect(0, 0, width-TITLEBAR_ICON_W, height-3);

        g.setColor(new Color(140, 140, 140));
        g.drawLine(0, 30, width-(TITLEBAR_ICON_W+1), 30);
        g.setColor(new Color(183, 183, 183));
        g.drawLine(0, 31, width-(TITLEBAR_ICON_W+1), 31);
        g.setColor(new Color(216, 216, 216));
        g.drawLine(0, 32, width-(TITLEBAR_ICON_W+1), 32);

        g.setFont(font);
        g.setColor(foreground);
        g.drawString(getTitle(), 25, 14);

	Icon ic = isSelected ? titlebarIcon : titlebarIcon2;
	ic.paintIcon(this, g, width - (TITLEBAR_ICON_W + 1), 0);
    }

    /**
     * Convenience method to clip the passed in text to the specified
     * size.
     */
    private String clippedText(String text, FontMetrics fm,
                                 int availTextWidth) {
        if ((text == null) || (text.equals("")))  {
            return "";
        }
        int textWidth = SwingUtilities.computeStringWidth(fm, text);
        String clipString = "...";
        if (textWidth > availTextWidth) {
            int totalWidth = SwingUtilities.computeStringWidth(fm, clipString);
            int nChars;
            for(nChars = 0; nChars < text.length(); nChars++) {
                totalWidth += fm.charWidth(text.charAt(nChars));
                if (totalWidth > availTextWidth) {
                    break;
                }
            }
            text = text.substring(0, nChars) + clipString;
        }
        return text;
    }


    /**
     * Actions used to <code>close</code> the <code>Window</code>.
     */
    private class CloseAction extends AbstractAction {
        public CloseAction() {
            super(
		UIManager.getString("RootPane.closeTitle", getLocale()),
		UIManager.getIcon("RootPane.closeIcon"));

            putValue(SHORT_DESCRIPTION, UIManager.getString("RootPane.closeTitle", getLocale()));
            putValue(MNEMONIC_KEY, new Integer(UIManager.getInt("RootPane.restoreMnemonic")));
            putValue(ACCELERATOR_KEY, (KeyStroke)UIManager.get("RootPane.closeKeyStroke"));
        }

        public void actionPerformed(ActionEvent e) {
            close();
        }
    }


    /**
     * Actions used to <code>iconfiy</code> the <code>Frame</code>.
     */
    private class IconifyAction extends AbstractAction {
        public IconifyAction() {
            super(
               UIManager.getString("RootPane.iconifyTitle", getLocale()),
		UIManager.getIcon("RootPane.iconifyIcon"));

            putValue(SHORT_DESCRIPTION, UIManager.getString("RootPane.iconifyTitle", getLocale()));
            putValue(MNEMONIC_KEY, new Integer(UIManager.getInt("RootPane.iconifyMnemonic")));
        }

        public void actionPerformed(ActionEvent e) {
            iconify();
        }
    }


    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    private class RestoreAction extends AbstractAction {
        public RestoreAction() {
               super(UIManager.getString("RootPane.restoreTitle", getLocale()),
		UIManager.getIcon("RootPane.minimizeIcon"));

            putValue(SHORT_DESCRIPTION, UIManager.getString("RootPane.restoreTitle", getLocale()));
            putValue(MNEMONIC_KEY, new Integer(UIManager.getInt("RootPane.restoreMnemonic")));
        }

        public void actionPerformed(ActionEvent e) {
            restore();
        }
    }


    /**
     * Actions used to <code>restore</code> the <code>Frame</code>.
     */
    private class MaximizeAction extends AbstractAction {
        public MaximizeAction() {
               super(UIManager.getString("RootPane.maximizeTitle", getLocale()),
		UIManager.getIcon("RootPane.maximizeIcon"));

            putValue(SHORT_DESCRIPTION, UIManager.getString("RootPane.maximizeTitle", getLocale()));
            putValue(MNEMONIC_KEY, new Integer(UIManager.getInt("RootPane.maximizeMnemonic")));
        }

        public void actionPerformed(ActionEvent e) {
            maximize();
        }
    }


    /**
     * Class responsible for drawing the system menu. Looks up the
     * image to draw from the Frame associated with the
     * <code>JRootPane</code>.
     */
    private class SystemMenuBar extends JMenuBar {

        public void paint(Graphics g) {
            Frame frame = getFrame();
            if (!frame.isActive()) return;

            Image image = (frame != null) ? frame.getIconImage() : null;

            if (image != null) {
                g.drawImage(image, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
            }
            else if (menuIcon != null) {
                menuIcon.paintIcon(this, g, 0, 0);
            }
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            return new Dimension(Math.max(IMAGE_WIDTH, size.width), Math.max(IMAGE_HEIGHT, size.height));
        }
    }

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    private class TitlePaneLayout implements LayoutManager {

        public void addLayoutComponent(String name, Component c) {}

        public void removeLayoutComponent(Component c) {}

        public Dimension preferredLayoutSize(Container c)  {
            return new Dimension(33, 33);
        }

        public Dimension minimumLayoutSize(Container c) {
            return preferredLayoutSize(c);
        }

        public void layoutContainer(Container c) {
            if (rootPane.getWindowDecorationStyle() != JRootPane.FRAME) {
                return;
            }

            int w = getWidth();
            int x = 0;
            int y = 3;
            int spacing = 5;
            int buttonHeight;
            int buttonWidth;

            if (closeButton != null && closeButton.getIcon() != null) {
                buttonHeight = closeButton.getIcon().getIconHeight();
                buttonWidth = closeButton.getIcon().getIconWidth();
            }
            else {
                buttonHeight = IMAGE_HEIGHT;
                buttonWidth = IMAGE_WIDTH;
            }

            x = spacing;
            menuBar.setBounds(x, y, 15, buttonHeight);

            x = w - spacing - buttonWidth - (TITLEBAR_ICON_W+4);
            if (closeButton != null) {
                closeButton.setBounds(x, y, buttonWidth, buttonHeight);
            }

            x -= (spacing + buttonWidth);

            if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
                if (toggleButton.getParent() != null) {
                    toggleButton.setBounds(x, y, buttonWidth, buttonHeight);
                    x -= (spacing + buttonWidth);
                }
            }

            if (iconifyButton != null && iconifyButton.getParent() != null) {
                iconifyButton.setBounds(x, y, buttonWidth, buttonHeight);
                x -= (spacing + buttonWidth);
            }
        }
    }



    /**
     * PropertyChangeListener installed on the Window. Updates the necessary
     * state as the state of the Window changes.
     */
    private class PropertyChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent pce) {
//            String name = pce.getPropertyName();
//
//            // Frame.state isn't currently bound.
//            if ("resizable".equals(name) || "state".equals(name)) {
//                Frame frame = getFrame();
//
//                if (frame != null) {
//                    setState(frame.getExtendedState(), true);
//                }
//                if ("resizable".equals(name)) {
//                    getRootPane().repaint();
//                }
//            }
//            else if ("title".equals(name)) {
//                repaint();
//            }
//            else if ("componentOrientation".equals(name)) {
//                revalidate();
//                repaint();
//            }
        }
    }


    /**
     * WindowListener installed on the Window, updates the state as necessary.
     */
    private class WindowHandler extends WindowAdapter {
        public void windowActivated(WindowEvent ev) {
            setActive(true);
        }

        public void windowDeactivated(WindowEvent ev) {
            setActive(false);
        }
    }



    private  int getInt(Object key, int defaultValue) {
        Object value = UIManager.get(key);

        if (value instanceof Integer) {
            return ((Integer)value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String)value);
            } catch (NumberFormatException nfe) {}
        }
        return defaultValue;
    }
}

