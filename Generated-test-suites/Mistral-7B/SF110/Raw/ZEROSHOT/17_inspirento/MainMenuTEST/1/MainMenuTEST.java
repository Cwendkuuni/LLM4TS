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
 // Test if MainMenu is created correctly
 assertNotNull(mainMenu);
 assertInstanceOf(JMenuBar.class, mainMenu);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu(menu);
 expectedMenu.setMnemonicKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK);
 JMenu actualMenu = mainMenu.createMenu(menu);
 // Compare expected and actual menus
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertEquals(expectedMenu.getMnemonic(), actualMenu.getMnemonic());
 }

 @Test
 public void testCreateMenuWithSubMenusAndItems() {
 String menu = "Edit";
 String subMenu = "Undo";
 String item = "Cut";
 JMenu expectedMenu = new JMenu(menu);
 JMenu expectedSubMenu = new JMenu(subMenu);
 JMenuItem expectedItem = new JMenuItem(item);
 expectedItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
 expectedSubMenu.add(expectedItem);
 expectedMenu.add(expectedSubMenu);
 JMenu actualMenu = mainMenu.createMenu(menu);
 JMenu actualSubMenu = (JMenu) actualMenu.findSubMenu("Edit");
 JMenuItem actualItem = (JMenuItem) actualSubMenu.findComponent(0);
 // Compare expected and actual menus, submenus and items
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertEquals(expectedSubMenu.getText(), actualSubMenu.getText());
 assertEquals(expectedItem.getText(), actualItem.getText());
 assertEquals(expectedItem.getAcceleratorKeyStroke().getKeyCode(), actualItem.getAccelerator().getKeyCode());
 }

 @Test
 public void testCreateMenuWithCheckboxItem() {
 String menu = "Help";
 String item = "#Preferences";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem(item);
 expectedMenuItem.setMnemonic('P');
 JMenu actualMenu = mainMenu.createMenu(menu);
 JCheckBoxMenuItem actualMenuItem = (JCheckBoxMenuItem) actualMenu.findComponent(actualMenu.getComponentCount() - 1);
 // Compare expected and actual checkbox items
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertEquals(expectedMenuItem.isSelected(), actualMenuItem.isSelected());
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuWithMissingLabel() {
 String menu = "MissingLabel";
 mainMenu.createMenu(menu);
 fail("MissingResourceException expected but not thrown");
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "File_New";
 JMenuItem item = new JMenuItem("New");
 mainMenu.addShortcutAndIcon(item, key);
 // Test if shortcut is added
 assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK), item.getAccelerator());
 // Test if icon is added
 assertNull(item.getIcon());
 try {
 String iconPath = UIResources.getString(key + "Icon");
 mainMenu.addShortcutAndIcon(item, key);
 assertNotNull(item.getIcon());
 } catch (MissingResourceException e) {
 fail("Icon file not found but expected");
 }
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // No action expected
 }
}