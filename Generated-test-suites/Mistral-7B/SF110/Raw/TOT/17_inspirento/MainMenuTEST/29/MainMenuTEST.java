// MainMenuTest.java
package com.allenstudio.ir.ui;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.SwingConstants.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void setUp() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        String menu = "Menu1";
        JMenu expectedMenu = new JMenu(menu);
        JMenu actualMenu = mainMenu.createMenu(menu);

        assertEquals(expectedMenu.getText(), actualMenu.getText());
        assertEquals(expectedMenu.isMnemonicSet(), actualMenu.isMnemonicSet());

        String submenu = "Submenu1";
        JMenu submenuExpected = new JMenu(submenu);
        JMenu submenuActual = mainMenu.createMenu(submenu);
        assertEquals(submenuExpected.getText(), submenuActual.getText());

        String item = "Item1";
        String itemLabel = "Item1 Label";
        JMenuItem expectedItem = new JMenuItem(itemLabel);
        JMenuItem actualItem = mainMenu.createMenu(item).findComponentOfType(JMenuItem.class);
        assertEquals(expectedItem.getText(), actualItem.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "menu1.item1";
        String shortcut = "Ctrl F1";
        String icon = "icon1.png";

        JMenuItem item = mainMenu.createMenu("Menu1").findComponentOfType(JMenu.class).findComponentOfType(JMenuItem.class).findComponentOfType(JMenuItem.class); // Assuming the items are nested 3 levels deep

        mainMenu.addShortcutAndIcon(item, key);

        assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());
        assertTrue(item.getIcon() instanceof ImageIcon);

        // Test for null argument
        mainMenu.addShortcutAndIcon(null, key);
        assertNull(item);

        // Test for null key
        mainMenu.addShortcutAndIcon(item, null);
        assertNull(item.getAccelerator());
    }

    @Test
    public void testAddShortcutAndIconWithMissingResources() {
        String key = "menu1.item1";
        String shortcut = "Ctrl F1";
        String icon = "icon1.png";

        JMenuItem item = mainMenu.createMenu("Menu1").findComponentOfType(JMenu.class).findComponentOfType(JMenuItem.class).findComponentOfType(JMenuItem.class); // Assuming the items are nested 3 levels deep

        mainMenu.addShortcutAndIcon(item, key);

        try {
            assertEquals(KeyStroke.getKeyStroke(shortcut), item.getAccelerator());
            fail(); // Expected MissingResourceException
        } catch (MissingResourceException e) {}

        try {
            assertTrue(item.getIcon() instanceof ImageIcon);
            fail(); // Expected MissingResourceException
        } catch (MissingResourceException e) {}
    }
}