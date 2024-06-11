package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.junit.*;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator = new MockInspirentoMediator(); // Use a mock mediator for testing

 @Before
 public void setUp() {
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstruction() {
 // Test that MainMenu is initialized correctly
 assertEquals(11, mainMenu.getMenuCount()); // Number of menus based on the provided UIResources string
 }

 @Test
 public void testCreateMenu() {
 String menuName = "File";
 try {
 JMenu menu = mainMenu.createMenu(menuName);
 // Check that menu was created with the correct label
 String expectedLabel = "File";
 assertEquals(expectedLabel, menu.getText());
 // Check that menu has no items by default
 assertEquals(0, menu.getMenuComponentCount());
 }
 catch (MissingResourceException e) {
 fail("Failed to load menu label: " + e.getMessage());
 }
 }

 @Test
 public void testCreateMenuWithSubMenusAndCheckboxItems() {
 String menuName = "Edit";
 try {
 JMenu menu = mainMenu.createMenu(menuName);
 // Check that menu was created with the correct label
 String expectedLabel = "Edit";
 assertEquals(expectedLabel, menu.getText());
 // Check that menu has submenus and checkbox items
 String subMenuName = "Undo";
 JMenu subMenu = (JMenu) menu.getComponent(0);
 assertEquals(subMenuName, subMenu.getText());
 JMenuItem checkboxItem = (JCheckBoxMenuItem) menu.getComponent(1);
 assertEquals("Undo", checkboxItem.getText());
assertTrue(checkboxItem.isArmed()); // Checkbox items are armed by default
 }
 catch (MissingResourceException e) {
 fail("Failed to load menu label: " + e.getMessage());
 }
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menuName = "Help";
 try {
 JMenu menu = mainMenu.createMenu(menuName);
 // Check that menu was created with the correct label
 String expectedLabel = "Help";
 assertEquals(expectedLabel, menu.getText());
 // Check that menu has a separator
 JComponent separator = menu.getComponent(0);
 assertTrue(separator instanceof JSeparator);
 }
 catch (MissingResourceException e) {
 fail("Failed to load menu label: " + e.getMessage());
 }
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuWithMissingLabel() {
 String menuName = "MissingLabel";
 mainMenu.createMenu(menuName);
 fail("Expected MissingResourceException but it did not occur");
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "File.New";
 JMenuItem item = new JMenuItem("New");
 mainMenu.addShortcutAndIcon(item, key);
 // Check that accelerator key was set
 String expectedShortcut = "Ctrl+N";
 assertEquals(expectedShortcut, item.getAccelerator().toString());
 // Check that icon was set
 String expectedIconPath = "icons/new.png";
 URL url = item.getIcon().getImage().getSource();
 assertEquals(expectedIconPath, url.toString());
 }

 @Test
 public void testCreateMenuItemWithMissingIconAndShortcut() {
 String key = "Help.About";
 JMenuItem item = new JMenuItem("About");
 mainMenu.addShortcutAndIcon(item, key);
 // Check that no icon or shortcut was set
 assertNull(item.getIcon());
 assertNull(item.getAccelerator());
 }
}