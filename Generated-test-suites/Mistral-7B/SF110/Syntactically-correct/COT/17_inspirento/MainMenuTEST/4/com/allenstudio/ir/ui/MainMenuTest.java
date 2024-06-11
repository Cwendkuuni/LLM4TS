package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator;

 @Before
 public void setup() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstructor() {
 // Basic functionality test
 mainMenu = new MainMenu(mediator);
 assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenuWithEmptyLabel() {
 // Edge case: empty menu label
 String menu = "";
 try {
 mainMenu.createMenu(menu);
 fail();
 } catch (Exception e) {
 // Expected exception
 assertTrue(true);
 }
 }

 @Test
 public void testCreateMenuWithNullMenuLabel() {
 // Edge case: null menu label
 String menu = null;
 try {
 mainMenu.createMenu(menu);
 fail();
 } catch (Exception e) {
 // Expected exception
 assertTrue(true);
 }
 }

 @Test
 public void testCreateMenuWithNoItems() {
 // Edge case: empty menu with no items
 String menu = "Menu";
 JMenu result = mainMenu.createMenu(menu);
 assertNotNull(result);
 assertEquals(0, result.getMenuComponentCount());
 }

 @Test
 public void testCreateMenuWithSeparatorItem() {
 // Edge case: menu with separator item
 String menu = "Menu|-";
 JMenu result = mainMenu.createMenu(menu);
 assertNotNull(result);
 assertEquals(1, result.getMenuComponentCount());
 assertInstanceOf(JSeparator.class, result.getComponent(0));
 }

 @Test
 public void testCreateMenuWithCheckboxMenuItem() {
 // Edge case: menu with checkbox item
 String menu = "Menu|Checkbox";
 String checkboxKey = "Checkbox";
 String checkboxLabel = "CheckboxLabel";
 String iconPath = "icon.png";
 String shortcut = "ctrl C";
 JCheckBoxMenuItem result = (JCheckBoxMenuItem) mainMenu.createMenu(menu).getComponent(0);
 assertEquals(checkboxLabel, result.getText());
 assertNull(result.getIcon());
 assertNull(result.getAccelerator());
 // Test setAccelerator
 mainMenu.addShortcutAndIcon(result, checkboxKey);
 assertNotNull(result.getAccelerator());
 assertEquals(KeyStroke.getKeyStroke(shortcut), result.getAccelerator());
 // Test setIcon
 mainMenu.addShortcutAndIcon(result, checkboxKey);
 String iconPathExpected = "icon.png";
 UIManager.LookAndFeelInfo currentLAF = UIManager.getLookAndFeel();
 try {
 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
 result.setIcon(new ImageIcon(getClass().getResource(iconPath)));
 assertEquals(true, result.isArmed());
 } finally {
 UIManager.setLookAndFeel(currentLAF.getClassName());
 }
 assertNotNull(result.getIcon());
 assertEquals(new ImageIcon(getClass().getResource(iconPathExpected)), result.getIcon());
 }

 @Test
 public void testCreateMenuWithMenuItem() {
 // Edge case: menu with menu item
 String menu = "Menu|Submenu";
 String submenuLabel = "SubmenuLabel";
 String submenuKey = "Submenu";
 String submenuIconPath = "icon.png";
 String submenuShortcut = "ctrl S";
 String itemLabel = "ItemLabel";
 String itemKey = "Item";
 String itemIconPath = "icon.png";
 String itemShortcut = "ctrl I";
 String menuLabel = "Menu";
 JMenu submenuResult = (JMenu) mainMenu.createMenu(menu).getComponent(0);
 assertEquals(submenuLabel, submenuResult.getText());
 assertNull(submenuResult.getIcon());
 assertNull(submenuResult.getAccelerator());
 JMenuItem itemResult = (JMenuItem) submenuResult.getComponent(0);
 assertEquals(itemLabel, itemResult.getText());
 assertNull(itemResult.getIcon());
 assertNull(itemResult.getAccelerator());
 // Test setAccelerator for submenu item
 mainMenu.addShortcutAndIcon(itemResult, itemKey);
 assertNotNull(itemResult.getAccelerator());
 assertEquals(KeyStroke.getKeyStroke(itemShortcut), itemResult.getAccelerator());
 // Test setIcon for submenu item
 mainMenu.addShortcutAndIcon(itemResult, itemKey);
 String iconPathExpected = "icon.png";
 UIManager.LookAndFeelInfo currentLAF = UIManager.getLookAndFeel();
 try {
 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
 itemResult.setIcon(new ImageIcon(getClass().getResource(iconPathExpected)));
 assertEquals(true, itemResult.isArmed());
 } finally {
 UIManager.setLookAndFeel(currentLAF.getClassName());
 }
 assertNotNull(itemResult.getIcon());
 assertEquals(new ImageIcon(getClass().getResource(iconPathExpected)), itemResult.getIcon());
 // Test createSubmenu
 JMenu submenu = mainMenu.createMenu(submenuKey);
 assertEquals(submenuLabel, submenu.getText());
 assertEquals(submenuResult, submenu.getMenuComponent(0));
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabelResource() {
 // Exception handling: missing menu label resource
 String menu = "MissingLabel";
 mainMenu.createMenu(menu);
 fail();
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingCheckboxMenuItemLabelResource() {
 // Exception handling: missing checkbox menu item label resource
 String menu = "Menu|Checkbox";
 String checkboxKey = "Checkbox";
 mainMenu.createMenu(menu);
 fail();
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabelResource() {
 // Exception handling: missing menu item label resource
 String menu = "Menu|Item";
 String itemKey = "Item";
 mainMenu.createMenu(menu);
 fail();
 }

 @Test
 public void testChanged() {
 // Edge case: changed method
 mainMenu.changed();
 // No action required, as this method is empty
 }
}