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
 public void testMainMenuCreation() {
 // Test that the MainMenu is initialized with the correct number of menus
 assertEquals(7, mainMenu.getMenuCount());
 }

 @Test
 public void testCreateMenu() {
 String menuLabel = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 }

 @Test
 public void testCreateMenuWithSubMenu() {
 String menuLabel = "Edit";
 String subMenuLabel = "Copy";
 JMenu expectedMenu = new JMenu("Edit");
 JMenu subMenu = new JMenu("Copy");
 expectedMenu.add(subMenu);
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 JMenu actualSubMenu = (JMenu) actualMenu.getComponent(0);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertEquals(subMenuLabel, actualSubMenu.getText());
 }

 @Test
 public void testCreateCheckboxMenuItem() {
 String menuLabel = "Tools";
 String checkboxMenuItemLabel = "Options";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("Options");
 JCheckBoxMenuItem actualMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu(menuLabel).getComponent(1);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 }

 @Test
 public void testCreateMenuItem() {
 String menuLabel = "Help";
 String menuItemLabel = "About";
 JMenuItem expectedMenuItem = new JMenuItem("About");
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.createMenu(menuLabel).getComponent(1);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 }

 @Test
 public void testAddShortcutAndIconToMenuItem() {
 String menuLabel = "Edit";
 String key = "Edit.Cut";
 String shortcut = "Ctrl+X";
 String iconPath = "/icon/cut.png";
 JMenuItem expectedMenuItem = new JMenuItem("Cut");
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut));
 expectedMenuItem.setIcon(new ImageIcon(new File(iconPath).toURL()));
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.createMenu(menuLabel).getComponent(2);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertNull(actualMenuItem.getAccelerator());
 assertNull(actualMenuItem.getIcon());
 mainMenu.addShortcutAndIcon(actualMenuItem, key);
 assertEquals(expectedMenuItem.getAccelerator(), actualMenuItem.getAccelerator());
 assertNotNull(actualMenuItem.getIcon());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingLabelResource() {
 mainMenu.createMenu("MissingLabel");
 fail("Expected MissingResourceException");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingShortcutResource() {
 mainMenu.addShortcutAndIcon(new JMenuItem("MissingShortcut"), "MissingShortcut");
 fail("Expected MissingResourceException");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingIconResource() {
 mainMenu.addShortcutAndIcon(new JMenuItem("MissingIcon"), "MissingIcon");
 fail("Expected MissingResourceException");
 }

}