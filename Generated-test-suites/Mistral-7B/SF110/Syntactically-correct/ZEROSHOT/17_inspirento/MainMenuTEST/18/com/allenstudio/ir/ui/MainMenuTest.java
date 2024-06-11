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
 // Test that the MainMenu object is not null
 assertNotNull(mainMenu);
 // Test that the mediator is set correctly
 assertEquals(mediator, mainMenu.getMediator());
 }

 @Test
 public void testCreateMenu() {
 String menuLabel = "File";
 JMenu menu = mainMenu.createMenu(menuLabel);
 // Test that the menu label is set correctly
 assertEquals(menuLabel, menu.getText());
 // Test that the menu has no items initially
 assertEquals(0, menu.getMenuComponentCount());
 }

 @Test
 public void testCreateMenuWithSubMenus() {
 String menuLabel = "Edit";
 JMenu menu = mainMenu.createMenu(menuLabel);
 // Test that the menu label is set correctly
 assertEquals(menuLabel, menu.getText());
 // Test that the menu has sub-menus
 String subMenuLabel = "Cut";
 JMenu subMenu = mainMenu.createMenu(subMenuLabel + ">");
 menu.add(subMenu);
 // Test that the sub-menu label is set correctly
 assertEquals(subMenuLabel, subMenu.getText());
 // Test that the sub-menu is empty initially
 assertEquals(0, subMenu.getMenuComponentCount());
 }

 @Test
 public void testCreateMenuWithCheckboxMenuItem() {
 String menuLabel = "Options";
 String checkboxMenuItemLabel = "Show Grid";
 JCheckBoxMenuItem checkboxMenuItem = null;
 JMenu menu = mainMenu.createMenu(menuLabel);
 // Add a checkbox menu item with the given label
 checkboxMenuItem = (JCheckBoxMenuItem) mainMenu.createMenuItem(checkboxMenuItemLabel, menuLabel, null);
 menu.add(checkboxMenuItem);
 // Test that the checkbox menu item label is set correctly
 assertEquals(checkboxMenuItemLabel, checkboxMenuItem.getText());
 // Test that the checkbox menu item mnemonic is set correctly
 assertEquals('S', checkboxMenuItem.getMnemonic());
 // Test that the checkbox menu item is unchecked initially
 assertFalse(checkboxMenuItem.isSelected());
 }

 @Test
 public void testCreateMenuWithMenuItem() {
 String menuLabel = "Help";
 String menuItemLabel = "About";
 JMenuItem menuItem = null;
 JMenu menu = mainMenu.createMenu(menuLabel);
 // Add a menu item with the given label
 menuItem = mainMenu.createMenuItem(menuItemLabel, menuLabel, null);
 menu.add(menuItem);
 // Test that the menu item label is set correctly
 assertEquals(menuItemLabel, menuItem.getText());
 // Test that the menu item mnemonic is set correctly
 assertEquals('A', menuItem.getMnemonic());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menuLabel = "File";
 String key = "Open";
 JMenuItem menuItem = null;
 JMenu menu = mainMenu.createMenu(menuLabel);
 // Create a menu item with the given key
 menuItem = mainMenu.createMenuItem(UIResources.getString(key + "Label"), menuLabel, null);
 menu.add(menuItem);
 // Add shortcut and icon to the menu item
 mainMenu.addShortcutAndIcon(menuItem, key);
 // Test that the shortcut is set correctly
 assertEquals(KeyStroke.getKeyStroke("ctrl O"), menuItem.getAccelerator());
 // Test that the icon is set correctly
 assertNotNull(menuItem.getIcon());
 }

}