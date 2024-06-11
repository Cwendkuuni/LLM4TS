package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

public class MainMenuTest {

 private InspirentoMediator mediator;
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 mediator = new InspirentoMediator();
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstructor() {
 MainMenu mainMenu = new MainMenu(mediator);
 assertNotNull(mainMenu);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 }

 @Test
 public void testCreateMenuWithLongLabel() {
 String menu = "File*New Project";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertEquals("New Project", actualMenu.getText().getResourceBundle().getString("NewProjectLabel"));
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menu = "File*-";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertTrue(actualMenu.getComponentCount() > 1);
 }

 @Test
 public void testCreateMenuWithSubMenu() {
 String menu = "Edit>Format";
 JMenu expectedMenu = new JMenu("Edit");
 JMenu subMenu = new JMenu("Format");
 expectedMenu.add(subMenu);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu.getText(), actualMenu.getText());
 assertEquals(subMenu.getText(), actualMenu.getComponent(0).getText());
 }

 @Test
 public void testCreateMenuWithCheckBoxMenuItem() {
 String menu = "Tools>Preferences";
 String item = "Preferences*ShowLineNumbers";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("ShowLineNumbers");
 JCheckBoxMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu(menu).getComponent(1);
 } catch (ArrayIndexOutOfBoundsException e) {
 fail("Expected menu to have at least one component.");
 }
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 }

 @Test
 public void testCreateMenuWithMenuItem() {
 String menu = "Help>About";
 String item = "About";
 JMenuItem expectedMenuItem = new JMenuItem("About");
 JMenuItem actualMenuItem = null;
 try {
 actualMenuItem = (JMenuItem) mainMenu.createMenu(menu).getComponent(1);
 } catch (ArrayIndexOutOfBoundsException e) {
 fail("Expected menu to have at least one component.");
 }
 assertEquals(expectedMenuItem.getText(), actualMenuItem.getText());
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "FileNewProject";
 String shortcut = "Ctrl N";
 String iconPath = "icons/new.png";
 JMenuItem menuItem = new JMenuItem("New Project");
 menuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut.charAt(0), shortcut.charAt(1)));
 menuItem.setIcon(new ImageIcon(new File(iconPath).toURI().toURL()));
 mainMenu.addShortcutAndIcon(menuItem, key);
 assertEquals(shortcut, menuItem.getAccelerator().getName());
 assertNotNull(menuItem.getIcon());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menu = "UnknownMenu";
 mainMenu.createMenu(menu);
 fail("Expected MissingResourceException to be thrown.");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() {
 String menu = "File";
 String item = "UnknownItem";
 mainMenu.createMenu(menu).add(new JMenuItem(item));
 fail("Expected MissingResourceException to be thrown.");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingIconPath() {
 String key = "FileNewProject";
 JMenuItem menuItem = new JMenuItem("New Project");
 mainMenu.addShortcutAndIcon(menuItem, key);
 fail("Expected MissingResourceException to be thrown.");
 }

 @Test(expected = NullPointerException.class)
 public void testNullMediator() {
 MainMenu mainMenu = new MainMenu(null);
 fail("Expected NullPointerException to be thrown.");
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // This method is empty, so no assertions are needed.
 }

}