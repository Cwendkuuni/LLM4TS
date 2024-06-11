package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import javax.swing.*;
import org.junit.*;

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
        String menuLabel = "File";
        JMenu menu = mainMenu.createMenu(menuLabel);
        assertEquals("Menu label should be 'File'", menuLabel, menu.getText());
        String fileName = "TestFile.txt";
        String subMenuLabel = "Open";
        JMenu openSubMenu = mainMenu.createMenu(subMenuLabel);
        menu.add(openSubMenu);
        JMenuItem openMenuItem = new JMenuItem("Open " + fileName);
        openSubMenu.add(openMenuItem);
        assertNotNull("Open menu item should not be null", openMenuItem);
        assertEquals("Open menu item label should be 'Open " + fileName + "'", "Open " + fileName, openMenuItem.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "File.Open";
        String shortcut = "Ctrl+O";
        String iconPath = "file_open.png";
        JMenuItem openMenuItem = new JMenuItem(key);
        mainMenu.addShortcutAndIcon(openMenuItem, key);
        assertEquals("Accelerator key should be 'Ctrl+O'", shortcut, openMenuItem.getAccelerator().toString());
        assertNotNull("Icon should not be null", openMenuItem.getIcon());
        URL url = mainMenu.getClass().getResource(iconPath);
        assertTrue("Icon file should exist", url != null);
    }

    @Test
    public void testMissingMenuLabel() {
        Assert.assertThrows(MissingResourceException.class, () -> mainMenu.createMenu("MissingMenu"));
    }

    @Test
    public void testMissingMenuItemLabel() {
        JMenu menu = mainMenu.createMenu("TestMenu");
        menu.addSeparator();
        Assert.assertThrows(MissingResourceException.class, () -> mainMenu.createMenu("MissingItem"));
    }

    @Test
    public void testCreateMenuWithEmptyString() {
        JMenu menu = mainMenu.createMenu("");
        assertNotNull("Menu should not be null", menu);
        assertEquals("Menu label should be an empty string", "", menu.getText());
    }

    @Test
    public void testAddShortcutAndIconWithEmptyStrings() {
        String key = "";
        String shortcut = "";
        String iconPath = "";
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, key);
        assertEquals("Accelerator key should be an empty string", "", menuItem.getAccelerator().toString());
        assertNull("Icon should be null", menuItem.getIcon());
    }
}