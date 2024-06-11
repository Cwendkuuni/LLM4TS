package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.awt.KeyStroke;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

    private MainMenu menu;
    private final List<String> menuLabels = new ArrayList<>(Arrays.asList("File", "Edit", "Help"));
    private final List<String> subMenuLabels = new ArrayList<>(Arrays.asList("Open", "Save", "Quit", "Cut", "Copy", "Paste", "Undo", "Redo", "About", "Contact Us"));
    private final List<String> checkBoxMenuItemLabels = new ArrayList<>(Arrays.asList("Show Tooltips", "Show Grid", "Show Line Numbers"));

    @Before
    public void setUp() {
        InspirentoMediator mediator = new InspirentoMediator(); // Create a mock mediator for testing
        menu = new MainMenu(mediator);
    }

    @After
    public void tearDown() {
        menu = null;
    }

    @Test
    public void testMainMenuCreation() {
        for (String label : menuLabels) {
            JMenu menu = menu.getComponent(0);
            int index = Arrays.asList(menu.getMenuComponent(i)).indexOf(menu.getComponent(0));
            assertEquals(label, menu.getName());
            assertEquals(label, menu.getText());
            assertEquals(SwingConstants.LEFT, menu.getHorizontalAlignment());
            assertEquals(label + LABEL_SUFFIX, menu.getName());
            JMenuItem fileExitItem = (JMenuItem) menu.getComponent(menu.getComponentCount() - 1);
            assertEquals("Exit", fileExitItem.getText());
        }
    }

    @Test
    public void testCreateMenu() {
        for (String menu : menuLabels) {
            JMenu menu = menu.getComponent(0);
            for (String subMenu : Arrays.asList(menuLabels.get(0), menuLabels.get(1))) {
                JMenu subMenu = (JMenu) menu.getComponent(menu.getComponentCount() - 1);
                for (String item : Arrays.asList(subMenuLabels.toArray(new String[0]))) {
                    if (!"-".equals(item)) {
                        JMenuItem itemMenuItem = (JMenuItem) subMenu.getComponent(subMenu.getComponentCount() - 1);
                        if (item.length() > 1 && item.charAt(item.length() - 1) == '>') {
                            String subMenuLabel = item.substring(0, item.length() - 1);
                            assertEquals(subMenuLabel, ((JMenu) subMenu.getComponent(0)).getText());
                        } else {
                            String itemLabel = UIResources.getString(item + "Label");
                            assertEquals(itemLabel, itemMenuItem.getText());
                            if (itemLabel.length() > 2 && itemLabel.charAt(itemLabel.length() - 2) == '*') {
                                assertEquals(itemLabel.substring(0, itemLabel.length() - 2), itemMenuItem.getName());
                            }
                        }

                        if (item.length() > 1 && item.charAt(item.length() - 1) == '>') {
                            JMenu subSubMenu = (JMenu) itemMenuItem;
                            assertEquals(item.substring(0, item.length() - 1), subSubMenu.getName());
                            assertEquals(item.substring(0, item.length() - 1), subSubMenu.getText());
                            List<String> subSubMenuItems = new ArrayList<>(Arrays.asList("Save As", "Export"));
                            for (String subSubMenuItem : subSubMenuItems) {
                                JMenuItem subSubMenuItemItem = (JMenuItem) subSubMenu.getComponent(subSubMenu.getComponentCount() - 1);
                                String subSubMenuItemLabel = UIResources.getString(subSubMenuItem + "Label");
                                assertEquals(subSubMenuItemLabel, subSubMenuItemItem.getText());
                                assertEquals(subSubMenuItemLabel, subSubMenuItemItem.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        for (String menu : menuLabels) {
            for (String item : Arrays.asList(menuLabels.get(0), menuLabels.get(1), menuLabels.get(1) + ":Cut", menuLabels.get(1) + ":Copy", menuLabels.get(1) + ":Paste")) {
                JMenuItem itemMenuItem = null;
                if (item.contains(":")) {
                    JMenu subMenu = (JMenu) menu.getComponent(menu.getComponentCount() - 1);
                    itemMenuItem = (JMenuItem) subMenu.getComponent( Arrays.asList(subMenu.getComponent(0)).indexOf(new JMenuItem(UIResources.getString(item.split(":")[0] + "Label"))));
                } else {
                    itemMenuItem = (JMenuItem) menu.getComponent(menu.getComponentCount() - 1);
                }
                String key = item;
                if (item.contains(":")) {
                    key = item.split(":")[1];
                }
                String shortcut = UIResources.getString(key + "Shortcut");
                assertEquals(KeyStroke.getKeyStroke(shortcut), itemMenuItem.getAccelerator());

                try {
                    String iconPath = UIResources.getString(key + "Icon");
                    URL url = new File(".").toURI().toURL(); // Assuming the test class is in the root directory for simplicity
                    URL testUrl = new URL(url, iconPath);
                    Image expectedIcon = ImageIO.read(new File("path/to/your/icon.png")); // Replace this with the actual icon path
                    Image actualIcon = itemMenuItem.getIcon().getImage();
                    assertEquals(expectedIcon.getWidth(null), actualIcon.getWidth(null));
                    assertEquals(expectedIcon.getHeight(null), actualIcon.getHeight(null));
                    assertEquals(expectedIcon, itemMenuItem.getIcon());
                } catch (Exception e) {
                    assertNull(itemMenuItem.getIcon());
                }
            }
        }
    }
}