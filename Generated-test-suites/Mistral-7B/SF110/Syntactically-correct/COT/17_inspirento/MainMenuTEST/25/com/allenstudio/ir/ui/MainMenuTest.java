package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.MenuElement;
import org.junit.*;

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
 // Test that the MainMenu object is created properly
 assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenu() {
 String menuName = "File";
 JMenu expectedMenu = new JMenu("File");
 try {
 JMenu actualMenu = mainMenu.createMenu(menuName);
 assertEquals(expectedMenu.getName(), actualMenu.getName());
 } catch (MissingResourceException e) {
 fail("Failed to load menu label: " + menuName);
 }
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menuName = "Edit";
 // Add a separator to the menu
 String separatorKey = "EditSeparator";
 String separatorLabel = UIResources.getString(separatorKey);
 mainMenu.createMenu(menuName);
 // Check that the separator was added to the menu
 JMenu actualMenu = (JMenu) mainMenu.getComponentAt(1);
 MenuElement[] elements = actualMenu.getMenuComponents();
 assertArrayEquals(new String[]{"", separatorLabel}, Arrays.asList(elements[0].getName(), elements[1].getName()).toArray(new String[0]));
 }

 @Test
 public void testCreateMenuWithMenuItem() {
 String menuName = "File";
 String itemName = "New";
 String itemKey = "New";
 String itemLabel = UIResources.getString(itemKey);
 JMenuItem expectedMenuItem = new JMenuItem(itemLabel);
 try {
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.createMenu(menuName).getComponent(0);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 } catch (MissingResourceException e) {
 fail("Failed to load menu item label: " + itemKey);
 }
 }

 @Test
 public void testCreateMenuWithCheckboxMenuItem() {
 String menuName = "Options";
 String itemName = "Save Config";
 String itemKey = "SaveConfig";
 String itemLabel = UIResources.getString(itemKey);
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem(itemLabel);
 try {
 JCheckBoxMenuItem actualMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu(menuName).getComponent(1);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 } catch (MissingResourceException e) {
 fail("Failed to load menu item label: " + itemKey);
 }
 }

 @Test
 public void testCreateMenuWithMenuItemAndShortcut() {
 String menuName = "File";
 String itemName = "Save";
 String itemKey = "Save";
 String itemLabel = UIResources.getString(itemKey);
 String shortcut = UIResources.getString(itemKey + "Shortcut");
 JMenuItem expectedMenuItem = new JMenuItem(itemLabel);
 expectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut));
 try {
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.createMenu(menuName).getComponent(1);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertEquals(expectedMenuItem.getAcceleratorKeyStroke(), actualMenuItem.getAccelerator());
 } catch (MissingResourceException e) {
 fail("Failed to load menu item label: " + itemKey);
 }
 }

 @Test
 public void testCreateMenuWithMenuItemAndIcon() {
 String menuName = "Help";
 String itemName = "About";
 String itemKey = "About";
 String itemLabel = UIResources.getString(itemKey);
 String iconPath = UIResources.getString(itemKey + "Icon");
 JMenuItem expectedMenuItem = new JMenuItem(itemLabel);
 expectedMenuItem.setIcon(new ImageIcon(new File(iconPath).getAbsolutePath()));
 try {
 JMenuItem actualMenuItem = (JMenuItem) mainMenu.createMenu(menuName).getComponent(0);
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 assertNotNull(actualMenuItem.getIcon());
 } catch (MissingResourceException e) {
 fail("Failed to load menu item label: " + itemKey);
 }
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menuName = "NonExistentMenu";
 mainMenu.createMenu(menuName);
 fail("Expected a MissingResourceException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullMediator() {
 try {
 new MainMenu(null);
 fail("Expected an IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
}

 @Test
 public void testChanged() {
 // This method is empty in the given class, so no testing is needed
}
}