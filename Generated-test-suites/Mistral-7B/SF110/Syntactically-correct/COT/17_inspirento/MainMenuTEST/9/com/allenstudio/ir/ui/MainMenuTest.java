package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.net.*;
import java.util.*;

public class MainMenuTest {

    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void setUp() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testMainMenuConstruction() {
        assertNotNull(mainMenu);
    }

    @Test
    public void testCreateMenu() {
        String menu = "File";
        JMenu expectedMenu = new JMenu("File");
        JMenu actualMenu = mainMenu.createMenu(menu);
        assertEquals(expectedMenu, actualMenu);
    }

    @Test
    public void testCreateMenuWithLabelAndMnemonic() {
        String menu = "File*";
        JMenu expectedMenu = new JMenu("File");
        expectedMenu.setMnemonic('F');
        JMenu actualMenu = mainMenu.createMenu(menu);
        assertEquals(expectedMenu, actualMenu);
    }

    @Test
    public void testCreateMenuWithSubMenu() {
        String menu = "Edit>";
        JMenu expectedMenu = new JMenu("Edit");
        JMenu subMenu = new JMenu("Paste");
        expectedMenu.add(subMenu);
        JMenu actualMenu = mainMenu.createMenu(menu);
        assertEquals(expectedMenu, actualMenu);
    }

    @Test
    public void testCreateMenuWithCheckboxMenuItem() {
        String menu = "Options##View";
        String key = "View";
        String label = "View";
        JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem(label);
        JCheckBoxMenuItem actualMenuItem = mainMenu.createMenu(menu).findJCheckBoxMenuItem(key);
        assertEquals(expectedMenuItem, actualMenuItem);
    }

    @Test
    public void testCreateMenuWithMenuItem() {
        String menu = "Help";
        String key = "Help";
        String label = "Help";
        JMenuItem expectedMenuItem = new JMenuItem(label);
        JMenuItem actualMenuItem = mainMenu.createMenu("Help").findJMenuItem(key);
        assertEquals(expectedMenuItem, actualMenuItem);
    }

    @Test
    public void testAddShortcutAndIconWithShortcut() {
        String key = "Edit*Cut";
        String shortcut = "Ctrl+X";
        JMenuItem menuItem = new JMenuItem("");
        mainMenu.addShortcutAndIcon(menuItem, key);
        assertArrayEquals(new String[]{"Ctrl", "X"}, menuItem.getAccelerator().toString().split("+"));
    }

    @Test
    public void testAddShortcutAndIconWithoutShortcut() {
        String key = "Help";
        JMenuItem menuItem = new JMenuItem("");
        mainMenu.addShortcutAndIcon(menuItem, key);
        assertNull(menuItem.getAccelerator());
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingResourceException() {
        String menu = "MissingMenu";
        mainMenu.createMenu(menu);
    }

    @Test
    public void testFindMenuItem() {
        String key = "Help";
        JMenuItem expectedMenuItem = new JMenuItem("Help");
        JMenuItem actualMenuItem = mainMenu.findJMenuItem(key);
        assertEquals(expectedMenuItem, actualMenuItem);
    }

    @Test
    public void testFindCheckboxMenuItem() {
        String key = "View##ShowGrid";
        JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("Show Grid");
        JCheckBoxMenuItem actualMenuItem = mainMenu.findJCheckBoxMenuItem(key);
        assertEquals(expectedMenuItem, actualMenuItem);
    }

    @Test
    public void testGetClassLoader() {
        ClassLoader classLoader = mainMenu.getClass().getClassLoader();
        assertNotNull(classLoader);
    }
}