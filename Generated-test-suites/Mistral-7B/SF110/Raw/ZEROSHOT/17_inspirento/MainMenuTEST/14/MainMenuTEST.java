package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;

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
 // Test if MainMenu is initialized with InspirentoMediator
 assertEquals("InspirentoMediator not set", mediator, mainMenu.mediator);
 }

 @Test
 public void testCreateMenu() {
 String menuLabel = "File";
 JMenu expectedMenu = new JMenu(menuLabel);
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 // Compare JMenu objects using reflection to check labels and mnemonics
 assertEquals("Menu labels do not match", expectedMenu.getName(), actualMenu.getName());
 assertEquals("Menu mnemonics do not match", expectedMenu.getMnemonic(), actualMenu.getMnemonic());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menuLabel = "File";
 String menuItemLabel = "New";
 String key = "File.New";
 JMenuItem actualMenuItem = null;

 // Find the JMenuItem with the given label and key
 for (Component component : mainMenu) {
 if (component instanceof JMenuItem && component.getName().equals(menuItemLabel)) {
 actualMenuItem = (JMenuItem) component;
 break;
 }
 }

 // Test if shortcut is added correctly
 assertEquals("Shortcut not set", "N", actualMenuItem.getAccelerator().toString());

 // Test if icon is added correctly
 assertNull("Icon should be null at first", actualMenuItem.getIcon());

 String iconPath = "icons/new.png";
 mainMenu.addShortcutAndIcon(actualMenuItem, key);

 // Test if shortcut is still set
 assertEquals("Shortcut not set after addShortcutAndIcon", "N", actualMenuItem.getAccelerator().toString());

 // Test if icon is added correctly
 URL url = mainMenu.getClass().getResource(iconPath);
 assertNotNull("Icon file not found", url);
 ImageIcon expectedIcon = new ImageIcon(url);
 ImageIcon actualIcon = actualMenuItem.getIcon();
 assertEquals("Icons do not match", expectedIcon.getImage(), actualIcon.getImage());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String nonExistingMenu = "NonExistingMenu";
 mainMenu.createMenu(nonExistingMenu);
 fail("MissingResourceException should be thrown");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
 String menuLabel = "File";
 String nonExistingMenuItemLabel = "NonExistingMenuItem";
 JMenuItem actualMenuItem = mainMenu.createMenu(menuLabel).findJMenuItem(nonExistingMenuItemLabel);
 fail("MissingResourceException should be thrown");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingShortcutLabel() {
 String key = "NonExistingShortcut";
 JMenuItem actualMenuItem = mainMenu.findJMenuItem(key.substring(0, key.length() - SHORTCUT_SUFFIX.length()));
 actualMenuItem.setAccelerator(null);
 actualMenuItem.setIcon(null);
 mainMenu.addShortcutAndIcon(actualMenuItem, key);
 fail("MissingResourceException should be thrown");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingIconLabel() {
 String key = "NonExistingIcon";
 JMenuItem actualMenuItem = mainMenu.findJMenuItem(key.substring(0, key.length() - ICON_SUFFIX.length()));
 actualMenuItem.setAccelerator(null);
 actualMenuItem.setIcon(null);
 mainMenu.addShortcutAndIcon(actualMenuItem, key);
 fail("MissingResourceException should be thrown");
 }
}