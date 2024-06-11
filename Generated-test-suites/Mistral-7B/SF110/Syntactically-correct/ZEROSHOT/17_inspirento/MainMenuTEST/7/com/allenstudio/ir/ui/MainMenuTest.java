package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
 public void testMainMenuCreation() {
 // Test if the MainMenu object is created successfully
 assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenu() {
 String menuLabel = "File";
 JMenu expectedMenu = new JMenu(menuLabel);
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 // Compare the menus created by the method and the expected one
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testAddShortcutAndIconToMenuItem() {
 String menuLabel = "Help";
 String itemKey = "About";
 String expectedShortcut = "Ctrl+A";
 String expectedIconPath = "icons/help.png";
 JMenuItem expectedMenuItem = new JMenuItem(menuLabel + " > " + itemKey);
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(expectedShortcut));
 expectedMenuItem.setIcon(new ImageIcon(new File(expectedIconPath).toURI().toURL()));

 String itemLabel = "About";
 String shortcut = "Ctrl+A";
 String iconPath = "icons/help.png";

 JMenuItem actualMenuItem = new JMenuItem(itemLabel);
 mainMenu.addShortcutAndIcon(actualMenuItem, itemKey);

 // Compare the actual menu item with the expected one
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertEquals(expectedMenuItem.getAcceleratorKeyStroke(), actualMenuItem.getAccelerator());
 try {
 assertEquals(new ImageIcon(new File(expectedIconPath).toURI().toURL()), actualMenuItem.getIcon());
 } catch (Exception e) {
 fail("Icon loading failed.");
 }
 }

 @Test
 public void testAddSeparatorToMenu() {
 String menuLabel = "File";
 JMenu expectedMenu = new JMenu(menuLabel);
 expectedMenu.addSeparator();
 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 actualMenu.addSeparator();
 // Compare the menus created by the method and the expected one
 assertEquals(expectedMenu, actualMenu);
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menuLabel = "MissingLabel";
 mainMenu.createMenu(menuLabel);
 fail("A MissingResourceException should be thrown.");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingItemLabel() {
 String menuLabel = "File";
 String itemKey = "MissingItem";
 mainMenu.createMenu(menuLabel);
 JMenu menu = (JMenu) mainMenu.getComponentAt(0);
 JMenuItem item = (JMenuItem) menu.getComponentAt(0);
 mainMenu.addShortcutAndIcon(item, itemKey);
 fail("A MissingResourceException should be thrown.");
 }

 @Test
 public void testMenuSize() {
 int expectedSize = 5; // Based on the given code and UIResources.getString("menu")
 int actualSize = mainMenu.getComponentCount();
 assertEquals(expectedSize, actualSize);
 }

}