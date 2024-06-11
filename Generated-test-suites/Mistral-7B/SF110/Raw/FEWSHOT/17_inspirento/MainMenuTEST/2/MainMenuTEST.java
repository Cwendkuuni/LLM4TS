package com.allenstudio.ir.ui;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        assertEquals(2, mainMenu.getMenuCount());

        JMenu menu = mainMenu.getComponentAt(0);
        assertEquals("File", menu.getText());
        JMenu subMenu = (JMenu) mainMenu.getComponentAt(1);
        assertEquals("Edit", subMenu.getText());
    }

    @Test
    public void testCreateMenu() {
        String[] expectedMenuItems = new String[]{"File", "Edit", "View", "Help"};
        List<String> menuItems = new ArrayList<>();

        for (String menu : InspirentoUtilities.tokenize(UIResources.getString("menu"))) {
            JMenu jMenu = MainMenu.createMenu(menu);
            if (jMenu != null) {
                String label = jMenu.getText();
                menuItems.add(label);
                for (Component component : jMenu.getComponents()) {
                    JMenuItem item = (JMenuItem) component;
                    String key = item.getText();
                    if (!"-".equals(key)) {
                        String expectedLabel = UIResources.getString(key + "Label");
                        assertEquals(expectedLabel, item.getText());
                    }
                }
            }
        }

        assertArrayEquals(expectedMenuItems, menuItems.toArray(new String[0]));
    }

    @Test
    public void testCreateMenuItem() {
        String itemLabel = "Save";
        JMenuItem item = MainMenu.createMenuItem("Save", mediator);
        assertEquals(itemLabel, item.getText());

        char expectedMnemonic = 'S';
        assertEquals(expectedMnemonic, item.getMnemonic());
    }

    @Test
    public void testCreateMenuWithSubMenu() {
        String menuLabel = "Edit";
        String submenuLabel = "Cut";
        JMenu menu = MainMenu.createMenu(menuLabel);
        JMenu subMenu = (JMenu) MainMenu.createMenuItem(submenuLabel, menu);
        assertEquals(submenuLabel, subMenu.getText());
        menu.add(subMenu);
    }

    @Test
    public void testCreateCheckboxMenuItem() {
        String key = "Preferences";
        String checkboxLabel = "Show tooltips";
        JCheckBoxMenuItem jMenuItem = MainMenu.createCheckboxMenuItem(checkboxLabel, key, null);
        assertEquals(checkboxLabel, jMenuItem.getText());
        assertEquals(KeyEvent.VK_T, jMenuItem.getMnemonic());

        boolean expectedState = true;
        jMenuItem.setSelected(expectedState);
        assertTrue(jMenuItem.isSelected());
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "Preferences";
        String shortcut = "Ctrl+P";
        String iconPath = "icons/settings.png";

        JMenuItem item = new JMenuItem("Preferences");
        MainMenu.addShortcutAndIcon(item, key);

        assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK), item.getAccelerator());

        File expectedIconFile = new File(iconPath);
        URL expectedIconUrl = this.getClass().getResource(iconPath);
        assertEquals(expectedIconUrl, item.getIcon().getImage().getSource());
    }

    @Test
    public void testNullInputs() {
        JMenuItem item = new JMenuItem("Preferences");
        MainMenu.addShortcutAndIcon(item, null);
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());

        JMenu menu = new JMenu("File");
        MainMenu.addShortcutAndIcon(null, "Save");
        assertNull(menu.getComponentAt(0));
    }
}