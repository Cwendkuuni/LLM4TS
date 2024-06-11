package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        String uiResourcesString = "menu.File|FileLabel*F|File|New|NewLabel*N|New|Exit|ExitLabel*E";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        assertEquals("size: " + menus.length, menus.length, mainMenu.getMenuCount());
        for (int i = 0; i < menus.length; i++) {
            assertEquals("menu: " + menus[i], menus[i], mainMenu.getComponentAt(i).getName());
        }
    }

    @Test
    public void testCreateMenu() {
        String uiResourcesString = "menu.File|FileLabel*F|File|New|NewLabel*N|New|Exit|ExitLabel*E";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu fileMenu = mainMenu.createMenu("menu.File");
        assertEquals("name: File", "File", fileMenu.getName());
        String[] fileMenuItems = {"New", "Exit"};
        for (int i = 0; i < fileMenuItems.length; i++) {
            JMenuItem item = (JMenuItem) fileMenu.getComponentAt(i);
            assertEquals("item name: " + fileMenuItems[i], fileMenuItems[i], item.getName());
        }
        assertEquals("size: " + fileMenu.getMenuComponentCount(), fileMenuItems.length, fileMenu.getMenuComponentCount());
    }

    @Test
    public void testCreateCheckBoxMenuItem() {
        String uiResourcesString = "#menu.File.Save|SaveLabel*S|Save|SaveAs|SaveAsLabel*A";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JCheckBoxMenuItem saveMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu("menu.File").getComponent(0);
        assertEquals("name: Save", "Save", saveMenuItem.getName());
        assertEquals("label: Save", "SaveLabel", saveMenuItem.getActionCommand());
        assertNull(saveMenuItem.getIcon());
        assertEquals("shortcut: S", KeyEvent.VK_S, saveMenuItem.getAccelerator().getKeyCode());
    }

    @Test
    public void testCreateMenuItem() {
        String uiResourcesString = "menu.File|FileLabel*F|File|New|NewLabel*N|New|Exit|ExitLabel*E";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem newMenuItem = (JMenuItem) mainMenu.createMenu("menu.File").getComponent(1);
        assertEquals("name: New", "New", newMenuItem.getName());
        assertEquals("label: NewLabel", "NewLabel", newMenuItem.getActionCommand());
        assertNull(newMenuItem.getIcon());
        assertEquals("shortcut: None", null, newMenuItem.getAccelerator());
    }

    @Test
    public void testAddSeparator() {
        String uiResourcesString = "menu.File|FileLabel*F|File|New|NewLabel*N|New|Exit|ExitLabel*E|--";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu fileMenu = mainMenu.createMenu("menu.File");
        assertEquals("size: " + fileMenu.getComponentCount(), 5, fileMenu.getComponentCount());
        JMenuItem newMenuItem = (JMenuItem) fileMenu.getComponent(2);
        assertEquals("name: New", "New", newMenuItem.getName());
        JSeparator separator = (JSeparator) fileMenu.getComponent(3);
        assertNotNull(separator);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuLabel() {
        String uiResourcesString = "menu.Unknown|UnknownLabel*U|Unknown|Unknown|UnknownLabel*U|Unknown";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        mainMenu.createMenu("menu.Unknown");
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuItemLabel() {
        String uiResourcesString = "menu.File|FileLabel*F|File|Unknown|UnknownLabel*U|Unknown";
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu fileMenu = mainMenu.createMenu("menu.File");
        JMenuItem unknownMenuItem = (JMenuItem) fileMenu.getComponent(1);
        mainMenu.createMenu(unknownMenuItem.getActionCommand());
    }
}