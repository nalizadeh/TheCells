/*--- (C) 1999-2006 Techniker Krankenkasse ---*/

package org.nalizadeh.designer.util.lf;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.net.URL;

public class SoneLookAndFeel extends MetalLookAndFeel {

        public static ImageIcon getImage(String name) {
                URL url = SoneLookAndFeel.class.getResource("img/" + name);
                if (url != null) {
                        return new ImageIcon(url);
                }
                return null;
        }

        Object textFieldBorder =
                new UIDefaults.ProxyLazyValue("org.nalizadeh.designer.util.lf.SoneBorders", "getTextFieldBorder");

        Object toolBarBorder =
                new UIDefaults.ProxyLazyValue("org.nalizadeh.designer.util.lf.SoneBorders", "getToolBarBorder");

        Object buttonBorder =
                new UIDefaults.ProxyLazyValue("org.nalizadeh.designer.util.lf.SoneBorders", "getButtonBorder");

        Object etchedBorder =
                new UIDefaults.ProxyLazyValue(
                        "javax.swing.plaf.BorderUIResource",
                        "getEtchedBorderUIResource"
                );

        Integer twelve = new Integer(12);
        Integer fontPlain = new Integer(Font.PLAIN);
        Integer fontBold = new Integer(Font.BOLD);
        Object dialogPlain12 =
                new UIDefaults.ProxyLazyValue(
                        "javax.swing.plaf.FontUIResource",
                        null,
                        new Object[] {"Dialog", fontPlain, twelve}
                );

        Object menuItemAcceleratorDelimiter = new String("+");
        Object marginBorder =
                new UIDefaults.ProxyLazyValue("javax.swing.plaf.basic.BasicBorders$MarginBorder");

        Object menuItemCheckIcon =
                new UIDefaults.ProxyLazyValue(
                        "javax.swing.plaf.basic.BasicIconFactory",
                        "getMenuItemCheckIcon"
                );

        Object menuItemArrowIcon =
                new UIDefaults.ProxyLazyValue(
                        "javax.swing.plaf.basic.BasicIconFactory",
                        "getMenuItemArrowIcon"
                );

        Object menuArrowIcon =
                new UIDefaults.ProxyLazyValue(
                        "javax.swing.plaf.basic.BasicIconFactory",
                        "getMenuArrowIcon"
                );

        Object menuBarBorder =
                new UIDefaults.ProxyLazyValue("org.nalizadeh.designer.util.lf.SoneBorders", "getMenuBarBorder");

        Object checkBoxMenuItemIcon =
                new UIDefaults.ProxyLazyValue(
                        "javax.swing.plaf.basic.BasicIconFactory",
                        "getCheckBoxMenuItemIcon"
                );

        Object radioButtonMenuItemIcon =
                new UIDefaults.ProxyLazyValue(
                        "javax.swing.plaf.basic.BasicIconFactory",
                        "getRadioButtonMenuItemIcon"
                );

        Object popupMenuBorder =
                new UIDefaults.ProxyLazyValue("org.nalizadeh.designer.util.lf.SoneBorders", "getPopupMenuBorder");

        /**
         * Description of the Field
         */
        public final static String VERSION = "1.0";

        /**
         * Gets the Name attribute of the SoneLookAndFeel object
         *
         * @return  The Name value
         */
        public String getName() {
                return "SoneLF";
        }

        /**
         * Gets the Description attribute of the SoneLookAndFeel object
         *
         * @return  The Description value
         */
        public String getDescription() {
                return "Sone Look and Feel";
        }

        /**
         * Gets the ID attribute of the SoneLookAndFeel object
         *
         * @return  The ID value
         */
        public String getID() {
                return "SoneLF";
        }

        /**
         * Gets the NativeLookAndFeel attribute of the SoneLookAndFeel object
         *
         * @return  The NativeLookAndFeel value
         */
        public boolean isNativeLookAndFeel() {
                return false;
        }

        /**
         * Gets the SupportedLookAndFeel attribute of the SoneLookAndFeel object
         *
         * @return  The SupportedLookAndFeel value
         */
        public boolean isSupportedLookAndFeel() {
                return true;
        }

        public UIDefaults getDefaults() {
                return super.getDefaults();
        }

        /**
         * Description of the Method
         *
         * @param  table  Description of Parameter
         */
        protected void initClassDefaults(UIDefaults table) {
                super.initClassDefaults(table);

                String basicPackageName = "org.nalizadeh.designer.util.lf.";

                Object[] uiDefaults = {
                                      "RootPaneUI", basicPackageName + "SoneRootPaneUI",
                                      "ButtonUI", basicPackageName + "SoneButtonUI",
                                      "CheckBoxUI", basicPackageName + "SoneCheckBoxUI",
                                      "MenuBarUI", basicPackageName + "SoneMenuBarUI",
                                      "MenuUI", basicPackageName + "SoneMenuUI",
                                      "MenuItemUI", basicPackageName + "SoneMenuItemUI",
                                      "ScrollBarUI", basicPackageName + "SoneScrollBarUI",
                                      "TextFieldUI", basicPackageName + "SoneTextFieldUI",
                                      "ComboBoxUI", basicPackageName + "SoneComboBoxUI",
                                      "PanelUI", basicPackageName + "SonePanelUI",
                                      "TableHeaderUI", basicPackageName + "SoneTableHeaderUI",
                                      "TreeUI", basicPackageName + "SoneTreeUI",
                                      "SplitPaneUI", basicPackageName + "SoneSplitPaneUI",
                                      "TabbedPaneUI", basicPackageName + "SoneTabbedPaneUI",
                                      "InternalFrameUI", basicPackageName + "SoneInternalFrameUI",
                };

                table.putDefaults(uiDefaults);
        }

        protected void initSystemColorDefaults(UIDefaults table) {
                super.initSystemColorDefaults(table);

                String[] defaultSystemColors = {
                                               "desktop", "#005C5C", /* Color of the desktop background */
                                               "activeCaption",
                                               "#000080", /* Color for captions (title bars) when they are active.
                                               */
                                               "activeCaptionText",
                                               "#FFFFFF", /* Text color for text in captions (title bars). */
                                               "activeCaptionBorder",
                                               "#C0C0C0", /* Border color for caption (title bar) window
                                               * borders. */
                                               "inactiveCaption",
                                               "#3F3F3B", /* Color for captions (title bars) when not active. */
                                               "inactiveCaptionText",
                                               "#C0C0C0", /* Text color for text in inactive captions (title
                                               * bars). */
                                               "inactiveCaptionBorder",
                                               "#C0C0C0", /* Border color for inactive caption (title bar)
                                               * window borders. */
                                               "window", "#F4F3E3", /* Default color for the interior of windows */
                                               "windowBorder", "#000000", /* ??? */
                                               "windowText", "#000000", /* ??? */
                                               "menu", "#F4F3E3", /* Background color for menus */
                                               "menuText", "#000000", /* Text color for menus  */
                                               "text", "#F4F3E3", /* Text background color */
                                               "textText", "#000000", /* Text foreground color */
                                               "textHighlight", "#95B1C1", //C6D5DD",//CBCBBD", /* Text background color when selected */
                                               "textHighlightText", "#000000", /* Text color when selected */
                                               "textInactiveText", "#808080", /* Text color when disabled */
                                               "control",
                                               "#95B1C1", /* Default color for controls (buttons, sliders, etc) */
                                               "controlText", "#000000", /* Default color for text in controls */
                                               "controlHighlight",
                                               "#C0C0C0", /* Specular highlight (opposite of the shadow) */
                                               "controlLtHighlight", "#FFFFFF", /* Highlight color for controls */
                                               "controlShadow", "#808080", /* Shadow color for controls */
                                               "controlDkShadow", "#000000", /* Dark shadow color for controls */
                                               "scrollbar", "#F4F3E3", /* Scrollbar background (usually the "track") */
                                               "info", "#FFFFE1", /* ??? */
                                               "infoText", "#000000" /* ??? */
                };

                loadSystemColors(table, defaultSystemColors, isNativeLookAndFeel());
        }

        protected void initComponentDefaults(UIDefaults table) {
                super.initComponentDefaults(table);

                table.remove("RootPane.frameBorder");
                table.put("RootPane.frameBorder", new org.nalizadeh.designer.util.lf.SoneBorders.FrameBorder());

                table.remove("RootPane.background");
                table.put("RootPane.background", new Color(193, 193, 193));

                table.remove("RootPane.shadow1");
                table.put("RootPane.shadow1", new Color(74, 74, 74));

                table.remove("RootPane.shadow2");
                table.put("RootPane.shadow2", new Color(120, 120, 120));

                table.remove("RootPane.highlight");
                table.put("RootPane.highlight", new Color(245, 245, 245));

                table.remove("RootPane.titlePaneActiveBackground");
                table.put("RootPane.titlePaneActiveBackground", new Color(23, 41, 114));

                table.remove("RootPane.titlePaneActiveForeground");
                table.put("RootPane.titlePaneActiveForeground", new Color(156, 165, 198));

                table.remove("SoneLF.sysFont");
                table.put("SoneLF.sysFont", new Font("Dialog", 0, 12));

                table.remove("RootPane.titleFont");
                table.put("RootPane.titleFont", new Font("Dialog", 0, 12));

                table.remove("RootPane.restoreTitle");
                table.put("RootPane.restoreTitle", "Wiederherstellen");

                table.remove("RootPane.iconifyTitle");
                table.put("RootPane.iconifyTitle", "Minimieren");

                table.remove("RootPane.maximizeTitle");
                table.put("RootPane.maximizeTitle", "Maximieren");

                table.remove("RootPane.closeTitle");
                table.put("RootPane.closeTitle", "Schlieﬂen");

                table.remove("RootPane.restoreMnemonic");
                table.put("RootPane.restoreMnemonic", new Integer(KeyEvent.VK_W));

                table.remove("RootPane.iconifyMnemonic");
                table.put("RootPane.iconifyMnemonic", new Integer(KeyEvent.VK_I));

                table.remove("RootPane.maximizeMnemonic");
                table.put("RootPane.maximizeMnemonic", new Integer(KeyEvent.VK_M));

                table.remove("RootPane.closeMnemonic");
                table.put("RootPane.closeMnemonic", new Integer(KeyEvent.VK_S));

                table.remove("RootPane.closeKeyStroke");
                table.put(
                        "RootPane.closeKeyStroke",
                        KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK)
                        );

                table.remove("RootPane.activeTitlebarIcon");
                table.put(
                        "RootPane.activeTitlebarIcon",
                        getImage("titlebar.gif")
                        );
                table.remove("RootPane.inactiveTitlebarIcon");
                table.put(
                        "RootPane.inactiveTitlebarIcon",
                        getImage("titlebar2.gif")
                        );

                table.remove("RootPane.maximizeIcon");
                table.put(
                        "RootPane.maximizeIcon",
                        getImage("maximize.gif")
                        );
                table.remove("RootPane.maximizeRolloverIcon");
                table.put(
                        "RootPane.maximizeRolloverIcon",
                        getImage("maximizeRollover.gif")
                        );

                table.remove("RootPane.minimizeIcon");
                table.put(
                        "RootPane.minimizeIcon",
                        getImage("minimize.gif")
                        );
                table.remove("RootPane.minimizeRolloverIcon");
                table.put(
                        "RootPane.minimizeRolloverIcon",
                        getImage("minimizeRollover.gif")
                        );

                table.remove("RootPane.closeIcon");
                table.put("RootPane.closeIcon", getImage("close.gif"));
                table.remove("RootPane.closeRolloverIcon");
                table.put(
                        "RootPane.closeRolloverIcon",
                        getImage("closeRollover.gif")
                        );

                table.remove("RootPane.iconifyIcon");
                table.put(
                        "RootPane.iconifyIcon",
                        getImage("iconify.gif")
                        );
                table.remove("RootPane.iconifyRolloverIcon");
                table.put(
                        "RootPane.iconifyRolloverIcon",
                        getImage("iconifyRollover.gif")
                        );

                table.remove("RootPane.menuIcon");
                table.put("RootPane.menuIcon", getImage("menu.gif"));
                table.remove("RootPane.menuRolloverIcon");
                table.put(
                        "RootPane.menuRolloverIcon",
                        getImage("menuRollover.gif")
                        );

                table.remove("InternalFrame.titleFont");
                table.put("InternalFrame.titleFont", new Font("Dialog", 0, 12));

                table.remove("InternalFrame.border");
                table.put("InternalFrame.border", new org.nalizadeh.designer.util.lf.SoneBorders.FrameBorder());

                table.remove("InternalFrame.activeTitleBackground");
                table.put("InternalFrame.activeTitleBackground", new Color(23, 41, 114));

                table.remove("InternalFrame.activeTitleForeground");
                table.put("InternalFrame.activeTitleForeground", new Color(156, 165, 198));

                table.remove("InternalFrame.menuIcon");
                table.put("InternalFrame.menuIcon", getImage("menu.gif"));
                table.remove("InternalFrame.menuRolloverIcon");
                table.put(
                        "InternalFrame.menuRolloverIcon",
                        getImage("menuRollover.gif")
                        );
                table.remove("InternalFrame.maximizeIcon");
                table.put(
                        "InternalFrame.maximizeIcon",
                        getImage("maximize.gif")
                        );
                table.remove("InternalFrame.maximizeRolloverIcon");
                table.put(
                        "InternalFrame.maximizeRolloverIcon",
                        getImage("maximizeRollover.gif")
                        );

                table.remove("InternalFrame.minimizeIcon");
                table.put(
                        "InternalFrame.minimizeIcon",
                        getImage("minimize.gif")
                        );
                table.remove("InternalFrame.minimizeRolloverIcon");
                table.put(
                        "InternalFrame.minimizeRolloverIcon",
                        getImage("minimizeRollover.gif")
                        );

                table.remove("InternalFrame.closeIcon");
                table.put("InternalFrame.closeIcon", getImage("close.gif"));
                table.remove("InternalFrame.closeRolloverIcon");
                table.put(
                        "InternalFrame.closeRolloverIcon",
                        getImage("closeRollover.gif")
                        );

                table.remove("InternalFrame.iconifyIcon");
                table.put(
                        "InternalFrame.iconifyIcon",
                        getImage("iconify.gif")
                        );
                table.remove("InternalFrame.iconifyRolloverIcon");
                table.put(
                        "InternalFrame.iconifyRolloverIcon",
                        getImage("iconifyRollover.gif")
                        );

                table.remove("InternalFrame.menuIcon");
                table.put("InternalFrame.menuIcon", getImage("menu.gif"));
                table.remove("InternalFrame.menuRolloverIcon");
                table.put(
                        "InternalFrame.menuRolloverIcon",
                        getImage("menuRollover.gif")
                        );

                table.remove("SoneLF.comboBoxArrow");
                table.put(
                        "SoneLF.comboBoxArrow",
                        getImage("combobox.gif")
                        );

                table.remove("TabbedPane.top.img1");
                table.put(
                        "TabbedPane.top.img1",
                        getImage("tabtop1.gif")
                        );
                table.remove("TabbedPane.top.img2");
                table.put(
                        "TabbedPane.top.img2",
                        getImage("tabtop2.gif")
                        );
                table.remove("TabbedPane.top.img3");
                table.put(
                        "TabbedPane.top.img3",
                        getImage("tabtop3.gif")
                        );
                table.remove("TabbedPane.top.img4");
                table.put(
                        "TabbedPane.top.img4",
                        getImage("tabtop4.gif")
                        );
                table.remove("TabbedPane.top.img5");
                table.put(
                        "TabbedPane.top.img5",
                        getImage("tabtop5.gif")
                        );
                table.remove("TabbedPane.top.img6");
                table.put(
                        "TabbedPane.top.img6",
                        getImage("tabtop6.gif")
                        );

        		table.remove("Tree.rendererFillBackground");
        		table.put("Tree.rendererFillBackground", Boolean.TRUE);
        		table.remove("Tree.background");
        		table.put("Tree.background", new Color(244, 243, 227));
        		table.remove("Tree.textBackground");
        		table.put("Tree.textBackground", new Color(244, 243, 227));

        		table.remove("TextField.border");
                table.put("TextField.border", textFieldBorder);

                table.remove("ToolBar.background");
                table.put("ToolBar.background", table.get("control"));
                table.remove("ToolBar.border");
                table.put("ToolBar.border", toolBarBorder);

                table.remove("etchedBorder");
                table.put("etchedBorder", etchedBorder);

                Object[] defaults = {

                                    //====================================================================================
                                    // *** Buttons
                                    //====================================================================================
                                    "Button.border", buttonBorder, "Button.font", dialogPlain12,

                                    "Label.font", dialogPlain12, "CheckBox.font", dialogPlain12, "RadioButton.font",
                                    dialogPlain12, "ComboBox.font", dialogPlain12,

                                    "ScrollBar.thumb", table.get("control"), "ScrollBar.bumpsh",
                                    getImage("scrollbumpsh.gif"), "ScrollBar.bumpsv",
                                    getImage("scrollbumpsv.gif"),

                                    "TabbedPane.font", dialogPlain12,
                                    //====================================================================================
                                    // *** Menus
                                    //====================================================================================
                                    "MenuBar.font", dialogPlain12, "MenuBar.background", table.get("menu"),
                                    "MenuBar.foreground", table.get("menuText"), "MenuBar.shadow",
                                    table.getColor("controlShadow"), "MenuBar.highlight",
                                    table.getColor("controlLtHighlight"), "MenuBar.border", menuBarBorder,
                                    "MenuBar.windowBindings", new Object[] {
                                    "F10", "takeFocus"
                }
                        ,

                        "MenuItem.font", dialogPlain12, "MenuItem.acceleratorFont", dialogPlain12,
                        "MenuItem.background", table.get("menu"), "MenuItem.foreground",
                        table.get("menuText"), "MenuItem.selectionForeground",
                        table.get("textHighlightText"), "MenuItem.selectionBackground",
                        new Color(203, 203, 189), //table.get("textHighlight"),
                        "MenuItem.disabledForeground", null, "MenuItem.acceleratorForeground",
                        table.get("menuText"), "MenuItem.acceleratorSelectionForeground",
                        table.get("textHighlightText"), "MenuItem.acceleratorDelimiter",
                        menuItemAcceleratorDelimiter, "MenuItem.border", marginBorder,
                        "MenuItem.borderPainted", Boolean.FALSE, "MenuItem.margin",
                        new InsetsUIResource(2, 2, 2, 2), "MenuItem.checkIcon", menuItemCheckIcon,
                        "MenuItem.arrowIcon", menuItemArrowIcon, "MenuItem.commandSound", null,
                        "MenuItem.lineShadow", table.get("controlShadow"), "MenuItem.lineHiglight",
                        table.get("controlLtHighlight"),

                        "RadioButtonMenuItem.font", dialogPlain12, "RadioButtonMenuItem.acceleratorFont",
                        dialogPlain12, "RadioButtonMenuItem.background", table.get("menu"),
                        "RadioButtonMenuItem.foreground", table.get("menuText"),
                        "RadioButtonMenuItem.selectionForeground", table.get("textHighlightText"),
                        "RadioButtonMenuItem.selectionBackground", table.get("textHighlight"),
                        "RadioButtonMenuItem.disabledForeground", null,
                        "RadioButtonMenuItem.acceleratorForeground", table.get("menuText"),
                        "RadioButtonMenuItem.acceleratorSelectionForeground",
                        table.get("textHighlightText"), "RadioButtonMenuItem.border", marginBorder,
                        "RadioButtonMenuItem.borderPainted", Boolean.FALSE, "RadioButtonMenuItem.margin",
                        new InsetsUIResource(2, 2, 2, 2), "RadioButtonMenuItem.checkIcon",
                        radioButtonMenuItemIcon, "RadioButtonMenuItem.arrowIcon", menuItemArrowIcon,
                        "RadioButtonMenuItem.commandSound", null,

                        "CheckBoxMenuItem.font", dialogPlain12, "CheckBoxMenuItem.acceleratorFont",
                        dialogPlain12, "CheckBoxMenuItem.background", table.get("menu"),
                        "CheckBoxMenuItem.foreground", table.get("menuText"),
                        "CheckBoxMenuItem.selectionForeground", table.get("textHighlightText"),
                        "CheckBoxMenuItem.selectionBackground", table.get("textHighlight"),
                        "CheckBoxMenuItem.disabledForeground", null,
                        "CheckBoxMenuItem.acceleratorForeground", table.get("menuText"),
                        "CheckBoxMenuItem.acceleratorSelectionForeground", table.get("textHighlightText"),
                        "CheckBoxMenuItem.border", marginBorder, "CheckBoxMenuItem.borderPainted",
                        Boolean.FALSE, "CheckBoxMenuItem.margin", new InsetsUIResource(2, 2, 2, 2),
                        "CheckBoxMenuItem.checkIcon", checkBoxMenuItemIcon, "CheckBoxMenuItem.arrowIcon",
                        menuItemArrowIcon, "CheckBoxMenuItem.commandSound", null,

                        "Menu.font", dialogPlain12, "Menu.acceleratorFont", dialogPlain12,
                        "Menu.background", table.get("menu"), "Menu.foreground", table.get("menuText"),
                        "Menu.selectionForeground", table.get("textHighlightText"),
                        "Menu.selectionBackground", table.get("textHighlight"), "Menu.disabledForeground",
                        null, "Menu.acceleratorForeground", table.get("menuText"),
                        "Menu.acceleratorSelectionForeground", table.get("textHighlightText"),
                        "Menu.border", marginBorder, "Menu.borderPainted", Boolean.FALSE, "Menu.margin",
                        new InsetsUIResource(2, 2, 2, 2), "Menu.checkIcon", menuItemCheckIcon,
                        "Menu.arrowIcon", menuArrowIcon, "Menu.menuPopupOffsetX", new Integer(0),
                        "Menu.menuPopupOffsetY", new Integer(0), "Menu.submenuPopupOffsetX", new Integer(0),
                        "Menu.submenuPopupOffsetY", new Integer(0), "Menu.shortcutKeys", new int[] {
                        KeyEvent.ALT_MASK
                }
                , "Menu.crossMenuMnemonic", Boolean.TRUE,
                        // PopupMenu
                        "PopupMenu.font", dialogPlain12, "PopupMenu.background", table.get("menu"),
                        "PopupMenu.foreground", table.get("menuText"), "PopupMenu.border", popupMenuBorder,
                        // Internal Frame Auditory Cue Mappings
                        "PopupMenu.popupSound", null,
                        // These window InputMap bindings are used when the Menu is
                        // selected.
                        "PopupMenu.selectedWindowInputMapBindings", new Object[] {
                        "ESCAPE", "cancel", "DOWN", "selectNext", "KP_DOWN", "selectNext", "UP",
                        "selectPrevious", "KP_UP", "selectPrevious", "LEFT", "selectParent", "KP_LEFT",
                        "selectParent", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "ENTER",
                        "return", "SPACE", "return"
                }, "PopupMenu.selectedWindowInputMapBindings.RightToLeft", new Object[] {
                        "LEFT", "selectChild", "KP_LEFT", "selectChild", "RIGHT", "selectParent",
                        "KP_RIGHT", "selectParent",
                }
                        //====================================================================================
                        // *** Menus
                        //====================================================================================
                        } ;
                table.putDefaults(defaults);
        }
}

/*--- Formatiert nach TK Code Konventionen vom 05.03.2002 ---*/
