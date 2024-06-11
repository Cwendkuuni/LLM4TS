package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = mock(InspirentoMediator.class); // Assuming you have a mocking framework like Mockito
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        String menuLabel = "File";
        JMenu fileMenu = mainMenu.createMenu(menuLabel);

        assertNotNull(fileMenu);
        assertEquals(menuLabel, fileMenu.getText());

        // Add more assertions as needed
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        String key = "Open";
        JMenuItem openItem = new JMenuItem("Open");

        mainMenu.addShortcutAndIcon(openItem, key);

        assertNotNull(openItem.getAccelerator());
        assertEquals(KeyEvent.VK_O, openItem.getAccelerator().getKeyChar());

        String iconPath = "/path/to/icon.png";
        assertEquals(iconPath, mainMenu.getClass().getResource(iconPath).toString());

        // Add more test cases as needed
    }

    @Test
    public void testCreateMenuItemWithSubMenusAndCheckboxes() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        String menuLabel = "Edit";
        JMenu editMenu = mainMenu.createMenu(menuLabel);

        String subMenuLabel = "Advanced";
        JMenu advancedSubMenu = new JMenu(subMenuLabel);
        editMenu.add(advancedSubMenu);

        String checkboxItemLabel = "Toggle Case";
        JCheckBoxMenuItem checkboxItem = new JCheckBoxMenuItem(checkboxItemLabel);
        advancedSubMenu.add(checkboxItem);

        mainMenu.addShortcutAndIcon(checkboxItem, checkboxItemLabel);

        assertNull(checkboxItem.getAccelerator()); // Checkbox menus don't have shortcuts
        assertNotNull(checkboxItem.getIcon());

        // Add more assertions as needed
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuLabel() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        String invalidMenuLabel = "InvalidMenuLabel";
        mainMenu.createMenu(invalidMenuLabel);

        fail("A MissingResourceException should be thrown");
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingItemLabel() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        String menuLabel = "File";
        JMenu fileMenu = mainMenu.createMenu(menuLabel);

        String invalidItemLabel = "InvalidItemLabel";
        JMenuItem invalidItem = new JMenuItem(invalidItemLabel);
        fileMenu.add(invalidItem);

        mainMenu.addShortcutAndIcon(invalidItem, invalidItemLabel);

        fail("A MissingResourceException should be thrown");
    }

    @Test
    public void testMissingShortcutLabel() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        String menuLabel = "File";
        JMenu fileMenu = mainMenu.createMenu(menuLabel);

        String itemKey = "Open";
        JMenuItem openItem = new JMenuItem(itemKey);
        fileMenu.add(openItem);

        mainMenu.addShortcutAndIcon(openItem, "OpenShortcut"); // Wrong key

        assertNull(openItem.getAccelerator());
    }

    @Test
    public void testMissingIconLabel() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        String menuLabel = "File";
        JMenu fileMenu = mainMenu.createMenu(menuLabel);

        String itemKey = "Open";
        JMenuItem openItem = new JMenuItem(itemKey);
        fileMenu.add(openItem);

        mainMenu.addShortcutAndIcon(openItem, itemKey); // Wrong key

        assertNull(openItem.getIcon());
    }
}