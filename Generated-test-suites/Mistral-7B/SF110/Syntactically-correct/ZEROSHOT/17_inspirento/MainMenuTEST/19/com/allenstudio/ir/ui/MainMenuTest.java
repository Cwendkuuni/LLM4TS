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
 public void setup() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuCreation() {
 // Test if MainMenu is initialized correctly
 assertEquals("Unexpected number of menus created", 6, mainMenu.getMenuCount());
 }

 @Test
 public void testCreateMenu() {
 String menuName = "File";
 JMenu menu = mainMenu.createMenu(menuName);
 // Test if menu is created with correct label
 assertEquals("Incorrect label for menu: " + menuName, menuName.substring(0, menuName.length() - 2), menu.getText());
 // Test if menu mnemonic is set correctly
 assertEquals("Incorrect mnemonic for menu: " + menuName, menuName.charAt(menuName.length() - 1), menu.getMnemonic());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String menuName = "File";
 String menuItemName = "Open";
 String shortcut = "Ctrl+O";
 String iconPath = "fileIcon.png";

 JMenuItem item = mainMenu.createMenu(menuName).getItem(0);
 mainMenu.addShortcutAndIcon(item, menuItemName);

 // Test if shortcut is set correctly
 assertEquals("Incorrect shortcut for menu item: " + menuItemName, shortcut, item.getAccelerator().toString());

 // Test if icon is set correctly
 assertEquals("Incorrect icon for menu item: " + menuItemName, iconPath, item.getIcon().getDescription(null));
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 mainMenu.createMenu("");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
 mainMenu.createMenu("File").add(new JMenuItem(""));
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingShortcut() {
 mainMenu.createMenu("File").getItem(0).setAccelerator(null);
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingIcon() {
 mainMenu.createMenu("File").getItem(0).setIcon(null);
 }
}