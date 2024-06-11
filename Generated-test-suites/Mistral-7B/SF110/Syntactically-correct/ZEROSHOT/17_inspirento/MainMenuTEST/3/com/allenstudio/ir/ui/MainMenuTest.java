package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator = new InspirentoMediator(); // Dummy mediator for testing

 @Test
 public void testMainMenuConstruction() {
 MainMenu menu = new MainMenu(mediator);
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenu() {
 String[] menus = {"File", "Edit", "Help"}; // Expected menus from UIResources.getString("menu")
 MainMenu menu = new MainMenu(mediator);
 JMenu fileMenu = menu.createMenu("File");
 assertEquals("File", fileMenu.getText());
 JMenu editMenu = menu.createMenu("Edit");
 assertEquals("Edit", editMenu.getText());
 JMenu helpMenu = menu.createMenu("Help");
 assertEquals("Help", helpMenu.getText());
 }

 @Test
 public void testCreateMenuItem() {
 String menuName = "File";
 String menuItemName = "Open";
 String menuItemLabel = "Open";
 String shortcut = "Ctrl+O";
 String iconPath = "icon/open.png";
 MainMenu menu = new MainMenu(mediator);
 JMenuItem openItem = menu.createMenuItem(menuItemName, menuItemLabel, shortcut, iconPath);
 assertEquals(menuItemLabel, openItem.getText());
 assertNotNull(openItem.getAccelerator());
 assertEquals(KeyStroke.getKeyStroke(shortcut), openItem.getAccelerator());
 assertNull(openItem.getIcon());

 menuItemLabel = "Open*";
 menuItemName = "Open";
 shortcut = "";
 iconPath = "";
 openItem = menu.createMenuItem(menuItemName, menuItemLabel, shortcut, iconPath);
 assertEquals(menuItemLabel.substring(0, menuItemLabel.length() - 2), openItem.getText());
 assertNull(openItem.getAccelerator());
 assertNull(openItem.getIcon());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menuName = "File";
 String menuItemName = "Open";
 String menuItemLabel = "Open";
 String shortcut = "Ctrl+O";
 String iconPath = "icon/open.png";
 MainMenu menu = new MainMenu(mediator);
 JMenuItem openItem = menu.createMenuItem(menuItemName, menuItemLabel, shortcut, iconPath);
 menu.addShortcutAndIcon(openItem, menuItemName);
 assertNotNull(openItem.getAccelerator());
 assertEquals(KeyStroke.getKeyStroke(shortcut), openItem.getAccelerator());
 assertNull(openItem.getIcon());

 menuItemLabel = "Open*";
 openItem = menu.createMenuItem(menuItemName, menuItemLabel, shortcut, iconPath);
 menu.addShortcutAndIcon(openItem, menuItemName);
 assertNotNull(openItem.getAccelerator());
 assertEquals(KeyStroke.getKeyStroke(shortcut), openItem.getAccelerator());
 assertNotNull(openItem.getIcon());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 MainMenu menu = new MainMenu(mediator);
 menu.createMenu("MissingLabel");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
 MainMenu menu = new MainMenu(mediator);
 JMenu fileMenu = menu.createMenu("File");
 menu.createMenuItem("MissingLabel", "", "", "");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingShortcut() {
 MainMenu menu = new MainMenu(mediator);
 JMenuItem openItem = menu.createMenuItem("Open", "Open", "", "");
 menu.addShortcutAndIcon(openItem, "Open");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingIcon() {
 MainMenu menu = new MainMenu(mediator);
 JMenuItem openItem = menu.createMenuItem("Open", "Open", "Ctrl+O", "");
 menu.addShortcutAndIcon(openItem, "Open");
 }
}