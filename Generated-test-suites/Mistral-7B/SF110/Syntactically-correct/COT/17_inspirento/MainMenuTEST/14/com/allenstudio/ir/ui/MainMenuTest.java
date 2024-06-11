package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.junit.*;
import org.junit.Test;

public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediator = new InspirentoMediator(); // Dummy mediator for testing

 @Before
 public void setUp() {
 mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstructor() {
 MainMenu instance = new MainMenu(mediator);
 assertNotNull(instance);
 }

 @Test
 public void testCreateMenu() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithLabelSuffix() {
 String menu = "Help*>About";
 String label = "About";
 JMenu expectedMenu = new JMenu(label);
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testCreateMenuWithCheckboxMenuItem() {
 String menu = "Config";
 String item = "#SaveConfig";
 JCheckBoxMenuItem expectedMenuItem = new JCheckBoxMenuItem("Save Config");
 JCheckBoxMenuItem actualMenuItem = mainMenu.createMenu(menu).findJCheckBoxMenuItem(item);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuWithJMenuItem() {
 String menu = "Help";
 String item = "HelpContents";
 JMenuItem expectedMenuItem = new JMenuItem("Help Contents");
 JMenuItem actualMenuItem = mainMenu.createMenu(menu).findJMenuItem(item);
 assertEquals(expectedMenuItem, actualMenuItem);
 }

 @Test
 public void testCreateMenuWithSeparator() {
 String menu = "File";
 JMenu expectedMenu = new JMenu("File");
 expectedMenu.addSeparator();
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertTrue(expectedMenu.getComponentCount() == actualMenu.getComponentCount());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 String menu = "MissingLabel";
 mainMenu.createMenu(menu);
 fail("Expected MissingResourceException");
 }

 @Test
 public void testAddShortcutAndIcon() {
 String key = "File_New";
 JMenuItem item = new JMenuItem("New");
 mainMenu.addShortcutAndIcon(item, key);
 String shortcut = "Ctrl N";
 assertEquals(KeyStroke.getKeyStroke(shortcut.charAt(0), shortcut.charAt(1)), item.getAccelerator());
 String iconPath = "icons/new.png";
 try {
 URL url = mainMenu.getClass().getResource(iconPath);
 assertEquals(url, item.getIcon().getImage().getSource());
 } catch (IOException e) {
 fail("Expected image file found");
 }
 }

 @Test
 public void testAddShortcutAndIcon_MissingShortcut() {
 String key = "File_Save_MissingShortcut";
 JMenuItem item = new JMenuItem("Save");
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getAccelerator());
 }

 @Test
 public void testAddShortcutAndIcon_MissingIcon() {
 String key = "File_Save_MissingIcon";
 JMenuItem item = new JMenuItem("Save");
 mainMenu.addShortcutAndIcon(item, key);
 assertNull(item.getIcon());
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 // No action expected
 }
}